package com.project.logistic_management_2.service.notification;

import com.project.logistic_management_2.config.NotificationWebSocketHandler;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class NotificationService {
    @Autowired
    private NotificationWebSocketHandler webSocketHandler;

    public void sendNotification(String message) {
        webSocketHandler.broadcast(message);
    }
}

