package mg.cnaps.gestion.ccl.project.repository;

import mg.cnaps.gestion.ccl.framework.core.repository.GenericRepository;
import mg.cnaps.gestion.ccl.project.entity.Client;
import org.springframework.stereotype.Repository;


@Repository
public interface ClientRepo extends GenericRepository<Client, String> { }