package hr.altima.sim.service;

import hr.altima.sim.AbstractDbUnitTest;
import hr.altima.sim.exception.InvalidImsiException;
import hr.altima.sim.exception.InvalidPhoneNumberException;
import hr.altima.sim.exception.InvalidSimCardNoException;
import hr.altima.sim.model.Sim;
import hr.altima.sim.model.SimAudit;
import hr.altima.sim.model.User;
import hr.altima.sim.model.enums.ActionEnum;
import hr.altima.sim.model.enums.EnvironmentEnum;
import hr.altima.sim.model.enums.OperatorEnum;
import hr.altima.sim.model.enums.SimServiceEnum;
import hr.altima.sim.model.enums.SimStateEnum;
import hr.altima.sim.model.enums.TeamEnum;
import hr.altima.sim.repository.SimAuditRepository;
import hr.altima.sim.repository.SimRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.samePropertyValuesAs;

public class SimServiceIT extends AbstractDbUnitTest {
    @Autowired
    SimService simService;

    @Autowired
    UserService userService;

    @Autowired
    SimRepository simRepository;

    @Autowired
    SimAuditRepository simAuditRepository;

    @Before
    public void init() throws Exception {
        User firstUser = userService.findById(1);
        User secondUser = userService.findById(2);

        Sim sim1 = new Sim.Builder()
                .msisdn("38763123456")
                .simNumber("8938711111222223333")
                .imsi("218031111122222")
                .user(firstUser)
                .operator(OperatorEnum.HEJ)
                .simService(SimServiceEnum.POSTPAID)
                .environment(EnvironmentEnum.TEST)
                .simState(SimStateEnum.ACTIVE)
                .build();

        Sim sim2 = new Sim.Builder()
                .msisdn("387644123456")
                .simNumber("8938711111222223334")
                .imsi("218031111122223")
                .user(secondUser)
                .operator(OperatorEnum.HALOO)
                .simService(SimServiceEnum.PREPAID)
                .environment(EnvironmentEnum.DEV)
                .simState(SimStateEnum.TERMINATED)
                .build();

        Sim sim3 = new Sim.Builder()
                .msisdn("38763123457")
                .simNumber("8938711111222223335")
                .imsi("218031111122224")
                .user(firstUser)
                .operator(OperatorEnum.HEJ)
                .simService(SimServiceEnum.POSTPAID)
                .environment(EnvironmentEnum.PRODUCTION)
                .simState(SimStateEnum.ACTIVE)
                .build();

        Sim sim4 = new Sim.Builder()
                .msisdn("387644333444")
                .simNumber("8938711111222223344")
                .imsi("218031111122244")
                .user(secondUser)
                .operator(OperatorEnum.HALOO)
                .simService(SimServiceEnum.POSTPAID)
                .environment(EnvironmentEnum.DEV)
                .simState(SimStateEnum.TERMINATED)
                .build();

        simService.createSim(sim1);
        simService.createSim(sim2);
        simService.createSim(sim3);
        simService.createSim(sim4);
    }

    @After
    public void deleteAll() {
        simAuditRepository.deleteAll();
        simRepository.deleteAll();
    }

    @Test
    public void isMsisdnExist_validMsisdn_shouldReturnTrue() {
        Sim sim = simService.findByMsisdn("38763123456");
        boolean validResult = simService.isMsisdnExist(sim);

        Assert.assertThat(validResult, is(equalTo(true)));
    }

    @Test
    public void isMsisdnExist_invalidMsisdn_shouldReturnFalse() {
        Sim sim = simService.findByMsisdn("38763123457");
        sim.setMsisdn("38736777888");
        boolean invalidResult = simService.isMsisdnExist(sim);

        Assert.assertThat(invalidResult, is(equalTo(false)));
    }

    @Test
    public void findByMsisdn_existingMsisdn_shouldReturnMsisdn() {
        Sim sim = simService.findByMsisdn("38763123456");

        Assert.assertThat(sim.getMsisdn(), is(equalTo("38763123456")));
    }

    @Test
    public void findByMsisdn_invalidMsisdns_ShouldReturnNullValue() {
        Sim sim = simService.findByMsisdn("38763321321");

        Assert.assertThat(sim, is(equalTo(null)));
    }

