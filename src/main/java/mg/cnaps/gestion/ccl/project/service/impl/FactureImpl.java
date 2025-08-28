package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.entity.Facture;
import mg.cnaps.gestion.ccl.project.entity.HistoFacture;
import mg.cnaps.gestion.ccl.project.entity.HistoriqueMvt;
import mg.cnaps.gestion.ccl.project.repository.FactureRepo;
import mg.cnaps.gestion.ccl.project.repository.HistoFactureRepo;
import mg.cnaps.gestion.ccl.project.service.FactureService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class FactureImpl extends GenericServiceImpl<Facture, String ,FactureRepo> implements FactureService {
    private final HistoFactureRepo histoFactureRepo;
    public FactureImpl(FactureRepo repo, HistoFactureRepo histoFactureRepo) {
        super(repo);
        this.histoFactureRepo = histoFactureRepo;
    }

    @Override
    public List<Facture> getFacturesByMouvement_Id(String mouvementId){
        return repository.getFacturesByMouvement_Id(mouvementId);
    }

    @Override
    public Facture findById(String id){
        List<Facture> factures = repository.findAll();
        return factures.stream().filter(f -> f.getId().equals(id)).findFirst().orElse(null);
    }
    @Override
    public Facture save(Facture facture){
        facture.setDhCreation(Timestamp.from(Instant.now()));
        facture = repository.save(facture);
        System.out.println("save safe : " + facture);
        HistoFacture  histoFacture = new HistoFacture();
        histoFacture.setFacture(facture);
        histoFacture.setEtat(facture.getEtat());
        histoFacture.setRefFacture(facture.getRefFacture());
        histoFacture.setMontant(facture.getMontantTotal());
        histoFacture.setDhAction(Timestamp.from(Instant.now()));
        histoFactureRepo.save(histoFacture);

        return facture;
    }

    @Override
    public Facture update (Facture facture ,  String id){
        if(!repository.existsById(id)){
            throw new RuntimeException("Not Found ID ");
        }
        facture.setDhCreation(Timestamp.from(Instant.now()));
        System.out.println("update safe : " + facture);
        facture = repository.save(facture);

        HistoFacture isExist = null;
        List<HistoFacture> historiqueMvts = this.histoFactureRepo.getHistoFactureByFacture_Id(id);
        for (HistoFacture histo: historiqueMvts ) {
            if(histo.getEtat().getId().equals(facture.getEtat().getId()) ) {
                isExist = histo;
                break;
            }
        }
        if(isExist==null){
            HistoFacture  histo = new HistoFacture();
            histo.setFacture(facture);
            histo.setEtat(facture.getEtat());
            histo.setRefFacture(facture.getRefFacture());
            histo.setMontant(facture.getMontantTotal());
            histo.setDhAction(Timestamp.from(Instant.now()));
            histoFactureRepo.save(histo);
        }
        return facture;
    }

}

