package app.warehouse.system.exception;

import app.warehouse.system.statics.MessageStatus;

import java.util.HashMap;

public class MessageHandler {

    public static HashMap<MessageStatus, String> hashMap = new HashMap<>();
    private HashMap<MessageStatus, String> message;

    public static void message(MessageStatus status, String message) {
        hashMap.clear();
        hashMap.put(status, message);
    }

    public MessageHandler(HashMap<MessageStatus, String> message) {
        this.message = message;
    }

    public HashMap<MessageStatus, String> getMessage() {
        return message;
    }

    public void setMessage(HashMap<MessageStatus, String> message) {
        this.message = message;
    }
}
