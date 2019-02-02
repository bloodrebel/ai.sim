package hr.altima.sim.service;

import hr.altima.sim.exception.InvalidModeratorException;
import hr.altima.sim.model.Moderator;
import hr.altima.sim.repository.ModeratorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModeratorService {

    private ModeratorRepository moderatorRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    @Autowired
    public ModeratorService(ModeratorRepository moderatorRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.moderatorRepository = moderatorRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public List<Moderator> findAll() {
        return moderatorRepository.findAll();
    }

    public Moderator findById(Integer id) {
        return moderatorRepository.findById(id).orElse(null);
    }

    public Moderator createModerator(Moderator moderator) throws InvalidModeratorException {
        if (moderator == null) {
            throw new InvalidModeratorException("Moderator is null");
        }
        moderator.setPassword(passwordEncoder.encode(moderator.getPassword()));
        return moderatorRepository.save(moderator);
    }

    public Moderator updateModerator(Moderator moderator, Integer id) throws InvalidModeratorException {
        if (!isModeratorValid(moderator)) {
            throw new InvalidModeratorException("Updating moderator failed, invalid credentials");
        }
        moderator.setId(id);

        return moderatorRepository.save(moderator);
    }

    public Moderator updateModeratorPassword(Moderator moderator, Integer id) throws InvalidModeratorException {
        if (!isPasswordValid(moderator)) {
            throw new InvalidModeratorException("Updating moderator failed, invalid password");
        }

        moderator.setId(id);
        moderator.setPassword(passwordEncoder.encode(moderator.getPassword()));

        return moderatorRepository.save(moderator);
    }

    public void delete(Integer id) {
        moderatorRepository.deleteById(id);
    }

    public Moderator login(Moderator moderator) {
        if (!isModeratorLoginValid(moderator)) {
            return null;
        }
        Authentication authenticationResult = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(moderator.getUsername(), moderator.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticationResult);
        Moderator logedModerator = moderatorRepository.findByUsername(moderator.getUsername());
        return logedModerator;
    }

    public boolean isModeratorValid(Moderator moderator) {

        String regex = "^[a-zA-Z]+$";
        if (moderator.getUsername().length() > 3 && moderator.getUsername().matches(regex) && moderator.getPassword().length() > 3) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPasswordValid(Moderator moderator) {
        if (moderator.getPassword().length() > 3) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isModeratorExists(Moderator moderator) {
        return moderatorRepository.findByUsername(moderator.getUsername()) != null;
    }

    public boolean isModeratorLoginValid(Moderator moderator) {
        if (!isModeratorExists(moderator)) {
            return false;
        }
        String moderatorCryptedPassword = moderatorRepository.findByUsername(moderator.getUsername()).getPassword();
        Boolean passwordValid = passwordEncoder.matches(moderator.getPassword(), moderatorCryptedPassword);
        return passwordValid;
    }

}
