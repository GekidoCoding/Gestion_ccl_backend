package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.entity.Frequence;
import mg.cnaps.gestion.ccl.project.repository.FrequenceRepo;
import mg.cnaps.gestion.ccl.project.service.FrequenceService;
import org.springframework.stereotype.Service;

@Service
public class FrequenceImpl extends GenericServiceImpl<Frequence, String ,FrequenceRepo> implements FrequenceService {
    public FrequenceImpl(FrequenceRepo repo) {
        super(repo);
    }
}