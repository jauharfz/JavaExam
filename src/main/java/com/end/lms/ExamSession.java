package com.end.lms;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class ExamSession {
    private boolean isActive;
    private final Map<String, ExamKeyLogger> studentKeyLoggers;
    private final List<String> activeStudentIds;

    public ExamSession() {
        this.studentKeyLoggers = new HashMap<>();
        this.activeStudentIds = new ArrayList<>();
        this.isActive = false;
    }

    public void startSession() {
        LocalDateTime startTime = LocalDateTime.now();
        this.isActive = true;
        System.out.println("Exam session started at: " + startTime);
    }

    public void endSession() {
        this.isActive = false;

        studentKeyLoggers.forEach((studentId, logger) -> System.out.println("Saving logs for student: " + studentId));
        System.out.println("Exam session ended at: " + LocalDateTime.now());
    }

    public void addActiveStudent(String studentId) {
        activeStudentIds.add(studentId);

        ExamKeyLogger keyLogger = new ExamKeyLogger();
        studentKeyLoggers.put(studentId, keyLogger);
        System.out.println("Student " + studentId + " added to session with keylogger");
    }

    public ExamKeyLogger getKeyLogger(String studentId) {
        return studentKeyLoggers.get(studentId);
    }

    public List<KeyLogEntry> getStudentCheatingLogs(String studentId) {
        ExamKeyLogger logger = studentKeyLoggers.get(studentId);
        if (logger != null) {
            List<KeyLogEntry> logs = logger.getCheatingLogs();
            System.out.println("Retrieved " + logs.size() + " log entries for student " + studentId);
            return logs;
        }
        return new ArrayList<>();
    }

    public boolean isActive() {
        return isActive;
    }

}
