package mg.cnaps.gestion.ccl.project.repository;

import mg.cnaps.gestion.ccl.framework.core.repository.GenericRepository;
import mg.cnaps.gestion.ccl.project.entity.Gestionnaire;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GestionnaireRepo extends GenericRepository<Gestionnaire, String> {
    @Override
    @Query("SELECT i FROM Gestionnaire i JOIN i.etat e WHERE e.etat = 'Actif'")
    List<Gestionnaire> findAll();
}