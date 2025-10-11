package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.jpa.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.entity.TypeNotification;
import mg.cnaps.gestion.ccl.project.service.TypeNotificationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/type_notification")
public class TypeNotificationController extends GenericController<TypeNotification , String ,TypeNotificationService > {
    public TypeNotificationController(TypeNotificationService service ) {
        super(service);
    }
}