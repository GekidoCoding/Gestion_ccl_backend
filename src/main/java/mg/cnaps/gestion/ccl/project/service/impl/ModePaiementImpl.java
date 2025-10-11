package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.jpa.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.entity.ModePaiement;
import mg.cnaps.gestion.ccl.project.repository.ModePaiementRepo;
import mg.cnaps.gestion.ccl.project.service.ModePaiementService;
import org.springframework.stereotype.Service;

@Service
public class ModePaiementImpl extends GenericServiceImpl<ModePaiement, String , ModePaiementRepo> implements ModePaiementService {
    public ModePaiementImpl(ModePaiementRepo repo) {
        super(repo);
    }
}