package org.bitbucket.risu8.nuije.hibernate.validator.constraints;

import org.bitbucket.risu8.nuije.hibernate.validator.constraints.constraintvalidator.UniqueColumnsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy={UniqueColumnsValidator.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueColumns {

    String tableName();

    String[] columnNames();

    String[] propertyNames();

    String message() default "{org.bitbucket.risu8.hibernate.validator.constraints.UniqueColumns.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        UniqueColumns[] value();
    }
}
