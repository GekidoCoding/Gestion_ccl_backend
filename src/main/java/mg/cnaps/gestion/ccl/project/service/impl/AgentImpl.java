package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.jpa.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.entity.existant.Agent;
import mg.cnaps.gestion.ccl.project.repository.AgentRepo;
import mg.cnaps.gestion.ccl.project.service.AgentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgentImpl extends GenericServiceImpl<Agent, String , AgentRepo> implements AgentService {


    public AgentImpl(AgentRepo agentRepo) {
        super(agentRepo);
    }

    @Override
    public List<Agent> searchByCriteria(Agent criteria) {
        List<Agent> allAgents = repository.findAll(); // Récupère tous les agents

        return allAgents.stream()
                .filter(a -> isEmpty(criteria.getMatricule()) ||
                        containsIgnoreCase(a.getMatricule(), criteria.getMatricule()))
                .filter(a -> isEmpty(criteria.getNom()) ||
                        containsIgnoreCase(a.getNom(), criteria.getNom()))
                .filter(a -> isEmpty(criteria.getPrenoms()) ||
                        containsIgnoreCase(a.getPrenoms(), criteria.getPrenoms()))
                .filter(a -> isEmpty(criteria.getMail()) ||
                        containsIgnoreCase(a.getMail(), criteria.getMail()))
                .filter(a -> isEmpty(criteria.getCodeService()) ||
                        equalsIgnoreCase(a.getCodeService(), criteria.getCodeService()))
                .filter(a -> isEmpty(criteria.getCodeDirection()) ||
                        equalsIgnoreCase(a.getCodeDirection(), criteria.getCodeDirection()))
                .collect(Collectors.toList());
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean containsIgnoreCase(String source, String value) {
        return source != null && source.toLowerCase().contains(value.toLowerCase());
    }

    private boolean equalsIgnoreCase(String source, String value) {
        return source != null && source.equalsIgnoreCase(value);
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