    @Test
    public void findAll_existingSims_shouldReturnAllSims() {
        User firstUser = userService.findById(1);
        User secondUser = userService.findById(2);
        List<Sim> sims = simService.findAll();
        Sim sim1 = new Sim();
        Sim sim2 = new Sim();
        Sim sim3 = new Sim();
        Sim sim4 = new Sim();

        for (Sim sim : sims) {
            if (sim.getMsisdn().equals("38763123456")) {
                sim1 = sim;
            } else if (sim.getMsisdn().equals("387644123456")) {
                sim2 = sim;
            } else if (sim.getMsisdn().equals("38763123457")) {
                sim3 = sim;
            } else {
                sim4 = sim;
            }
        }
        Assert.assertThat(sims.size(), is(equalTo(4)));

        Assert.assertThat(sim1.getSimNumber(), is(equalTo("8938711111222223333")));
        Assert.assertThat(sim1.getImsi(), is(equalTo("218031111122222")));
        Assert.assertThat(sim1.getUser(), samePropertyValuesAs(firstUser));
        Assert.assertThat(sim1.getOperator(), is(equalTo(OperatorEnum.HEJ)));
        Assert.assertThat(sim1.getSimService(), is(equalTo(SimServiceEnum.POSTPAID)));
        Assert.assertThat(sim1.getEnvironment(), is(equalTo(EnvironmentEnum.TEST)));
        Assert.assertThat(sim1.getSimState(), is(equalTo(SimStateEnum.ACTIVE)));

        Assert.assertThat(sim2.getSimNumber(), is(equalTo("8938711111222223334")));
        Assert.assertThat(sim2.getImsi(), is(equalTo("218031111122223")));
        Assert.assertThat(sim2.getUser(), samePropertyValuesAs(secondUser));
        Assert.assertThat(sim2.getOperator(), is(equalTo(OperatorEnum.HALOO)));
        Assert.assertThat(sim2.getSimService(), is(equalTo(SimServiceEnum.PREPAID)));
        Assert.assertThat(sim2.getEnvironment(), is(equalTo(EnvironmentEnum.DEV)));
        Assert.assertThat(sim2.getSimState(), is(equalTo(SimStateEnum.TERMINATED)));

        Assert.assertThat(sim3.getSimNumber(), is(equalTo("8938711111222223335")));
        Assert.assertThat(sim3.getImsi(), is(equalTo("218031111122224")));
        Assert.assertThat(sim3.getUser(), samePropertyValuesAs(firstUser));
        Assert.assertThat(sim3.getOperator(), is(equalTo(OperatorEnum.HEJ)));
        Assert.assertThat(sim3.getSimService(), is(equalTo(SimServiceEnum.POSTPAID)));
        Assert.assertThat(sim3.getEnvironment(), is(equalTo(EnvironmentEnum.PRODUCTION)));
        Assert.assertThat(sim3.getSimState(), is(equalTo(SimStateEnum.ACTIVE)));

        Assert.assertThat(sim4.getSimNumber(), is(equalTo("8938711111222223344")));
        Assert.assertThat(sim4.getImsi(), is(equalTo("218031111122244")));
        Assert.assertThat(sim4.getUser(), samePropertyValuesAs(secondUser));
        Assert.assertThat(sim4.getOperator(), is(equalTo(OperatorEnum.HALOO)));
        Assert.assertThat(sim4.getSimService(), is(equalTo(SimServiceEnum.POSTPAID)));
        Assert.assertThat(sim4.getEnvironment(), is(equalTo(EnvironmentEnum.DEV)));
        Assert.assertThat(sim4.getSimState(), is(equalTo(SimStateEnum.TERMINATED)));
    }

    @Test
    public void findAllPaginatedSims() {
        Page<Sim> sims = simService.findAllPaginatedSims(0, 4);

        User firstUser = userService.findById(1);
        User secondUser = userService.findById(2);
        Sim sim1 = new Sim();
        Sim sim2 = new Sim();
        Sim sim3 = new Sim();
        Sim sim4 = new Sim();

        for (Sim sim : sims) {
            if (sim.getMsisdn().equals("38763123456")) {
                sim1 = sim;
            } else if (sim.getMsisdn().equals("387644123456")) {
                sim2 = sim;
            } else if (sim.getMsisdn().equals("38763123457")) {
                sim3 = sim;
            } else {
                sim4 = sim;
            }
        }
        Assert.assertThat(sims.getTotalPages(), is(equalTo(1)));
        Assert.assertThat(sims.getNumberOfElements(), is(equalTo(4)));
        Assert.assertThat(sims.getTotalElements(), is(equalTo(4L)));

        Assert.assertThat(sim1.getSimNumber(), is(equalTo("8938711111222223333")));
        Assert.assertThat(sim1.getImsi(), is(equalTo("218031111122222")));
        Assert.assertThat(sim1.getUser(), samePropertyValuesAs(firstUser));
        Assert.assertThat(sim1.getOperator(), is(equalTo(OperatorEnum.HEJ)));
        Assert.assertThat(sim1.getSimService(), is(equalTo(SimServiceEnum.POSTPAID)));
        Assert.assertThat(sim1.getEnvironment(), is(equalTo(EnvironmentEnum.TEST)));
        Assert.assertThat(sim1.getSimState(), is(equalTo(SimStateEnum.ACTIVE)));

        Assert.assertThat(sim2.getSimNumber(), is(equalTo("8938711111222223334")));
        Assert.assertThat(sim2.getImsi(), is(equalTo("218031111122223")));
        Assert.assertThat(sim2.getUser(), samePropertyValuesAs(secondUser));
        Assert.assertThat(sim2.getOperator(), is(equalTo(OperatorEnum.HALOO)));
        Assert.assertThat(sim2.getSimService(), is(equalTo(SimServiceEnum.PREPAID)));
        Assert.assertThat(sim2.getEnvironment(), is(equalTo(EnvironmentEnum.DEV)));
        Assert.assertThat(sim2.getSimState(), is(equalTo(SimStateEnum.TERMINATED)));

        Assert.assertThat(sim3.getSimNumber(), is(equalTo("8938711111222223335")));
        Assert.assertThat(sim3.getImsi(), is(equalTo("218031111122224")));
        Assert.assertThat(sim3.getUser(), samePropertyValuesAs(firstUser));
        Assert.assertThat(sim3.getOperator(), is(equalTo(OperatorEnum.HEJ)));
        Assert.assertThat(sim3.getSimService(), is(equalTo(SimServiceEnum.POSTPAID)));
        Assert.assertThat(sim3.getEnvironment(), is(equalTo(EnvironmentEnum.PRODUCTION)));
        Assert.assertThat(sim3.getSimState(), is(equalTo(SimStateEnum.ACTIVE)));

        Assert.assertThat(sim4.getSimNumber(), is(equalTo("8938711111222223344")));
        Assert.assertThat(sim4.getImsi(), is(equalTo("218031111122244")));
        Assert.assertThat(sim4.getUser(), samePropertyValuesAs(secondUser));
        Assert.assertThat(sim4.getOperator(), is(equalTo(OperatorEnum.HALOO)));
        Assert.assertThat(sim4.getSimService(), is(equalTo(SimServiceEnum.POSTPAID)));
        Assert.assertThat(sim4.getEnvironment(), is(equalTo(EnvironmentEnum.DEV)));
        Assert.assertThat(sim4.getSimState(), is(equalTo(SimStateEnum.TERMINATED)));
    }

