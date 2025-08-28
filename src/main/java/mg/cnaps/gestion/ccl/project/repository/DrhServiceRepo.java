package mg.cnaps.gestion.ccl.project.repository;

import mg.cnaps.gestion.ccl.framework.core.repository.GenericRepository;
import mg.cnaps.gestion.ccl.project.entity.existant.DrhService;
import org.springframework.stereotype.Repository;

@Repository
public interface DrhServiceRepo extends GenericRepository<DrhService, String> { }