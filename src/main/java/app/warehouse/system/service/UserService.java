package app.warehouse.system.service;

import app.warehouse.system.security.request.LoginRequest;
import app.warehouse.system.security.request.RegisterRequest;
import app.warehouse.system.security.response.JwtResponse;

public interface UserService {

    JwtResponse login(LoginRequest request);

    void register(RegisterRequest request);
}
