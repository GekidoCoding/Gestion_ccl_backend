package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.jpa.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.entity.ModeleInfra;
import mg.cnaps.gestion.ccl.project.repository.ModeleInfraRepo;
import mg.cnaps.gestion.ccl.project.service.ModeleInfraService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModeleInfraImpl extends GenericServiceImpl<ModeleInfra, String , ModeleInfraRepo> implements ModeleInfraService {
    public ModeleInfraImpl(ModeleInfraRepo repo) {
        super(repo);
    }

    @Override
    public String[] getColorExist(){
        List<ModeleInfra> modeleInfraList = this.findAll();
        List<String> colorExist = new ArrayList<String>();
        for (ModeleInfra m : modeleInfraList) {
            colorExist.add(m.getCouleur());
        }
        return colorExist.toArray(new String[0]);
    }

}