    @Test
    public void findAllPaginatedUserSims() {
        Page<Sim> sims = simService.findAllPaginatedUserSims(0, 2, "Pesa");

        User user = userService.findById(1);
        Sim sim1 = new Sim();
        Sim sim2 = new Sim();
        for (Sim sim : sims) {
            if (sim.getMsisdn().equals("38763123456")) {
                sim1 = sim;
            } else {
                sim2 = sim;
            }
        }
        Assert.assertThat(sims.getTotalPages(), is(equalTo(1)));
        Assert.assertThat(sims.getNumberOfElements(), is(equalTo(2)));
        Assert.assertThat(sims.getTotalElements(), is(equalTo(2L)));

        Assert.assertThat(sim1.getSimNumber(), is(equalTo("8938711111222223333")));
        Assert.assertThat(sim1.getImsi(), is(equalTo("218031111122222")));
        Assert.assertThat(sim1.getUser(), samePropertyValuesAs(user));
        Assert.assertThat(sim1.getOperator(), is(equalTo(OperatorEnum.HEJ)));
        Assert.assertThat(sim1.getSimService(), is(equalTo(SimServiceEnum.POSTPAID)));
        Assert.assertThat(sim1.getEnvironment(), is(equalTo(EnvironmentEnum.TEST)));
        Assert.assertThat(sim1.getSimState(), is(equalTo(SimStateEnum.ACTIVE)));

        Assert.assertThat(sim2.getSimNumber(), is(equalTo("8938711111222223335")));
        Assert.assertThat(sim2.getImsi(), is(equalTo("218031111122224")));
        Assert.assertThat(sim2.getUser(), samePropertyValuesAs(user));
        Assert.assertThat(sim2.getOperator(), is(equalTo(OperatorEnum.HEJ)));
        Assert.assertThat(sim2.getSimService(), is(equalTo(SimServiceEnum.POSTPAID)));
        Assert.assertThat(sim2.getEnvironment(), is(equalTo(EnvironmentEnum.PRODUCTION)));
        Assert.assertThat(sim2.getSimState(), is(equalTo(SimStateEnum.ACTIVE)));
    }

    @Test
    public void findAllPaginatedTeamSims() {
        Page<Sim> sims = simService.findAllPaginatedTeamSims(0, 2, TeamEnum.DEV);

        User user = userService.findById(1);
        Sim sim1 = new Sim();
        Sim sim2 = new Sim();
        for (Sim sim : sims) {
            if (sim.getMsisdn().equals("38763123456")) {
                sim1 = sim;
            } else {
                sim2 = sim;
            }
        }
        Assert.assertThat(sims.getTotalPages(), is(equalTo(1)));
        Assert.assertThat(sims.getNumberOfElements(), is(equalTo(2)));
        Assert.assertThat(sims.getTotalElements(), is(equalTo(2L)));

        Assert.assertThat(sim1.getSimNumber(), is(equalTo("8938711111222223333")));
        Assert.assertThat(sim1.getImsi(), is(equalTo("218031111122222")));
        Assert.assertThat(sim1.getUser(), samePropertyValuesAs(user));
        Assert.assertThat(sim1.getOperator(), is(equalTo(OperatorEnum.HEJ)));
        Assert.assertThat(sim1.getSimService(), is(equalTo(SimServiceEnum.POSTPAID)));
        Assert.assertThat(sim1.getEnvironment(), is(equalTo(EnvironmentEnum.TEST)));
        Assert.assertThat(sim1.getSimState(), is(equalTo(SimStateEnum.ACTIVE)));

        Assert.assertThat(sim2.getSimNumber(), is(equalTo("8938711111222223335")));
        Assert.assertThat(sim2.getImsi(), is(equalTo("218031111122224")));
        Assert.assertThat(sim2.getUser(), samePropertyValuesAs(user));
        Assert.assertThat(sim2.getOperator(), is(equalTo(OperatorEnum.HEJ)));
        Assert.assertThat(sim2.getSimService(), is(equalTo(SimServiceEnum.POSTPAID)));
        Assert.assertThat(sim2.getEnvironment(), is(equalTo(EnvironmentEnum.PRODUCTION)));
        Assert.assertThat(sim2.getSimState(), is(equalTo(SimStateEnum.ACTIVE)));
    }

    @Test
    public void findAllPaginatedEnvironmentSims() {
        Page<Sim> sims = simService.findAllPaginatedEnvironmentSims(0, 2, EnvironmentEnum.DEV.toString());

        User user = userService.findById(2);
        Sim sim1 = new Sim();
        Sim sim2 = new Sim();
        for (Sim sim : sims) {
            if (sim.getMsisdn().equals("387644123456")) {
                sim1 = sim;
            } else {
                sim2 = sim;
            }
        }
        Assert.assertThat(sims.getTotalPages(), is(equalTo(1)));
        Assert.assertThat(sims.getNumberOfElements(), is(equalTo(2)));
        Assert.assertThat(sims.getTotalElements(), is(equalTo(2L)));

        Assert.assertThat(sim1.getSimNumber(), is(equalTo("8938711111222223334")));
        Assert.assertThat(sim1.getImsi(), is(equalTo("218031111122223")));
        Assert.assertThat(sim1.getUser(), samePropertyValuesAs(user));
        Assert.assertThat(sim1.getOperator(), is(equalTo(OperatorEnum.HALOO)));
        Assert.assertThat(sim1.getSimService(), is(equalTo(SimServiceEnum.PREPAID)));
        Assert.assertThat(sim1.getEnvironment(), is(equalTo(EnvironmentEnum.DEV)));
        Assert.assertThat(sim1.getSimState(), is(equalTo(SimStateEnum.TERMINATED)));

        Assert.assertThat(sim2.getSimNumber(), is(equalTo("8938711111222223344")));
        Assert.assertThat(sim2.getImsi(), is(equalTo("218031111122244")));
        Assert.assertThat(sim2.getUser(), samePropertyValuesAs(user));
        Assert.assertThat(sim2.getOperator(), is(equalTo(OperatorEnum.HALOO)));
        Assert.assertThat(sim2.getSimService(), is(equalTo(SimServiceEnum.POSTPAID)));
        Assert.assertThat(sim2.getEnvironment(), is(equalTo(EnvironmentEnum.DEV)));
        Assert.assertThat(sim2.getSimState(), is(equalTo(SimStateEnum.TERMINATED)));
    }

