package app.warehouse.system.controller;

import app.warehouse.system.security.request.LoginRequest;
import app.warehouse.system.security.request.RegisterRequest;
import app.warehouse.system.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
