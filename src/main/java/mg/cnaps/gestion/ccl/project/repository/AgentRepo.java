package mg.cnaps.gestion.ccl.project.repository;

import mg.cnaps.gestion.ccl.framework.core.repository.GenericRepository;
import mg.cnaps.gestion.ccl.project.entity.existant.Agent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentRepo extends GenericRepository<Agent, String> {
    @Query("SELECT a FROM Agent a WHERE " +
            "(:matricule IS NULL OR LOWER(a.matricule) LIKE LOWER('%' || :matricule || '%')) AND " +
            "(:nom IS NULL OR LOWER(a.nom) LIKE LOWER('%' || :nom || '%')) AND " +
            "(:prenoms IS NULL OR LOWER(a.prenoms) LIKE LOWER('%' || :prenoms || '%')) AND " +
            "(:mail IS NULL OR LOWER(a.mail) LIKE LOWER('%' || :mail || '%')) AND " +
            "(:codeService IS NULL OR a.codeService = :codeService) AND " +
            "(:codeDirection IS NULL OR a.codeDirection = :codeDirection)")
    List<Agent> searchByCriteria(@Param("matricule") String matricule,
                                 @Param("nom") String nom,
                                 @Param("prenoms") String prenoms,
                                 @Param("mail") String mail,
                                 @Param("codeService") String codeService,
                                 @Param("codeDirection") String codeDirection);

    @Query(value = "SELECT a  FROM Agent a  LEFT JOIN Gestionnaire g ON a.matricule = g.agent.matricule WHERE g.agent.matricule IS NULL")
    List<Agent> findWithoutGestionnaire();


}