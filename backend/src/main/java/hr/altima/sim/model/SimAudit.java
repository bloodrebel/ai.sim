package hr.altima.sim.model;

import hr.altima.sim.model.enums.ActionEnum;
import hr.altima.sim.model.enums.EnvironmentEnum;
import hr.altima.sim.model.enums.OperatorEnum;
import hr.altima.sim.model.enums.SimServiceEnum;
import hr.altima.sim.model.enums.SimStateEnum;

import java.sql.Date;
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
@Table(name = "ai_sim_audit")
public class SimAudit {
    @Id
    @Column(name = "aud_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer auditId;

    @Column(name = "id")
    private Integer id;

    @Column(name = "action")
    @Enumerated(EnumType.ORDINAL)
    private ActionEnum action;

    @Column(name = "action_date")
    private Date actionDate;

    @Column(name = "environment")
    private String environment;

    @Column(name = "imsi")
    private String imsi;

    @Column(name = "msisdn")
    private String msisdn;

    @Column(name = "operator")
    private String operator;

    @Column(name = "sim_num")
    private String simNumber;

    @Column(name = "sim_service")
    private String simService;

    @Column(name = "sim_state")
    private String simState;

    @Column(name = "user_nickname")
    private String userNickname;

    public SimAudit() {

    }

    public SimAudit(Builder builder) {
        this.action = builder.action;
        this.actionDate = builder.actionDate;
        this.id = builder.id;
        this.environment = builder.environment;
        this.imsi = builder.imsi;
        this.msisdn = builder.msisdn;
        this.operator = builder.operator;
        this.simNumber = builder.simNumber;
        this.simService = builder.simService;
        this.simState = builder.simState;
        this.userNickname = builder.userNickname;
    }

    public Integer getAuditId() {
        return auditId;
    }

    public void setAuditId(Integer auditId) {
        this.auditId = auditId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ActionEnum getAction() {
        return action;
    }

    public void setAction(ActionEnum action) {
        this.action = action;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getSimNumber() {
        return simNumber;
    }

    public void setSimNumber(String simNumber) {
        this.simNumber = simNumber;
    }

    public String getSimService() {
        return simService;
    }

    public void setSimService(String simService) {
        this.simService = simService;
    }

    public String getSimState() {
        return simState;
    }

    public void setSimState(String simState) {
        this.simState = simState;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    @Override
    public String toString() {
        return "SimAudit{" +
                "auditId=" + auditId +
                ", id=" + id +
                ", action=" + action +
                ", actionDate=" + actionDate +
                ", environment=" + environment +
                ", imsi='" + imsi + '\'' +
                ", msisdn='" + msisdn + '\'' +
                ", operator=" + operator +
                ", simNumber='" + simNumber + '\'' +
                ", simService=" + simService +
                ", simState=" + simState +
                ", user=" + userNickname +
                '}';
    }

    public static class Builder {
        private ActionEnum action;
        private Date actionDate;
        private Integer id;
        private String environment;
        private String imsi;
        private String msisdn;
        private String operator;
        private String simNumber;
        private String simService;
        private String simState;
        private String userNickname;

        public Builder() {

        }

        public Builder action(ActionEnum action) {
            this.action = action;
            return this;
        }

        public Builder actionDate(Date actionDate) {
            this.actionDate = actionDate;
            return this;
        }

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder environment(String environment) {
            this.environment = environment;
            return this;
        }

        public Builder imsi(String imsi) {
            this.imsi = imsi;
            return this;
        }

        public Builder msisdn(String msisdn) {
            this.msisdn = msisdn;
            return this;
        }

        public Builder operator(String operator) {
            this.operator = operator;
            return this;
        }

        public Builder simNumber(String simNumber) {
            this.simNumber = simNumber;
            return this;
        }

        public Builder simService(String simService) {
            this.simService = simService;
            return this;
        }

        public Builder simState(String simState) {
            this.simState = simState;
            return this;
        }

        public Builder userNickname(String userNickname) {
            this.userNickname = userNickname;
            return this;
        }

        public SimAudit build() {
            return new SimAudit(this);
        }
    }
}

