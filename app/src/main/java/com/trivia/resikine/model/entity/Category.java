package com.trivia.resikine.model.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public int lastScore;

    public Category(String name, int lastScore) {
        this.name = name;
        this.lastScore = lastScore;
    }
}