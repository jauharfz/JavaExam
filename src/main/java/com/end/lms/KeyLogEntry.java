package com.end.lms;

import java.time.LocalDateTime;

public class KeyLogEntry {
    private String action;
    private LocalDateTime timestamp;

    public KeyLogEntry(String action) {
        this.action = action;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", timestamp, action);
    }
}
