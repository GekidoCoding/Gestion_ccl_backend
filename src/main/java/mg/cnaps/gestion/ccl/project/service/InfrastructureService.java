package mg.cnaps.gestion.ccl.project.service;

import mg.cnaps.gestion.ccl.framework.core.service.GenericService;
import mg.cnaps.gestion.ccl.project.entity.CategorieInfra;
import mg.cnaps.gestion.ccl.project.entity.Infrastructure;
import mg.cnaps.gestion.ccl.project.entity.Localisation;
import mg.cnaps.gestion.ccl.project.entity.ModeleInfra;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;


public interface InfrastructureService extends GenericService<Infrastructure, String> {
    void delete(String id , String observation) throws  Exception;
    Page<Infrastructure> findByCriteriaAll(int page, int pageSize,
                                           ModeleInfra[] modeles,
                                           Localisation[] localisations,
                                           Infrastructure criteria,
                                           CategorieInfra catInfra,
                                           Timestamp debut,
                                           Timestamp fin);
}