    @Test
    public void findAllPaginatedSimServiceSims() {
        Page<Sim> sims = simService.findAllPaginatedSimServiceSims(0, 1, SimServiceEnum.PREPAID.toString());

        User user = userService.findById(2);
        Sim sim1 = new Sim();
        for (Sim sim : sims) {
            if (sim.getMsisdn().equals("387644123456")) {
                sim1 = sim;
            }
        }
        Assert.assertThat(sims.getTotalPages(), is(equalTo(1)));
        Assert.assertThat(sims.getNumberOfElements(), is(equalTo(1)));
        Assert.assertThat(sims.getTotalElements(), is(equalTo(1L)));

        Assert.assertThat(sim1.getSimNumber(), is(equalTo("8938711111222223334")));
        Assert.assertThat(sim1.getImsi(), is(equalTo("218031111122223")));
        Assert.assertThat(sim1.getUser(), samePropertyValuesAs(user));
        Assert.assertThat(sim1.getOperator(), is(equalTo(OperatorEnum.HALOO)));
        Assert.assertThat(sim1.getSimService(), is(equalTo(SimServiceEnum.PREPAID)));
        Assert.assertThat(sim1.getEnvironment(), is(equalTo(EnvironmentEnum.DEV)));
        Assert.assertThat(sim1.getSimState(), is(equalTo(SimStateEnum.TERMINATED)));
    }

    @Test
    public void findAllPaginatedOperatorSims() {
        Page<Sim> sims = simService.findAllPaginatedOperatorSims(0, 2, OperatorEnum.HEJ.toString());

        User user = userService.findById(1);
        Sim sim1 = new Sim();
        Sim sim2 = new Sim();
        for (Sim sim : sims) {
            if (sim.getMsisdn().equals("38763123456")) {
                sim1 = sim;
            } else {
                sim2 = sim;
            }
        }
        Assert.assertThat(sims.getTotalPages(), is(equalTo(1)));
        Assert.assertThat(sims.getNumberOfElements(), is(equalTo(2)));
        Assert.assertThat(sims.getTotalElements(), is(equalTo(2L)));

        Assert.assertThat(sim1.getSimNumber(), is(equalTo("8938711111222223333")));
        Assert.assertThat(sim1.getImsi(), is(equalTo("218031111122222")));
        Assert.assertThat(sim1.getUser(), samePropertyValuesAs(user));
        Assert.assertThat(sim1.getOperator(), is(equalTo(OperatorEnum.HEJ)));
        Assert.assertThat(sim1.getSimService(), is(equalTo(SimServiceEnum.POSTPAID)));
        Assert.assertThat(sim1.getEnvironment(), is(equalTo(EnvironmentEnum.TEST)));
        Assert.assertThat(sim1.getSimState(), is(equalTo(SimStateEnum.ACTIVE)));

        Assert.assertThat(sim2.getSimNumber(), is(equalTo("8938711111222223335")));
        Assert.assertThat(sim2.getImsi(), is(equalTo("218031111122224")));
        Assert.assertThat(sim2.getUser(), samePropertyValuesAs(user));
        Assert.assertThat(sim2.getOperator(), is(equalTo(OperatorEnum.HEJ)));
        Assert.assertThat(sim2.getSimService(), is(equalTo(SimServiceEnum.POSTPAID)));
        Assert.assertThat(sim2.getEnvironment(), is(equalTo(EnvironmentEnum.PRODUCTION)));
        Assert.assertThat(sim2.getSimState(), is(equalTo(SimStateEnum.ACTIVE)));
    }

    @Test
    public void findAllPaginatedSimStateSims() {
        Page<Sim> sims = simService.findAllPaginatedSimStateSims(0, 2, SimStateEnum.ACTIVE.toString());

        User user = userService.findById(1);
        Sim sim1 = new Sim();
        Sim sim2 = new Sim();
        for (Sim sim : sims) {
            if (sim.getMsisdn().equals("38763123456")) {
                sim1 = sim;
            } else {
                sim2 = sim;
            }
        }
        Assert.assertThat(sims.getTotalPages(), is(equalTo(1)));
        Assert.assertThat(sims.getNumberOfElements(), is(equalTo(2)));
        Assert.assertThat(sims.getTotalElements(), is(equalTo(2L)));

        Assert.assertThat(sim1.getSimNumber(), is(equalTo("8938711111222223333")));
        Assert.assertThat(sim1.getImsi(), is(equalTo("218031111122222")));
        Assert.assertThat(sim1.getUser(), samePropertyValuesAs(user));
        Assert.assertThat(sim1.getOperator(), is(equalTo(OperatorEnum.HEJ)));
        Assert.assertThat(sim1.getSimService(), is(equalTo(SimServiceEnum.POSTPAID)));
        Assert.assertThat(sim1.getEnvironment(), is(equalTo(EnvironmentEnum.TEST)));
        Assert.assertThat(sim1.getSimState(), is(equalTo(SimStateEnum.ACTIVE)));

        Assert.assertThat(sim2.getSimNumber(), is(equalTo("8938711111222223335")));
        Assert.assertThat(sim2.getImsi(), is(equalTo("218031111122224")));
        Assert.assertThat(sim2.getUser(), samePropertyValuesAs(user));
        Assert.assertThat(sim2.getOperator(), is(equalTo(OperatorEnum.HEJ)));
        Assert.assertThat(sim2.getSimService(), is(equalTo(SimServiceEnum.POSTPAID)));
        Assert.assertThat(sim2.getEnvironment(), is(equalTo(EnvironmentEnum.PRODUCTION)));
        Assert.assertThat(sim2.getSimState(), is(equalTo(SimStateEnum.ACTIVE)));
    }

