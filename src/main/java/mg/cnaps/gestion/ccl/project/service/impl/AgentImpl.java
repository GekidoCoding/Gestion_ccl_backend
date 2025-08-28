package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.entity.existant.Agent;
import mg.cnaps.gestion.ccl.project.repository.AgentRepo;
import mg.cnaps.gestion.ccl.project.service.AgentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentImpl extends GenericServiceImpl<Agent, String , AgentRepo> implements AgentService {


    public AgentImpl(AgentRepo agentRepo) {
        super(agentRepo);
    }

    @Override
    public List<Agent> searchByCriteria(Agent criteria){
        return repository.searchByCriteria(
                criteria.getMatricule() != null && !criteria.getMatricule().isEmpty() ? criteria.getMatricule() : null,
                criteria.getNom() != null && !criteria.getNom().isEmpty() ? criteria.getNom() : null,
                criteria.getPrenoms() != null && !criteria.getPrenoms().isEmpty() ? criteria.getPrenoms() : null,
                criteria.getMail() != null && !criteria.getMail().isEmpty() ? criteria.getMail() : null,
                criteria.getCodeService() != null && !criteria.getCodeService().isEmpty() ? criteria.getCodeService() : null,
                criteria.getCodeDirection() != null && !criteria.getCodeDirection().isEmpty() ? criteria.getCodeDirection() : null
        );

    }

    @Override
    public List<Agent> findAllNotGestionnaire() {
        return repository.findWithoutGestionnaire();
    }

    @Override
    public Page<Agent> getPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateEntree"));
        return this.repository.findAll(pageable);
    }



}