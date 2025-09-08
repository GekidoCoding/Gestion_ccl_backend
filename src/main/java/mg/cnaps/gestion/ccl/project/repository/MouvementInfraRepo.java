package mg.cnaps.gestion.ccl.project.repository;

import mg.cnaps.gestion.ccl.framework.core.repository.GenericRepository;
import mg.cnaps.gestion.ccl.project.entity.MouvementInfra;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface MouvementInfraRepo extends GenericRepository<MouvementInfra, String> {
    @Transactional
    void deleteByMouvement_Id(String mouvementId);
}