    @Test
    public void findAllByEnvironment_findExistingSim_ShouldReturnSim() {
        User user = userService.findById(2);
        List<Sim> sims = simService.findAllByEnvironment(EnvironmentEnum.DEV);
        Sim sim1 = new Sim();
        Sim sim2 = new Sim();

        for (Sim sim : sims) {
            if (sim.getMsisdn().equals("387644123456")) {
                sim1 = sim;
            } else {
                sim2 = sim;
            }
        }
        Assert.assertThat(sims.size(), is(equalTo(2)));

        Assert.assertThat(sim1.getSimNumber(), is(equalTo("8938711111222223334")));
        Assert.assertThat(sim1.getImsi(), is(equalTo("218031111122223")));
        Assert.assertThat(sim1.getUser(), samePropertyValuesAs(user));
        Assert.assertThat(sim1.getOperator(), is(equalTo(OperatorEnum.HALOO)));
        Assert.assertThat(sim1.getSimService(), is(equalTo(SimServiceEnum.PREPAID)));
        Assert.assertThat(sim1.getEnvironment(), is(equalTo(EnvironmentEnum.DEV)));
        Assert.assertThat(sim1.getSimState(), is(equalTo(SimStateEnum.TERMINATED)));

        Assert.assertThat(sim2.getSimNumber(), is(equalTo("8938711111222223344")));
        Assert.assertThat(sim2.getImsi(), is(equalTo("218031111122244")));
        Assert.assertThat(sim2.getUser(), samePropertyValuesAs(user));
        Assert.assertThat(sim2.getOperator(), is(equalTo(OperatorEnum.HALOO)));
        Assert.assertThat(sim2.getSimService(), is(equalTo(SimServiceEnum.POSTPAID)));
        Assert.assertThat(sim2.getEnvironment(), is(equalTo(EnvironmentEnum.DEV)));
        Assert.assertThat(sim2.getSimState(), is(equalTo(SimStateEnum.TERMINATED)));
    }

    @Test
    public void findAllByUserNickname_findExistingSim_shouldReturnSim() {
        User user = userService.findById(1);
        List<Sim> sims = simService.findAllByUserNickName("Pesa");

        Sim sim1 = new Sim();
        Sim sim2 = new Sim();

        for (Sim sim : sims) {
            if (sim.getMsisdn().equals("38763123456")) {
                sim1 = sim;
            } else {
                sim2 = sim;
            }
        }
        Assert.assertThat(sims.size(), is(equalTo(2)));

        Assert.assertThat(sim1.getSimNumber(), is(equalTo("8938711111222223333")));
        Assert.assertThat(sim1.getImsi(), is(equalTo("218031111122222")));
        Assert.assertThat(sim1.getUser(), samePropertyValuesAs(user));
        Assert.assertThat(sim1.getOperator(), is(equalTo(OperatorEnum.HEJ)));
        Assert.assertThat(sim1.getSimService(), is(equalTo(SimServiceEnum.POSTPAID)));
        Assert.assertThat(sim1.getEnvironment(), is(equalTo(EnvironmentEnum.TEST)));
        Assert.assertThat(sim1.getSimState(), is(equalTo(SimStateEnum.ACTIVE)));

        Assert.assertThat(sim2.getSimNumber(), is(equalTo("8938711111222223335")));
        Assert.assertThat(sim2.getImsi(), is(equalTo("218031111122224")));
        Assert.assertThat(sim2.getUser(), samePropertyValuesAs(user));
        Assert.assertThat(sim2.getOperator(), is(equalTo(OperatorEnum.HEJ)));
        Assert.assertThat(sim2.getSimService(), is(equalTo(SimServiceEnum.POSTPAID)));
        Assert.assertThat(sim2.getEnvironment(), is(equalTo(EnvironmentEnum.PRODUCTION)));
        Assert.assertThat(sim2.getSimState(), is(equalTo(SimStateEnum.ACTIVE)));
    }

