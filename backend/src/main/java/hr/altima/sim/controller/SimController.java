package hr.altima.sim.controller;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import hr.altima.sim.model.Sim;
import hr.altima.sim.model.SimAudit;
import hr.altima.sim.model.enums.ActionEnum;
import hr.altima.sim.model.enums.EnvironmentEnum;
import hr.altima.sim.model.enums.OperatorEnum;
import hr.altima.sim.model.enums.SimServiceEnum;
import hr.altima.sim.model.enums.SimStateEnum;
import hr.altima.sim.model.enums.TeamEnum;
import hr.altima.sim.pi.ProvisioningService;
import hr.altima.sim.repository.SimAuditRepository;
import hr.altima.sim.service.SimService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sims")
public class SimController {

    private final Logger logger = LoggerFactory.getLogger(SimController.class);
    private SimService simService;
    private ProvisioningService provisioningService;

    @Autowired
    public SimController(SimService simService, ProvisioningService provisioningService) {
        this.simService = simService;
        this.provisioningService = provisioningService;
    }

    @Autowired
    private SimAuditRepository simAuditRepository;


    @RequestMapping(value = "/{id}", method = GET)
    public ResponseEntity<Sim> findById(@PathVariable Integer id) {
        Sim simFromRepo = simService.findById(id);
        if (simFromRepo == null) {
            logger.error("SIM card with id {} not found", id);
            return new ResponseEntity<>(NOT_FOUND);
        }
        logger.debug("Find SIM card by id {}", id);
        return new ResponseEntity<>(simFromRepo, OK);
    }

    @RequestMapping(value = "msisdn/{msisdn}", method = GET)
    public ResponseEntity<Sim> findByMsisdn(@PathVariable("msisdn") String msisdn) {
        Sim simFromRepo = simService.findByMsisdn(msisdn);
        if (simFromRepo == null) {
            logger.error("SIM card with msisdn {} not found", msisdn);
            return new ResponseEntity<>(NO_CONTENT);
        }
        logger.debug("Find SIM card by msisdn {}", msisdn);
        return new ResponseEntity<>(simFromRepo, OK);
    }

    @RequestMapping(value = "imsi/{imsi}", method = GET)
    public ResponseEntity<Sim> findByImsi(@PathVariable("imsi") String imsi) {
        Sim simFromRepo = simService.findByImsi(imsi);
        if (simFromRepo == null) {
            logger.error("SIM card with imsi {} not found", imsi);
            return new ResponseEntity<>(NO_CONTENT);
        }
        logger.debug("Find SIM card by imsi {}", imsi);
        return new ResponseEntity<>(simFromRepo, OK);
    }

    @RequestMapping(value = "simNumber/{simNumber}", method = GET)
    public ResponseEntity<Sim> findBySimNumber(@PathVariable("simNumber") String simNumber) {
        Sim simFromRepo = simService.findBySimNumber(simNumber);
        if (simFromRepo == null) {
            logger.error("SIM card with sim number {} not found", simNumber);
            return new ResponseEntity<>(NO_CONTENT);
        }
        logger.debug("Find SIM card by sim number {}", simNumber);
        return new ResponseEntity<>(simFromRepo, OK);
    }

    @RequestMapping(value = "sim/{msisdn}", method = POST)
    public ResponseEntity<Sim> createSimFromPi(@PathVariable("msisdn") String msisdn, @RequestParam("nickname") String nickname) {
        Sim simFromRepo = simService.findByMsisdn(msisdn);
        if (simFromRepo != null) {
            logger.error("Sim with number {} already exist", msisdn);
            return new ResponseEntity<>(CONFLICT);
        }
        Sim createdSim = provisioningService.createSim(msisdn, nickname);
        return new ResponseEntity<>(createdSim, OK);
    }

    @RequestMapping(value = "sim/{msisdn}", method = PUT)
    public ResponseEntity<Sim> updateSimFromPi(@PathVariable("msisdn") String msisdn, @RequestParam("nickname") String nickname) {
        Sim simFromRepo = simService.findByMsisdn(msisdn);
        if (simFromRepo == null) {
            logger.error("Sim with number {} doesn't exist", msisdn);
            return new ResponseEntity<>(NOT_FOUND);
        }
        Sim updatedSim = provisioningService.updateSim(simFromRepo, nickname);
        return new ResponseEntity<>(updatedSim, OK);
    }

