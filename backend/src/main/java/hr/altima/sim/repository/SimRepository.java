package hr.altima.sim.repository;

import hr.altima.sim.model.Sim;
import hr.altima.sim.model.enums.EnvironmentEnum;
import hr.altima.sim.model.enums.OperatorEnum;
import hr.altima.sim.model.enums.SimServiceEnum;
import hr.altima.sim.model.enums.SimStateEnum;
import hr.altima.sim.model.enums.TeamEnum;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SimRepository extends JpaRepository<Sim, Integer> {

    Sim findByMsisdn(String msisdn);

    Sim findByImsi(String imsi);

    Sim findBySimNumber(String simNumber);

    List<Sim> findAllByEnvironment(EnvironmentEnum environmentEnum);

    @Query(value = "select s from Sim s where upper(s.environment) like concat('%', upper(:environment),'%')")
    Page<Sim> findAllByEnvironment(@Param("environment") String environment, Pageable pageRequest);

    List<Sim> findAllByUserNickName(String nickName);

    @Query(value = "select s from Sim s join s.user u on lower(u.nickName) like concat('%', lower(:nickName),'%')")
    Page<Sim> findAllByUserNickName(@Param("nickName") String nickName, Pageable pageRequest);

    List<Sim> findAllBySimService(SimServiceEnum simService);

    @Query(value = "select s from Sim s where upper(s.simService) like concat('%', upper(:simService),'%')")
    Page<Sim> findAllBySimService(@Param("simService") String simService, Pageable pageRequest);

    @Query("select s from Sim s join s.user u on u.team = :team")
    List<Sim> findAllByTeam(@Param("team") TeamEnum team);

    Page<Sim> findAllByUserTeam(TeamEnum team, Pageable pageRequest);

    List<Sim> findAllByOperator(OperatorEnum operator);

    @Query(value = "select s from Sim s where upper(s.operator) like concat('%', upper(:operator),'%')")
    Page<Sim> findAllByOperator(@Param("operator") String operator, Pageable pageRequest);

    List<Sim> findAllBySimState(SimStateEnum simState);

    @Query(value = "select s from Sim s where upper(s.simState) like concat('%', upper(:simState),'%')")
    Page<Sim> findAllBySimState(@Param("simState") String simState, Pageable pageRequest);
}