package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.entity.HistoFacture;
import mg.cnaps.gestion.ccl.project.repository.HistoFactureRepo;
import mg.cnaps.gestion.ccl.project.service.HistoFactureService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoFactureImpl extends GenericServiceImpl<HistoFacture, String ,HistoFactureRepo> implements HistoFactureService {
    public HistoFactureImpl(HistoFactureRepo repo) {
        super(repo);
    }

    @Override
    public List<HistoFacture> getHistoByFactureId(String factureId) {
        return repository.getHistoFactureByFacture_Id(factureId);
    }
}