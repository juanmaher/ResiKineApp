package com.trivia.resikine.controller;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.trivia.resikine.model.entity.Category;
import com.trivia.resikine.model.repository.TriviaRepository;
import java.util.List;

/**
 * Usamos AndroidViewModel en lugar de ViewModel porque necesitamos
 * el contexto de la aplicación para inicializar la base de datos de Room.
 */
public class HomeViewModel extends AndroidViewModel {

    private final TriviaRepository repository;
    private final LiveData<List<Category>> allCategories;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        // Inicializamos el repositorio
        repository = new TriviaRepository(application);
        // Obtenemos el LiveData que observa la tabla de categorías
        allCategories = repository.getAllCategories();
    }

    /**
     * Este método será observado por la MainActivity.
     * Cualquier cambio en la DB (como un nuevo puntaje) notificará a la vista.
     */
    public LiveData<List<Category>> getAllCategories() {
        return allCategories;
    }
}