package mg.cnaps.gestion.ccl.project.controller;

import mg.cnaps.gestion.ccl.framework.general.error.ErrorResponse;
import mg.cnaps.gestion.ccl.framework.jpa.core.controller.GenericController;
import mg.cnaps.gestion.ccl.project.entity.Client;
import mg.cnaps.gestion.ccl.project.entity.Infrastructure;
import mg.cnaps.gestion.ccl.project.entity.TypeClient;
import mg.cnaps.gestion.ccl.project.service.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/client")
public class ClientController extends GenericController<Client , String ,ClientService > {
    public ClientController(ClientService service ) {
        super(service);
    }


    @PostMapping("/check/similarity")
    public ResponseEntity<?> checkSimilarity(@RequestBody Client client) {
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(getService().checkSimilarity(client));
        } catch (Exception e) {
            e.printStackTrace();
            ErrorResponse error = new ErrorResponse(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @GetMapping("/search/criteria")
    public Page<Client> findByCriteria(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "pageSize") int pageSize,
            @RequestParam(value = "designationClient", required = false) String designationClient,
            @RequestParam(value = "email",required = false) String email,
            @RequestParam(value = "cin",required = false) String cin,
            @RequestParam(value = "contacts",required = false) String contacts,
            @RequestParam(value = "fonction",required = false) String fonction,
            @RequestParam(value = "typeClientId",required = false) String typeClientId,
            @RequestParam(value = "infraIds",required = false) List<String> infraIds
    ) {


        Client criteria = new Client();
        criteria.setDesignationClient(designationClient);
        criteria.setEmail(email);
        criteria.setCin(cin);
        criteria.setContacts(contacts);
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

    @GetMapping("/totalMouvement/{clientId}")
    public ResponseEntity<Integer> getTotalMouvements(@PathVariable("clientId") String clientId) {
        return ResponseEntity.ok( service.getTotalMouvements(clientId));
    }


}