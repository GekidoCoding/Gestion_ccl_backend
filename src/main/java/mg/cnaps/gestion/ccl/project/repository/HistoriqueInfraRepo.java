package mg.cnaps.gestion.ccl.project.repository;

import mg.cnaps.gestion.ccl.framework.jpa.core.repository.GenericRepository;
import mg.cnaps.gestion.ccl.project.entity.HistoriqueInfra;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistoriqueInfraRepo extends GenericRepository<HistoriqueInfra, String> {
    List<HistoriqueInfra> findHistoriqueInfraByInfrastructure_Id(String infrastructureId);
}