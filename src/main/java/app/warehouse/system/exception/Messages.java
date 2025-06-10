package app.warehouse.system.exception;

public class Messages {

    public static String SUCCESS = "%s was %s successfully!";
    public static String CREATED = "Account was created successfully";
    public static String EXISTS = "%s already exists!";
    public static String NOT_FOUND = "%s not found!";
    protected String message;

    public Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
