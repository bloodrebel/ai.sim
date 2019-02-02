package hr.altima.sim.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UserServiceTest {
    @InjectMocks
    UserService userService;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateEMail_firstValidEMail_shouldReturnTrue() {
        String eMail = "first_test@altima.hr";
        boolean validResult = userService.validateEMail(eMail);
        assertThat(validResult, is(equalTo(true)));
    }

    @Test
    public void validateEMail_secondValidEMail_shouldReturnTrue() {
        String eMail = "second.test@verso.ba";
        boolean validResult = userService.validateEMail(eMail);
        assertThat(validResult, is(equalTo(true)));
    }

    @Test
    public void validateEMail_wrongLastTwoChar_shouldReturFalse() {
        String eMail = "test@altima.com";
        boolean invalidResult = userService.validateEMail(eMail);
        assertThat(invalidResult, is(equalTo(false)));
    }

    @Test
    public void validateEmail_wrongChar_shouldReturnFalse() {
        String eMail = "test@gmail.ba";
        boolean invalidResult = userService.validateEMail(eMail);
        assertThat(invalidResult, is(equalTo(false)));
    }

    @Test
    public void validateEmail_numberInsteadChar_shouldReturnFalse() {
        String eMail = "1test2@altima.hr";
        boolean invalidResult = userService.validateEMail(eMail);
        assertThat(invalidResult, is(equalTo(false)));
    }

    @Test
    public void validateEmail_unallowedChar_shouldReturnFalse() {
        String eMail = "test*/@verso.hr";
        boolean invalidResult = userService.validateEMail(eMail);
        assertThat(invalidResult, is(equalTo(false)));
    }

    @Test
    public void validateEmail_withoutPoint_shouldReturnFalse() {
        String eMail = "test@altimahr";
        boolean invalidResult = userService.validateEMail(eMail);
        assertThat(invalidResult, is(equalTo(false)));
    }

    @Test
    public void validateEmail_withoutMonkey_shouldReturnFalse() {
        String eMail = "testaltima.hr";
        boolean invalidResult = userService.validateEMail(eMail);
        assertThat(invalidResult, is(equalTo(false)));
    }

}
