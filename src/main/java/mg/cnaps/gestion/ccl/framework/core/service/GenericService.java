package mg.cnaps.gestion.ccl.framework.core.service;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

public interface GenericService <T , ID extends Serializable>{
    List<T> findAll();
    T findById(ID id);
    T save(T dto);

    List<T> saveAll (List<T> entities);

    T update(T dto , ID id);
    void delete(ID id) throws Exception;
    List<T> findByField(String fieldname , Object fieldvalue);
    Page<T> findPaginated(int page , int pageSize);
}