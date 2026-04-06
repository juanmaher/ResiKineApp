package com.trivia.resikine.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.trivia.resikine.controller.QuizViewModel;
import com.trivia.resikine.view.fragment.QuestionFragment;

public class QuizActivity extends AppCompatActivity {
    private int categoryId;
    private QuizViewModel quizViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        categoryId = getIntent().getIntExtra("CATEGORY_ID", -1);

        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        // Cargamos las preguntas desde el repositorio a través del ViewModel
        quizViewModel.init(categoryId);

        // Observamos cuando la trivia termina para ir a ResultActivity
        quizViewModel.getQuizFinished().observe(this, finished -> {
            if (finished) {
                Intent intent = new Intent(this, ResultActivity.class);
                intent.putExtra("SCORE", quizViewModel.getScore());
                intent.putExtra("CATEGORY_ID", categoryId);
                startActivity(intent);
                finish();
            }
        });

        // Primer fragmento
        if (savedInstanceState == null) {
            loadFragment();
        }

        // Manejo de salida con OnBackPressed
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitDialog();
            }
        });
    }

    public void loadFragment() {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.fragment_container, new QuestionFragment())
                .commit();
    }

    private void showExitDialog() {
        new AlertDialog.Builder(this)
                .setTitle("¿Abandonar?")
                .setMessage("Si sales ahora, perderás tu progreso.")
                .setPositiveButton("Salir", (d, w) -> finish())
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
