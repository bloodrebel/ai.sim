package hr.altima.sim.service;

import hr.altima.sim.exception.InvalidEMailException;
import hr.altima.sim.model.User;
import hr.altima.sim.model.enums.TeamEnum;
import hr.altima.sim.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final String emailDomainRegex = "^[a-zA-Z_.+-]+@(?:(?:[a-zA-Z]+\\.)?[a-zA-Z]+\\.)?(verso|altima)\\.(hr$|ba$)";
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findAllByTeam(TeamEnum team) {
        return userRepository.findAllByTeam(team);
    }

    public Page<User> findAllPaginated(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size));
    }

    public Page<User> findAllPaginatedByTeam(TeamEnum team, int page, int size) {
        return userRepository.findAllByTeam(team, PageRequest.of(page, size));
    }

    public User findByNickname(String nickname) {
        return userRepository.findByNickName(nickname);
    }

    public User createUser(User user) {
        if (!validateEMail(user.geteMail())) {
            throw new InvalidEMailException("Invalid E-mail");
        }
        return userRepository.save(user);
    }

    public User updateUser(User user, Integer id) {
        user.setId(id);
        if (!validateEMail(user.geteMail())) {
            throw new InvalidEMailException("Invalid E-mail");
        }
        return userRepository.save(user);
    }

    public User deleteUser(Integer id) {
        User deletedUser = this.findById(id);
        userRepository.delete(deletedUser);
        return deletedUser;
    }

    boolean validateEMail(String eMail) {
        if (eMail.length() < 254 && eMail.matches(emailDomainRegex)) {
            return true;
        }
        return false;
    }

    public boolean isUsernameExist(User user) {
        return userRepository.findByNickName(user.getNickName()) != null;
    }


}
