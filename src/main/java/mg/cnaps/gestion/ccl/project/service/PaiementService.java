package mg.cnaps.gestion.ccl.project.service;

import mg.cnaps.gestion.ccl.framework.jpa.core.service.GenericService;
import mg.cnaps.gestion.ccl.project.entity.Paiement;

import java.util.List;


public interface PaiementService extends GenericService<Paiement, String> {
    List<Paiement> findPaiementByFacture_Id(String factureId);

    Double getTotalAcompteByFacture_Id(String factureId);
}