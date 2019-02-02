package hr.altima.sim.controller;

import hr.altima.sim.model.User;
import hr.altima.sim.model.enums.TeamEnum;
import hr.altima.sim.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> findById(@PathVariable("id") Integer id) {
        User repoUser = userService.findById(id);
        if (repoUser == null) {
            logger.error("User with {} id is not found", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.debug("User with {} id is found", id);
        return new ResponseEntity<>(repoUser, HttpStatus.OK);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<User>> findAll() {
        List<User> repoUserList = userService.findAll();
        if (repoUserList.isEmpty() || repoUserList == null) {
            logger.error("List of users is empty");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.debug("Find all users");
        return new ResponseEntity<>(repoUserList, HttpStatus.OK);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, params = {"team"})
    public ResponseEntity<List<User>> findAllByTeam(@RequestParam("team") TeamEnum team) {
        List<User> repoUserList = userService.findAllByTeam(team);
        if (repoUserList.isEmpty() || repoUserList == null) {
            logger.error("List of users in {} team is empty", team);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.debug("Find all users in {} team", team);
        return new ResponseEntity<>(repoUserList, HttpStatus.OK);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, params = {"page", "size"})
    public ResponseEntity<Page<User>> findAllPaginated(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        Page<User> repoUserList = userService.findAllPaginated(page, size);
        if (repoUserList.isEmpty() || repoUserList == null) {
            logger.error("List of users is empty");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.debug("Find all users");
        return new ResponseEntity<>(repoUserList, HttpStatus.OK);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, params = {"page", "size", "team"})
    public ResponseEntity<Page<User>> findAllPaginatedByTeam(@RequestParam("page") Integer page, @RequestParam("size") Integer size, @RequestParam("team") TeamEnum team) {
        Page<User> repoUserList = userService.findAllPaginatedByTeam(team, page, size);
        if (repoUserList.isEmpty() || repoUserList == null) {
            logger.error("List of users in {} team is empty", team);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        logger.debug("Find all users in {} team", team);
        return new ResponseEntity<>(repoUserList, HttpStatus.OK);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (userService.isUsernameExist(user)) {
            logger.error("Unable to create. A User with username {} already exist", user.getNickName());
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        User createdUser = userService.createUser(user);
        logger.debug("Create user with {} id", createdUser.getId());
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable("id") Integer id) {
        User updateUser = userService.findById(id);
        if (updateUser == null) {
            logger.error("Unable to update. User with id {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.debug("Updated User with id {}", id);
        userService.updateUser(user, id);
        return new ResponseEntity<>(updateUser, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<User> deleteUser(@PathVariable("id") Integer id) {
        User deletedUser = userService.findById(id);
        if (deletedUser == null) {
            logger.error("Unable to delete. User with id {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.debug("Delete User with id {}", id);
        userService.deleteUser(id);
        return new ResponseEntity<>(deletedUser, HttpStatus.ACCEPTED);
    }
}