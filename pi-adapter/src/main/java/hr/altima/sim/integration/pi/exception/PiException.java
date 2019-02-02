package hr.altima.sim.integration.pi.exception;

import hr.altima.pi.integration.rest.ContinueResponse;
import hr.altima.pi.integration.rest.Response;
import hr.altima.pi.integration.rest.WorkflowResponse;

/**
 * Covers exceptional cases in invoking PI workflows and operations.
 */
public class PiException extends RuntimeException {
    /**
     * This field represents workflow run ID on PI and is normally non-empty only on workflow execution exceptions.
     */
    private Response response;

    public PiException(Response response, String message) {
        super(message);
        this.response = response;
    }

    public PiException(Exception ex) {
        super(ex);
    }

    public PiException(String message) {
        super(message);
    }

    public Response getResponse() {
        return response;
    }

    public Long getRunId() {
        if (response instanceof ContinueResponse) {
            return ((ContinueResponse) response).getRunId();
        } else if (response instanceof WorkflowResponse) {
            return ((WorkflowResponse) response).getRunId();
        }
        return null;
    }

}
