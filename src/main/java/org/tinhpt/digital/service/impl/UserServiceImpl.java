package org.tinhpt.digital.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.tinhpt.digital.dto.request.ChangePassword;
import org.tinhpt.digital.dto.request.UpdateUserRequest;
import org.tinhpt.digital.dto.response.BankResponse;
import org.tinhpt.digital.dto.response.RoleResponse;
import org.tinhpt.digital.dto.response.UserResponse;
import org.tinhpt.digital.entity.Role;
import org.tinhpt.digital.entity.User;
import org.tinhpt.digital.entity.common.Audit;
import org.tinhpt.digital.repository.RoleRepository;
import org.tinhpt.digital.repository.UserRepository;
import org.tinhpt.digital.service.AwsS3Service;
import org.tinhpt.digital.service.UserService;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse getMe(Long userId) {
        Optional<User> optionalUser = userRepository.findUserWithRoleById(userId);
        User user = optionalUser.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .image(user.getImage())
                .provider(user.getProvider())
                .role(RoleResponse.builder()
                        .id(user.getRole().getId())
                        .name(user.getRole().getName())
                        .build())
                .isPassword(user.getPassword() != null)
                .build();
    }

    @Override
    public BankResponse uploadAvatar(Long userId, String fileName) {
        String uniqueFileName = UUID.randomUUID() + "-" + fileName;

        try{
            User user = getUser(userId);

            String presignedUrl = getAvatarPresignedUrl(uniqueFileName);
            user.setImage(uniqueFileName);

            Audit audit = user.getAudit();
            audit.setUpdatedBy(user);
            audit.setUpdatedAt(new Date());

            userRepository.save(user);
            if(!user.getImage().isEmpty()){
                deletedAvatarInS3(user.getImage());
            }

            return BankResponse.builder()
                    .responseCode("202")
                    .responseMessage(presignedUrl)
                    .build();

        }catch (Exception e){
            return BankResponse.builder()
                    .responseCode("404")
                    .responseMessage(e.getMessage())
                    .build();
        }
    }

    public String getAvatarPresignedUrl(String fileName){
        BankResponse presignedUrl = awsS3Service.generatePresignedUrl(fileName);

        return presignedUrl.getResponseMessage();
    }

    public void deletedAvatarInS3(String fileName){
        awsS3Service.deletedFile(fileName);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = getUser(id);

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .image(user.getImage())
                .provider(user.getProvider())
                .role(RoleResponse.builder()
                        .id(user.getRole().getId())
                        .name(user.getRole().getName())
                        .build())
                .isPassword(user.getPassword() != null)
                .build();
    }

    @Override
    public BankResponse updateUser(Long userId, Long id, UpdateUserRequest request) {
        User user = getUser(id);
        User userUpdated = getUser(userId);


        Role role = roleRepository.findById(request.getRoleId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));

        user.setRole(role);
        Audit audit = user.getAudit();
        audit.setUpdatedAt(new Date());
        audit.setUpdatedBy(userUpdated);

        userRepository.save(user);

        return BankResponse.builder()
                .responseCode("200")
                .responseMessage("Update user is successfully")
                .build();
    }

    private User getUser(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Override
    public BankResponse changePassword(Long userId, ChangePassword request) {
        User user = getUser(userId);
        String hashPassword = passwordEncoder.encode(request.getNewPassword());

        if(user.getPassword().isEmpty()){
            user.setPassword(hashPassword);

            Audit audit = user.getAudit();
            audit.setUpdatedBy(user);
            audit.setUpdatedAt(new Date());

            userRepository.save(user);

            return BankResponse.builder()
                    .responseCode("200")
                    .responseMessage("Change Password is successfully")
                    .build();
        }else{
            if(validatePassword(request, userId)){
                user.setPassword(hashPassword);

                Audit audit = user.getAudit();
                audit.setUpdatedBy(user);
                audit.setUpdatedAt(new Date());

                userRepository.save(user);

                return BankResponse.builder()
                        .responseCode("200")
                        .responseMessage("Change Password is successfully")
                        .build();
            }else {
                return BankResponse.builder()
                        .responseCode("404")
                        .responseMessage("Old password is not correct")
                        .build();
            }
        }

    }

    private Boolean validatePassword(ChangePassword request, Long userId){
        User user = getUser(userId);

        if(request.getOldPassword().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Old password is required");
        }

        if(request.getOldPassword().equals(request.getNewPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New password must be different old password");
        }

        return passwordEncoder.matches(request.getOldPassword(), user.getPassword());
    }
}
