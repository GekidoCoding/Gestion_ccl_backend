package mg.cnaps.gestion.ccl.framework.jpa.core.service.implementation;

import java.io.Serializable;
import java.util.List;

import mg.cnaps.gestion.ccl.framework.jpa.core.repository.GenericRepository;
import mg.cnaps.gestion.ccl.framework.jpa.core.service.GenericService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;


public abstract class GenericServiceImpl<T, ID extends Serializable , R extends GenericRepository<T , ID> > implements GenericService<T , ID> {
    protected final R   repository;

    public GenericServiceImpl(R repo ){
        this.repository=repo;
    }

    @Override
    public List<T> findAll(){
        return repository.findAll();
    }

    @Override
    public T findById(ID id){
        return repository.findById(id).orElseThrow(()-> new RuntimeException("Not Found ID ") );
    }

    @Override
    public T save (T entity ){
        try{
            return repository.save(entity);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> saveAll(List<T> entities){
        return repository.saveAll(entities);
    }


    @Override
    public T update (T entity , ID id ){
        if(!repository.existsById(id)){
            throw new RuntimeException("Not Found ID ");
        }
        return repository.save(entity);
    }
    @Override
    public void delete(ID id ) throws Exception{
        repository.deleteById(id);    
    }

    @Override 
    public List<T> findByField(String fieldname  , Object value){
        Specification<T> spec = (root , query , cb)->cb.equal(root.get(fieldname) , value);
        return repository.findAll(spec);
    }
    @Override
    public Page<T> findPaginated(int page, int pageSize){
        Pageable pageable = PageRequest.of(page, pageSize);
        return repository.findAll(pageable);
    }

}

