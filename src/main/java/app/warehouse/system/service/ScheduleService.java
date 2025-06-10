package app.warehouse.system.service;

import app.warehouse.system.exception.MessageHandler;

public interface ScheduleService {

    MessageHandler scheduleOrder(Long orderId, String date);
}
