package org.tinhpt.digital.dto.request;

import java.util.List;

import org.tinhpt.digital.type.PermissionsAction;
import org.tinhpt.digital.type.SubjectName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePermissionRequest {
    private SubjectName subject;
    private List<PermissionsAction> action;
}
