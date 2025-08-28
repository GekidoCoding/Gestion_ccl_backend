package mg.cnaps.gestion.ccl.project.repository;

import mg.cnaps.gestion.ccl.framework.core.repository.GenericRepository;
import mg.cnaps.gestion.ccl.project.entity.Etat;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

@Repository
public interface EtatRepo extends GenericRepository<Etat, String> {
    @NotNull Etat getEtatByEtat(String etat);
    @NotNull Etat getEtatByCode(Integer code);

}