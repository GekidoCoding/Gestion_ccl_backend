package mg.cnaps.gestion.ccl.project.repository;

import mg.cnaps.gestion.ccl.framework.jpa.core.repository.GenericRepository;
import mg.cnaps.gestion.ccl.project.entity.CategorieInfra;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorieInfraRepo extends GenericRepository<CategorieInfra, String> { }