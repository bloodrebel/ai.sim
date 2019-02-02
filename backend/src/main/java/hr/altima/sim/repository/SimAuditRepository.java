package hr.altima.sim.repository;

import hr.altima.sim.model.SimAudit;
import hr.altima.sim.model.enums.ActionEnum;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SimAuditRepository extends JpaRepository<SimAudit, Integer> {
    @Query("select s from SimAudit s where s.id = :id and s.action = :revtype")
    SimAudit findDeletedAuditBySimId(@Param("id") Integer id, @Param("revtype") ActionEnum revtype);

    List<SimAudit> findAllById(Integer simId);

    Page<SimAudit> findAllById(Integer id, Pageable pageRequest);

    List<SimAudit> findAllByIdAndAction(Integer id, ActionEnum actionEnum);

    Page<SimAudit> findAllByIdAndAction(Integer id, ActionEnum actionEnum, Pageable pageRequest);
}
