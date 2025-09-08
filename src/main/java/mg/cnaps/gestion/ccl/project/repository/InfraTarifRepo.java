package mg.cnaps.gestion.ccl.project.repository;

import mg.cnaps.gestion.ccl.framework.core.repository.GenericRepository;
import mg.cnaps.gestion.ccl.project.entity.InfraTarif;
import org.springframework.stereotype.Repository;


@Repository
public interface InfraTarifRepo extends GenericRepository<InfraTarif, String> { }