package hr.altima.sim.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ai_external_application")
public class ExternalApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true)
    private Integer id;

    @Column(name = "code")
    private String code;

    @Column(name = "url")
    private String url;

    @Column(name = "token")
    private String token;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ExternalApplication)) {
            return false;
        }

        ExternalApplication appDefinition = (ExternalApplication) o;
        return Objects.equals(code, appDefinition.code);
    }

    @Override
    public String toString() {
        return "ExternalApplication{" +
            "id=" + id +
            ", code='" + code + '\'' +
            ", url='" + url + '\'' +
            ", token='" + token + '\'' +
            '}';
    }
}
