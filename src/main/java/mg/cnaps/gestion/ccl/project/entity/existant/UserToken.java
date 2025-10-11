package mg.cnaps.gestion.ccl.project.entity.existant;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserToken {
    private String token;
    private String matricule;
    public UserToken() {
    }

    public UserToken(String token) {
        this.token = token;
    }
}

