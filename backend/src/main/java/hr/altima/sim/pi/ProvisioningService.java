package hr.altima.sim.pi;

import static hr.altima.sim.model.enums.EnvironmentEnum.DEV;
import static hr.altima.sim.model.enums.EnvironmentEnum.PRODUCTION;
import static hr.altima.sim.model.enums.EnvironmentEnum.TEST;
import static hr.altima.sim.model.enums.OperatorEnum.HALOO;
import static hr.altima.sim.model.enums.OperatorEnum.HEJ;
import static hr.altima.sim.model.enums.SimServiceEnum.POSTPAID;
import static hr.altima.sim.model.enums.SimServiceEnum.PREPAID;
import static hr.altima.sim.model.enums.SimStateEnum.ACTIVE;
import static hr.altima.sim.model.enums.SimStateEnum.PREACTIVATED;
import static hr.altima.sim.model.enums.SimStateEnum.TERMINATED;

import hr.altima.pi.integration.rest.ExecutionContext;
import hr.altima.pi.integration.rest.OperationResponse;
import hr.altima.sim.exception.NotFoundException;
import hr.altima.sim.exception.OperationExecutionException;
import hr.altima.sim.model.ExternalApplication;
import hr.altima.sim.model.Sim;
import hr.altima.sim.model.User;
import hr.altima.sim.model.enums.EnvironmentEnum;
import hr.altima.sim.model.enums.SimStateEnum;
import hr.altima.sim.service.ExternalApplicationService;
import hr.altima.sim.service.SimService;
import hr.altima.sim.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProvisioningService {

    private final Logger logger = LoggerFactory.getLogger(ProvisioningService.class);

    private static final String SERVICE_PROVIDER = "serviceProvider";

    private static final String STATE = "state";

    private static final String PAYMENT_MODE = "paymentMode";

    private static final String MSISDN = "msisdn";

    private static final String IMSI = "imsi";

    private static final String SIM_CARD_NO = "simCardNo";

    private static final String ENVIRONMENT = "environment";

    private static final String TARIFF = "tariff";

    private static final String PRODUCT = "product";

    private static final String PI_DEV = "PI_DEV";

    private PiService piService;

    private ExternalApplicationService externalApplicationService;

    private SimService simService;

    private UserService userService;

    @Autowired
    public ProvisioningService(PiService piService, ExternalApplicationService externalApplicationService, SimService simService, UserService userService) {
        this.piService = piService;
        this.externalApplicationService = externalApplicationService;
        this.simService = simService;
        this.userService = userService;
    }

    public Sim createSim(String msisdn, String nickname) {
        User userFromRepo = userService.findByNickname(nickname);
        if (userFromRepo == null) {
            throw new NotFoundException("User whit nickname " + nickname + " does not exist");
        }
        Sim simFromPi = fetchDataFromPi(msisdn);
        simFromPi.setUser(userFromRepo);

        return simService.createSim(simFromPi);
    }

    public Sim updateSim(Sim simFromRepo, String nickname) {
        User userFromRepo = userService.findByNickname(nickname);
        if (userFromRepo == null) {
            throw new NotFoundException("User whit nickname " + nickname + " does not exist");
        }
        Sim simFromPi = fetchDataFromPi(simFromRepo.getMsisdn());
        simFromPi.setUser(userFromRepo);

        return simService.updateSim(simFromPi, simFromRepo.getId());
    }

    private Sim fetchDataFromPi(String msisdn) {
        Map<String, String> map;
        ExternalApplication piDevApp = externalApplicationService.findByCode(PI_DEV);

        if (piDevApp == null) {
            throw new NotFoundException("Application doesn't exist");
        }

        try {
            OperationResponse operationResponse = piService.executeCcQuery(msisdn, piDevApp.getUrl());
            map = convertCcContextToMap(operationResponse.getContext());
        } catch (Exception e) {
            logger.error("Error in execution of CC_QUERY operation: {}", e.getMessage());
            throw new OperationExecutionException(e.getMessage());
        }

        String environment = getEnvironmentByExternalApplication(piDevApp.getCode());
        map.put(ENVIRONMENT, environment);

        try {
            OperationResponse rimGetPairedSimResponse = piService.rimGetPairedSim(msisdn, piDevApp.getUrl());
            map.put(SIM_CARD_NO, getSimCardNoFromRimContext(rimGetPairedSimResponse.getContext()));
        } catch (Exception e) {
            logger.error("Error in execution of RIM_GET_PAIRED_SIM operation: {}", e.getMessage());
            throw new OperationExecutionException(e.getMessage());
        }
        map.put(MSISDN, msisdn);

        return new Sim.Builder()
            .msisdn(map.get(MSISDN))
            .imsi(map.get(IMSI))
            .operator(map.get(SERVICE_PROVIDER).equals("HTM") ? HEJ : HALOO)
            .simService(map.get(PAYMENT_MODE).equals("PRE") ? PREPAID : POSTPAID)
            .simState(getSimState(map.get(STATE)))
            .environment(EnvironmentEnum.valueOf(map.get(ENVIRONMENT)))
            .simNumber(map.get(SIM_CARD_NO))
            .build();
    }

    private Map<String, String> convertCcContextToMap(ExecutionContext executionContext) {
        Map<String, String> map = new HashMap<>();
        map.put(PRODUCT, executionContext.getStringProperty(PRODUCT));
        map.put(PAYMENT_MODE, executionContext.getStringProperty(PAYMENT_MODE));
        map.put(IMSI, executionContext.getStringProperty(IMSI));
        map.put(SERVICE_PROVIDER, executionContext.getStringProperty(SERVICE_PROVIDER));
        map.put(TARIFF, executionContext.getStringProperty(TARIFF));
        map.put(STATE, executionContext.getStringProperty(STATE));

        return map;
    }

    private String getSimCardNoFromRimContext(ExecutionContext executionContext) {
        return executionContext.getStringProperty("iccid");
    }

    private String getEnvironmentByExternalApplication(String externalAppCode) {
        String environment = null;
        if (externalAppCode.contains("DEV")) {
            environment = DEV.toString();
        } else if (externalAppCode.contains("TEST")) {
            environment = TEST.toString();
        } else if (externalAppCode.contains("PROD")) {
            environment = PRODUCTION.toString();
        }

        return environment;
    }

    private SimStateEnum getSimState(String ccState) {
        SimStateEnum simState = null;

        switch (ccState) {
            case "PREDAKTIVIRAN":
                simState = PREACTIVATED;
                break;
            case "AKTIVAN":
                simState = ACTIVE;
                break;
            case "TERMINIRAN":
                simState = TERMINATED;
                break;
            default:
                break;
        }
        return simState;
    }
}
