package hr.altima.sim.integration.pi;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

/**
 * Base for other proxies.
 */
public abstract class Adapter {

    private RestTemplate restTemplate;

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Custom RestTemplate that is produces and consumes JSON payloads.
     *
     * @return
     */
    protected RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            restTemplate = createRestTemplate();
        }
        return restTemplate;
    }


    protected RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(getHttpRequestFactory());
        restTemplate.setMessageConverters(getMessageConverters());
        return restTemplate;
    }

    /**
     * By default it is the JSON converter.
     *
     * @return The list of message converters to use
     */
    protected List<HttpMessageConverter<?>> getMessageConverters() {
        return Collections.singletonList(new MappingJackson2HttpMessageConverter());
    }

    protected abstract ClientHttpRequestFactory getHttpRequestFactory();
}
