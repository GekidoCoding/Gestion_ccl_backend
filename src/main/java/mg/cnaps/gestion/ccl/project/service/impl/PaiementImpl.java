package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.jpa.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.config.CclPropertyService;
import mg.cnaps.gestion.ccl.project.entity.Facture;
import mg.cnaps.gestion.ccl.project.entity.Paiement;
import mg.cnaps.gestion.ccl.project.entity.dto.mouvement.MouvementDto;
import mg.cnaps.gestion.ccl.project.exception.EtatNotValidException;
import mg.cnaps.gestion.ccl.project.exception.NoResteException;
import mg.cnaps.gestion.ccl.project.repository.EtatRepo;
import mg.cnaps.gestion.ccl.project.repository.FactureRepo;
import mg.cnaps.gestion.ccl.project.repository.PaiementRepo;
import mg.cnaps.gestion.ccl.project.service.FactureService;
import mg.cnaps.gestion.ccl.project.service.PaiementService;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaiementImpl extends GenericServiceImpl<Paiement, String , PaiementRepo> implements PaiementService {
    private final CclPropertyService cclPropertyService;
    private final FactureRepo factureRepo;
    private final EtatRepo etatRepo;

    public PaiementImpl(PaiementRepo repo, CclPropertyService cclPropertyService, FactureRepo factureRepo, EtatRepo etatRepo) {
        super(repo);
        this.cclPropertyService = cclPropertyService;
        this.factureRepo = factureRepo;
        this.etatRepo = etatRepo;

    }
    @Override
    public List<Paiement> findPaiementByFacture_Id(String factureId) {
        return this.findAll()
                .stream()
                .filter(p -> p.getFacture() != null
                        && factureId.equals(p.getFacture().getId()))
                .collect(Collectors.toList());
    }
    @Override
    public Double getTotalAcompteByFacture_Id(String factureId) {
        return this.findAll()
                .stream()
                .filter(p -> p.getFacture() != null
                        && factureId.equals(p.getFacture().getId()))
                .mapToDouble(Paiement::getAcompteVerse)
                .sum();
    }



    @Override
    public Paiement save (Paiement paiement){
        Facture facture = factureRepo.findById(paiement.getFacture().getId()).orElse(null);
        assert facture != null;
        if(facture.getEtat().getCode().equals(cclPropertyService.getReelleCode())){
            Double reste = facture.getTotalDu()-this.getTotalAcompteByFacture_Id(paiement.getFacture().getId());
            double monnaie = paiement.getAcompteVerse() - reste;
            if(monnaie>=0){
                facture.setEtat(etatRepo.getEtatByCode(cclPropertyService.getPayeCode()));
                factureRepo.save(facture);
                paiement.setMonnaie(monnaie);
            }else{
                paiement.setMonnaie(0.0);
            }
            paiement.setReste(reste);
            paiement.setDatePaiement(Timestamp.valueOf(LocalDateTime.now()));
            if(reste>0){
                return super.save(paiement);
            }
            else{
                throw new NoResteException("Impossible d'inserer si le reste de cet facture est de zéro");
            }
        }
        else{
            throw new EtatNotValidException("Seul une facture avec un état réelle peut avoir de paiement ");
        }
    }

    @Override
    public void delete (String id){
        Paiement paiement = this.findById(id);
        if(paiement.getFacture().getEtat().getCode().equals(cclPropertyService.getPayeCode())){
           Facture facture = paiement.getFacture();
           facture.setEtat(etatRepo.getEtatByCode(cclPropertyService.getReelleCode()));
           factureRepo.save(facture);
        }
        repository.deleteById(id);
    }







}