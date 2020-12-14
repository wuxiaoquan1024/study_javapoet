package com.study.poet.build.typespec;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({
    ElementType.METHOD,
    ElementType.FIELD
})
public @interface TypeA {
  int id() default 0;
}
