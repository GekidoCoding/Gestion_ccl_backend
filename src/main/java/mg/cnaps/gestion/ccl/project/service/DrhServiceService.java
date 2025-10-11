package mg.cnaps.gestion.ccl.project.service;

import mg.cnaps.gestion.ccl.framework.jpa.core.service.GenericService;
import mg.cnaps.gestion.ccl.project.entity.existant.DrhService;
import org.springframework.data.domain.Page;

public interface DrhServiceService extends GenericService<DrhService, String> {
    Page<DrhService> getPaginated(int page , int size );
}