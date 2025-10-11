package mg.cnaps.gestion.ccl.project.service;

import mg.cnaps.gestion.ccl.framework.jpa.core.service.GenericService;
import mg.cnaps.gestion.ccl.project.entity.Gestionnaire;


public interface GestionnaireService extends GenericService<Gestionnaire, String> {
    String [] getEmailGestionnaire();
}