    @RequestMapping(method = GET)
    public ResponseEntity<List<Sim>> findAll() {
        List<Sim> allSims = simService.findAll();
        if (allSims.isEmpty()) {
            logger.error("List of sims is empty");
            return new ResponseEntity<>(NO_CONTENT);
        }
        logger.debug("Find all SIM cards");
        return new ResponseEntity<>(allSims, OK);
    }

    @RequestMapping(value = "env/{env}", method = GET)
    public ResponseEntity<List<Sim>> findAllByEnvironment(@PathVariable("env") EnvironmentEnum environmentEnum) {
        List<Sim> allSims = simService.findAllByEnvironment(environmentEnum);
        if (allSims.isEmpty()) {
            logger.error("Cannot find any SIM card with {} environment type", environmentEnum);
            return new ResponseEntity<>(NO_CONTENT);
        }
        logger.debug("Find all SIM cards by {} environment type", environmentEnum);
        return new ResponseEntity<>(allSims, OK);
    }

    @RequestMapping(value = "nickname/{nickname}", method = GET)
    public ResponseEntity<List<Sim>> findAllByNickname(@PathVariable("nickname") String nickName) {
        List<Sim> allSims = simService.findAllByUserNickName(nickName);
        if (allSims.isEmpty()) {
            logger.error("Cannot find any SIM card with {} user nickname", nickName);
            return new ResponseEntity<>(NO_CONTENT);
        }
        logger.debug("Find all SIM cards by {} user nickname", nickName);
        return new ResponseEntity<>(allSims, OK);
    }

    @RequestMapping(method = POST)
    public ResponseEntity<Sim> createSim(@RequestBody Sim sim) {
        if (simService.isMsisdnExist(sim)) {
            logger.error("Unable to create. A SIM with msisdn {} already exist", sim.getMsisdn());
            return new ResponseEntity<>(CONFLICT);
        }
        Sim newSim = simService.createSim(sim);
        logger.debug("Create SIM card {}", newSim.getMsisdn());
        return new ResponseEntity<>(newSim, OK);
    }

    @RequestMapping(value = "/{id}", method = PUT)
    public ResponseEntity<Sim> updateSim(@RequestBody Sim sim, @PathVariable Integer id) {
        Sim updatedSim = simService.updateSim(sim, id);
        if (updatedSim == null) {
            logger.error("Unable to update. SIM card with id {} not found", id);
            return new ResponseEntity<>(NOT_FOUND);
        }
        logger.debug("Update SIM card {}", sim.getMsisdn());
        return new ResponseEntity<>(updatedSim, OK);
    }

    @RequestMapping(value = "/{id}", method = DELETE)
    public ResponseEntity<Void> deleteSim(@PathVariable Integer id) {
        Sim existingSim = simService.findById(id);
        if (existingSim == null) {
            logger.error("Unable to delete. SIM card with id {} not found", id);
            return new ResponseEntity<>(NOT_FOUND);
        }
        simService.deleteSim(existingSim);
        logger.debug("Delete SIM card with id {}", id);
        return new ResponseEntity<>(OK);
    }

    @RequestMapping(value = "service/{service}", method = GET)
    public ResponseEntity<List<Sim>> findAllBySimService(@PathVariable SimServiceEnum service) {
        List<Sim> simsByService = simService.findAllBySimService(service);
        if (simsByService.isEmpty()) {
            logger.error("Cannot find any SIM card by {} sim service", service);
            return new ResponseEntity<>(NOT_FOUND);
        }
        logger.debug("Find all SIM cards by {} sim service", service);
        return new ResponseEntity<>(simsByService, OK);
    }

    @RequestMapping(value = "team/{team}", method = GET)
    public ResponseEntity<List<Sim>> findAllByTeam(@PathVariable TeamEnum team) {
        List<Sim> simsByTeam = simService.findAllByTeam(team);
        if (simsByTeam.isEmpty()) {
            logger.error("Cannot find any SIM card by {} team", team);
            return new ResponseEntity<>(NOT_FOUND);
        }
        logger.debug("Find all SIM cards by {} team", team);
        return new ResponseEntity<>(simsByTeam, OK);
    }

