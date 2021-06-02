package database;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Getter {
    String alias();
    Class<? extends Row> target() default Row.class;
}
