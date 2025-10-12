package mg.cnaps.gestion.ccl.project.util;

import mg.cnaps.gestion.ccl.project.config.CclPropertyService;
import mg.cnaps.gestion.ccl.project.entity.*;
import mg.cnaps.gestion.ccl.project.entity.dto.facture.FactureDto;
import mg.cnaps.gestion.ccl.project.entity.dto.mouvement.MouvementDto;
import mg.cnaps.gestion.ccl.project.exception.UnauthorizedUpdateException;
import mg.cnaps.gestion.ccl.project.repository.GestionnaireRepo;
import mg.cnaps.gestion.ccl.project.repository.HistoriqueMvtRepo;
import mg.cnaps.gestion.ccl.project.repository.MouvementInfraRepo;
import mg.cnaps.gestion.ccl.project.repository.MouvementRepo;
import mg.cnaps.gestion.ccl.project.service.FactureService;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
public class MouvementUtil {

    private final CclPropertyService cclPropertyService;
    private final FactureService factureService;
    private final MouvementInfraRepo mouvementInfraRepo;
    private final HistoriqueMvtRepo historiqueRepo;
    private final GestionnaireRepo gestionnaireRepo;

    public MouvementUtil(CclPropertyService cclPropertyService, FactureService factureService,
                         MouvementInfraRepo mouvementInfraRepo, HistoriqueMvtRepo historiqueRepo, GestionnaireRepo gestionnaireRepo) {
        this.cclPropertyService = cclPropertyService;
        this.factureService = factureService;
        this.mouvementInfraRepo = mouvementInfraRepo;
        this.historiqueRepo = historiqueRepo;
        this.gestionnaireRepo = gestionnaireRepo;
    }

    public boolean ifPayedTotalite(String mouvementId) {
        List<FactureDto> factures = this.factureService.getFacturesReellePayeByMouvement_Id(mouvementId);
        double totalDu = 0.0;
        double totalAcompteVerse = 0.0;
        for (FactureDto facture : factures) {
            totalDu += facture.getTotalDu();
            totalAcompteVerse += facture.getTotalAcompte();
        }
        return totalAcompteVerse > totalDu;
    }

    public boolean ifAuthorizedOccupation(String mouvementId) {
        List<FactureDto> factures = this.factureService.getFacturesReellePayeByMouvement_Id(mouvementId);
        double totalDu = 0.0;
        double totalAcompteVerse = 0.0;
        for (FactureDto facture : factures) {
            totalDu += facture.getTotalDu();
            totalAcompteVerse += facture.getTotalAcompte();
        }
        double avanceObligatoire = totalDu * (cclPropertyService.getAvancePaiement() / 100);
        return totalAcompteVerse >= avanceObligatoire;
    }

    public void verifierNiveauProcessus(Mouvement nouveau, Mouvement ancien) {
        Integer nouveauProcessus = nouveau.getTypeMouvement().getNiveauProcessus();
        Integer ancienProcessus = ancien.getTypeMouvement().getNiveauProcessus();

        String nouveauType = nouveau.getTypeMouvement().getNom();
        String ancienType = ancien.getTypeMouvement().getNom();

        if (nouveauProcessus != null && ancienProcessus != null) {
            if (nouveauProcessus != 0 && nouveauProcessus < ancienProcessus) {
                throw new RuntimeException(
                        "Impossible de mettre à jour : passage de type mouvement " + ancienType +
                                " (niveau " + ancienProcessus + ") vers " + nouveauType +
                                " (niveau " + nouveauProcessus + ") interdit (sauf pour le niveau 0)."
                );
            }
        }
    }

    public void verifierPassageOccupation(Mouvement entity) {
        if (entity.getTypeMouvement().getId().equals(cclPropertyService.getOccupationId())) {
            Facture facture = factureService.getFactureReelleByMouvementId(entity.getId());
            if (facture == null) {
                throw new UnauthorizedUpdateException(
                        "Le passage au statut Occupation est impossible tant qu’aucune facture réelle n’a été réglée par le client pour le mouvement #" + entity.getId()
                );
            }
        }
    }

    public void sauvegarderInfrastructures(List<MouvementInfra> mouvementInfras) {
        this.mouvementInfraRepo.saveAll(mouvementInfras);
    }

    public void historiserMouvement(Mouvement entity, String id , Gestionnaire gestionnaire) {
        List<HistoriqueMvt> historiqueMvts = historiqueRepo.findHistoriqueMvtByMouvement_Id(id);

        boolean existeDeja = historiqueMvts.stream()
                .anyMatch(histo -> histo.getTypeMouvement().getId().equals(entity.getTypeMouvement().getId()));

        if (!existeDeja) {
            HistoriqueMvt historique = new HistoriqueMvt();
            historique.setTypeMouvement(entity.getTypeMouvement());
            historique.setMouvement(entity);
            historique.setGestionnaire(gestionnaire);
            historique.setDhAction(Timestamp.valueOf(java.time.LocalDateTime.now()));
            historiqueRepo.save(historique);
        }
    }

