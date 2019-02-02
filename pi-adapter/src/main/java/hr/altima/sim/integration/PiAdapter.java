package hr.altima.sim.integration;


import hr.altima.pi.integration.rest.OperationRequest;
import hr.altima.pi.integration.rest.OperationResponse;
import hr.altima.pi.integration.rest.WorkflowRequest;
import hr.altima.pi.integration.rest.WorkflowResponse;
import hr.altima.sim.config.PiProperties;
import hr.altima.sim.integration.pi.Adapter;
import hr.altima.sim.integration.pi.exception.PiException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@Component
public class PiAdapter extends Adapter {
    private PiProperties piProperties;

    @Autowired
    public PiAdapter(PiProperties piProperties) {
        this.piProperties = piProperties;
    }

    public WorkflowResponse executeWorkflow(String appUrl, WorkflowRequest request) throws PiException {
        String url = String.format("http://%s/integration/rest/workflows/%s/execute/", appUrl, request.getCode());

        WorkflowResponse response = getRestTemplate().postForObject(url, request, WorkflowResponse.class);
        String responseMessage = response.getMessage();

        if (!response.isSuccessful() && (responseMessage == null || !responseMessage.contains("Workflow not found"))) {
            throw new PiException(response, response.getMessage());
        }

        return response;
    }

    @Async
    public Future<WorkflowResponse> executeWorkflowAsync(String appUrl, WorkflowRequest request) {
        try {
            return new AsyncResult<>(executeWorkflow(appUrl, request));
        } catch (Exception ex) {
            throw new PiException(ex);
        }
    }

    public OperationResponse executeOperation(String appUrl, OperationRequest request) throws PiException {
        String url = String.format("http://%s/integration/rest/operations/%s/execute/", appUrl, request.getCode());

        OperationResponse response = getRestTemplate().postForObject(url, request, OperationResponse.class);
        String responseMessage = response.getMessage();

        if (!response.isSuccessful() && (responseMessage == null || !responseMessage.contains("Operation not found"))) {
            throw new PiException(response, response.getMessage());
        }

        return response;
    }

    @Async
    public Future<OperationResponse> executeOperationAsync(String appUrl, OperationRequest request) {
        try {
            return new AsyncResult<>(executeOperation(appUrl, request));
        } catch (Exception ex) {
            throw new PiException(ex);
        }
    }

    @Override
    protected ClientHttpRequestFactory getHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(piProperties.getConnectionTimeoutMs());
        factory.setReadTimeout(piProperties.getReadTimeoutMs());
        return factory;
    }
}
