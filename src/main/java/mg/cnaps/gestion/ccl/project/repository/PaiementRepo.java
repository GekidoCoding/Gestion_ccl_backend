package mg.cnaps.gestion.ccl.project.repository;

import mg.cnaps.gestion.ccl.framework.jpa.core.repository.GenericRepository;
import mg.cnaps.gestion.ccl.project.entity.Paiement;
import org.springframework.stereotype.Repository;

@Repository
public interface PaiementRepo extends GenericRepository<Paiement, String> {
}
