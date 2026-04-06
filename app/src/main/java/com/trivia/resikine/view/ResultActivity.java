package com.trivia.resikine.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.trivia.resikine.R;
import com.trivia.resikine.model.repository.TriviaRepository;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        int score = getIntent().getIntExtra("SCORE", 0);
        int categoryId = getIntent().getIntExtra("CATEGORY_ID", -1);

        TextView txtScore = findViewById(R.id.txt_final_score);
        txtScore.setText("Puntaje: " + score);

        // Guardar en Base de Datos (MVC/MVVM style)
        TriviaRepository repo = new TriviaRepository(getApplication());
        repo.updateScore(categoryId, score);

        // Botón Reintentar
        findViewById(R.id.btn_retry).setOnClickListener(v -> {
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra("CATEGORY_ID", categoryId);
            startActivity(intent);
            finish();
        });

        // Botón Menú Principal
        findViewById(R.id.btn_home).setOnClickListener(v -> {
            // Al hacer finish, volvemos a la MainActivity que ya está en el stack
            finish();
        });
    }
}
