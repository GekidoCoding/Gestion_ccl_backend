package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.config.CclPropertyService;
import mg.cnaps.gestion.ccl.project.entity.*;
import mg.cnaps.gestion.ccl.project.entity.dto.mouvement.MouvementCalendarDto;
import mg.cnaps.gestion.ccl.project.entity.dto.mouvement.MouvementDto;
import mg.cnaps.gestion.ccl.project.exception.UnauthorizedException;
import mg.cnaps.gestion.ccl.project.exception.UnauthorizedUpdateException;
import mg.cnaps.gestion.ccl.project.repository.HistoriqueMvtRepo;
import mg.cnaps.gestion.ccl.project.repository.MouvementInfraRepo;
import mg.cnaps.gestion.ccl.project.repository.MouvementRepo;
import mg.cnaps.gestion.ccl.project.service.FactureService;
import mg.cnaps.gestion.ccl.project.service.MouvementService;
import mg.cnaps.gestion.ccl.project.service.TypeMouvementService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MouvementImpl extends GenericServiceImpl<Mouvement, String , MouvementRepo> implements MouvementService {
    private final MouvementRepo mouvementRepo;
    private final HistoriqueMvtRepo historiqueRepo;
    private final CclPropertyService cclPropertyService;
    private final TypeMouvementService typeMouvementService;
    private final MouvementInfraRepo mouvementInfraRepo;
    private final FactureService factureService;

    public MouvementImpl(MouvementRepo repo , MouvementRepo mouvementRepo, HistoriqueMvtRepo historiqueRepo, CclPropertyService cclPropertyService, TypeMouvementService typeMouvementService, MouvementInfraRepo mouvementInfraRepo, FactureService factureService) {
        super(repo);
        this.mouvementRepo=mouvementRepo;
        this.historiqueRepo = historiqueRepo;
        this.cclPropertyService = cclPropertyService;
        this.typeMouvementService = typeMouvementService;
        this.mouvementInfraRepo = mouvementInfraRepo;
        this.factureService = factureService;
    }
    public Mouvement update(Mouvement entity, String id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Not Found ID");
        }

        Mouvement ancien = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mouvement introuvable avec l'ID " + id));

        // Vérification si c’est un classement
        if (ancien.getTypeMouvement().getId().equals(cclPropertyService.getClassementId())) {
            throw new UnauthorizedUpdateException(
                    "Un mouvement de type Classement n'est plus modifiable pour #" + entity.getId()
            );
        }

        // Vérification du processus
        verifierNiveauProcessus(entity, ancien);

        // Vérification passage en Occupation
        verifierPassageOccupation(entity);

        // Sauvegarde mouvement + infrastructures
        entity.setDhMouvement(Timestamp.valueOf(LocalDateTime.now()));

        List<MouvementInfra> mouvementInfras = entity.getMouvementInfras();

        entity = repository.save(entity);

        this.sauvegarderInfrastructures(mouvementInfras);
        // Historiser si nécessaire
        historiserMouvement(entity, id);

        return entity;
    }

    private void verifierNiveauProcessus(Mouvement nouveau, Mouvement ancien) {
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

    private void verifierPassageOccupation(Mouvement entity) {
        if (entity.getTypeMouvement().getId().equals(cclPropertyService.getOccupationId())) {
            Facture facture = factureService.getFactureReelleByMouvementId(entity.getId());
            if (facture == null) {
                throw new UnauthorizedUpdateException(
                        "Le passage au statut Occupation est impossible tant qu’aucune facture réelle n’a été réglée par le client pour le mouvement #" + entity.getId()
                );
            }
        }
    }

    private void sauvegarderInfrastructures(List<MouvementInfra> mouvementInfras) {
        this.mouvementInfraRepo.saveAll(mouvementInfras);
    }

    private void historiserMouvement(Mouvement entity, String id) {
        List<HistoriqueMvt> historiqueMvts = historiqueRepo.findHistoriqueMvtByMouvement_Id(id);

        boolean existeDeja = historiqueMvts.stream()
                .anyMatch(histo -> histo.getTypeMouvement().getId().equals(entity.getTypeMouvement().getId()));

        if (!existeDeja) {
            HistoriqueMvt historique = new HistoriqueMvt();
            historique.setTypeMouvement(entity.getTypeMouvement());
            historique.setMouvement(entity);
            historique.setDhAction(Timestamp.valueOf(LocalDateTime.now()));
            historiqueRepo.save(historique);
        }
    }



    @Override
    public Mouvement save(Mouvement entity) {
        System.out.println("mouvement new:" + entity);
        entity.setDhMouvement(Timestamp.valueOf(LocalDateTime.now()));

        List<MouvementInfra> mouvementInfras = entity.getMouvementInfras();

        entity = repository.save(entity);

        this.sauvegarderInfrastructures(mouvementInfras);

        HistoriqueMvt historique = new HistoriqueMvt();
        historique.setTypeMouvement(entity.getTypeMouvement());
        historique.setMouvement(entity);
        historique.setDhAction(Timestamp.valueOf(LocalDateTime.now()));
        historiqueRepo.save(historique);

        return entity;
    }

    @Override
    public List<Mouvement> getMouvementByClient_Id(String client_id) {
        return mouvementRepo.getMouvementByClient_Id(client_id)
                .stream()
                .filter(this::isInfrastructureActive)
                .collect(Collectors.toList());
    }


    @Override
    public List<Mouvement> getMouvementByInfrastructure_Id(String infrastructure_id) {
        return mouvementRepo.getMouvementByInfrastructureId(infrastructure_id)
                .stream()
                .filter(this::isInfrastructureActive)
                .collect(Collectors.toList());
    }


    @Override
    public Page<MouvementDto> getMouvementByClient_Id(String client_id, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Mouvement> pageMouvements =
                mouvementRepo.getMouvementByClient_IdOrderByDhMouvementDesc(client_id, pageable);

        List<MouvementDto> filtered = pageMouvements.getContent().stream()
                .filter(this::isInfrastructureActive)
                .map(MouvementDto::new)
                .collect(Collectors.toList());

        return new PageImpl<>(filtered, pageable, filtered.size());
    }


    @Override
    public Page<MouvementDto> getMouvementByInfrastructure_Id(String infrastructure_id, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Mouvement> pages =
                mouvementRepo.getMouvementByInfrastructureId(infrastructure_id, pageable);

        List<MouvementDto> filtered = pages.getContent().stream()
                .filter(this::isInfrastructureActive)
                .map(MouvementDto::new)
                .collect(Collectors.toList());

        return new PageImpl<>(filtered, pageable, filtered.size());
    }
    @Override
    public List<MouvementCalendarDto> getMouvementCalendarDto() {
        return mouvementRepo.findAll().stream()
                .filter(this::isInfrastructureActive)
                .filter(mouvement -> {
                    String id = mouvement.getTypeMouvement().getId();
                    return cclPropertyService.getOccupationId().equals(id) || cclPropertyService.getReservationId().equals(id);
                })
                .map(MouvementCalendarDto::new)
                .collect(Collectors.toList());
    }
    private boolean ifContainsInfra(Mouvement mouvement, String infraId) {
        if (mouvement == null || infraId == null || mouvement.getMouvementInfras() == null) {
            return true;
        }

        for (MouvementInfra minf : mouvement.getMouvementInfras()) {
            System.out.println("minf ifContainsInfra:"+minf.getInfrastructure().getNom());
            if (minf.getInfrastructure() != null && infraId.equals(minf.getInfrastructure().getId())) {
                return true;
            }
        }
        return false;
    }


    @Override
    public List<MouvementCalendarDto> getMouvementCalendarDtoByInfratructureId(String infraId) {
        System.out.println("id infrastructure calendar search:" + infraId);

        return mouvementRepo.findAll().stream()
                .filter(this::isInfrastructureActive)
                .filter(mouvement -> {
                    boolean infraBool = (infraId == null || ifContainsInfra(mouvement, infraId));
                    String id = mouvement.getTypeMouvement().getId();
                    return (cclPropertyService.getOccupationId().equals(id)
                            || cclPropertyService.getReservationId().equals(id))
                            && infraBool;
                })
                .map(MouvementCalendarDto::new)
                .collect(Collectors.toList());
    }
    private boolean ifInModeles(Mouvement mouvement, String[] modelesIds) {
        if (modelesIds == null || modelesIds.length == 0) {
            System.out.println("criteria calendar nullisation");
            return true;
        }

        return mouvement.getMouvementInfras().stream()
                .anyMatch(mi -> mi.getInfrastructure() != null
                        && mi.getInfrastructure().getModeleInfra() != null
                        && mi.getInfrastructure().getModeleInfra().getId() != null
                        && Arrays.asList(modelesIds).contains(mi.getInfrastructure().getModeleInfra().getId()));
    }



    @Override
    public List<MouvementCalendarDto> getMouvementCalendarDtoByCriteria(String infraId, String[] modelesIds) {
        return this.getListReservationOccupation().stream()
                .filter(this::isInfrastructureActive)
                .filter(mouvement -> {

                    boolean matchInfra = ifContainsInfra(mouvement, infraId);
                    boolean matchModele = ifInModeles(mouvement, modelesIds);

                    return matchInfra && matchModele;
                })
                .map(MouvementCalendarDto::new)
                .collect(Collectors.toList());
    }



    @Override
    public Page<MouvementDto> getAllPaginated(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "dhMouvement"));
        Page<Mouvement> pages = mouvementRepo.findAll(pageable);

        List<MouvementDto> filtered = pages.getContent().stream()
                .filter(this::isInfrastructureActive)
                .map(MouvementDto::new)
                .collect(Collectors.toList());

        return new PageImpl<>(filtered, pageable, filtered.size());
    }

    @Override
    public Page<MouvementDto> getAllCriteria(int page, int pageSize, Mouvement criteria ,String catInfraId) {
        Page<MouvementDto> pages = this.getAllPaginated(page, pageSize);

        List<MouvementDto> filtered = pages.getContent().stream()
                .filter(Objects::nonNull)
                .filter(mouvementDto -> {
                    try {
                        return matchCriteria(criteria, mouvementDto ,  catInfraId);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(page, pageSize);
        return new PageImpl<>(filtered, pageable, filtered.size());
    }

    private boolean containsCatInfra(Mouvement mouvement, String catInfraId) {
        if (mouvement.getMouvementInfras() == null || catInfraId == null) {
            return false;
        }

        return mouvement.getMouvementInfras().stream()
                .map(MouvementInfra::getInfrastructure)
                .filter(infra -> infra != null && infra.getModeleInfra() != null
                        && infra.getModeleInfra().getCatInfra() != null)
                .anyMatch(infra -> catInfraId.equals(infra.getModeleInfra().getCatInfra().getId()));
    }


    private boolean matchCriteria(Mouvement criteria, MouvementDto mouvementDto, String catInfraId) {
        if (criteria == null || mouvementDto == null) {
            return false;
        }

        if (criteria.getMouvementInfras() != null && !criteria.getMouvementInfras().isEmpty()) {
            Mouvement mouvement = this.mouvementRepo.findById(mouvementDto.getId()).orElse(null);
            assert mouvement != null;
            if (!containsCatInfra(mouvement, catInfraId)) {
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

        if (criteria.getPeriodeDebut() != null && criteria.getPeriodeFin() != null) {
            List<HistoriqueMvt> historiques = historiqueRepo.findHistoriqueMvtByMouvement_Id(mouvementDto.getId());
            if (historiques == null || historiques.isEmpty()) {
                return false;
            }

            for (HistoriqueMvt histo : historiques) {
                if (histo != null && histo.getDhAction() != null) {
                    Timestamp dhAction = histo.getDhActionTimestamp();
                    if (dhAction.before(criteria.getPeriodeDebut()) || dhAction.after(criteria.getPeriodeFin())) {
                        return false;
                    }
                }
            }
        }

        return true;
    }


    @Override
    public Mouvement accorderMouvement(String id){
        Mouvement mouvement = this.mouvementRepo.findById(id).orElse(null) ;
        if(mouvement!=null ){
            if( mouvement.getTypeMouvement().getId().equals(cclPropertyService.getReservationId())){
                mouvement.setTypeMouvement(typeMouvementService.findById(cclPropertyService.getOccupationId()));
                return this.update(mouvement , mouvement.getId());
            }
            else{
                throw new UnauthorizedException("Il n 'y a que les reservations qui peuvent etre accorder en occupation ");
            }
        }
        return null;
    }

    @Override
    public Mouvement classerMouvement(String id){
        Mouvement mouvement = this.mouvementRepo.findById(id).orElse(null) ;
        assert mouvement != null;
        mouvement.setTypeMouvement(typeMouvementService.findById(cclPropertyService.getClassementId()));
        return this.update(mouvement, mouvement.getId());
    }
    private boolean isInfrastructureActive(Mouvement mouvement) {
        if (mouvement == null || mouvement.getMouvementInfras() == null) {
            return false;
        }

        return mouvement.getMouvementInfras().stream()
                .map(MouvementInfra::getInfrastructure)
                .filter(infra -> infra != null && infra.getEtat() != null && infra.getEtat().getCode() != null)
                .anyMatch(infra -> !infra.getEtat().getCode().equals(cclPropertyService.getInactifCode()));
    }

    public List<Mouvement> getListReservationOccupation() {
        List<TypeMouvement> typeMouvements = this.typeMouvementService.getTypeMouvementAddingMouvement();
        return this.findAll().stream()
                .filter(mouvement -> typeMouvements.contains(mouvement.getTypeMouvement()))
                .collect(Collectors.toList());
    }
    @Override
    public List<MouvementDto> getListMouvementConflict(Mouvement mouvement) {
        List<Mouvement> mouvements = this.getListReservationOccupation();

        if (mouvement == null || mouvement.getPeriodeDebut() == null || mouvement.getPeriodeFin() == null) {
            return Collections.emptyList();
        }

        return new MouvementDto().toMouvementDtos( mouvements.stream()
                .filter(m -> m.getId() != null && !m.getId().equals(mouvement.getId()))
                .filter(m -> m.getPeriodeDebut() != null && m.getPeriodeFin() != null)
                .filter(m -> !m.getPeriodeFin().before(mouvement.getPeriodeDebut()) &&
                        !m.getPeriodeDebut().after(mouvement.getPeriodeFin()))
                .collect(Collectors.toList()));
    }




}