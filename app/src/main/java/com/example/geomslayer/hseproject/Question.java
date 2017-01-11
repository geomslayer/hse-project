package com.example.geomslayer.hseproject;

import java.util.ArrayList;

/**
 * Created by geomslayer on 11.01.17.
 */

class Question {

    private String question;
    private ArrayList<String> options;
    private int answer;

    public Question() {
        this.question = "Question";
        this.options = new ArrayList<>();
        options.add("Option1");
        options.add("Option2");
        options.add("Option3");
        this.answer = 0;
    }

    public Question(String question, ArrayList<String> options, int answer) {
        this.question = question;
        this.options = options;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public void addOption(String option) {
        options.add(option);
    }

    public boolean checkAnswer(int cur) {
        return cur == answer;
    }

}
