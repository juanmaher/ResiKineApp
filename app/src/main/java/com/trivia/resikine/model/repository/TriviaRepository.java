package com.trivia.resikine.model.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.trivia.resikine.model.dao.CategoryDao;
import com.trivia.resikine.model.dao.QuestionDao;
import com.trivia.resikine.model.database.TriviaDatabase;
import com.trivia.resikine.model.entity.Category;
import com.trivia.resikine.model.entity.Question;

import java.util.List;

public class TriviaRepository {
    private final CategoryDao categoryDao;
    private final QuestionDao questionDao;

    public TriviaRepository(Application application) {
        TriviaDatabase db = TriviaDatabase.getInstance(application);
        categoryDao = db.categoryDao();
        questionDao = db.questionDao();
    }

    // Retorna LiveData para que el HomeViewModel observe cambios automáticamente
    public LiveData<List<Category>> getAllCategories() {
        return categoryDao.getAllCategories();
    }

    public void updateScore(int categoryId, int score) {
        TriviaDatabase.databaseWriteExecutor.execute(() -> {
            categoryDao.updateLastScore(categoryId, score);
        });
    }

    public List<Question> getQuestionsForCategory(int categoryId) {
        // Esta llamada debe hacerse en un hilo secundario antes de pasar los datos al QuizViewModel
        return questionDao.getQuestionsByCategoryRandom(categoryId);
    }
}