    @Test
    public void createSim_newValidUser_userCreated() throws Exception {
        User firstUser = userService.findById(1);
        Sim sim = new Sim.Builder()
                .msisdn("38763789789")
                .imsi("218031111122227")
                .user(firstUser)
                .simNumber("8938711111222223337")
                .operator(OperatorEnum.HEJ)
                .simService(SimServiceEnum.POSTPAID)
                .environment(EnvironmentEnum.TEST)
                .simState(SimStateEnum.TERMINATED)
                .build();

        Sim createdSim = simService.createSim(sim);

        List<SimAudit> simAudits = simAuditRepository.findAllByIdAndAction(sim.getId(), ActionEnum.ADD);

        SimAudit simAuditCreate = simAudits.get(simAudits.size() - 1);

        Assert.assertThat(createdSim.getMsisdn(), is(equalTo(sim.getMsisdn())));
        Assert.assertThat(createdSim.getImsi(), is(equalTo(sim.getImsi())));
        Assert.assertThat(createdSim.getUser(), is(equalTo(sim.getUser())));
        Assert.assertThat(createdSim.getSimNumber(), is(equalTo(sim.getSimNumber())));
        Assert.assertThat(createdSim.getOperator(), is(equalTo(sim.getOperator())));
        Assert.assertThat(createdSim.getSimService(), is(equalTo(sim.getSimService())));
        Assert.assertThat(createdSim.getEnvironment(), is(equalTo(sim.getEnvironment())));
        Assert.assertThat(createdSim.getSimState(), is(equalTo(sim.getSimState())));

        Assert.assertThat(simAuditCreate.getId(), is(equalTo(sim.getId())));
        Assert.assertThat(simAuditCreate.getAction(), is(equalTo(ActionEnum.ADD)));
        Assert.assertThat(simAuditCreate.getMsisdn(), is(equalTo(sim.getMsisdn())));
        Assert.assertThat(simAuditCreate.getImsi(), is(equalTo(sim.getImsi())));
        Assert.assertThat(simAuditCreate.getSimNumber(), is(equalTo(sim.getSimNumber())));
        Assert.assertThat(simAuditCreate.getEnvironment(), is(equalTo(sim.getEnvironment().toString())));
        Assert.assertThat(simAuditCreate.getOperator(), is(equalTo(sim.getOperator().toString())));
        Assert.assertThat(simAuditCreate.getSimService(), is(equalTo(sim.getSimService().toString())));
        Assert.assertThat(simAuditCreate.getSimState(), is(equalTo(sim.getSimState().toString())));
        Assert.assertThat(simAuditCreate.getUserNickname(), is(equalTo(sim.getUser().getNickName())));
    }

    @Test(expected = InvalidPhoneNumberException.class)
    public void createSim_invalidMsisdn_shouldTrowException() throws Exception {
        User firstUser = userService.findById(1);
        Sim sim = new Sim.Builder()
                .msisdn("38563456456")
                .imsi("218031111122228")
                .user(firstUser)
                .simNumber("8938711111222223338")
                .operator(OperatorEnum.HEJ)
                .simService(SimServiceEnum.PREPAID)
                .environment(EnvironmentEnum.TEST)
                .simState(SimStateEnum.ACTIVE)
                .build();

        simService.createSim(sim);
    }

    @Test(expected = InvalidImsiException.class)
    public void createSim_invalidImsi_shouldTrowException() throws Exception {
        User secondUser = userService.findById(2);
        Sim sim = new Sim.Builder()
                .msisdn("387644789789")
                .imsi("118031111122228")
                .user(secondUser)
                .simNumber("8938711111222223339")
                .operator(OperatorEnum.HALOO)
                .simService(SimServiceEnum.POSTPAID)
                .environment(EnvironmentEnum.TEST)
                .simState(SimStateEnum.TERMINATED)
                .build();

        simService.createSim(sim);
    }

    @Test(expected = InvalidSimCardNoException.class)
    public void createSim_invalidSimNumber_shouldTrowException() throws Exception {
        User secondUser = userService.findById(2);
        Sim sim = new Sim.Builder()
                .msisdn("387644789780")
                .imsi("218031111122228")
                .user(secondUser)
                .simNumber("0938711111222223339")
                .operator(OperatorEnum.HALOO)
                .simService(SimServiceEnum.PREPAID)
                .environment(EnvironmentEnum.DEV)
                .simState(SimStateEnum.TERMINATED)
                .build();

        simService.createSim(sim);
    }

    @Test
    public void updateSim_updateValidSim_ShouldReturnSim() throws Exception {
        User user = userService.findById(2);
        Sim sim = simService.findByMsisdn("38763123456");
        sim.setImsi("218031111122111");
        sim.setUser(user);
        sim.setSimNumber("8938711111222223111");

        Sim updatedSim = simService.updateSim(sim, sim.getId());

        List<SimAudit> simAudits = simAuditRepository.findAllByIdAndAction(sim.getId(), ActionEnum.MOD);

        SimAudit simAuditUpdate = simAudits.get(simAudits.size() - 1);

        Assert.assertThat(updatedSim.getImsi(), is(equalTo("218031111122111")));

        Assert.assertThat(updatedSim.getUser().getId(), is(equalTo(sim.getUser().getId())));
        Assert.assertThat(updatedSim.getUser().getFirstName(), is(equalTo(sim.getUser().getFirstName())));
        Assert.assertThat(updatedSim.getUser().getLastName(), is(equalTo(sim.getUser().getLastName())));
        Assert.assertThat(updatedSim.getUser().getNickName(), is(equalTo(sim.getUser().getNickName())));
        Assert.assertThat(updatedSim.getUser().geteMail(), is(equalTo(sim.getUser().geteMail())));
        Assert.assertThat(updatedSim.getUser().getTeam(), is(equalTo(sim.getUser().getTeam())));

        Assert.assertThat(updatedSim.getSimNumber(), is(equalTo("8938711111222223111")));

        Assert.assertThat(simAuditUpdate.getId(), is(equalTo(sim.getId())));
        Assert.assertThat(simAuditUpdate.getAction(), is(equalTo(ActionEnum.MOD)));
        Assert.assertThat(simAuditUpdate.getMsisdn(), is(equalTo(sim.getMsisdn())));
        Assert.assertThat(simAuditUpdate.getImsi(), is(equalTo('*' + sim.getImsi())));
        Assert.assertThat(simAuditUpdate.getSimNumber(), is(equalTo('*' + sim.getSimNumber())));
        Assert.assertThat(simAuditUpdate.getEnvironment(), is(equalTo(sim.getEnvironment().toString())));
        Assert.assertThat(simAuditUpdate.getOperator(), is(equalTo(sim.getOperator().toString())));
        Assert.assertThat(simAuditUpdate.getSimService(), is(equalTo(sim.getSimService().toString())));
        Assert.assertThat(simAuditUpdate.getSimState(), is(equalTo(sim.getSimState().toString())));
        Assert.assertThat(simAuditUpdate.getUserNickname(), is(equalTo('*' + sim.getUser().getNickName())));
    }

