package app.warehouse.system.service.implementation;

import app.warehouse.system.model.Customer;
import app.warehouse.system.model.Role;
import app.warehouse.system.model.User;
import app.warehouse.system.repository.CustomerRepository;
import app.warehouse.system.repository.RoleRepository;
import app.warehouse.system.repository.UserRepository;
import app.warehouse.system.security.jwt.JwtUtils;
import app.warehouse.system.security.request.LoginRequest;
import app.warehouse.system.security.request.RegisterRequest;
import app.warehouse.system.security.response.JwtResponse;
import app.warehouse.system.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;

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
    public void register(RegisterRequest request) {

        //check if user is already registered
        if (userRepository.findUserByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("User with email: " + request.getEmail() + " already exists!");
        }
        if (userRepository.findUserByUsername(request.getUsername()).isPresent()) {
            throw new IllegalStateException("User with username: " + request.getUsername() + " already exists!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);

        Role role = roleRepository.findRoleByRoleName(request.getAccountType()).orElseThrow(() ->
                new IllegalStateException("Account type " + request.getAccountType() + " does not exists!"));
        Set<Role> roleSet= new HashSet<>();
        roleSet.add(role);
        user.setRoles(roleSet);
        userRepository.save(user);

        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setUser(user);
        customerRepository.save(customer);
    }
}
