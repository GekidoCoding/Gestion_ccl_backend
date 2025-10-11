package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.jpa.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.config.CclPropertyService;
import mg.cnaps.gestion.ccl.project.entity.TypeMouvement;
import mg.cnaps.gestion.ccl.project.repository.TypeMouvementRepo;
import mg.cnaps.gestion.ccl.project.service.TypeMouvementService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TypeMouvementImpl extends GenericServiceImpl<TypeMouvement, String , TypeMouvementRepo> implements TypeMouvementService {
    private final CclPropertyService cclPropertyService;
    public TypeMouvementImpl(TypeMouvementRepo repo, CclPropertyService cclPropertyService) {
        super(repo);
        this.cclPropertyService = cclPropertyService;
    }

    @Override
    public List<TypeMouvement> getTypeMouvementAddingMouvement() {
        return this.findAll().stream()
                .filter(tm -> tm.getId().equals(cclPropertyService.getReservationId())
                        || tm.getId().equals(cclPropertyService.getRenseignementId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TypeMouvement> getTypeMouvementAdding() {
        return this.findAll().stream()
                .filter(tm -> tm.getId().equals(cclPropertyService.getReservationId())
                        || tm.getId().equals(cclPropertyService.getRenseignementId())
                        ||  tm.getId().equals(cclPropertyService.getOccupationId()))
                .collect(Collectors.toList());
    }
}