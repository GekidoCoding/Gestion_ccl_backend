package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.entity.existant.DrhService;
import mg.cnaps.gestion.ccl.project.service.DrhServiceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cnaps/gestion/ccl/drh_service")
public class DrhServiceController extends GenericController<DrhService , String ,DrhServiceService > {
    public DrhServiceController(DrhServiceService service ) {
        super(service);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<DrhService>> getPaginated2(@RequestParam("page") int page,@RequestParam("size") int size) {
        return ResponseEntity.ok( this.service.getPaginated(page, size));
    }
}