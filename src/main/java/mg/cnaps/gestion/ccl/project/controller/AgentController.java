package mg.cnaps.gestion.ccl.project.controller;


import lombok.RequiredArgsConstructor;
import mg.cnaps.gestion.ccl.project.entity.existant.Agent;
import mg.cnaps.gestion.ccl.project.entity.existant.DrhService;
import mg.cnaps.gestion.ccl.project.service.AgentService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cnaps/gestion/ccl/agent")
public class AgentController {
    private final AgentService service;

    @GetMapping
    public ResponseEntity<List<Agent>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/not_gestionnaire")
    public ResponseEntity<List<Agent>> getAllNotGestionnaire() {
        return ResponseEntity.ok(service.findAllNotGestionnaire());
    }

    @GetMapping("/criteria")
    public List<Agent> searchByCriteria(
            @RequestParam(required = false) String matricule,
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String prenoms,
            @RequestParam(required = false) String mail,
            @RequestParam(required = false) String codeService,
            @RequestParam(required = false) String codeDirection
    ) {
        Agent criteria = new Agent();
        criteria.setMatricule(matricule);
        criteria.setNom(nom);
        criteria.setPrenoms(prenoms);
        criteria.setMail(mail);
        criteria.setCodeService(codeService);
        criteria.setCodeDirection(codeDirection);
        return service.searchByCriteria(criteria);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<Agent>> getPaginated(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
        return ResponseEntity.ok( this.service.getPaginated(page, pageSize));
    }


}
