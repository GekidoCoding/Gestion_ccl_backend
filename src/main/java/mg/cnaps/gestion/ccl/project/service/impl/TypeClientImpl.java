package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.entity.TypeClient;
import mg.cnaps.gestion.ccl.project.repository.TypeClientRepo;
import mg.cnaps.gestion.ccl.project.service.TypeClientService;
import org.springframework.stereotype.Service;

@Service
public class TypeClientImpl extends GenericServiceImpl<TypeClient, String , TypeClientRepo> implements TypeClientService {
    public TypeClientImpl(TypeClientRepo repo) {
        super(repo);
    }
}