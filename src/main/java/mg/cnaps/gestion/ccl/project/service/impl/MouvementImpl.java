package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.config.CclPropertyService;
import mg.cnaps.gestion.ccl.project.entity.HistoriqueMvt;
import mg.cnaps.gestion.ccl.project.entity.Mouvement;
import mg.cnaps.gestion.ccl.project.entity.dto.mouvement.MouvementCalendarDto;
import mg.cnaps.gestion.ccl.project.entity.dto.mouvement.MouvementDto;
import mg.cnaps.gestion.ccl.project.exception.UnauthorizedException;
import mg.cnaps.gestion.ccl.project.repository.FactureRepo;
import mg.cnaps.gestion.ccl.project.repository.HistoriqueMvtRepo;
import mg.cnaps.gestion.ccl.project.repository.MouvementRepo;
import mg.cnaps.gestion.ccl.project.service.MouvementService;
import mg.cnaps.gestion.ccl.project.service.TypeMouvementService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MouvementImpl extends GenericServiceImpl<Mouvement, String , MouvementRepo> implements MouvementService {
    private final MouvementRepo mouvementRepo;
    private final HistoriqueMvtRepo historiqueRepo;
    private final CclPropertyService cclPropertyService;
    private final TypeMouvementService typeMouvementService;

    public MouvementImpl(MouvementRepo repo , MouvementRepo mouvementRepo, HistoriqueMvtRepo historiqueRepo, CclPropertyService cclPropertyService, TypeMouvementService typeMouvementService) {
        super(repo);
        this.mouvementRepo=mouvementRepo;
        this.historiqueRepo = historiqueRepo;
        this.cclPropertyService = cclPropertyService;
        this.typeMouvementService = typeMouvementService;
    }
    @Override
    public Mouvement update (Mouvement entity , String id ){
        System.out.println(entity.toString());

        if(!repository.existsById(id)){
            throw new RuntimeException("Not Found ID ");
        }
        entity.setDhMouvement(Timestamp.from(Instant.now()));
        entity= repository.save(entity);

        HistoriqueMvt isExist = null;
        List<HistoriqueMvt> historiqueMvts = this.historiqueRepo.findHistoriqueMvtByMouvement_Id(id);
        for (HistoriqueMvt histo: historiqueMvts ) {
            if(histo.getTypeMouvement().getId().equals(entity.getTypeMouvement().getId()) ) {
                isExist = histo;
                break;
            }
        }
        if(isExist==null){
            HistoriqueMvt historique = new HistoriqueMvt();
            historique.setTypeMouvement(entity.getTypeMouvement());
            historique.setMouvement(entity);
            historique.setDhAction(Timestamp.from(Instant.now()));
            historiqueRepo.save(historique);
        }
        return entity;
    }

    @Override
    public Mouvement save(Mouvement entity){
        System.out.println("mouvement new:"+ entity);
        entity.setDhMouvement(Timestamp.from(Instant.now()));
        entity= repository.save(entity);
        //        Gestionnaire gestionnaire = new Gestionnaire();
        HistoriqueMvt historique = new HistoriqueMvt();
//        historique.setGestionnaire(gestionnaire);
        historique.setTypeMouvement(entity.getTypeMouvement());
        historique.setMouvement(entity);
        historique.setDhAction(Timestamp.from(Instant.now()));
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
        return mouvementRepo.getMouvementByInfrastructure_Id(infrastructure_id)
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
                mouvementRepo.getMouvementByInfrastructure_IdOrderByDhMouvementDesc(infrastructure_id, pageable);

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
    @Override
    public List<MouvementCalendarDto> getMouvementCalendarDtoByInfratructureId(String infratructureId) {
        return mouvementRepo.findAll().stream()
                .filter(this::isInfrastructureActive)
                .filter(mouvement -> {
                    boolean infraBool = mouvement.getInfrastructure().getId().equals(infratructureId);
                    String id = mouvement.getTypeMouvement().getId();
                    return cclPropertyService.getOccupationId().equals(id) || cclPropertyService.getReservationId().equals(id) && infraBool;
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
    public Page<MouvementDto> getAllCriteria(int page, int pageSize, Mouvement criteria) {
        Page<MouvementDto> pages = this.getAllPaginated(page, pageSize);

        List<MouvementDto> filtered = pages.getContent().stream()
                .filter(Objects::nonNull)
                .filter(mouvementDto -> {
                    try {
                        return matchCriteria(criteria, mouvementDto);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(page, pageSize);
        return new PageImpl<>(filtered, pageable, filtered.size());
    }


    private boolean matchCriteria(Mouvement criteria, MouvementDto mouvement) {
        if (criteria == null || mouvement == null) {
            return false;
        }
        if (criteria.getInfrastructure() != null
                && criteria.getInfrastructure().getModeleInfra() != null
                && criteria.getInfrastructure().getModeleInfra().getCatInfra() != null
                && criteria.getInfrastructure().getModeleInfra().getCatInfra().getId() != null) {

            String idCatInfra = criteria.getInfrastructure().getModeleInfra().getCatInfra().getId();

            if (mouvement.getInfrastructure() != null
                    && mouvement.getInfrastructure().getModeleInfra() != null
                    && mouvement.getInfrastructure().getModeleInfra().getCatInfra() != null
                    && mouvement.getInfrastructure().getModeleInfra().getCatInfra().getId() != null) {

                String idCatMouv = mouvement.getInfrastructure().getModeleInfra().getCatInfra().getId();

                if (!idCatInfra.equals(idCatMouv)) {
                    return false;
                }
            } else {
                return false;
            }
        }

        if (criteria.getTypeMouvement() != null
                && criteria.getTypeMouvement().getId() != null) {

            String idTypeCriteria = criteria.getTypeMouvement().getId();

            if (mouvement.getTypeMouvement() != null
                    && mouvement.getTypeMouvement().getId() != null) {

                String idTypeMouv = mouvement.getTypeMouvement().getId();

                if (!idTypeCriteria.equals(idTypeMouv)) {
                    return false;
                }
            } else {
                return false;
            }
        }
        if (criteria.getPeriodeDebut() != null && criteria.getPeriodeFin() != null) {

            List<HistoriqueMvt> historiques = historiqueRepo.findHistoriqueMvtByMouvement_Id(mouvement.getId());
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
        return mouvement != null &&
                mouvement.getInfrastructure() != null &&
                mouvement.getInfrastructure().getEtat() != null &&
                mouvement.getInfrastructure().getEtat().getCode() != null &&
                !mouvement.getInfrastructure().getEtat().getCode().equals(cclPropertyService.getInactifCode());
    }


}