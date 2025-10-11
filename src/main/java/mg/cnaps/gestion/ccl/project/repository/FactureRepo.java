package mg.cnaps.gestion.ccl.project.repository;

import mg.cnaps.gestion.ccl.framework.jpa.core.repository.GenericRepository;
import mg.cnaps.gestion.ccl.project.entity.Facture;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactureRepo extends GenericRepository<Facture, String> {
    List<Facture> getFacturesByMouvement_Id(String mouvementId);

}