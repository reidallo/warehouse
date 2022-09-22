package app.warehouse.system.exception;

public class ExceptionHandler extends RuntimeException{

    public static final String NOT_FOUND = "%s not found!";
    public ExceptionHandler(String message) {
        super(message);
    }
}
