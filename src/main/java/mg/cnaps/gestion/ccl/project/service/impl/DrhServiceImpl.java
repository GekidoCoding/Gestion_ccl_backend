package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.jpa.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.entity.existant.DrhService;
import mg.cnaps.gestion.ccl.project.repository.DrhServiceRepo;
import mg.cnaps.gestion.ccl.project.service.DrhServiceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DrhServiceImpl extends GenericServiceImpl<DrhService, String ,DrhServiceRepo > implements DrhServiceService {
    public DrhServiceImpl(DrhServiceRepo repo) {
        super(repo);
    }

    @Override
    public List<DrhService> findAll(){
        return this.repository.findAll(Sort.by(Sort.Direction.ASC, "libelleService"));
    }

    @Override
    public Page<DrhService> getPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "libelleService"));
        return this.repository.findAll(pageable);
    }
}