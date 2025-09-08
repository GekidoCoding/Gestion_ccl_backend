package mg.cnaps.gestion.ccl.project.service;

import mg.cnaps.gestion.ccl.framework.core.service.GenericService;
import mg.cnaps.gestion.ccl.project.entity.TypeMouvement;

import java.util.List;


public interface TypeMouvementService extends GenericService<TypeMouvement, String> {
    List<TypeMouvement> getTypeMouvementAddingMouvement();
}