package mg.cnaps.gestion.ccl.project.util;

import mg.cnaps.gestion.ccl.project.entity.Gestionnaire;
import mg.cnaps.gestion.ccl.project.exception.UnauthorizedUpdateException;
import mg.cnaps.gestion.ccl.project.repository.GestionnaireRepo;
import org.springframework.stereotype.Component;

@Component
public class GestionnaireUtil {
    private final GestionnaireRepo gestionnaireRepo;

    public GestionnaireUtil(GestionnaireRepo gestionnaireRepo) {
        this.gestionnaireRepo = gestionnaireRepo;
    }

    public Gestionnaire getGestionnaireHisto(String matricule) {
        Gestionnaire gestionnaire = new Gestionnaire();
        if(matricule!=null && !matricule.isEmpty()){
            gestionnaire = gestionnaireRepo.findByAgent_Matricule(matricule);
//            System.out.println("gestionnaire: "+gestionnaire.getAgent().getMatricule());
            if(gestionnaire==null){
                throw new UnauthorizedUpdateException("Aucun gestionnaire avec le matricule "+matricule+" , alors non authorisee");
            }
        }

        return gestionnaire;
    }

}
