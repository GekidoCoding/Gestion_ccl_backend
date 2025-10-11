package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.jpa.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.entity.CategorieInfra;
import mg.cnaps.gestion.ccl.project.repository.CategorieInfraRepo;
import mg.cnaps.gestion.ccl.project.service.CategorieInfraService;
import org.springframework.stereotype.Service;

@Service
public class CategorieInfraImpl extends GenericServiceImpl<CategorieInfra, String , CategorieInfraRepo> implements CategorieInfraService {
    public CategorieInfraImpl(CategorieInfraRepo repo) {
        super(repo);
    }
}