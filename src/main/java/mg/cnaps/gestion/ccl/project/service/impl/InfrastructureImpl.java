package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.config.CclPropertyService;
import mg.cnaps.gestion.ccl.project.entity.*;
import mg.cnaps.gestion.ccl.project.exception.EtatNotFoundException;
import mg.cnaps.gestion.ccl.project.repository.EtatRepo;
import mg.cnaps.gestion.ccl.project.repository.HistoriqueInfraRepo;
import mg.cnaps.gestion.ccl.project.repository.InfrastructureRepo;
import mg.cnaps.gestion.ccl.project.repository.MouvementRepo;
import mg.cnaps.gestion.ccl.project.service.InfrastructureService;
import mg.cnaps.gestion.ccl.project.util.CclProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class InfrastructureImpl extends GenericServiceImpl<Infrastructure, String , InfrastructureRepo> implements InfrastructureService {
    private final  EtatRepo etatRepo;
    private final HistoriqueInfraRepo historiqueInfraRepo;
    private final MouvementRepo mouvementRepo;
    private final CclPropertyService cclPropertyService;
    public InfrastructureImpl(InfrastructureRepo repo, EtatRepo etatRepo, HistoriqueInfraRepo historiqueInfraRepo, MouvementRepo mouvementRepo, CclPropertyService cclPropertyService) {
        super(repo);
        this.etatRepo = etatRepo;
        this.historiqueInfraRepo = historiqueInfraRepo;
        this.mouvementRepo = mouvementRepo;
        this.cclPropertyService = cclPropertyService;
    }

    @Override
    public List<Infrastructure> findAll() {

        List<Infrastructure> list= super.findAll();
        return list.stream()
                .filter(infra -> !infra.getEtat().getCode().equals(cclPropertyService.getInactifCode()))
                .collect(Collectors.toList());
    }

    @Override
    public Page<Infrastructure> findPaginated(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Infrastructure> pageContent = repository.findAll(pageable);
        List<Infrastructure> filteredContent = pageContent.getContent().stream()
                .filter(infra -> !infra.getEtat().getCode().equals(cclPropertyService.getInactifCode()))
                .collect(Collectors.toList());
        return new PageImpl<>(filteredContent, pageable, filteredContent.size());
    }


    @Override
    public Infrastructure update (Infrastructure entity , String id ){
        if(!repository.existsById(id)){
            throw new RuntimeException("Not Found ID ");
        }

        entity= repository.save(entity);
        //        Gestionnaire gestionnaire = new Gestionnaire();
        HistoriqueInfra historiqueInfra = new HistoriqueInfra();
//        historiqueInfra.setGestionnaire(gestionnaire);
        historiqueInfra.setEtat(entity.getEtat());
        historiqueInfra.setInfrastructure(entity);
        historiqueInfra.setObservation("");
        historiqueInfra.setDhAction(Timestamp.from(Instant.now()));
        historiqueInfraRepo.save(historiqueInfra);
        return entity;
    }

    @Override
    public Infrastructure save(Infrastructure entity){
        entity= repository.save(entity);
        //        Gestionnaire gestionnaire = new Gestionnaire();
        HistoriqueInfra historiqueInfra = new HistoriqueInfra();
        //        historiqueInfra.setGestionnaire(gestionnaire);
        historiqueInfra.setEtat(entity.getEtat());
        historiqueInfra.setInfrastructure(entity);
        historiqueInfra.setObservation("");
        historiqueInfra.setDhAction(Timestamp.from(Instant.now()));
        historiqueInfraRepo.save(historiqueInfra);
        return entity;
    }

    @Override
    public void delete(String id)throws Exception {
        Infrastructure infra = repository.getReferenceById(id);
        Etat etat= etatRepo.getEtatByCode(new CclProperty().getInactifCode());
        if(etat!=null){
            infra.setEtat(etat);
        }else{
            throw new EtatNotFoundException("Etat Inactif Non Trouvee dans la table Etat , veuillez l' inserez ! ");
        }
        this.repository.save(infra);
    }

    @Override
    public void delete(String id ,  String observation)throws Exception {
        Infrastructure infra = repository.getReferenceById(id);
        Etat etat= etatRepo.getEtatByCode(new CclProperty().getInactifCode());
        if(etat!=null){
            HistoriqueInfra historiqueInfra = new HistoriqueInfra();
            historiqueInfra.setObservation(observation);
            historiqueInfra.setEtat(etat);
            historiqueInfra.setInfrastructure(infra);
            historiqueInfra.setDhAction(Timestamp.from(Instant.now()) );
            historiqueInfraRepo.save(historiqueInfra);
            infra.setEtat(etat);
        }else{
            throw new EtatNotFoundException("Etat Inactif Non Trouvee dans la table Etat , veuillez l' inserez ! ");
        }

        this.repository.save(infra);
    }
    @Override
    public Page<Infrastructure> findByCriteriaAll(int page, int pageSize,
                                                  ModeleInfra[] modeles,
                                                  Localisation[] localisations,
                                                  Infrastructure criteria,
                                                  CategorieInfra catInfra,
                                                  Timestamp debut,
                                                  Timestamp fin) {
        Page<Infrastructure> infrastructures = this.findPaginated(page, pageSize);

        List<Infrastructure> filtered = infrastructures.getContent().stream()
                .filter(Objects::nonNull)
                .filter(infra -> matchCriteria(infra, criteria))
                .filter(infra -> matchModeleAndCategorie(infra, modeles, catInfra))
                .filter(infra -> matchLocalisations(infra, localisations))
                .filter(infra -> isNotOccupiedDuring(infra, debut, fin))
                .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(page, pageSize);
        return new PageImpl<>(filtered, pageable, infrastructures.getTotalElements());
    }


    private boolean matchCriteria(Infrastructure infra, Infrastructure criteria) {
        if (criteria != null) {
            if (criteria.getNom() != null && !criteria.getNom().isEmpty()) {
                if (infra.getNom() == null || !infra.getNom().toLowerCase().contains(criteria.getNom().toLowerCase())) {
                    return false;
                }
            }
            if (criteria.getNumero() != null && !criteria.getNumero().isEmpty()) {
                return infra.getNumero() != null && infra.getNumero().toLowerCase().contains(criteria.getNumero().toLowerCase());
            }

        }
        return true;
    }

    private boolean matchModeleAndCategorie(Infrastructure infra, ModeleInfra[] modeles, CategorieInfra catInfra) {
        boolean match = true;

        if (modeles != null && modeles.length > 0) {
            match = Arrays.stream(modeles)
                    .anyMatch(m -> infra.getModeleInfra() != null && infra.getModeleInfra().getId().equals(m.getId()));
            if (!match) {
                return false;
            }
        }

        if (catInfra != null) {
            return infra.getModeleInfra() != null &&
                    infra.getModeleInfra().getCatInfra() != null &&
                    infra.getModeleInfra().getCatInfra().getId().equals(catInfra.getId());
        }

        return true;
    }

    private boolean isNotOccupiedDuring(Infrastructure infra, Timestamp debut, Timestamp fin) {
        if (debut != null && fin != null) {
            List<Mouvement> mouvements = mouvementRepo.getMouvementByInfrastructure_Id(infra.getId());
            String occupationId = cclPropertyService.getOccupationId();

            for (Mouvement mvt : mouvements) {
                if (mvt.getTypeMouvement() != null && occupationId != null &&
                        occupationId.equals(mvt.getTypeMouvement().getId())) {

                    Timestamp mvtDebut = mvt.getPeriodeDebut();
                    Timestamp mvtFin = mvt.getPeriodeFin();

                    if (mvtDebut != null && mvtFin != null) {
                        boolean chevauche = (mvtFin.after(debut) && mvtDebut.before(fin));
                        if (chevauche) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    private boolean matchLocalisations(Infrastructure infra, Localisation[] localisations) {
        if (localisations != null && localisations.length > 0) {
            return Arrays.stream(localisations)
                    .anyMatch(loc -> infra.getLocalisation() != null &&
                            loc.getId().equals(infra.getLocalisation().getId()));
        }
        return true;
    }





}