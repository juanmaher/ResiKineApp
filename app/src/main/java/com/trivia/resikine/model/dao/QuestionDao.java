package com.trivia.resikine.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.trivia.resikine.model.entity.Question;

import java.util.List;

@Dao
public interface QuestionDao {

    @Query("SELECT * FROM questions WHERE categoryId = :catId")
    List<Question> getQuestionsByCategory(int catId);

    @Query("SELECT * FROM questions WHERE categoryId = :catId ORDER BY RANDOM()")
    List<Question> getQuestionsByCategoryRandom(int catId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestions(List<Question> questions);

    @Query("DELETE FROM questions")
    void deleteAll();

    @Insert
    void insertQuestionSync(Question question);
}