    @RequestMapping(value = "operator/{operator}", method = GET)
    public ResponseEntity<List<Sim>> findAllByOperator(@PathVariable OperatorEnum operator) {
        List<Sim> simsByOperator = simService.findAllByOperator(operator);
        if (simsByOperator.isEmpty()) {
            logger.error("Cannot find any SIM card by {} operator", operator);
            return new ResponseEntity<>(NOT_FOUND);
        }
        logger.debug("Find all SIM cards by {} operator", operator);
        return new ResponseEntity<>(simsByOperator, OK);
    }

    @RequestMapping(value = "simState/{simState}", method = GET)
    public ResponseEntity<List<Sim>> findAllBySimState(@PathVariable SimStateEnum simState) {
        List<Sim> simsByOperator = simService.findAllBySimState(simState);
        if (simsByOperator.isEmpty()) {
            logger.error("Cannot find any SIM card by {} sim state", simState);
            return new ResponseEntity<>(NOT_FOUND);
        }
        logger.debug("Find all SIM cards by {} sim state", simState);
        return new ResponseEntity<>(simsByOperator, OK);
    }

    @RequestMapping(method = GET, params = {"page", "size"})
    public ResponseEntity<Page<Sim>> findAllPaginatedSims(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        Page<Sim> allPagedSims = simService.findAllPaginatedSims(page, size);
        if (allPagedSims.isEmpty()) {
            logger.error("List of sims is empty");
            return new ResponseEntity<>(NO_CONTENT);
        }
        logger.debug("Find all SIM cards. Page: " + page + ". Size: " + size);
        return new ResponseEntity<>(allPagedSims, OK);
    }

    @RequestMapping(method = GET, params = {"page", "size", "nickname"})
    public ResponseEntity<Page<Sim>> findAllPaginatedUserSims(@RequestParam("page") Integer page, @RequestParam("size") Integer size, @RequestParam("nickname") String nickName) {
        Page<Sim> allPagedUserSims = simService.findAllPaginatedUserSims(page, size, nickName);
        if (allPagedUserSims.isEmpty()) {
            logger.error("List of sims is empty from user: " + nickName);
            return new ResponseEntity<>(NO_CONTENT);
        }
        logger.debug("Find all SIM cards. Page: " + page + ". Size: " + size + ". User: " + nickName);
        return new ResponseEntity<>(allPagedUserSims, OK);
    }

    @RequestMapping(method = GET, params = {"page", "size", "team"})
    public ResponseEntity<Page<Sim>> findAllPaginatedTeamSims(@RequestParam("page") Integer page, @RequestParam("size") Integer size, @RequestParam("team") TeamEnum teamEnum) {
        Page<Sim> allPagedTeamSims = simService.findAllPaginatedTeamSims(page, size, teamEnum);
        if (allPagedTeamSims.isEmpty()) {
            logger.error("List of sims is empty for team: " + teamEnum);
            return new ResponseEntity<>(NO_CONTENT);
        }
        logger.debug("Find all SIM cards. Page: " + page + ". Size: " + size + ". Team: " + teamEnum);
        return new ResponseEntity<>(allPagedTeamSims, OK);
    }

    @RequestMapping(method = GET, params = {"page", "size", "simService"})
    public ResponseEntity<Page<Sim>> findAllPaginatedSimServiceSims(@RequestParam("page") Integer page, @RequestParam("size") Integer size, @RequestParam("simService") String simServiceEnum) {
        Page<Sim> allPagedTeamSims = simService.findAllPaginatedSimServiceSims(page, size, simServiceEnum);
        if (allPagedTeamSims.isEmpty()) {
            logger.error("List of sims is empty for sim service: " + simServiceEnum);
            return new ResponseEntity<>(NO_CONTENT);
        }
        logger.debug("Find all SIM cards. Page: " + page + ". Size: " + size + ". Sim service: " + simServiceEnum);
        return new ResponseEntity<>(allPagedTeamSims, OK);
    }

