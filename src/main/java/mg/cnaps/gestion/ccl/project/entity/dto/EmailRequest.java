package mg.cnaps.gestion.ccl.project.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequest {
    private String id;
    private String[] destinataires;
}
