package com.end.lms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student extends User {
    private Map<String, List<Answer>> examAnswers = new HashMap<>();

    public Student(String userId, String username, String password) {
        super(userId, username, password);
    }

    public void submitAnswer(Answer answer) {
        if (answer.validate()) {
            answer.submit();
            examAnswers.computeIfAbsent(answer.getExamId(), k -> new ArrayList<>())
                    .add(answer);
        }
    }

}
