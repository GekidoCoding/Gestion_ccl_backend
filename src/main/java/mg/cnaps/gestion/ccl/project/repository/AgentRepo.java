package mg.cnaps.gestion.ccl.project.repository;

import mg.cnaps.gestion.ccl.framework.jpa.core.repository.GenericRepository;
import mg.cnaps.gestion.ccl.project.entity.existant.Agent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentRepo extends GenericRepository<Agent, String> {

    @Query(value = "SELECT * FROM intranet.view_annuaire_electronique a " +
            "WHERE (:matricule IS NULL OR LOWER(a.matricule) LIKE LOWER(CONCAT('%', :matricule, '%'))) " +
            "AND (:nom IS NULL OR LOWER(a.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) " +
            "AND (:prenoms IS NULL OR LOWER(a.prenoms) LIKE LOWER(CONCAT('%', :prenoms, '%'))) " +
            "AND (:mail IS NULL OR LOWER(a.mail) LIKE LOWER(CONCAT('%', :mail, '%'))) " +
            "AND (:codeService IS NULL OR a.code_service = :codeService) " +
            "AND (:codeDirection IS NULL OR a.code_direction = :codeDirection)",
            nativeQuery = true)
    List<Agent> searchByCriteria(@Param("matricule") String matricule,
                                 @Param("nom") String nom,
                                 @Param("prenoms") String prenoms,
                                 @Param("mail") String mail,
                                 @Param("codeService") String codeService,
                                 @Param("codeDirection") String codeDirection);

    @Query(value = "SELECT a.* FROM intranet.view_annuaire_electronique a " +
            "LEFT JOIN ccl2_gestionnaire g ON a.matricule = g.matricule_gestionnaire " +
            "WHERE g.matricule_gestionnaire IS NULL",
            nativeQuery = true)
    List<Agent> findWithoutGestionnaire();

    @Query(value = "SELECT * FROM intranet.view_annuaire_electronique a WHERE a.matricule=:matricule " , nativeQuery = true)
    Agent findByMatricule(@Param("matricule") String matricule);

}
