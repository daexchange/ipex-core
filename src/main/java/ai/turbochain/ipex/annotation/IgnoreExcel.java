package ai.turbochain.ipex.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreExcel {

    boolean value() default true ;
}
