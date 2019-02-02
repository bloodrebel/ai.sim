package hr.altima.sim.repository;

import hr.altima.sim.model.ExternalApplication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalApplicationRepository extends JpaRepository<ExternalApplication, Integer> {

    ExternalApplication findByCode(String code);
}
