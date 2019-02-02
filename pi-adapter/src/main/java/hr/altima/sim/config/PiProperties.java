package hr.altima.sim.config;

import static java.util.Objects.requireNonNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix = "pi")
@Validated
public class PiProperties {
    private Integer connectionTimeoutMs = 100;

    private Integer readTimeoutMs = 1000000;

    public Integer getConnectionTimeoutMs() {
        return this.connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs(Integer connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public Integer getReadTimeoutMs() {
        return this.readTimeoutMs;
    }

    public void setReadTimeoutMs(Integer readTimeoutMs) {
        this.readTimeoutMs = readTimeoutMs;
    }

    @Override
    public String toString() {
        return "PiProperties{" +
            ", connectionTimeoutMs=" + connectionTimeoutMs +
            ", readTimeoutMs=" + readTimeoutMs +
            '}';
    }

}
