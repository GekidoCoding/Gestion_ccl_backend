package mg.cnaps.gestion.ccl.project.repository;

import mg.cnaps.gestion.ccl.framework.core.repository.GenericRepository;
import mg.cnaps.gestion.ccl.project.entity.HistoFacture;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistoFactureRepo extends GenericRepository<HistoFacture, String> {
    List<HistoFacture> getHistoFactureByFacture_Id(String factureId);
}