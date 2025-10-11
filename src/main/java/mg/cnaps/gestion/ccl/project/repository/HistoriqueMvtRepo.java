package mg.cnaps.gestion.ccl.project.repository;

import mg.cnaps.gestion.ccl.framework.jpa.core.repository.GenericRepository;
import mg.cnaps.gestion.ccl.project.entity.HistoriqueMvt;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistoriqueMvtRepo extends GenericRepository<HistoriqueMvt, String> {
    // Récupérer les historiques par ID du mouvement
    @Query(value = "SELECT * FROM CCL2_HISTORIQUE_MVT WHERE ID_MOUVEMENT = :mouvementId", nativeQuery = true)
    List<HistoriqueMvt> findHistoriqueMvtByMouvement_Id(@Param("mouvementId") String mouvementId);

    // Récupérer les historiques par ID du type de mouvement
    @Query(value = "SELECT * FROM CCL2_HISTORIQUE_MVT WHERE ID_TYPE_MOUVEMENT = :typeMouvementId", nativeQuery = true)
    List<HistoriqueMvt> findHistoriqueMvtsByTypeMouvement_Id(@Param("typeMouvementId") String typeMouvementId);

}