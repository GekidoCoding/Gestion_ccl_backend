package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.jpa.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.entity.Localisation;
import mg.cnaps.gestion.ccl.project.repository.LocalisationRepo;
import mg.cnaps.gestion.ccl.project.service.LocalisationService;
import org.springframework.stereotype.Service;

@Service
public class LocalisationImpl extends GenericServiceImpl<Localisation, String , LocalisationRepo> implements LocalisationService {
    public LocalisationImpl(LocalisationRepo repo) {
        super(repo);
    }
}