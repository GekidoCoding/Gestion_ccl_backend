package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.entity.existant.Direction;
import mg.cnaps.gestion.ccl.project.service.DirectionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cnaps/gestion/ccl/direction")
public class DirectionController extends GenericController<Direction , String ,DirectionService > {
    public DirectionController(DirectionService service ) {
        super(service);
    }
}