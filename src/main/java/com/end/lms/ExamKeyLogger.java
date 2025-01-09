package com.end.lms;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;

public class ExamKeyLogger {

    private final List<KeyLogEntry> keyLogs;
    private String lastAction;
    private long lastLogTime;
    private static final long LOG_DELAY = 1000;

    public ExamKeyLogger() {
        this.keyLogs = new ArrayList<>();
        this.lastAction = "";
        this.lastLogTime = 0;
    }

    public void logKeyEvent(KeyEvent event) {
        String action = createKeyEventDescription(event);
        if (action != null) {
            addLogEntry(action);
        }
    }

    public void logWindowUnfocused() {
        addLogEntry("Switching Window");
    }

    private void addLogEntry(String action) {
        long currentTime = System.currentTimeMillis();

        if (!action.equals(lastAction) || (currentTime - lastLogTime) >= LOG_DELAY) {
            KeyLogEntry entry = new KeyLogEntry(action);
            keyLogs.add(entry);
            lastAction = action;
            lastLogTime = currentTime;
        }
    }

    private String createKeyEventDescription(KeyEvent event) {
        if (event.isControlDown()) {
            if (event.getCode() == KeyCode.C)
                return "Pressed Ctrl+C";
            if (event.getCode() == KeyCode.V)
                return "Pressed Ctrl+V";
            if (event.getCode() == KeyCode.TAB)
                return "Pressed Ctrl+Tab";
            if (event.getCode() == KeyCode.WINDOWS)
                return "Pressed Ctrl+Win";
        }

        if (event.isAltDown()) {
            if (event.getCode() == KeyCode.TAB)
                return "Pressed Alt+Tab";
            if (event.getCode() == KeyCode.F4)
                return "Pressed Alt+F4";
        }

        if (event.getCode() == KeyCode.WINDOWS)
            return "Pressed Win";
        if (event.getCode() == KeyCode.PRINTSCREEN)
            return "Pressed PrtSc";

        return null;
    }

    public List<KeyLogEntry> getCheatingLogs() {
        return new ArrayList<>(keyLogs);
    }
}
