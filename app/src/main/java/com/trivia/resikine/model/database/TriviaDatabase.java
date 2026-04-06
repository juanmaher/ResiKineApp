package com.trivia.resikine.model.database;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.trivia.resikine.model.dao.CategoryDao;
import com.trivia.resikine.model.dao.QuestionDao;
import com.trivia.resikine.model.entity.Category;
import com.trivia.resikine.model.entity.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Category.class, Question.class}, version = 1)
public abstract class TriviaDatabase extends RoomDatabase {

    public abstract CategoryDao categoryDao();
    public abstract QuestionDao questionDao();

    private static volatile TriviaDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static TriviaDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (TriviaDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    TriviaDatabase.class, "trivia_db")
                            .addCallback(sRoomDatabaseCallback) // <--- El Callback
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                CategoryDao catDao = INSTANCE.categoryDao();
                QuestionDao queDao = INSTANCE.questionDao();

                // 1. Insertar Categorías
                long idDeporte = catDao.insertCategorySync(new Category("Deportes", 0));
                long idCine = catDao.insertCategorySync(new Category("Cine", 0));

                // 2. Insertar Preguntas asociadas a esos IDs
                queDao.insertQuestionSync(new Question((int)idDeporte,
                        "¿Quién ganó el mundial de 2022?",
                        new ArrayList<>(List.of("Francia", "Argentina", "Brasil", "Alemania")),
                        2));

                queDao.insertQuestionSync(new Question((int)idCine,
                        "¿Quién dirigió 'Oppenheimer'?",
                        new ArrayList<>(List.of("Spielberg", "Tarantino", "Nolan", "Scorsese")),
                        3));
            });
        }
    };
}
