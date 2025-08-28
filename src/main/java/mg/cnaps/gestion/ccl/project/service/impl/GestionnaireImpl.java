package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.entity.Etat;
import mg.cnaps.gestion.ccl.project.entity.Gestionnaire;
import mg.cnaps.gestion.ccl.project.exception.EtatNotFoundException;
import mg.cnaps.gestion.ccl.project.repository.EtatRepo;
import mg.cnaps.gestion.ccl.project.repository.GestionnaireRepo;
import mg.cnaps.gestion.ccl.project.service.GestionnaireService;
import mg.cnaps.gestion.ccl.project.util.CclProperty;
import org.springframework.stereotype.Service;

@Service
public class GestionnaireImpl extends GenericServiceImpl<Gestionnaire, String ,GestionnaireRepo> implements GestionnaireService {
    private final EtatRepo etatRepo;
    public GestionnaireImpl(GestionnaireRepo repo, EtatRepo etatRepo) {
        super(repo);
        this.etatRepo = etatRepo;
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
}