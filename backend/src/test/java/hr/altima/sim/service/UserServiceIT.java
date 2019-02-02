package hr.altima.sim.service;

import hr.altima.sim.AbstractDbUnitTest;
import hr.altima.sim.model.User;
import hr.altima.sim.model.enums.TeamEnum;
import hr.altima.sim.repository.UserRepository;
import hr.altima.sim.exception.InvalidEMailException;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.List;

public class UserServiceIT extends AbstractDbUnitTest {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @After
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Test
    public void findById_findExistingUser_returnUser() {
        User user = userService.findById(1);

        assertThat(user.getFirstName(), is(equalTo("Pero")));
        assertThat(user.getLastName(), is(equalTo("Peric")));
        assertThat(user.getNickName(), is(equalTo("Pesa")));
        assertThat(user.geteMail(), is(equalTo("pericpero@altima.hr")));
        assertThat(user.getTeam(), is(equalTo(TeamEnum.DEV)));
    }

    @Test
    public void findById_findNonExistingUser_shouldReturnNullValue() {
        User user = userService.findById(44);
        assertThat(user, is(equalTo(null)));
    }

    @Test
    public void findAllUsers_findExistingUsers_returnAllUsers() {

        List<User> users = userService.findAll();
        User userPero = new User();
        User userMario = new User();
        User userStipe = new User();
        User userBojan = new User();
        User userMarin = new User();

        for (User user : users) {
            if (user.getFirstName().equals("Pero")) {
                userPero = user;
            } else if (user.getFirstName().equals("Mario")) {
                userMario = user;
            } else if (user.getFirstName().equals("Stipe")) {
                userStipe = user;
            } else if (user.getFirstName().equals("Bojan")) {
                userBojan = user;
            } else {
                userMarin = user;
            }
        }

        assertThat(users.size(), is(equalTo(5)));

        assertThat(userPero.getFirstName(), is(equalTo("Pero")));
        assertThat(userPero.getLastName(), is(equalTo("Peric")));
        assertThat(userPero.getNickName(), is(equalTo("Pesa")));
        assertThat(userPero.geteMail(), is(equalTo("pericpero@altima.hr")));
        assertThat(userPero.getTeam(), is(equalTo(TeamEnum.DEV)));

        assertThat(userMario.getFirstName(), is(equalTo("Mario")));
        assertThat(userMario.getLastName(), is(equalTo("Mandzukic")));
        assertThat(userMario.getNickName(), is(equalTo("Mandzo")));
        assertThat(userMario.geteMail(), is(equalTo("mandzoo@altima.hr")));
        assertThat(userMario.getTeam(), is(equalTo(TeamEnum.OC)));

        assertThat(userStipe.getFirstName(), is(equalTo("Stipe")));
        assertThat(userStipe.getLastName(), is(equalTo("Pletikosa")));
        assertThat(userStipe.getNickName(), is(equalTo("peSti")));
        assertThat(userStipe.geteMail(), is(equalTo("pletikosastipe@altima.hr")));
        assertThat(userStipe.getTeam(), is(equalTo(TeamEnum.SAP)));

        assertThat(userBojan.getFirstName(), is(equalTo("Bojan")));
        assertThat(userBojan.getLastName(), is(equalTo("Bogdanovic")));
        assertThat(userBojan.getNickName(), is(equalTo("babo")));
        assertThat(userBojan.geteMail(), is(equalTo("baboretta@altima.hr")));
        assertThat(userBojan.getTeam(), is(equalTo(TeamEnum.DEV)));

        assertThat(userMarin.getFirstName(), is(equalTo("Marin")));
        assertThat(userMarin.getLastName(), is(equalTo("Cilic")));
        assertThat(userMarin.getNickName(), is(equalTo("promasaj")));
        assertThat(userMarin.geteMail(), is(equalTo("cilicmarin@altima.hr")));
        assertThat(userMarin.getTeam(), is(equalTo(TeamEnum.SAP)));
    }

