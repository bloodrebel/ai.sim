package hr.altima.sim.service;

import hr.altima.sim.exception.InvalidImsiException;
import hr.altima.sim.exception.InvalidPhoneNumberException;
import hr.altima.sim.exception.InvalidSimCardNoException;
import hr.altima.sim.model.Sim;
import hr.altima.sim.model.SimAudit;
import hr.altima.sim.model.enums.ActionEnum;
import hr.altima.sim.model.enums.EnvironmentEnum;
import hr.altima.sim.model.enums.OperatorEnum;
import hr.altima.sim.model.enums.SimServiceEnum;
import hr.altima.sim.model.enums.SimStateEnum;
import hr.altima.sim.model.enums.TeamEnum;
import hr.altima.sim.repository.SimAuditRepository;
import hr.altima.sim.repository.SimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

@Service
public class SimService {
    private SimRepository simRepository;
    private SimAuditRepository auditRepository;

    @Autowired
    public SimService(SimRepository simRepository, SimAuditRepository auditRepository) {
        this.simRepository = simRepository;
        this.auditRepository = auditRepository;
    }

    boolean validateMsisdn(Sim sim) {
        if (sim.getOperator().equals(OperatorEnum.HEJ)) {
            return sim.getMsisdn().startsWith("38763") && sim.getMsisdn().length() == 11 && sim.getMsisdn().matches("[0-9]+");
        } else {
            return sim.getMsisdn().startsWith("387644") && sim.getMsisdn().length() == 12 && sim.getMsisdn().matches("[0-9]+");
        }
    }

    boolean validateImsi(String imsi) {
        if (imsi.length() == 15) {
            String mcc = imsi.substring(0, 3);
            String mnc = imsi.substring(3, 5);
            return imsi.matches("[0-9]+") && mcc.equals("218") && mnc.equals("03");
        }
        return false;
    }

    public boolean isMsisdnExist(Sim sim) {
        return simRepository.findByMsisdn(sim.getMsisdn()) != null;
    }

    public Sim findById(Integer id) {
        return simRepository.findById(id).orElse(null);
    }

    public Sim findByMsisdn(String msisdn) {
        return simRepository.findByMsisdn(msisdn);
    }

    public Sim findByImsi(String imsi) {
        return simRepository.findByImsi(imsi);
    }

    public Sim findBySimNumber(String simNumber) {
        return simRepository.findBySimNumber(simNumber);
    }

    public List<Sim> findAll() {
        return simRepository.findAll();
    }

    public Page<Sim> findAllPaginatedSims(int page, int size) {
        return simRepository.findAll(PageRequest.of(page, size));
    }

    public Page<Sim> findAllPaginatedUserSims(int page, int size, String userNickName) {
        return simRepository.findAllByUserNickName(userNickName, PageRequest.of(page, size));
    }

    public Page<Sim> findAllPaginatedTeamSims(int page, int size, TeamEnum team) {
        return simRepository.findAllByUserTeam(team, PageRequest.of(page, size));
    }

    public Page<Sim> findAllPaginatedEnvironmentSims(int page, int size, String environmentEnum) {
        return simRepository.findAllByEnvironment(environmentEnum, PageRequest.of(page, size));
    }

    public Page<Sim> findAllPaginatedSimServiceSims(int page, int size, String simServiceEnum) {
        return simRepository.findAllBySimService(simServiceEnum, PageRequest.of(page, size));
    }

    public Page<Sim> findAllPaginatedOperatorSims(int page, int size, String operatorEnum) {
        return simRepository.findAllByOperator(operatorEnum, PageRequest.of(page, size));
    }

    public Page<Sim> findAllPaginatedSimStateSims(int page, int size, String simStateEnum) {
        return simRepository.findAllBySimState(simStateEnum, PageRequest.of(page, size));
    }

    public List<Sim> findAllByEnvironment(EnvironmentEnum environmentEnum) {
        return simRepository.findAllByEnvironment(environmentEnum);
    }

    public List<Sim> findAllByUserNickName(String nickName) {
        return simRepository.findAllByUserNickName(nickName);
    }

    @Transactional
    public Sim createSim(Sim sim) {
        if (!validateMsisdn(sim)) {
            throw new InvalidPhoneNumberException("Invalid MSISDN");
        } else if (!validateImsi(sim.getImsi())) {
            throw new InvalidImsiException("Invalid IMSI");
        } else if (!validateSimNumber(sim.getSimNumber())) {
            throw new InvalidSimCardNoException("Invalid SIM number");
        }
        Sim createdSim = simRepository.save(sim);
        saveAudit(null, createdSim, ActionEnum.ADD);
        return createdSim;
    }

