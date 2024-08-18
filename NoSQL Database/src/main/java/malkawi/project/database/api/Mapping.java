package malkawi.project.database.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) //to use at runtime
@Target(ElementType.METHOD)
public @interface Mapping {

    String value();

}
