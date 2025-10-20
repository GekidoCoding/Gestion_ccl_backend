package mg.cnaps.gestion.ccl.framework.excel.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
    String value() default "";               // Nom de la colonne
    String[] colors() default {};            // Format: {"clé:couleur", "clé:couleur"}
}
