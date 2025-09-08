package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.entity.InfraTarif;
import mg.cnaps.gestion.ccl.project.repository.InfraTarifRepo;
import mg.cnaps.gestion.ccl.project.service.InfraTarifService;
import org.springframework.stereotype.Service;

@Service
public class InfraTarifImpl extends GenericServiceImpl<InfraTarif, String , InfraTarifRepo> implements InfraTarifService {
    public InfraTarifImpl(InfraTarifRepo repo) {
        super(repo);
    }
}