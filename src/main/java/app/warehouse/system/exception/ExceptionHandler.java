package app.warehouse.system.exception;

public class ExceptionHandler extends RuntimeException{

    public static final String NOT_FOUND = "%s not found!";
    public static final String NOT_ENOUGH = "There are not enough %s on the warehouse!";
    public static final String  NO_ACCESS = "You do not have access!";
    public ExceptionHandler(String message) {
        super(message);
    }
}