    @Transactional
    public Sim updateSim(Sim sim, Integer id) {
        if (!validateMsisdn(sim)) {
            throw new InvalidPhoneNumberException("Invalid MSISDN");
        } else if (!validateSimNumber(sim.getSimNumber())) {
            throw new InvalidSimCardNoException("Invalid SIM number");
        } else if (!validateImsi(sim.getImsi())) {
            throw new InvalidImsiException("Invalid IMSI");
        }
        Sim existingSim = this.findById(id);
        Sim oldSim = new Sim.Builder()
                .msisdn(existingSim.getMsisdn())
                .imsi(existingSim.getImsi())
                .simNumber(existingSim.getSimNumber())
                .operator(existingSim.getOperator())
                .simService(existingSim.getSimService())
                .environment(existingSim.getEnvironment())
                .simState(existingSim.getSimState())
                .user(existingSim.getUser())
                .build();
        sim.setId(id);
        Sim updatedSim = simRepository.save(sim);
        saveAudit(oldSim, updatedSim, ActionEnum.MOD);
        return updatedSim;
    }

    @Transactional
    public void deleteSim(Sim sim) {
        Sim simFromRepo = simRepository.findById(sim.getId()).orElse(null);
        simRepository.delete(sim);
        saveAudit(null, simFromRepo, ActionEnum.DEL);
        simRepository.flush();
    }

    public List<Sim> findAllBySimService(SimServiceEnum simService) {
        return simRepository.findAllBySimService(simService);
    }

    public List<Sim> findAllByTeam(TeamEnum team) {
        return simRepository.findAllByTeam(team);
    }

    public List<Sim> findAllByOperator(OperatorEnum operator) {
        return simRepository.findAllByOperator(operator);
    }

    public List<Sim> findAllBySimState(SimStateEnum simStateEnum) {
        return simRepository.findAllBySimState(simStateEnum);
    }

    boolean validateSimNumber(String simNumber) {
        if (simNumber.matches("[0-9]+") && simNumber.length() == 19 && simNumber.startsWith("89387")) {
            return true;
        }
        return false;
    }

    public List<SimAudit> findAllAudits() {
        return auditRepository.findAll();
    }

    public SimAudit findDeletedAuditBySimId(Integer id, ActionEnum action) {
        return auditRepository.findDeletedAuditBySimId(id, action);
    }

    public Page<SimAudit> findPaginatedSimAuditBySimId(int page, int size, Integer simId) {
        return auditRepository.findAllById(simId, PageRequest.of(page, size));
    }

    public Page<SimAudit> findPaginatedSimAuditForSimByAction(int page, int size, Integer simId, ActionEnum actionEnum) {
        return auditRepository.findAllByIdAndAction(simId, actionEnum, PageRequest.of(page, size));
    }

    SimAudit saveAudit(Sim oldSim, Sim sim, ActionEnum action) {
        String nickname = sim.getUser().getNickName();

        SimAudit audit = new SimAudit.Builder()
                .action(action)
                .actionDate(new Date(Calendar.getInstance().getTimeInMillis()))
                .id(sim.getId())
                .msisdn(sim.getMsisdn())
                .imsi(sim.getImsi())
                .simNumber(sim.getSimNumber())
                .environment(sim.getEnvironment().toString())
                .operator(sim.getOperator().toString())
                .simService(sim.getSimService().toString())
                .simState(sim.getSimState().toString())
                .userNickname(nickname)
                .build();

        if (action.equals(ActionEnum.MOD)) {
            if (!oldSim.getMsisdn().equals(sim.getMsisdn())) {
                audit.setMsisdn('*' + sim.getMsisdn());
            }
            if (!oldSim.getImsi().equals(sim.getImsi())) {
                audit.setImsi('*' + sim.getImsi());
            }
            if (!oldSim.getSimNumber().equals(sim.getSimNumber())) {
                audit.setSimNumber('*' + sim.getSimNumber());
            }
            if (!oldSim.getEnvironment().equals(sim.getEnvironment())) {
                audit.setEnvironment('*' + sim.getEnvironment().toString());
            }
            if (!oldSim.getSimService().equals(sim.getSimService())) {
                audit.setSimService('*' + sim.getSimService().toString());
            }
            if (!oldSim.getOperator().equals(sim.getOperator())) {
                audit.setOperator('*' + sim.getOperator().toString());
            }
            if (!oldSim.getSimState().equals(sim.getSimState())) {
                audit.setSimState('*' + sim.getSimState().toString());
            }
            if (!oldSim.getUser().getNickName().equals(sim.getUser().getNickName())) {
                audit.setUserNickname('*' + nickname);
            }
        }

        audit = auditRepository.saveAndFlush(audit);
        return audit;
    }
}