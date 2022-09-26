package app.warehouse.system.service.implementation;

import app.warehouse.system.dto.PasswordResetDto;
import app.warehouse.system.exception.ExceptionHandler;
import app.warehouse.system.exception.MessageHandler;
import app.warehouse.system.exception.Messages;
import app.warehouse.system.model.Customer;
import app.warehouse.system.model.ResetPassword;
import app.warehouse.system.model.Role;
import app.warehouse.system.model.User;
import app.warehouse.system.repository.CustomerRepository;
import app.warehouse.system.repository.ResetPasswordRepository;
import app.warehouse.system.repository.RoleRepository;
import app.warehouse.system.repository.UserRepository;
import app.warehouse.system.security.jwt.JwtUtils;
import app.warehouse.system.security.request.LoginRequest;
import app.warehouse.system.security.request.RegisterRequest;
import app.warehouse.system.security.response.JwtResponse;
import app.warehouse.system.service.EmailService;
import app.warehouse.system.service.UserService;
import app.warehouse.system.statics.MessageStatus;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final EmailService emailService;
    private final ResetPasswordRepository resetPasswordRepository;

    @Override
    public JwtResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateJwtToken(authentication);

        return new JwtResponse(token);
    }

    @Override
    @Transactional
    public MessageHandler register(RegisterRequest request) {

        if (userRepository.findUserByEmail(request.getEmail()).isPresent()) {
            throw new ExceptionHandler("User with email: " + request.getEmail() + " already exists!");
        }
        if (userRepository.findUserByUsername(request.getUsername()).isPresent()) {
            throw new ExceptionHandler("User with username: " + request.getUsername() + " already exists!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);

        Role role = roleRepository.findRoleByRoleName(request.getAccountType()).orElseThrow(() ->
                new ExceptionHandler("Account type " + request.getAccountType() + " does not exists!"));
        Set<Role> roleSet= new HashSet<>();
        roleSet.add(role);
        user.setRoles(roleSet);
        userRepository.save(user);

        Customer customer = Customer.builder().firstName(request.getFirstName()).lastName(request.getLastName())
                .user(user).address(request.getAddress()).postalCode(request.getPostalCode()).city(request.getCity())
                .state(request.getState()).phone(request.getPhone()).build();
        customerRepository.save(customer);

        MessageHandler.message(MessageStatus.SUCCESS, (Messages.CREATED));
        return new MessageHandler(MessageHandler.hashMap);
    }

    @Override
    @Transactional
    public MessageHandler forgotPassword(HttpServletRequest httpRequest, String email) {

        User user = userRepository.findUserByEmail(email).orElseThrow(() ->
                new ExceptionHandler(String.format(ExceptionHandler.NOT_FOUND, "User")));

        String subject = "Reset Password";
        String token = UUID.randomUUID().toString();
        String url = "http://localhost:8080" + httpRequest.getContextPath() + "/password/reset?token=" + token;

        ResetPassword resetPassword = new ResetPassword(token, user);
        resetPasswordRepository.save(resetPassword);
        emailService.sendEmailToResetPassword(email, url, token, subject);

        MessageHandler.message(MessageStatus.SUCCESS, "We have sent a link to your email account!");
        return new MessageHandler(MessageHandler.hashMap);
    }

    @Override
    public MessageHandler changePassword(String token, PasswordResetDto passwordResetDto) {

        ResetPassword resetPassword = resetPasswordRepository.findByToken(token).orElseThrow(() ->
                new ExceptionHandler("This token is not valid!"));
        if (Instant.now().isAfter(resetPassword.getExpirationDate()))
            throw new ExceptionHandler("This token has expired!");
        if (!passwordResetDto.getPassword().equals(passwordResetDto.getRePassword()))
            throw new ExceptionHandler("Passwords do not match!");

        User user = userRepository.findUserByEmail(resetPassword.getUser().getEmail()).orElseThrow(() ->
                new ExceptionHandler(String.format(ExceptionHandler.NOT_FOUND, "User")));
        user.setPassword(passwordEncoder.encode(passwordResetDto.getPassword()));
        userRepository.save(user);

        MessageHandler.message(MessageStatus.SUCCESS, String.format(Messages.SUCCESS, "Password", "changed"));
        return new MessageHandler(MessageHandler.hashMap);
    }
}
