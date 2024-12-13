package com.project.logistic_management_2.config;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.CloseStatus;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.Set;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NotificationWebSocketHandler extends TextWebSocketHandler {
    // Lưu trữ các session đang mở
    private Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    // Lưu thời điểm tương tác cuối cùng của mỗi session
    private Map<String, Instant> lastActiveTime = new ConcurrentHashMap<>();

    // Khởi tạo một scheduler để check idle timeout
    public NotificationWebSocketHandler() {
        // Kiểm tra mỗi 1 phút xem có session nào idle quá 5 phút thì đóng
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            Instant now = Instant.now();
            for (WebSocketSession session : sessions) {
                Instant lastActive = lastActiveTime.get(session.getId());
                if (lastActive != null) {
                    long idleDuration = now.getEpochSecond() - lastActive.getEpochSecond();
                    if (idleDuration > 300) { // 300 giây = 5 phút
                        try {
                            session.close(CloseStatus.SESSION_NOT_RELIABLE);
                        } catch (Exception e) {
                            // ignore
                        }
                    }
                }
            }
        }, 1, 1, TimeUnit.MINUTES);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        lastActiveTime.put(session.getId(), Instant.now());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Cập nhật thời gian tương tác
        lastActiveTime.put(session.getId(), Instant.now());

        // Parse JSON message từ client, giả sử dạng {"message":"trung"}
        // Sử dụng Jackson để parse
        String payload = message.getPayload();
        // Có thể dùng ObjectMapper để parse JSON:
        // ObjectMapper mapper = new ObjectMapper();
        // Map<String, String> map = mapper.readValue(payload, new TypeReference<Map<String,String>>(){});
        // String clientMsg = map.get("message");

        // Ở đây ví dụ đơn giản, assume payload luôn có dạng {"message":"xxx"}
        String clientMsg = payload.replace("{\"message\":\"","").replace("\"}","");

        String response = "{\"message\":\"hello " + clientMsg + "\"}";
        session.sendMessage(new TextMessage(response));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        lastActiveTime.remove(session.getId());
    }

    // Hàm này để broadcast message đến mọi session
    public void broadcast(String msg) {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(msg));
                } catch (Exception e) {
                    // handle error
                }
            }
        }
    }
}

