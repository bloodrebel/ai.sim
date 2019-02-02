package hr.altima.sim.model;

import hr.altima.sim.model.enums.EnvironmentEnum;
import hr.altima.sim.model.enums.OperatorEnum;
import hr.altima.sim.model.enums.SimServiceEnum;
import hr.altima.sim.model.enums.SimStateEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ai_sim")
public class Sim {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "msisdn")
    private String msisdn;

    @Column(name = "sim_num")
    private String simNumber;

    @Column(name = "imsi")
    private String imsi;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @Column(name = "operator")
    @Enumerated(EnumType.STRING)
    private OperatorEnum operator;

    @Column(name = "sim_service")
    @Enumerated(EnumType.STRING)
    private SimServiceEnum simService;

    @Column(name = "environment")
    @Enumerated(EnumType.STRING)
    private EnvironmentEnum environment;

    @Column(name = "sim_state")
    @Enumerated(EnumType.STRING)
    private SimStateEnum simState;

    public Sim() {

    }

    public Sim(Builder builder) {
        this.msisdn = builder.msisdn;
        this.simNumber = builder.simNumber;
        this.imsi = builder.imsi;
        this.user = builder.user;
        this.operator = builder.operator;
        this.simService = builder.simService;
        this.environment = builder.environment;
        this.simState = builder.simState;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getSimNumber() {
        return simNumber;
    }

    public void setSimNumber(String simNumber) {
        this.simNumber = simNumber;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OperatorEnum getOperator() {
        return operator;
    }

    public void setOperator(OperatorEnum operator) {
        this.operator = operator;
    }

    public SimServiceEnum getSimService() {
        return simService;
    }

    public void setSimService(SimServiceEnum simService) {
        this.simService = simService;
    }

    public EnvironmentEnum getEnvironment() {
        return environment;
    }

    public void setEnvironment(EnvironmentEnum environment) {
        this.environment = environment;
    }

    public SimStateEnum getSimState() {
        return simState;
    }

    public void setSimState(SimStateEnum simState) {
        this.simState = simState;
    }

    @Override
    public String toString() {
        return "Sim{" +
                ", id=" + id +
                ", msisdn=" + msisdn +
                ", simNumber=" + simNumber +
                ", imsi=" + imsi +
                ", user=" + user +
                ", operator=" + operator +
                ", simService=" + simService +
                ", environment=" + environment +
                ", simState=" + simState +
                '}';
    }

    public static class Builder {
        private String msisdn;
        private String simNumber;
        private String imsi;
        private User user;
        private OperatorEnum operator;
        private SimServiceEnum simService;
        private EnvironmentEnum environment;
        private SimStateEnum simState;

        public Builder() {

        }

        public Builder msisdn(String msisdn) {
            this.msisdn = msisdn;
            return this;
        }

        public Builder simNumber(String simNumber) {
            this.simNumber = simNumber;
            return this;
        }

        public Builder imsi(String imsi) {
            this.imsi = imsi;
            return this;
        }

        public Builder user(User user) {
            this.user = user;
            return this;
        }

        public Builder operator(OperatorEnum operator) {
            this.operator = operator;
            return this;
        }

        public Builder simService(SimServiceEnum simService) {
            this.simService = simService;
            return this;
        }

        public Builder environment(EnvironmentEnum environment) {
            this.environment = environment;
            return this;
        }

        public Builder simState(SimStateEnum simState) {
            this.simState = simState;
            return this;
        }

        public Sim build() {
            return new Sim(this);
        }
    }
}
