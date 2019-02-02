package hr.altima.sim.service;

import hr.altima.sim.model.Sim;
import hr.altima.sim.model.enums.OperatorEnum;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class SimServiceTest {
    @InjectMocks
    SimService simService;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateMsisdn_firstValidMsisdn_shouldReturnTrue() {
        Sim sim = new Sim();
        sim.setMsisdn("38763123456");
        sim.setOperator(OperatorEnum.HEJ);
        boolean validResult = simService.validateMsisdn(sim);
        assertThat(validResult, is(equalTo(true)));
    }

    @Test
    public void validateMsisdn_secondValidMsisdn_shouldReturnTrue() {
        Sim sim = new Sim();
        sim.setMsisdn("387644123456");
        sim.setOperator(OperatorEnum.HALOO);
        boolean validResult = simService.validateMsisdn(sim);
        assertThat(validResult, is(equalTo(true)));
    }

    @Test
    public void validateMsisdn_wrongFirstNumber_shouldReturnFalse() {
        Sim sim = new Sim();
        sim.setMsisdn("12363123456");
        sim.setOperator(OperatorEnum.HALOO);
        boolean invalidResult = simService.validateMsisdn(sim);
        assertThat(invalidResult, is(equalTo(false)));
    }

    @Test
    public void validateMsisdn_fewerNumber_shouldReturnFalse() {
        Sim sim = new Sim();
        sim.setMsisdn("3876312345");
        sim.setOperator(OperatorEnum.HEJ);
        boolean invalidResult = simService.validateMsisdn(sim);
        assertThat(invalidResult, is(equalTo(false)));
    }

    @Test
    public void validateMsisdn_moreNumbers_shouldReturnFalse() {
        Sim sim = new Sim();
        sim.setMsisdn("3876312345678");
        sim.setOperator(OperatorEnum.HEJ);
        boolean invalidResult = simService.validateMsisdn(sim);
        assertThat(invalidResult, is(equalTo(false)));
    }

    @Test
    public void validateMsisdn_msisdnWithChars_ShouldReturnFalse() {
        Sim sim = new Sim();
        sim.setMsisdn("38763g23w56");
        sim.setOperator(OperatorEnum.HEJ);
        boolean invalidResult = simService.validateMsisdn(sim);
        assertThat(invalidResult, is(equalTo(false)));
    }

    @Test
    public void validateImsi_validImsi_ShouldReturnTrue() {
        String imsi = "218031111111111";
        boolean validResult = simService.validateImsi(imsi);
        assertThat(validResult, is(equalTo(true)));
    }

    @Test
    public void validateImsi_wrongFirstNumber_shouldReturnFalse() {
        String imsi = "123451111111111";
        boolean invalidResult = simService.validateImsi(imsi);
        assertThat(invalidResult, is(equalTo(false)));
    }

    @Test
    public void validateImsi_wrongMccNumber_shouldReturnFalse() {
        String imsi = "123031111111111";
        boolean invalidResult = simService.validateImsi(imsi);
        assertThat(invalidResult, is(equalTo(false)));
    }

    @Test
    public void validateImsi_wrongMncNumber_shouldReturnFalse() {
        String imsi = "218121111111111";
        boolean invalidResult = simService.validateImsi(imsi);
        assertThat(invalidResult, is(equalTo(false)));
    }

    @Test
    public void validateImsi_fewerNumber_shouldReturnFalse() {
        String imsi = "21803111";
        boolean invalidResult = simService.validateImsi(imsi);
        assertThat(invalidResult, is(equalTo(false)));
    }

    @Test
    public void validateImsi_moreNumber_shouldReturnFalse() {
        String imsi = "21803111111111111111111";
        boolean invalidResult = simService.validateImsi(imsi);
        assertThat(invalidResult, is(equalTo(false)));
    }

    @Test
    public void validateImsi_imsiWithChars_shouldReturnFalse() {
        String imsi = "21803a123456b78";
        boolean invalidResult = simService.validateImsi(imsi);
        assertThat(invalidResult, is(equalTo(false)));
    }

    @Test
    public void validateSimNumber_validSimNumber_shouldReturnTrue() {
        String simNumber = "8938711111111111111";
        boolean validResult = simService.validateSimNumber(simNumber);
        assertThat(validResult, is(equalTo(true)));
    }

    @Test
    public void validateSimNumber_wrongFirstNumber_shouldReturnFalse() {
        String simNumber = "1234511111111111111";
        boolean invalidResult = simService.validateSimNumber(simNumber);
        assertThat(invalidResult, is(equalTo(false)));
    }

    @Test
    public void validateSimNumber_fewerNumbers_shouldReturnFalse() {
        String simNumber = "893871111";
        boolean invalidResult = simService.validateSimNumber(simNumber);
        assertThat(invalidResult, is(equalTo(false)));
    }

    @Test
    public void validateSimNumber_moreNumbers_shouldReturnFalse() {
        String simNumber = "89387111111111111111111";
        boolean invalidResult = simService.validateSimNumber(simNumber);
        assertThat(invalidResult, is(equalTo(false)));
    }

    @Test
    public void validateSimNumber_simNumberWithChars_shouldReturnFalse() {
        String simNumber = "89387f123456789gtre";
        boolean invalidResult = simService.validateSimNumber(simNumber);
        assertThat(invalidResult, is(equalTo(false)));
    }
}