    @Test
    public void createUser_newValidUser_UserCreated() throws InvalidEMailException {
        User user = new User();
        user.setFirstName("Ivo");
        user.setLastName("Ivic");
        user.setNickName("Ivek");
        user.seteMail("ivo@altima.hr");
        user.setTeam(TeamEnum.SAP);

        User createdUser = userService.createUser(user);

        assertThat(createdUser.getFirstName(), is(equalTo("Ivo")));
        assertThat(createdUser.getLastName(), is(equalTo("Ivic")));
        assertThat(createdUser.getNickName(), is(equalTo("Ivek")));
        assertThat(createdUser.geteMail(), is(equalTo("ivo@altima.hr")));
        assertThat(createdUser.getTeam(), is(equalTo(TeamEnum.SAP)));
    }

    @Test(expected = InvalidEMailException.class)
    public void createUser_invalidEMail_shouldThrowException() throws InvalidEMailException {
        User user = new User();
        user.setFirstName("Mato");
        user.setLastName("Matic");
        user.setNickName("Matan");
        user.seteMail("mato@gmail.com");
        user.setTeam(TeamEnum.OC);

        userService.createUser(user);
    }

    @Test(expected = Exception.class)
    public void createUser_repeatedNickname_shouldThrowException() throws InvalidEMailException {
        User user = new User();
        user.setFirstName("Marko");
        user.setLastName("Markic");
        user.setNickName("Mandzo");
        user.seteMail("marko@altima.ba");
        user.setTeam(TeamEnum.DEV);

        userService.createUser(user);
    }

    @Test(expected = Exception.class)
    public void createUser_repeatedEMail_shouldThrowException() throws InvalidEMailException {
        User user = new User();
        user.setFirstName("Marko");
        user.setLastName("Markic");
        user.setNickName("Mara");
        user.seteMail("mandzoo@altima.hr");
        user.setTeam(TeamEnum.DEV);

        userService.createUser(user);
    }

    @Test(expected = Exception.class)
    public void createUser_userWithoutNickname_shouldThrowException() throws InvalidEMailException {
        User user = new User();
        user.setFirstName("Ante");
        user.setLastName("Antic");
        user.seteMail("ante@altima.ba");
        user.setTeam(TeamEnum.SAP);

        userService.createUser(user);
    }

    @Test
    public void updateUser_updateExistingUser_userUpdated() throws InvalidEMailException {
        User user = userService.findById(1);
        user.setFirstName("Mate");
        user.seteMail("mate@altima.hr");
        user.setTeam(TeamEnum.OC);
        User updatedUser = userService.updateUser(user, user.getId());

        assertThat(updatedUser.getFirstName(), is(equalTo("Mate")));
        assertThat(updatedUser.geteMail(), is(equalTo("mate@altima.hr")));
        assertThat(updatedUser.getTeam(), is(equalTo(TeamEnum.OC)));
    }

    @Test(expected = InvalidEMailException.class)
    public void updateUser_updateInvalidEMail_shouldThrowException() throws InvalidEMailException {
        User user = userService.findById(4);
        user.seteMail("iko@gmail.com");

        userService.updateUser(user, user.getId());
    }

    @Test(expected = Exception.class)
    public void updateUser_alreadyExistingNickname_shouldThrowException() throws InvalidEMailException {
        User user = userService.findById(3);
        user.setNickName("Pesa");

        userService.updateUser(user, user.getId());
    }

    @Test
    public void deleteUser_deleteValidUser_userDeleted() {
        User user = userService.findById(5);
        userService.deleteUser(user.getId());
        User deletedUser = userService.findById(5);
        assertThat(deletedUser, is(equalTo(null)));
    }

    @Test
    public void isUsernameExists_validUsername_shouldReturnTrue() {
        User user = new User();
        user.setNickName("Pesa");
        boolean nickname = userService.isUsernameExist(user);

        assertThat(nickname, is(equalTo(true)));
    }

    @Test
    public void isUsernameExists_invalidUsername_shouldReturnFalse() {
        User user = new User();
        user.setNickName("Jozo");
        boolean nickname = userService.isUsernameExist(user);

        assertThat(nickname, is(equalTo(false)));
    }
}
