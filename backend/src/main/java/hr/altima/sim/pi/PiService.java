package hr.altima.sim.pi;

import hr.altima.pi.integration.rest.ExecutionContext;
import hr.altima.pi.integration.rest.OperationRequest;
import hr.altima.pi.integration.rest.OperationResponse;
import hr.altima.sim.integration.PiAdapter;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PiService {

    private PiAdapter piAdapter;

    private static final String RIM_GET_PAIRED_SIM = "RIM_GET_PAIRED_SIM";

    private static final String CC_QUERY = "CC_QUERY";

    @Autowired
    public PiService(PiAdapter piAdapter) {
        this.piAdapter = piAdapter;
    }

    OperationResponse executeCcQuery(String msisdn, String url) {
        ExecutionContext executionContext = new ExecutionContext();
        executionContext.addProperty("serviceIdentifier", "MSISDN");
        executionContext.addProperty("consumptionDate", DateTime.now().toString());
        executionContext.addProperty("userIdentifier", msisdn);

        OperationRequest operationRequest = new OperationRequest(CC_QUERY, executionContext);

        return piAdapter.executeOperation(url, operationRequest);
    }

    OperationResponse rimGetPairedSim(String msisdn, String url) {
        ExecutionContext executionContext = new ExecutionContext();
        executionContext.addProperty("msisdn", msisdn);

        OperationRequest operationRequest = new OperationRequest(RIM_GET_PAIRED_SIM, executionContext);

        return piAdapter.executeOperation(url, operationRequest);
    }
}
