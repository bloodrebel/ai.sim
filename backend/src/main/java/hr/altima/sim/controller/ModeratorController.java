package hr.altima.sim.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

import hr.altima.sim.exception.InvalidModeratorException;
import hr.altima.sim.model.Moderator;
import hr.altima.sim.service.ModeratorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ModeratorController {

    private final Logger logger = LoggerFactory.getLogger(ModeratorController.class);
    private ModeratorService moderatorService;

    @Autowired
    public ModeratorController(ModeratorService moderatorService) {
        this.moderatorService = moderatorService;
    }

    @RequestMapping(value = "/moderators", method = RequestMethod.GET)
    public ResponseEntity<List<Moderator>> findAll() {
        List<Moderator> moderators = moderatorService.findAll();
        if (moderators.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(moderators, HttpStatus.OK);
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<Moderator> login(@RequestBody Moderator moderator) {
        Moderator loggedModerator = moderatorService.login(moderator);
        if (loggedModerator == null) {
            logger.info("Unable to find a Moderator with username {} ", moderator.getUsername());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        logger.debug("moderator with {} username has loged in", moderator.getUsername());
        return new ResponseEntity<>(loggedModerator, HttpStatus.OK);
    }

    @RequestMapping(value = "/moderators/changePassword/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Moderator> changePassword(@RequestBody Moderator moderator, @PathVariable Integer id) throws InvalidModeratorException {
        Moderator updatedModerator = moderatorService.findById(id);
        if (updatedModerator == null) {
            logger.error("Unable to update. Moderator with id {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        updatedModerator.setPassword(moderator.getPassword());
        updatedModerator = moderatorService.updateModeratorPassword(updatedModerator, id);
        logger.debug("Updated Moderator password with id {}", id);

        return new ResponseEntity<>(updatedModerator, HttpStatus.OK);
    }

    @RequestMapping(value = "/admin/changeRole/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Moderator> changeRole(@RequestBody Moderator moderator, @PathVariable Integer id) throws InvalidModeratorException {
        Moderator updatedModerator = moderatorService.findById(id);
        if (updatedModerator == null) {
            logger.error("Unable to update. Moderator with id {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        updatedModerator.setRole(moderator.getRole());
        updatedModerator = moderatorService.updateModerator(updatedModerator, id);
        logger.debug("Updated Moderator role with id {} to {} ", id, moderator.getRole());

        return new ResponseEntity<>(updatedModerator, HttpStatus.OK);
    }

    @RequestMapping(value = "/admin/delete/{id}", method = DELETE)
    public ResponseEntity<Void> deleteSim(@PathVariable Integer id) {
        Moderator existingModerator = moderatorService.findById(id);
        if (existingModerator == null) {
            logger.error("Unable to delete. Moderator with id {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        moderatorService.delete(id);
        logger.debug("Delete Moderator with id {}", id);
        return new ResponseEntity<>(OK);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<Moderator> register(@RequestBody Moderator moderator) throws InvalidModeratorException {
        if (moderatorService.isModeratorExists(moderator)) {
            logger.info("Unable to create. A Moderator with username {} already exist", moderator.getUsername());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (!moderatorService.isModeratorValid(moderator)) {
            logger.info("Unable to create. Invalid moderator credentials");
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        Moderator createdModerator = moderatorService.createModerator(moderator);
        logger.debug("Create moderator with {} id", moderator.getId());
        return new ResponseEntity<>(createdModerator, HttpStatus.CREATED);
    }

}
