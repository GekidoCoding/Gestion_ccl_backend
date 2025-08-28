package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.entity.ModeleInfra;
import mg.cnaps.gestion.ccl.project.repository.ModeleInfraRepo;
import mg.cnaps.gestion.ccl.project.service.ModeleInfraService;
import org.springframework.stereotype.Service;

@Service
public class ModeleInfraImpl extends GenericServiceImpl<ModeleInfra, String , ModeleInfraRepo> implements ModeleInfraService {
    public ModeleInfraImpl(ModeleInfraRepo repo) {
        super(repo);
    }
}