    @Test(expected = InvalidPhoneNumberException.class)
    public void updateSim_invalidMsisdn_shouldThrowException() throws Exception {
        Sim sim = simService.findByMsisdn("387644123456");
        sim.setMsisdn("387624789711");

        simService.updateSim(sim, sim.getId());
    }

    @Test(expected = InvalidSimCardNoException.class)
    public void updateSim_invalidSimNumber_shouldThrowException() throws Exception {
        Sim sim = simService.findByMsisdn("38763123457");
        sim.setSimNumber("0938711111222223300");

        simService.updateSim(sim, sim.getId());
    }

    @Test(expected = InvalidImsiException.class)
    public void updateSim_invalidImsi_shouldThrowException() throws Exception {
        Sim sim = simService.findByMsisdn("387644123456");
        sim.setImsi("118031111122200");

        simService.updateSim(sim, sim.getId());
    }

    @Test
    public void deleteSim_deleteValidSim_simDeleted() {
        Sim existingSim = simService.findByMsisdn("38763123456");
        simService.deleteSim(existingSim);
        Sim deletedSim = simService.findByMsisdn("38763123456");

        List<SimAudit> simAudits = simAuditRepository.findAllByIdAndAction(existingSim.getId(), ActionEnum.DEL);

        SimAudit simAuditDel = simAudits.get(simAudits.size() - 1);

        Assert.assertThat(deletedSim, is(equalTo(null)));

        Assert.assertThat(simAuditDel.getId(), is(equalTo(existingSim.getId())));
        Assert.assertThat(simAuditDel.getAction(), is(equalTo(ActionEnum.DEL)));
        Assert.assertThat(simAuditDel.getMsisdn(), is(equalTo(existingSim.getMsisdn())));
        Assert.assertThat(simAuditDel.getImsi(), is(equalTo(existingSim.getImsi())));
        Assert.assertThat(simAuditDel.getSimNumber(), is(equalTo(existingSim.getSimNumber())));
        Assert.assertThat(simAuditDel.getEnvironment(), is(equalTo(existingSim.getEnvironment().toString())));
        Assert.assertThat(simAuditDel.getOperator(), is(equalTo(existingSim.getOperator().toString())));
        Assert.assertThat(simAuditDel.getSimService(), is(equalTo(existingSim.getSimService().toString())));
        Assert.assertThat(simAuditDel.getSimState(), is(equalTo(existingSim.getSimState().toString())));
        Assert.assertThat(simAuditDel.getUserNickname(), is(equalTo(existingSim.getUser().getNickName())));
    }

    @Test
    public void findAllBySimService_findExistingSim_returnSim() {
        User firstUser = userService.findById(1);
        User secondUser = userService.findById(2);
        List<Sim> sims = simService.findAllBySimService(SimServiceEnum.POSTPAID);
        Sim sim1 = new Sim();
        Sim sim2 = new Sim();
        Sim sim3 = new Sim();

        for (Sim sim : sims) {
            if (sim.getMsisdn().equals("38763123456")) {
                sim1 = sim;
            } else if (sim.getMsisdn().equals("38763123457")) {
                sim2 = sim;
            } else {
                sim3 = sim;
            }
        }
        Assert.assertThat(sims.size(), is(equalTo(3)));

        Assert.assertThat(sim1.getSimNumber(), is(equalTo("8938711111222223333")));
        Assert.assertThat(sim1.getImsi(), is(equalTo("218031111122222")));
        Assert.assertThat(sim1.getUser(), samePropertyValuesAs(firstUser));
        Assert.assertThat(sim1.getOperator(), is(equalTo(OperatorEnum.HEJ)));
        Assert.assertThat(sim1.getSimService(), is(equalTo(SimServiceEnum.POSTPAID)));
        Assert.assertThat(sim1.getEnvironment(), is(equalTo(EnvironmentEnum.TEST)));
        Assert.assertThat(sim1.getSimState(), is(equalTo(SimStateEnum.ACTIVE)));

        Assert.assertThat(sim2.getSimNumber(), is(equalTo("8938711111222223335")));
        Assert.assertThat(sim2.getImsi(), is(equalTo("218031111122224")));
        Assert.assertThat(sim2.getUser(), samePropertyValuesAs(firstUser));
        Assert.assertThat(sim2.getOperator(), is(equalTo(OperatorEnum.HEJ)));
        Assert.assertThat(sim2.getSimService(), is(equalTo(SimServiceEnum.POSTPAID)));
        Assert.assertThat(sim2.getEnvironment(), is(equalTo(EnvironmentEnum.PRODUCTION)));
        Assert.assertThat(sim2.getSimState(), is(equalTo(SimStateEnum.ACTIVE)));

        Assert.assertThat(sim3.getSimNumber(), is(equalTo("8938711111222223344")));
        Assert.assertThat(sim3.getImsi(), is(equalTo("218031111122244")));
        Assert.assertThat(sim3.getUser(), samePropertyValuesAs(secondUser));
        Assert.assertThat(sim3.getOperator(), is(equalTo(OperatorEnum.HALOO)));
        Assert.assertThat(sim3.getSimService(), is(equalTo(SimServiceEnum.POSTPAID)));
        Assert.assertThat(sim3.getEnvironment(), is(equalTo(EnvironmentEnum.DEV)));
        Assert.assertThat(sim3.getSimState(), is(equalTo(SimStateEnum.TERMINATED)));
    }

