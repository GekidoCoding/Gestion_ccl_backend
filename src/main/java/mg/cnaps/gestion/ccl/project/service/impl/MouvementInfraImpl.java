package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.entity.MouvementInfra;
import mg.cnaps.gestion.ccl.project.repository.MouvementInfraRepo;
import mg.cnaps.gestion.ccl.project.service.MouvementInfraService;
import org.springframework.stereotype.Service;

@Service
public class MouvementInfraImpl extends GenericServiceImpl<MouvementInfra, String , MouvementInfraRepo> implements MouvementInfraService {
    public MouvementInfraImpl(MouvementInfraRepo repo) {
        super(repo);
    }
}

