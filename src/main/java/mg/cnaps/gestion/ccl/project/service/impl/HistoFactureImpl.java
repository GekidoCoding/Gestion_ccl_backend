package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.jpa.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.entity.Facture;
import mg.cnaps.gestion.ccl.project.entity.HistoFacture;
import mg.cnaps.gestion.ccl.project.entity.dto.facture.FactureDto;
import mg.cnaps.gestion.ccl.project.repository.HistoFactureRepo;
import mg.cnaps.gestion.ccl.project.service.FactureService;
import mg.cnaps.gestion.ccl.project.service.HistoFactureService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HistoFactureImpl extends GenericServiceImpl<HistoFacture, String ,HistoFactureRepo> implements HistoFactureService {
    public  final FactureService factureService;
    public HistoFactureImpl(HistoFactureRepo repo, FactureService factureService) {
        super(repo);
        this.factureService = factureService;
    }

    @Override
    public List<HistoFacture> getHistoByFactureId(String factureId) {
        return factureService.getHistoFactureByFacture_Id(factureId);
    }

    @Override
    public List<HistoFacture> getHistoByMouvementId(String mouvementId) {
        List<FactureDto> factures = this.factureService.getFacturesByMouvement_Id(mouvementId);
        List<HistoFacture> histoFactures = new ArrayList<>();
        for (FactureDto facture : factures) {
            List<HistoFacture> histoFactureList = this.getHistoByFactureId(facture.getId());
            histoFactures.addAll(histoFactureList);
        }
        return histoFactures;
    }
}