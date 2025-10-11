package mg.cnaps.gestion.ccl.project.service;

import mg.cnaps.gestion.ccl.framework.jpa.core.service.GenericService;
import mg.cnaps.gestion.ccl.project.entity.Etat;

import java.util.List;


public interface EtatService extends GenericService<Etat, String> {
    List<Etat> getEtatsFacture();
    List<Etat> getEtatsAutre();
}