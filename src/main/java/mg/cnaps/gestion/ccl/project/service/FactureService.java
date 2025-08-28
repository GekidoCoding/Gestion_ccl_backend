package mg.cnaps.gestion.ccl.project.service;

import mg.cnaps.gestion.ccl.framework.core.service.GenericService;
import mg.cnaps.gestion.ccl.project.entity.Facture;

import java.util.List;


public interface FactureService extends GenericService<Facture, String> {
     List<Facture> getFacturesByMouvement_Id(String mouvementId);
}