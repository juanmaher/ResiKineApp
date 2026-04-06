package com.trivia.resikine.model.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "questions")
public class Question {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int categoryId;
    public String questionText;
    public List<String> options;
    public int correctAnswer; // 1 to 4

    public Question(int categoryId, String questionText, List<String> options, int correctAnswer) {
        this.categoryId = categoryId;
        this.questionText = questionText;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }
}
