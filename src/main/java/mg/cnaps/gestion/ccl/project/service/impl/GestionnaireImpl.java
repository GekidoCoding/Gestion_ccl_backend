package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.jpa.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.entity.Etat;
import mg.cnaps.gestion.ccl.project.entity.Gestionnaire;
import mg.cnaps.gestion.ccl.project.entity.existant.Agent;
import mg.cnaps.gestion.ccl.project.exception.EtatNotFoundException;
import mg.cnaps.gestion.ccl.project.repository.AgentRepo;
import mg.cnaps.gestion.ccl.project.repository.EtatRepo;
import mg.cnaps.gestion.ccl.project.repository.GestionnaireRepo;
import mg.cnaps.gestion.ccl.project.service.GestionnaireService;
import mg.cnaps.gestion.ccl.project.util.CclProperty;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GestionnaireImpl extends GenericServiceImpl<Gestionnaire, String ,GestionnaireRepo> implements GestionnaireService {
    private final EtatRepo etatRepo;
    private final AgentRepo agentRepo;
    public GestionnaireImpl(GestionnaireRepo repo, EtatRepo etatRepo, AgentRepo agentRepo) {
        super(repo);
        this.etatRepo = etatRepo;
        this.agentRepo = agentRepo;
    }

    @Override
    public void delete(String id)throws Exception {
        Gestionnaire infra = repository.getReferenceById(id);
        Etat etat= etatRepo.getEtatByCode(new CclProperty().getInactifCode());
        if(etat!=null){
            infra.setEtat(etat);
        }else{
            throw new EtatNotFoundException("Etat Inactif Non Trouvee dans la table Etat , veuillez l' inserez ! ");
        }
        this.repository.save(infra);
    }

    @Override
    public String [] getEmailGestionnaire(){
        List<String> emails = new ArrayList<String>();
        List<Gestionnaire> gestionnaires = repository.findAll();
        Agent agent=new Agent();
        for(Gestionnaire g : gestionnaires){
             agent= agentRepo.findByMatricule(g.getMatriculeGestionnaire());
             if(agent!=null){
                 emails.add(agent.getMail());
             }
        }
       return emails.toArray(new String[0]);
    }
}