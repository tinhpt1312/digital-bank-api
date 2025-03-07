package org.tinhpt.digital.dto.response;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String image;
    private String provider;
    private boolean isPassword;
    private RoleResponse role;
}