    @RequestMapping(method = GET, params = {"page", "size", "environment"})
    public ResponseEntity<Page<Sim>> findAllPaginatedEnvironmentSims(@RequestParam("page") Integer page, @RequestParam("size") Integer size, @RequestParam("environment") String environmentEnum) {
        Page<Sim> allPagedTeamSims = simService.findAllPaginatedEnvironmentSims(page, size, environmentEnum);
        if (allPagedTeamSims.isEmpty()) {
            logger.error("List of sims is empty for environment: " + environmentEnum);
            return new ResponseEntity<>(NO_CONTENT);
        }
        logger.debug("Find all SIM cards. Page: " + page + ". Size: " + size + ". Environment: " + environmentEnum);
        return new ResponseEntity<>(allPagedTeamSims, OK);
    }

    @RequestMapping(method = GET, params = {"page", "size", "operator"})
    public ResponseEntity<Page<Sim>> findAllPaginatedOperatorSims(@RequestParam("page") Integer page, @RequestParam("size") Integer size, @RequestParam("operator") String operatorEnum) {
        Page<Sim> allPagedTeamSims = simService.findAllPaginatedOperatorSims(page, size, operatorEnum);
        if (allPagedTeamSims.isEmpty()) {
            logger.error("List of sims is empty for operator: " + operatorEnum);
            return new ResponseEntity<>(NO_CONTENT);
        }
        logger.debug("Find all SIM cards. Page: " + page + ". Size: " + size + ". Operator: " + operatorEnum);
        return new ResponseEntity<>(allPagedTeamSims, OK);
    }

    @RequestMapping(method = GET, params = {"page", "size", "simState"})
    public ResponseEntity<Page<Sim>> findAllPaginatedSimStateSims(@RequestParam("page") Integer page, @RequestParam("size") Integer size, @RequestParam("simState") String simStateEnum) {
        Page<Sim> allPagedTeamSims = simService.findAllPaginatedSimStateSims(page, size, simStateEnum);
        if (allPagedTeamSims.isEmpty()) {
            logger.error("List of sims is empty for sim state: " + simStateEnum);
            return new ResponseEntity<>(NO_CONTENT);
        }
        logger.debug("Find all SIM cards. Page: " + page + ". Size: " + size + ". State: " + simStateEnum);
        return new ResponseEntity<>(allPagedTeamSims, OK);
    }

    @RequestMapping(value = "/history", method = GET)
    public ResponseEntity<List<SimAudit>> findHistory() {
        List<SimAudit> simAudits = simService.findAllAudits();
        if (simAudits.isEmpty()) {
            return new ResponseEntity<>(NO_CONTENT);
        }
        return new ResponseEntity<>(simAudits, OK);
    }

    @RequestMapping(value = "/history", method = GET, params = {"page", "size", "simId"})
    public ResponseEntity<Page<SimAudit>> findAllPaginatedSimAuditsBySimId(@RequestParam("page") Integer page, @RequestParam("size") Integer size, @RequestParam("simId") Integer simId) {
        Page<SimAudit> simAuditPage = simService.findPaginatedSimAuditBySimId(page, size, simId);
        if (simAuditPage.isEmpty()) {
            logger.error("Cannot find any sim audits for {} sim id", simId);
            return new ResponseEntity<>(NOT_FOUND);
        }
        logger.debug("Find all Sim Audits for sim with id: " + simId);
        return new ResponseEntity<>(simAuditPage, OK);
    }

    @RequestMapping(value = "/history", method = GET, params = {"page", "size", "simId", "action"})
    public ResponseEntity<Page<SimAudit>> findAllPaginatedSimAuditsBySimIdAndAction(@RequestParam("page") Integer page, @RequestParam("size") Integer size, @RequestParam("simId") Integer simId, @RequestParam("action") ActionEnum actionEnum) {
        Page<SimAudit> simAuditPage = simService.findPaginatedSimAuditForSimByAction(page, size, simId, actionEnum);
        if (simAuditPage.isEmpty()) {
            logger.error("Cannot find any sim audits for {} sim id and action {}", simId, actionEnum);
            return new ResponseEntity<>(NOT_FOUND);
        }
        logger.debug("Find all Sim Audits for sim with id: " + simId + "and action: " + actionEnum);
        return new ResponseEntity<>(simAuditPage, OK);
    }

}