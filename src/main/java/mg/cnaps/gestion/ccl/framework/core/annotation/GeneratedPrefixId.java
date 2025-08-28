package mg.cnaps.gestion.ccl.framework.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(TYPE)
public @interface GeneratedPrefixId {
    String sequence();       // Nom de la séquence
    String prefix();         // Ex: USR
    int length();            // Longueur des chiffres, ex: 8 pour 00000001
    String db() default "oracle"; // Type de base de données (postgresql ou oracle)
}
