package mg.cnaps.gestion.ccl.framework.jpa.core.generator;

import mg.cnaps.gestion.ccl.framework.jpa.core.annotation.GeneratedPrefixId;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import javax.persistence.Query;
import java.io.Serializable;

public class CustomIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object entity) throws HibernateException {
        Class<?> clazz = entity.getClass();

        GeneratedPrefixId annotation = clazz.getAnnotation(GeneratedPrefixId.class);
        if (annotation == null) {
            throw new HibernateException("@GeneratedPrefixId annotation is missing on entity " + clazz.getName());
        }

        String sequence = annotation.sequence();
        String prefix = annotation.prefix();
        int length = annotation.length();
        String db = annotation.db().toLowerCase();

        String sql;
        switch (db) {
            case "oracle":
                sql = "SELECT " + sequence + ".NEXTVAL FROM dual";
                break;
            case "postgresql":
                sql = "SELECT nextval('" + sequence + "')";
                break;
            default:
                throw new HibernateException("Unsupported DB type: " + db);
        }

        Query query = session.createNativeQuery(sql);
        Long nextVal = ((Number) query.getSingleResult()).longValue();

        String numberPart = String.format("%0" + length + "d", nextVal);
        return prefix + numberPart;
    }
}
