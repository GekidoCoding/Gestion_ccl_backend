package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.entity.Etat;
import mg.cnaps.gestion.ccl.project.repository.EtatRepo;
import mg.cnaps.gestion.ccl.project.service.EtatService;
import org.springframework.stereotype.Service;

@Service
public class EtatImpl extends GenericServiceImpl<Etat, String ,EtatRepo> implements EtatService {
    public EtatImpl(EtatRepo repo) {
        super(repo);
    }
}