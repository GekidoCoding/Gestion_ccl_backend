package mg.cnaps.gestion.ccl.project.service;

import mg.cnaps.gestion.ccl.framework.core.service.GenericService;
import mg.cnaps.gestion.ccl.project.entity.Facture;
import mg.cnaps.gestion.ccl.project.entity.HistoFacture;

import java.util.List;


public interface HistoFactureService extends GenericService<HistoFacture, String> {
    List<HistoFacture> getHistoByFactureId(String factureId);
}