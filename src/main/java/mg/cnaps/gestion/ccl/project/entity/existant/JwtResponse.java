package mg.cnaps.gestion.ccl.project.entity.existant;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class JwtResponse {
    public UserToken user;

    public JwtResponse() {
    }

}