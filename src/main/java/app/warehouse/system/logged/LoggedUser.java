package app.warehouse.system.logged;

import org.springframework.security.core.context.SecurityContextHolder;

public class LoggedUser {

    public static String loggedInUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
