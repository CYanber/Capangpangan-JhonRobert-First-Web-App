package com.userhub.security;

import com.userhub.entity.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a controller (or method) as protected. A valid Bearer token is required.
 * If {@link #role()} is set, the authenticated user must also have that role.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresAuth {
    Role[] role() default {};
}
