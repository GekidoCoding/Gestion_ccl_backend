package mg.cnaps.gestion.ccl.project.service;

import mg.cnaps.gestion.ccl.framework.core.service.GenericService;
import mg.cnaps.gestion.ccl.project.entity.Client;
import mg.cnaps.gestion.ccl.project.entity.Infrastructure;
import org.springframework.data.domain.Page;

import java.util.List;


public interface ClientService extends GenericService<Client, String> {
    Page<Client> findByCriteriaPaginated(int page, int pageSize, Client criteria, List<Infrastructure> infrastructures);
//    List<Client> findByInfrastructures(List<Infrastructure> infrastructures)
    Integer getTotalPersonnes(String clientId);
    Integer getTotalMouvements(String clientId);
}