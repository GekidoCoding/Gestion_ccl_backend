package mg.cnaps.gestion.ccl.project.service;

import mg.cnaps.gestion.ccl.framework.core.service.GenericService;
import mg.cnaps.gestion.ccl.project.entity.HistoriqueInfra;

import java.util.List;


public interface HistoriqueInfraService extends GenericService<HistoriqueInfra, String> {
    public List<HistoriqueInfra> findHistoriqueInfraByInfrastructure_Id(String mouvementId);
}