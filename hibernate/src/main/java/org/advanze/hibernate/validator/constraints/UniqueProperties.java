package org.advanze.hibernate.validator.constraints;

import org.bitbucket.risu8.nuije.hibernate.validator.constraints.constraintvalidator.UniquePropertiesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy={UniquePropertiesValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueProperties {

    Class<?> value();

    String message() default "{org.bitbucket.risu8.hibernate.validator.constraints.UniqueProperties.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        UniqueProperties[] value();
    }
}
