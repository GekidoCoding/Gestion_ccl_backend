package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.jpa.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.entity.TypeNotification;
import mg.cnaps.gestion.ccl.project.repository.TypeNotificationRepo;
import mg.cnaps.gestion.ccl.project.service.TypeNotificationService;
import org.springframework.stereotype.Service;

@Service
public class TypeNotificationImpl extends GenericServiceImpl<TypeNotification, String , TypeNotificationRepo> implements TypeNotificationService {
    public TypeNotificationImpl(TypeNotificationRepo repo) {
        super(repo);
    }


}