package mg.cnaps.gestion.ccl.project.service.impl;

import mg.cnaps.gestion.ccl.framework.core.service.implementation.GenericServiceImpl;
import mg.cnaps.gestion.ccl.project.entity.existant.Direction;
import mg.cnaps.gestion.ccl.project.repository.DirectionRepo;
import mg.cnaps.gestion.ccl.project.service.DirectionService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectionImpl extends GenericServiceImpl<Direction, String , DirectionRepo> implements DirectionService {
    public DirectionImpl(DirectionRepo repo) {
        super(repo);
    }

    @Override
    public List<Direction> findAll(){
        return repository.findAll(Sort.by(Sort.Direction.ASC, "libelleDirection"));
    }
}

