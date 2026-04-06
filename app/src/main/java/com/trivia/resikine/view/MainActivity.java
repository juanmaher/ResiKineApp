package com.trivia.resikine.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.trivia.resikine.model.entity.Category;
import com.trivia.resikine.R;
import com.trivia.resikine.controller.HomeViewModel;
import com.trivia.resikine.view.adapter.CategoryAdapter;

public class MainActivity extends AppCompatActivity {

    private HomeViewModel homeViewModel;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Configurar RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_categories);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // El adaptador recibe el callback para cuando el usuario toca una temática
        adapter = new CategoryAdapter(category -> {
            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            intent.putExtra("CATEGORY_ID", category.id);
            intent.putExtra("CATEGORY_NAME", category.name);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        // 2. Inicializar el ViewModel
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // 3. Observar los datos
        homeViewModel.getAllCategories().observe(this, categories -> {
            // Actualizar la lista en el adaptador usando DiffUtil
            adapter.submitList(categories);

            // Ejecutar la animación de entrada escalonada si es la primera vez
            recyclerView.scheduleLayoutAnimation();
        });
    }
}