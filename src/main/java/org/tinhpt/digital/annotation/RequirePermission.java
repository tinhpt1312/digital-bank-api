package org.tinhpt.digital.annotation;


import org.tinhpt.digital.type.PermissionsAction;
import org.tinhpt.digital.type.SubjectName;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {
    SubjectName subject();
    PermissionsAction action();
}
