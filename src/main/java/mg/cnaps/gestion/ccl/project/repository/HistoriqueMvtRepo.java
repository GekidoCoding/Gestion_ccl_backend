package mg.cnaps.gestion.ccl.project.repository;

import mg.cnaps.gestion.ccl.framework.core.repository.GenericRepository;
import mg.cnaps.gestion.ccl.project.entity.HistoriqueInfra;
import mg.cnaps.gestion.ccl.project.entity.HistoriqueMvt;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistoriqueMvtRepo extends GenericRepository<HistoriqueMvt, String> {
    List<HistoriqueMvt> findHistoriqueMvtByMouvement_Id(String mouvementId);
    List<HistoriqueMvt> findHistoriqueMvtsByTypeMouvement_Id(String typeMouvementId);
}