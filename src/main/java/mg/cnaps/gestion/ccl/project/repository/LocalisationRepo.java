package mg.cnaps.gestion.ccl.project.repository;

import mg.cnaps.gestion.ccl.framework.jpa.core.repository.GenericRepository;
import mg.cnaps.gestion.ccl.project.entity.Localisation;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalisationRepo extends GenericRepository<Localisation, String> { }