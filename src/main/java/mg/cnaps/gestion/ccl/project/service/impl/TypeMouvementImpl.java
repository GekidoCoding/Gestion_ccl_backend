package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.entity.TypeMouvement;
import mg.cnaps.gestion.ccl.project.repository.TypeMouvementRepo;
import mg.cnaps.gestion.ccl.project.service.TypeMouvementService;
import org.springframework.stereotype.Service;

@Service
public class TypeMouvementImpl extends GenericServiceImpl<TypeMouvement, String , TypeMouvementRepo> implements TypeMouvementService {
    public TypeMouvementImpl(TypeMouvementRepo repo) {
        super(repo);
    }
}