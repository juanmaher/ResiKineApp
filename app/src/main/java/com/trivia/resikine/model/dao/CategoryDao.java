package com.trivia.resikine.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.trivia.resikine.model.entity.Category;

import java.util.List;

@Dao
public interface CategoryDao {

    @Query("SELECT * FROM categories ORDER BY name ASC")
    LiveData<List<Category>> getAllCategories();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCategories(List<Category> categories);

    @Query("UPDATE categories SET lastScore = :score WHERE id = :categoryId")
    void updateLastScore(int categoryId, int score);

    // Útil para el seed inicial
    @Query("SELECT COUNT(*) FROM categories")
    int getCategoryCount();

    @Insert
    long insertCategorySync(Category category);
}
