package hr.altima.sim.repository;

import hr.altima.sim.model.Moderator;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeratorRepository extends JpaRepository<Moderator, Integer> {

    Moderator findByUsername(String username);

}
