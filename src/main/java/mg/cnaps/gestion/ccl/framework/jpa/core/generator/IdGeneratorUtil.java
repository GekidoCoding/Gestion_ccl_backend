package mg.cnaps.gestion.ccl.framework.jpa.core.generator;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class IdGeneratorUtil {

    private static EntityManager staticEm;

    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    public void init() {
        staticEm = this.em;
    }

    public static String generate(String sequence, String prefix, int length, String dbType) {
        String sql;
        switch (dbType.toLowerCase()) {
            case "oracle":
                sql = "SELECT " + sequence + ".NEXTVAL FROM dual";
                break;
            case "postgresql":
                sql = "SELECT nextval('" + sequence + "')";
                break;
            default:
                throw new IllegalArgumentException("Unsupported DB type: " + dbType);
        }

        Long nextVal = ((Number) staticEm.createNativeQuery(sql).getSingleResult()).longValue();
        String numberPart = String.format("%0" + length + "d", nextVal);
        return prefix + numberPart;
    }
}
