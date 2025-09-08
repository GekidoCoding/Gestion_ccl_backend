package mg.cnaps.gestion.ccl.project.repository;

import mg.cnaps.gestion.ccl.framework.core.repository.GenericRepository;
import mg.cnaps.gestion.ccl.project.entity.Frequence;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FrequenceRepo extends GenericRepository<Frequence, String> { }