package mg.cnaps.gestion.ccl.project.repository;

import mg.cnaps.gestion.ccl.framework.jpa.core.repository.GenericRepository;
import mg.cnaps.gestion.ccl.project.entity.InfraTarif;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InfraTarifRepo extends GenericRepository<InfraTarif, String> {
    public List<InfraTarif> getAllByInfrastructure_Id( String infrastructure_Id);
}