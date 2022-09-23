package app.warehouse.system.exception;

public class Messages {

    public static String SUCCESS = "%s were %s successfully!";
    public static String EXISTS = "%s already exists!";
    public static String NOT_FOUND = "%s not found!";
    public static String WRONG_DATE="End date can not be before start date";
    protected String message;

    public Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
