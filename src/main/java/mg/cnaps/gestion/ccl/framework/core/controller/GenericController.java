package mg.cnaps.gestion.ccl.framework.core.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import mg.cnaps.gestion.ccl.framework.core.service.GenericService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

@RequiredArgsConstructor
public abstract class GenericController<T, ID extends Serializable, S extends GenericService<T, ID>> implements Serializable {

    protected final S service;

    @GetMapping
    public ResponseEntity<List<T>> getAll() {
        return ResponseEntity.ok(getService().findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<T> getById(@PathVariable ID id) {
        return ResponseEntity.ok(getService().findById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<T> create(@RequestBody T dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(getService().save(dto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<T> update(@PathVariable ID id, @RequestBody @Valid T dto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(dto);
        System.out.println("update generic miditra : " + jsonString);
        return ResponseEntity.ok(getService().update(dto, id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) throws Exception {
        getService().delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<T>> search(@RequestParam String field, @RequestParam Object value) {
        return ResponseEntity.ok(getService().findByField(field, value));
    }
    @GetMapping("/pagination")
    public ResponseEntity<Page<T>> pagination(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer pageSize) {
        return ResponseEntity.ok(service.findPaginated(page , pageSize) );
    }
    protected S getService() {
        return service;
    }
}