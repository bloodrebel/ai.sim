package hr.altima.sim.repository;

import hr.altima.sim.model.User;
import hr.altima.sim.model.enums.TeamEnum;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByNickName(String nickname);

    List<User> findAllByTeam(TeamEnum team);

    Page<User> findAllByTeam(TeamEnum team, Pageable pageRequest);
}