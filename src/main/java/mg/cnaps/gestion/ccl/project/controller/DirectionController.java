package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.jpa.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.entity.existant.Direction;
import mg.cnaps.gestion.ccl.project.service.DirectionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/direction")
public class DirectionController extends GenericController<Direction , String ,DirectionService > {
    public DirectionController(DirectionService service ) {
        super(service);
    }
}