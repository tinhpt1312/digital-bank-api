package org.tinhpt.digital.helper;


import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.tinhpt.digital.annotation.RequirePermission;
import org.tinhpt.digital.type.PermissionsAction;
import org.tinhpt.digital.type.SubjectName;

@Aspect
@Component
@RequiredArgsConstructor
public class PermissionAspect {

    @Before("@annotation(requirePermission)")
    public void checkPermission(JoinPoint joinPoint, RequirePermission requirePermission) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean hasPermission = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> {
                    String[] parts = authority.split("_");
                    SubjectName subject = SubjectName.valueOf(parts[0]);
                    PermissionsAction action = PermissionsAction.valueOf(parts[1]);
//
//                    if (action == PermissionsAction.MANAGE) {
//                        return true;
//                    }

                    return subject.canAccess(requirePermission.subject()) &&
                            action.canDo(requirePermission.action());
                });

        if (!hasPermission) {
            throw new AccessDeniedException("Access denied");
        }
    }
}