    @Test
    public void findAllByTeam_findExistingSim_ShouldReturnSim() {
        User user = userService.findById(2);
        List<Sim> sims = simService.findAllByTeam(TeamEnum.OC);
        Sim sim1 = new Sim();
        Sim sim2 = new Sim();

        for (Sim sim : sims) {
            if (sim.getMsisdn().equals("387644123456")) {
                sim1 = sim;
            } else {
                sim2 = sim;
            }
        }
        Assert.assertThat(sims.size(), is(equalTo(2)));

        Assert.assertThat(sim1.getSimNumber(), is(equalTo("8938711111222223334")));
        Assert.assertThat(sim1.getImsi(), is(equalTo("218031111122223")));
        Assert.assertThat(sim1.getUser(), samePropertyValuesAs(user));
        Assert.assertThat(sim1.getOperator(), is(equalTo(OperatorEnum.HALOO)));
        Assert.assertThat(sim1.getSimService(), is(equalTo(SimServiceEnum.PREPAID)));
        Assert.assertThat(sim1.getEnvironment(), is(equalTo(EnvironmentEnum.DEV)));
        Assert.assertThat(sim1.getSimState(), is(equalTo(SimStateEnum.TERMINATED)));

        Assert.assertThat(sim2.getSimNumber(), is(equalTo("8938711111222223344")));
        Assert.assertThat(sim2.getImsi(), is(equalTo("218031111122244")));
        Assert.assertThat(sim2.getUser(), samePropertyValuesAs(user));
        Assert.assertThat(sim2.getOperator(), is(equalTo(OperatorEnum.HALOO)));
        Assert.assertThat(sim2.getSimService(), is(equalTo(SimServiceEnum.POSTPAID)));
        Assert.assertThat(sim2.getEnvironment(), is(equalTo(EnvironmentEnum.DEV)));
        Assert.assertThat(sim2.getSimState(), is(equalTo(SimStateEnum.TERMINATED)));
    }

    @Test
    public void findAllByOperator_findExistingSim_shouldReturnSim() {
        User user = userService.findById(1);
        List<Sim> sims = simService.findAllByOperator(OperatorEnum.HEJ);
        Sim sim1 = new Sim();
        Sim sim2 = new Sim();

        for (Sim sim : sims) {
            if (sim.getMsisdn().equals("38763123456")) {
                sim1 = sim;
            } else {
                sim2 = sim;
            }
        }
        Assert.assertThat(sims.size(), is(equalTo(2)));

        Assert.assertThat(sim1.getSimNumber(), is(equalTo("8938711111222223333")));
        Assert.assertThat(sim1.getImsi(), is(equalTo("218031111122222")));
        Assert.assertThat(sim1.getUser(), samePropertyValuesAs(user));
        Assert.assertThat(sim1.getOperator(), is(equalTo(OperatorEnum.HEJ)));
        Assert.assertThat(sim1.getSimService(), is(equalTo(SimServiceEnum.POSTPAID)));
        Assert.assertThat(sim1.getEnvironment(), is(equalTo(EnvironmentEnum.TEST)));
        Assert.assertThat(sim1.getSimState(), is(equalTo(SimStateEnum.ACTIVE)));

        Assert.assertThat(sim2.getSimNumber(), is(equalTo("8938711111222223335")));
        Assert.assertThat(sim2.getImsi(), is(equalTo("218031111122224")));
        Assert.assertThat(sim2.getUser(), samePropertyValuesAs(user));
        Assert.assertThat(sim2.getOperator(), is(equalTo(OperatorEnum.HEJ)));
        Assert.assertThat(sim2.getSimService(), is(equalTo(SimServiceEnum.POSTPAID)));
        Assert.assertThat(sim2.getEnvironment(), is(equalTo(EnvironmentEnum.PRODUCTION)));
        Assert.assertThat(sim2.getSimState(), is(equalTo(SimStateEnum.ACTIVE)));
    }

    @Test
    public void findAllBySimState_findExistingSim_shouldReturnSim() {
        User user = userService.findById(1);
        List<Sim> sims = simService.findAllBySimState(SimStateEnum.ACTIVE);
        Sim sim1 = new Sim();
        Sim sim2 = new Sim();

        for (Sim sim : sims) {
            if (sim.getMsisdn().equals("38763123456")) {
                sim1 = sim;
            } else {
                sim2 = sim;
            }
        }
        Assert.assertThat(sims.size(), is(equalTo(2)));

        Assert.assertThat(sim1.getSimNumber(), is(equalTo("8938711111222223333")));
        Assert.assertThat(sim1.getImsi(), is(equalTo("218031111122222")));
        Assert.assertThat(sim1.getUser(), samePropertyValuesAs(user));
        Assert.assertThat(sim1.getOperator(), is(equalTo(OperatorEnum.HEJ)));
        Assert.assertThat(sim1.getSimService(), is(equalTo(SimServiceEnum.POSTPAID)));
        Assert.assertThat(sim1.getEnvironment(), is(equalTo(EnvironmentEnum.TEST)));
        Assert.assertThat(sim1.getSimState(), is(equalTo(SimStateEnum.ACTIVE)));

        Assert.assertThat(sim2.getSimNumber(), is(equalTo("8938711111222223335")));
        Assert.assertThat(sim2.getImsi(), is(equalTo("218031111122224")));
        Assert.assertThat(sim2.getUser(), samePropertyValuesAs(user));
        Assert.assertThat(sim2.getOperator(), is(equalTo(OperatorEnum.HEJ)));
        Assert.assertThat(sim2.getSimService(), is(equalTo(SimServiceEnum.POSTPAID)));
        Assert.assertThat(sim2.getEnvironment(), is(equalTo(EnvironmentEnum.PRODUCTION)));
        Assert.assertThat(sim2.getSimState(), is(equalTo(SimStateEnum.ACTIVE)));
    }
}

