package app.warehouse.system.security.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RegisterRequest {

    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String username;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String accountType;
    @NotNull
    private String address;
    @NotNull
    private Integer postalCode;
    @NotNull
    private String city;
    @NotNull
    private String state;
    @NotNull
    private String phone;
}
