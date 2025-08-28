package mg.cnaps.gestion.ccl.framework.core.service.implementation;

import mg.cnaps.gestion.ccl.framework.core.repository.GenericRepository;
import mg.cnaps.gestion.ccl.framework.core.service.GenericService;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public abstract class GenericServiceImplDto<T, D, ID extends Serializable> implements GenericService<D, ID> {

    protected final GenericRepository<T, ID> repository;
    protected final ModelMapper modelMapper;
    private final Class<T> entityClass;
    private final Class<D> dtoClass;

    public GenericServiceImplDto(GenericRepository<T, ID> repository, ModelMapper modelMapper, Class<T> entityClass, Class<D> dtoClass) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.entityClass = entityClass;
        this.dtoClass = dtoClass;
    }

    @Override
    public List<D> findAll() {
        return repository.findAll()
                .stream()
                .map(entity -> modelMapper.map(entity, dtoClass))
                .collect(Collectors.toList());
    }

    @Override
    public D findById(ID id) {
        T entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        return modelMapper.map(entity, dtoClass);
    }

    @Override
    public D save(D dto) {
        T entity = modelMapper.map(dto, entityClass);
        T saved = repository.save(entity);
        return modelMapper.map(saved, dtoClass);
    }

    @Override
    public D update(D dto, ID id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Not found");
        }
        T entity = modelMapper.map(dto, entityClass);
        T updated = repository.save(entity);
        return modelMapper.map(updated, dtoClass);
    }

    @Override
    public void delete(ID id) {
        repository.deleteById(id);
    }

    @Override
    public List<D> findByField(String fieldName, Object value) {
        Specification<T> spec = (root, query, cb) -> cb.equal(root.get(fieldName), value);
        return repository.findAll(spec)
                .stream()
                .map(entity -> modelMapper.map(entity, dtoClass))
                .collect(Collectors.toList());
    }
}
