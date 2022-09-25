package app.warehouse.system.controller;

import app.warehouse.system.dto.PasswordResetDto;
import app.warehouse.system.exception.MessageHandler;
import app.warehouse.system.security.request.LoginRequest;
import app.warehouse.system.security.request.RegisterRequest;
import app.warehouse.system.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @PostMapping(value = "/register")
    public void register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
    }

    @PostMapping(value = "/password")
    public MessageHandler forgotPassword(@RequestParam(name = "email") String email, HttpServletRequest httpRequest) {
        return userService.forgotPassword(httpRequest, email);
    }

    @PutMapping(value = "/password/reset")
    public MessageHandler changePassword(
            @RequestParam(name = "token") String token,
            @RequestBody PasswordResetDto passwordResetDto) {
        return userService.changePassword(token, passwordResetDto);
    }
}
