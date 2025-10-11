package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.jpa.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.config.CclPropertyService;
import mg.cnaps.gestion.ccl.project.entity.*;
import mg.cnaps.gestion.ccl.project.entity.dto.mouvement.MouvementCalendarDto;
import mg.cnaps.gestion.ccl.project.entity.dto.mouvement.MouvementDto;
import mg.cnaps.gestion.ccl.project.exception.UnauthorizedException;
import mg.cnaps.gestion.ccl.project.exception.UnauthorizedUpdateException;
import mg.cnaps.gestion.ccl.project.repository.EtatRepo;
import mg.cnaps.gestion.ccl.project.repository.HistoriqueMvtRepo;
import mg.cnaps.gestion.ccl.project.repository.MouvementRepo;
import mg.cnaps.gestion.ccl.project.service.EtatService;
import mg.cnaps.gestion.ccl.project.service.MouvementService;
import mg.cnaps.gestion.ccl.project.service.TypeMouvementService;
import mg.cnaps.gestion.ccl.project.util.GestionnaireUtil;
import mg.cnaps.gestion.ccl.project.util.MouvementUtil;
import mg.cnaps.gestion.ccl.project.util.PageUtil;
import mg.cnaps.gestion.ccl.project.util.TimestampUtil;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MouvementImpl extends GenericServiceImpl<Mouvement, String, MouvementRepo> implements MouvementService {
    private final MouvementRepo mouvementRepo;
    private final HistoriqueMvtRepo historiqueRepo;
    private final CclPropertyService cclPropertyService;
    private final TypeMouvementService typeMouvementService;
    private final MouvementUtil mouvementUtil;
    private final EtatRepo etatRepo;
    private final GestionnaireUtil gestionnaireUtil;
    public MouvementImpl(MouvementRepo repo, MouvementRepo mouvementRepo, HistoriqueMvtRepo historiqueRepo,
                         CclPropertyService cclPropertyService, TypeMouvementService typeMouvementService,
                         MouvementUtil mouvementUtil, EtatRepo etatRepo, GestionnaireUtil gestionnaireUtil) {
        super(repo);
        this.mouvementRepo = mouvementRepo;
        this.historiqueRepo = historiqueRepo;
        this.cclPropertyService = cclPropertyService;
        this.typeMouvementService = typeMouvementService;
        this.mouvementUtil = mouvementUtil;
        this.etatRepo = etatRepo;
        this.gestionnaireUtil = gestionnaireUtil;
    }

    @Override
    public List<Mouvement> findAll() {
        try{
            Etat etatInactif = etatRepo.getEtatByCode(cclPropertyService.getInactifCode());
            return repository.findAll().stream()
                    .sorted(Comparator.comparing(Mouvement::getDhMouvement).reversed())
                    .filter(m -> !m.getEtat().getId().equals(etatInactif.getId()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Override
    public Mouvement update(Mouvement entity, String id, String matricule){
        return this.update(entity, id ,false ,matricule);
    }

    public Mouvement update(Mouvement entity, String id , boolean classification , String matricule) {
        //verifier gestionnaire
        Gestionnaire gestionnaire = gestionnaireUtil.getGestionnaireHisto(matricule);
        if (!repository.existsById(id)) {
            throw new RuntimeException("Not Found ID");
        }

        Mouvement ancien = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mouvement introuvable avec l'ID " + id));

        if (!classification && ancien.getTypeMouvement().getId().equals(cclPropertyService.getClassementId())) {
            throw new UnauthorizedUpdateException(
                    "Un mouvement de type Classement n'est plus modifiable pour #" + entity.getId()
            );
        }
        if (entity.getTypeMouvement().getId().equals(cclPropertyService.getOccupationId())) {
            if (!mouvementUtil.ifAuthorizedOccupation(entity.getId())) {
                throw new UnauthorizedUpdateException("On ne peut pas passer en occupation si " + cclPropertyService.getAvancePaiement() + " % des factures réelles ne sont pas payées ");
            }
        }


        mouvementUtil.verifierNiveauProcessus(entity, ancien);

        mouvementUtil.verifierPassageOccupation(entity);

        entity.setDhMouvement(Timestamp.valueOf(LocalDateTime.now()));

        List<MouvementInfra> mouvementInfras = entity.getMouvementInfras();


        entity = repository.save(entity);

        mouvementUtil.sauvegarderInfrastructures(mouvementInfras);
        mouvementUtil.historiserMouvement(entity, id , gestionnaire);

        return entity;
    }

    @Override
    public Mouvement save(Mouvement entity, String matricule) {
        try{
            Gestionnaire gestionnaire = this.gestionnaireUtil.getGestionnaireHisto(matricule);
            entity.setDhMouvement(Timestamp.valueOf(LocalDateTime.now()));

            List<MouvementInfra> mouvementInfras = entity.getMouvementInfras();
            entity.setEtat(this.etatRepo.getEtatByCode(cclPropertyService.getActifCode()));
            entity = repository.save(entity);

            mouvementUtil.sauvegarderInfrastructures(mouvementInfras);

            HistoriqueMvt historique = new HistoriqueMvt();
            historique.setTypeMouvement(entity.getTypeMouvement());
            historique.setMouvement(entity);
            historique.setGestionnaire(gestionnaire);

            historique.setDhAction(Timestamp.valueOf(LocalDateTime.now()));
            historiqueRepo.save(historique);

            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }

    @Override
    public List<Mouvement> getMouvementByClient_Id(String client_id) {
        return mouvementRepo.getMouvementByClient_Id(client_id)
                .stream()
                .filter(mouvementUtil::isInfrastructureActive)
                .collect(Collectors.toList());
    }

    @Override
    public List<Mouvement> getMouvementByInfrastructure_Id(String infrastructure_id) {
        return mouvementRepo.getMouvementByInfrastructureId(infrastructure_id)
                .stream()
                .filter(mouvementUtil::isInfrastructureActive)
                .collect(Collectors.toList());
    }

    @Override
    public Page<MouvementDto> getMouvementByClient_Id(String client_id, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Mouvement> pageMouvements =
                mouvementRepo.getMouvementByClient_IdOrderByDhMouvementDesc(client_id, pageable);

        List<MouvementDto> filtered = pageMouvements.getContent().stream()
                .filter(mouvementUtil::isInfrastructureActive)
                .map(MouvementDto::new)
                .collect(Collectors.toList());

        return new PageImpl<>(filtered, pageable, pageMouvements.getTotalElements());
    }

    @Override
    public Page<MouvementDto> getMouvementByInfrastructure_Id(String infrastructure_id, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Mouvement> pages =
                mouvementRepo.getMouvementByInfrastructureId(infrastructure_id, pageable);

        List<MouvementDto> filtered = pages.getContent().stream()
                .filter(mouvementUtil::isInfrastructureActive)
                .map(MouvementDto::new)
                .collect(Collectors.toList());

        return new PageImpl<>(filtered, pageable, pages.getTotalElements());
    }

    @Override
    public List<MouvementCalendarDto> getMouvementCalendarDto() {
        List<Mouvement> mouvements= this.getListReservationOccupation();
        List<MouvementCalendarDto> mvtCalendars = new ArrayList<>();
        for (Mouvement mouvement: mouvements) {
            for (MouvementInfra mvtInfra : mouvement.getMouvementInfras()) {
                mvtCalendars.add(new MouvementCalendarDto(mouvement , mvtInfra.getInfrastructure() ));
            }
        }
        return mvtCalendars;
    }

    @Override
    public List<MouvementCalendarDto> getMouvementCalendarDtoByInfratructureId(String infraId) {

        return this.findAll().stream()
                .filter(mouvementUtil::isInfrastructureActive)
                .filter(mouvement -> {
                    boolean infraBool = (infraId == null || mouvementUtil.ifContainsInfra(mouvement, infraId));
                    String id = mouvement.getTypeMouvement().getId();
                    return (cclPropertyService.getOccupationId().equals(id)
                            || cclPropertyService.getReservationId().equals(id))
                            && infraBool;
                })
                .map(MouvementCalendarDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<MouvementCalendarDto> getMouvementCalendarDtoByCriteria(String infraId, String[] modelesIds) {
        return this.getListReservationOccupation().stream()
                .filter(mouvementUtil::isInfrastructureActive)
                .filter(mouvement -> {
                    boolean matchInfra = mouvementUtil.ifContainsInfra(mouvement, infraId);
                    boolean matchModele = mouvementUtil.ifInModeles(mouvement, modelesIds);
                    return matchInfra && matchModele;
                })
                .map(MouvementCalendarDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public Page<MouvementDto> getAllPaginated(int page, int pageSize) {
        // Récupérer tous les mouvements triés
        List<Mouvement> allMouvements = this.findAll();

        // Filtrer ceux dont l'infrastructure est active et mapper en DTO
        List<MouvementDto> filtered = allMouvements.stream()
                .filter(mouvementUtil::isInfrastructureActive)
                .map(MouvementDto::new)
                .collect(Collectors.toList());

        // Pagination
        List<MouvementDto> paged = new PageUtil<MouvementDto>().paginateList(page, pageSize, filtered);

        return new PageImpl<>(paged, PageRequest.of(page, pageSize), filtered.size());
    }

    @Override
    public Page<MouvementDto> getAllCriteria( int page, int pageSize, Mouvement criteria, String catInfraId) {
        try{
            // Récupérer tous les mouvements filtrés par infrastructure active
            List<Mouvement> allMouvements =this.findAll();

            List<MouvementDto> filtered = allMouvements.stream()
                    .filter(mouvementUtil::isInfrastructureActive)
                    .map(MouvementDto::new)
                    .filter(mouvementDto -> {
                        try {
                            return mouvementUtil.matchCriteria(criteria, mouvementDto, catInfraId, historiqueRepo, mouvementRepo);
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());

            // Pagination
            List<MouvementDto> paged = new PageUtil<MouvementDto>().paginateList(page, pageSize, filtered);

            return new PageImpl<>(paged, PageRequest.of(page, pageSize), filtered.size());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }


    @Override
    public Mouvement accorderMouvement(String id) {
        Mouvement mouvement = this.mouvementRepo.findById(id).orElse(null);
        if (mouvement != null) {
            if (mouvement.getTypeMouvement().getId().equals(cclPropertyService.getReservationId())) {
                mouvement.setTypeMouvement(typeMouvementService.findById(cclPropertyService.getOccupationId()));
                return this.update(mouvement, mouvement.getId());
            } else {
                throw new UnauthorizedException("Il n'y a que les réservations qui peuvent être accordées en occupation");
            }
        }
        return null;
    }

    @Override
    public void annuler(String id) {
        Mouvement mouvement = this.findById(id);
        mouvement.setEtat(etatRepo.getEtatByCode(cclPropertyService.getInactifCode()));
        repository.save(mouvement);
    }

    @Override
    public Mouvement classerMouvement(String id, String matricule) {
        try{
            Mouvement mouvement = this.mouvementRepo.findById(id).orElse(null);
            assert mouvement != null;
            mouvement.setTypeMouvement(typeMouvementService.findById(cclPropertyService.getClassementId()));
            return this.update(mouvement, mouvement.getId() , true , matricule);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
    public List<Mouvement> getListByTypeMouvementId(String typeMouvementId) {
        return this.findAll().stream()
                .filter(mouvement -> (mouvement.getTypeMouvement().getId().equals(typeMouvementId) ))
                .collect(Collectors.toList());
    }

    public List<Mouvement> getListReservationOccupation() {
        List<TypeMouvement> typeMouvements = this.typeMouvementService.getTypeMouvementAddingMouvement();
        return this.findAll().stream()
                .filter(mouvementUtil::isInfrastructureActive)
                .filter(mouvement -> typeMouvements.contains(mouvement.getTypeMouvement()))
                .collect(Collectors.toList());
    }

    @Override
    public List<MouvementDto> getListMouvementConflict(Mouvement mouvement) {
        List<Mouvement> mouvements = this.getListReservationOccupation();

        if (mouvement == null || mouvement.getPeriodeDebut() == null || mouvement.getPeriodeFin() == null) {
            return Collections.emptyList();
        }

        // Récupérer les IDs des infrastructures cibles (du mouvement en cours)
        Set<String> cibleInfraIds = Optional.ofNullable(mouvement.getMouvementInfras())
                .orElse(Collections.emptyList())
                .stream()
                .map(MouvementInfra::getInfrastructure)
                .filter(Objects::nonNull)
                .map(Infrastructure::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<Mouvement> mouvementsEnConflit = mouvements.stream()
                // Exclure le même mouvement
                .filter(m -> m.getId() != null && !m.getId().equals(mouvement.getId()))
                // Périodes valides
                .filter(m -> m.getPeriodeDebut() != null && m.getPeriodeFin() != null)
                // Chevauchement temporel
                .filter(m -> TimestampUtil.isOverlap(m.getPeriodeDebut(), m.getPeriodeFin(),
                        mouvement.getPeriodeDebut(), mouvement.getPeriodeFin()))
                // Au moins une infrastructure commune (compare les IDs)
                .filter(m -> Optional.ofNullable(m.getMouvementInfras())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(MouvementInfra::getInfrastructure)
                        .filter(Objects::nonNull)
                        .map(Infrastructure::getId)
                        .filter(Objects::nonNull)
                        .anyMatch(cibleInfraIds::contains))
                .collect(Collectors.toList());

        return new MouvementDto().toMouvementDtos(mouvementsEnConflit);
    }

    @Override
    public List<Mouvement> getOccupationNotPayedInDelai() {
        LocalDate today = LocalDate.now();
        int delai = cclPropertyService.getDelaiPaiementJour();

        List<Mouvement> mouvements = this.getListByTypeMouvementId(cclPropertyService.getOccupationId());
        List<Mouvement> mouvementsNotPayed = new ArrayList<>();

        for (Mouvement mouvement : mouvements) {
            LocalDate debut = mouvement.getPeriodeDebut().toLocalDateTime().toLocalDate();

            long daysBetween = ChronoUnit.DAYS.between(today, debut);

            if (daysBetween > 0 && daysBetween <= delai) {
                if (!this.mouvementUtil.ifPayedTotalite(mouvement.getId())) {
                    mouvementsNotPayed.add(mouvement);
                }
            }
        }
        return mouvementsNotPayed;
    }
    @Override
    public List<Mouvement> getOccupationPayedInDelai() {
        LocalDate today = LocalDate.now();
        int delai = cclPropertyService.getPreparationSignalementJour();

        List<Mouvement> mouvements = this.getListByTypeMouvementId(cclPropertyService.getOccupationId());
        List<Mouvement> mouvementsPayed = new ArrayList<>();

        for (Mouvement mouvement : mouvements) {
            LocalDate debut = mouvement.getPeriodeDebut().toLocalDateTime().toLocalDate();

            long daysBetween = ChronoUnit.DAYS.between(today, debut);

            if (daysBetween > 0 && daysBetween <= delai) {
                if (this.mouvementUtil.ifPayedTotalite(mouvement.getId())) {
                    mouvementsPayed.add(mouvement);
                }
            }
        }
        return mouvementsPayed;
    }
}