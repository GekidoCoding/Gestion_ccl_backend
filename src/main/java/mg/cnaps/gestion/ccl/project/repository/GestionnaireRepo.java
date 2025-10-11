package mg.cnaps.gestion.ccl.project.repository;

import mg.cnaps.gestion.ccl.framework.jpa.core.repository.GenericRepository;
import mg.cnaps.gestion.ccl.project.entity.Gestionnaire;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GestionnaireRepo extends GenericRepository<Gestionnaire, String> {
    @Override
    @Query("SELECT i FROM Gestionnaire i JOIN i.etat e WHERE e.etat = 'Actif'")
    List<Gestionnaire> findAll();

    @Query(value = "SELECT * FROM CCL2_GESTIONNAIRE WHERE MATRICULE_GESTIONNAIRE = :matricule", nativeQuery = true)
    Gestionnaire findByAgent_Matricule(@Param("matricule") String matricule);
}