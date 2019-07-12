package ai.turbochain.ipex.annotation;


import java.lang.annotation.*;

import ai.turbochain.ipex.constant.AdminModule;

@Target({ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLog {
    String operation();
    AdminModule module();
}

