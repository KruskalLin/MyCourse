package mycourse.web;


import mycourse.dao.RoleDao;
import mycourse.dao.UserDao;
import mycourse.entity.*;
import mycourse.exception.InternalException;
import mycourse.exception.ResourceNotFoundException;
import mycourse.security.CurrentUser;
import mycourse.security.UserPrincipal;
import mycourse.utils.SendMailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import mycourse.payloads.*;
import mycourse.security.JwtTokenProvider;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2018/7/9
 * @Todo: a test controller for my auth app
 */

@RestController
@RequestMapping("/")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDao userRepository;

    @Autowired
    RoleDao roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    private SendMailUtils sendMailUtils;

    @PostMapping(value = "/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if(!user.getUserStatus().equals(UserStatus.CREATE)) {
            throw new ResourceNotFoundException("User not found");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }


    @PostMapping(value = "/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        User user = userRepository.findByUsername(signUpRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if(!user.getActivateCode().equals(signUpRequest.getCode())){
            return new ResponseEntity(new ApiResponse(false, "激活失败"), HttpStatus.UNAUTHORIZED);
        }
        if(user.getUserStatus().equals(UserStatus.CREATE)){
            return new ResponseEntity(new ApiResponse(false, "失败"), HttpStatus.UNAUTHORIZED);
        }
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        if (signUpRequest.getUsername().substring(signUpRequest.getUsername().indexOf('@') + 1).equals("smail.nju.edu.cn")){
            Set<Role> set = new HashSet<>();
            Role role = roleRepository.findByName(RoleName.ROLE_STUDENT).orElseThrow(() -> new InternalException("No roles found"));
            set.add(role);
            user.setRoles(set);
            String number = signUpRequest.getUsername().substring(0, signUpRequest.getUsername().indexOf('@'));
            user.setNumber(number);
            if(number.substring(0, 2).equals("DG")){
                user.setDegree(Degree.DOCTOR);
                user.setGrade(number.substring(2, 4));
            } else if (number.substring(0, 2).equals("MG")){
                user.setDegree(Degree.MASTER);
                user.setGrade(number.substring(2, 4));
            } else {
                user.setDegree(Degree.BACHELOR);
                user.setGrade(number.substring(0, 2));
            }
        } else if (signUpRequest.getUsername().substring(signUpRequest.getUsername().indexOf('@') + 1).equals("nju.edu.cn")){
            Set<Role> set = new HashSet<>();
            Role role = roleRepository.findByName(RoleName.ROLE_TEACHER).orElseThrow(() -> new InternalException("No roles found"));
            set.add(role);
            user.setRoles(set);
        }
        user.setUserStatus(UserStatus.CREATE);
        User result = userRepository.save(user);
        return new ResponseEntity(new ApiResponse(true, "用户注册成功"), HttpStatus.ACCEPTED);
    }

    @GetMapping("/user/sendEmail")
    public ResponseEntity<?> sendActivatedCode(@RequestParam("username") String username, @RequestParam("password") String password) {
        User user = userRepository.findByUsername(username)
                .orElse(new User(username, password));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setUserStatus(UserStatus.INACTIVATE);
        userRepository.save(user);
        sendMailUtils.SEND_ACTIVATE_EMAIL(user);
        return new ResponseEntity(new ApiResponse(true, "发送成功"), HttpStatus.ACCEPTED);
    }

    @GetMapping("/user/cancel")
    public ResponseEntity<?> cancel(@CurrentUser UserPrincipal userPrincipal) {
        User user = userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setUserStatus(UserStatus.CANCEL);
        userRepository.save(user);
        return new ResponseEntity(new ApiResponse(true, "注销成功"), HttpStatus.ACCEPTED);
    }

    @GetMapping("/user/info")
    public UserProfile getUserProfile(@CurrentUser UserPrincipal userPrincipal) {
        User user = userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", userPrincipal.getUsername()));
        UserProfile userProfile = null;
        if (user.getDegree() != null) {
            userProfile = new UserProfile(user.getId(), user.getUsername(), user.getRoles(), user.getNumber(), user.getName(), user.getDegree().name(), user.getGrade());
        } else {
            userProfile = new UserProfile(user.getId(), user.getUsername(), user.getRoles(), user.getNumber(), user.getName(), null, user.getGrade());
        }
        return userProfile;
    }

    @GetMapping("/user/update")
    public ResponseEntity<?> updateUserProfile(@RequestParam(name = "name") String name, @CurrentUser UserPrincipal userPrincipal) {
        User user = userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", userPrincipal.getUsername()));
        user.setName(name);
        userRepository.save(user);
        return new ResponseEntity(new ApiResponse(true, "更新成功"), HttpStatus.ACCEPTED);
    }





}