    public boolean ifContainsInfra(Mouvement mouvement, String infraId) {
        if (mouvement == null || infraId == null || mouvement.getMouvementInfras() == null) {
            return true;
        }

        for (MouvementInfra minf : mouvement.getMouvementInfras()) {
            if (minf.getInfrastructure() != null && infraId.equals(minf.getInfrastructure().getId())) {
                return true;
            }
        }
        return false;
    }

    public boolean ifInModeles(Mouvement mouvement, String[] modelesIds) {
        if (modelesIds == null || modelesIds.length == 0) {
            return true;
        }

        return mouvement.getMouvementInfras().stream()
                .anyMatch(mi -> mi.getInfrastructure() != null
                        && mi.getInfrastructure().getModeleInfra() != null
                        && mi.getInfrastructure().getModeleInfra().getId() != null
                        && Arrays.asList(modelesIds).contains(mi.getInfrastructure().getModeleInfra().getId()));
    }

    public boolean containsAllInfrastructures(Mouvement mouvement, Mouvement criteria) {
        if (mouvement.getMouvementInfras() == null || criteria.getMouvementInfras() == null) {
            return false;
        }

        return criteria.getMouvementInfras().stream()
                .map(MouvementInfra::getInfrastructure)
                .filter(Objects::nonNull)
                .allMatch(criteriaInfra ->
                        mouvement.getMouvementInfras().stream()
                                .map(MouvementInfra::getInfrastructure)
                                .filter(Objects::nonNull)
                                .anyMatch(infra -> infra.getId().equals(criteriaInfra.getId()))
                );
    }



    public boolean containsCatInfra(Mouvement mouvement, String catInfraId) {
        if (mouvement.getMouvementInfras() == null || catInfraId == null) {
            return false;
        }

        return mouvement.getMouvementInfras().stream()
                .map(MouvementInfra::getInfrastructure)
                .filter(infra -> infra != null && infra.getModeleInfra() != null
                        && infra.getModeleInfra().getCatInfra() != null)
                .anyMatch(infra -> catInfraId.equals(infra.getModeleInfra().getCatInfra().getId()));
    }



    public boolean matchCriteria(Mouvement criteria, MouvementDto mouvementDto, String catInfraId, HistoriqueMvtRepo historiqueRepo, MouvementRepo mouvementRepo) {
        if (criteria == null || mouvementDto == null) {
            return false;
        }

        if (criteria.getMouvementInfras() != null && !criteria.getMouvementInfras().isEmpty()) {
            Mouvement mouvement = mouvementRepo.findById(mouvementDto.getId()).orElse(null);
            assert mouvement != null;
            if (!containsCatInfra(mouvement, catInfraId)) {
                return false;
            }
            if (!containsAllInfrastructures(mouvement , criteria)) {
                return false;
            }
        }



        if (criteria.getTypeMouvement() != null && criteria.getTypeMouvement().getId() != null) {
            String idTypeCriteria = criteria.getTypeMouvement().getId();
            if (mouvementDto.getTypeMouvement() == null
                    || !idTypeCriteria.equals(mouvementDto.getTypeMouvement().getId())) {
                return false;
            }
        }
        if(criteria.getId()!=null){
            if(mouvementDto.getId()==null || !mouvementDto.getId().contains(criteria.getId())){
                return false;
            }
        }
        if (criteria.getClient() != null && criteria.getClient().getId() != null) {
            if (mouvementDto.getClient() == null ||
                    mouvementDto.getClient().getId() == null ||
                    !mouvementDto.getClient().getId().contains(criteria.getClient().getId())) {
                return false;
            }
        }


        if (criteria.getPeriodeDebut() != null && criteria.getPeriodeFin() != null) {
            List<HistoriqueMvt> historiques = historiqueRepo.findHistoriqueMvtByMouvement_Id(mouvementDto.getId());
            if (historiques == null || historiques.isEmpty()) {
                return false;
            }

            return historiques.stream()
                    .anyMatch(histo -> {
                        Timestamp dh = histo.getDhActionTimestamp();
                        return dh != null &&
                                !dh.before(criteria.getPeriodeDebut()) &&
                                !dh.after(criteria.getPeriodeFin());
                    });

        }

        return true;
    }

    public boolean isInfrastructureActive(Mouvement mouvement) {
        if (mouvement == null || mouvement.getMouvementInfras() == null) {
            return false;
        }

        return mouvement.getMouvementInfras().stream()
                .map(MouvementInfra::getInfrastructure)
                .filter(infra -> infra != null && infra.getEtat() != null && infra.getEtat().getCode() != null)
                .anyMatch(infra -> !infra.getEtat().getCode().equals(cclPropertyService.getInactifCode()));
    }

}