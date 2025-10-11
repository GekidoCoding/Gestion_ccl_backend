package mg.cnaps.gestion.ccl.project.service;

import mg.cnaps.gestion.ccl.framework.jpa.core.service.GenericService;
import mg.cnaps.gestion.ccl.project.entity.existant.Agent;
import org.springframework.data.domain.Page;
import java.util.List;


public interface AgentService extends GenericService<Agent, String> {
   List<Agent> searchByCriteria(Agent criteria);
   List<Agent> findAllNotGestionnaire();
   Page<Agent> getPaginated(int page, int size);
}