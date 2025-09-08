package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.config.CclPropertyService;
import mg.cnaps.gestion.ccl.project.entity.Etat;
import mg.cnaps.gestion.ccl.project.repository.EtatRepo;
import mg.cnaps.gestion.ccl.project.service.EtatService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EtatImpl extends GenericServiceImpl<Etat, String ,EtatRepo> implements EtatService {

    private final CclPropertyService cclPropertyService;
    public EtatImpl(EtatRepo repo, CclPropertyService cclPropertyService) {
        super(repo);
        this.cclPropertyService = cclPropertyService;
    }

    public List<Etat> getEtatsFacture() {
        return this.findAll().stream()
                .filter(etat -> !etat.getCode().equals(cclPropertyService.getInactifCode())
                        && !etat.getCode().equals(cclPropertyService.getActifCode()))
                .collect(Collectors.toList());
    }

    public List<Etat> getEtatsAutre() {
        return this.findAll().stream()
                .filter(etat -> etat.getCode().equals(cclPropertyService.getInactifCode())
                        || etat.getCode().equals(cclPropertyService.getActifCode()))
                .collect(Collectors.toList());
    }

}