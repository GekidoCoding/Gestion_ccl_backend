package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.config.CclPropertyService;
import mg.cnaps.gestion.ccl.project.entity.*;
import mg.cnaps.gestion.ccl.project.exception.TarifNotValidException;
import mg.cnaps.gestion.ccl.project.repository.EtatRepo;
import mg.cnaps.gestion.ccl.project.repository.FactureRepo;
import mg.cnaps.gestion.ccl.project.repository.HistoFactureRepo;
import mg.cnaps.gestion.ccl.project.repository.MouvementRepo;
import mg.cnaps.gestion.ccl.project.service.EtatService;
import mg.cnaps.gestion.ccl.project.service.FactureService;
import mg.cnaps.gestion.ccl.project.util.TimestampUtil;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FactureImpl extends GenericServiceImpl<Facture, String ,FactureRepo> implements FactureService {
    private final HistoFactureRepo histoFactureRepo;
    private final MouvementRepo mouvementRepo;
    private final CclPropertyService cclPropertyService;
    private final EtatRepo etatRepo;
    public FactureImpl(FactureRepo repo, HistoFactureRepo histoFactureRepo, MouvementRepo mouvementRepo, CclPropertyService cclPropertyService, EtatRepo etatRepo) {
        super(repo);
        this.histoFactureRepo = histoFactureRepo;
        this.mouvementRepo = mouvementRepo;
        this.cclPropertyService = cclPropertyService;
        this.etatRepo = etatRepo;
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
        facture.setDhCreation(Timestamp.valueOf(LocalDateTime.now()));
        facture = repository.save(facture);
        HistoFacture  histoFacture = new HistoFacture();
        histoFacture.setFacture(facture);
        histoFacture.setEtat(facture.getEtat());
        histoFacture.setRefFacture(facture.getRefFacture());
        histoFacture.setMontant(facture.getMontantTotal());
        histoFacture.setDhAction(Timestamp.valueOf(LocalDateTime.now()));
        histoFactureRepo.save(histoFacture);

        return facture;
    }

    @Override
    public Facture update (Facture facture ,  String id){
        if(!repository.existsById(id)){
            throw new RuntimeException("Not Found ID ");
        }
        facture.setDhCreation(Timestamp.valueOf(LocalDateTime.now()));
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
            histo.setDhAction(Timestamp.valueOf(LocalDateTime.now()));
            histoFactureRepo.save(histo);
        }
        return facture;
    }

    @Override
    public Facture getDefaultFactureProformaByMouvement_Id(String mouvementId){
        Facture factureDefault = new Facture();
        Mouvement mouvement =mouvementRepo.findById(mouvementId).orElse(null);
        assert mouvement != null;
        if(mouvement.getTypeMouvement().getId().equals(cclPropertyService.getReservationId())
                || mouvement.getTypeMouvement().getId().equals(cclPropertyService.getOccupationId())
        ){
            double remise = cclPropertyService.getFactureRemise();
            Timestamp periodeDebut = mouvement.getPeriodeDebut();
            Timestamp periodeFin = mouvement.getPeriodeFin();
            double montantTotal=0.0;
            double acompteVerse=0.0;
            for (MouvementInfra mvtInfra:mouvement.getMouvementInfras()){
                Double tarifUnitaire = getTarifUnitaire(mvtInfra);
                Double duree=TimestampUtil.caculateDifferenceTimestamp(cclPropertyService, periodeDebut , periodeFin, mvtInfra.getFrequence().getId());
                montantTotal+=duree*tarifUnitaire;
            }
            acompteVerse=montantTotal *(remise/100);
            factureDefault.setDhCreation(Timestamp.valueOf(LocalDateTime.now()));
            factureDefault.setMouvement(mouvement);
            factureDefault.setAcompteVerse(acompteVerse);
            factureDefault.setMontantTotal(montantTotal);
            factureDefault.setRemise(remise);
            factureDefault.setEtat(etatRepo.getEtatByCode( cclPropertyService.getProformaCode()));
//            facture.setGestionnaire();
            return  factureDefault;
        }

        return factureDefault;
    }

    private  Double getTarifUnitaire(MouvementInfra mvtInfra) {
        Frequence frequence = mvtInfra.getFrequence();
        List<InfraTarif> tarifs=  mvtInfra.getInfrastructure().getInfraTarifs();
        Double tarifUnitaire=-1.0;
        for (InfraTarif infraTarif:tarifs){
            if(infraTarif.getFrequence().getId().equals(frequence.getId() )){
                tarifUnitaire = infraTarif.getTarifInfra();
                break;
            }
        }
        if(tarifUnitaire==-1.0){
            throw new TarifNotValidException("Il n'existe pas de tarif avec la frequence "+frequence.getLibelle() +" Pour l'infrastructure "+ mvtInfra.getInfrastructure().getNom()+"("+ mvtInfra.getInfrastructure().getNumero()+")");
        }
        return tarifUnitaire;
    }

    @Override
    public Facture getFactureReelleByMouvementId(String mouvementId){
        List<Facture> factures= this.repository.getFacturesByMouvement_Id(mouvementId);
        for (Facture facture:factures){
            if(!facture.getEtat().getCode().equals(cclPropertyService.getProformaCode())){
                return facture;
            }
        }
        return null;
    }



}

