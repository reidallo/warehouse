package app.warehouse.system.service;

import app.warehouse.system.dto.PasswordResetDto;
import app.warehouse.system.exception.MessageHandler;
import app.warehouse.system.security.request.LoginRequest;
import app.warehouse.system.security.request.RegisterRequest;
import app.warehouse.system.security.response.JwtResponse;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    JwtResponse login(LoginRequest request);

    void register(RegisterRequest request);

    MessageHandler forgotPassword(HttpServletRequest httpRequest, String email);

    MessageHandler changePassword(String token, PasswordResetDto passwordResetDto);
}
