package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.entity.Client;
import mg.cnaps.gestion.ccl.project.entity.Infrastructure;
import mg.cnaps.gestion.ccl.project.entity.TypeClient;
import mg.cnaps.gestion.ccl.project.service.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cnaps/gestion/ccl/client")
public class ClientController extends GenericController<Client , String ,ClientService > {
    public ClientController(ClientService service ) {
        super(service);
    }

    @GetMapping("/search/criteria")
    public Page<Client> findByCriteria(
            @RequestParam int page,
            @RequestParam int pageSize,
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String cin,
            @RequestParam(required = false) String fonction,
            @RequestParam(required = false) String typeClientId,
            @RequestParam(required = false) List<String> infraIds // <--- Liste des ID infrastructure
    ) {
        Client criteria = new Client();
        criteria.setNom(nom);
        criteria.setEmail(email);
        criteria.setCin(cin);
        criteria.setFonction(fonction);

        if (typeClientId != null && !typeClientId.isEmpty()) {
            TypeClient tc = new TypeClient();
            tc.setId(typeClientId);
            criteria.setTypeClient(tc);
        }

        List<Infrastructure> infrastructures = new ArrayList<>();
        if (infraIds != null && !infraIds.isEmpty()) {
            infrastructures = infraIds.stream()
                    .map(id -> {
                        Infrastructure infra = new Infrastructure();
                        infra.setId(id);
                        return infra;
                    })
                    .collect(Collectors.toList());
        }

        return service.findByCriteriaPaginated(page, pageSize, criteria, infrastructures);
    }

    @GetMapping("/total/{clientId}")
    public ResponseEntity<Integer> getTotal(@PathVariable("clientId") String clientId) {
        return ResponseEntity.ok( service.getTotalPersonnes(clientId));
    }


}