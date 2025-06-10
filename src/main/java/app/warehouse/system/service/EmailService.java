package app.warehouse.system.service;

public interface EmailService {

    void sendEmailDeclinedOrder(String firstName, String lastName, String orderNumber, String message, String email,
                                String subject);

    void sendEmailUnderDelivery(String firstName, String lastName, String orderNumber, String email, String date,
                                String subject);

    void sendEmailToResetPassword(String email, String url, String token, String subject);
}
