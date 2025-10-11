package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.jpa.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.entity.HistoriqueInfra;
import mg.cnaps.gestion.ccl.project.repository.HistoriqueInfraRepo;
import mg.cnaps.gestion.ccl.project.service.HistoriqueInfraService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoriqueInfraImpl extends GenericServiceImpl<HistoriqueInfra, String , HistoriqueInfraRepo> implements HistoriqueInfraService {
    public HistoriqueInfraImpl(HistoriqueInfraRepo repo) {
        super(repo);
    }

    @Override
    public List<HistoriqueInfra> findHistoriqueInfraByInfrastructure_Id(String mouvementId) {
        return repository.findHistoriqueInfraByInfrastructure_Id(mouvementId);
    }
}