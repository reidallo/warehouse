package app.warehouse.system.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetDto {

    private String password;
    private String rePassword;
}
