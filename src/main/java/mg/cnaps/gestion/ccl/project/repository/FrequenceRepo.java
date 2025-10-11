package mg.cnaps.gestion.ccl.project.repository;

import mg.cnaps.gestion.ccl.framework.jpa.core.repository.GenericRepository;
import mg.cnaps.gestion.ccl.project.entity.Frequence;
import org.springframework.stereotype.Repository;

@Repository
public interface FrequenceRepo extends GenericRepository<Frequence, String> { }