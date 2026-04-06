package com.trivia.resikine.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.trivia.resikine.R;
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
            // 1. "Early Return": Si es null, no hacemos nada y salimos del observer
            if (finished == null) {
                return;
            }

            // 2. Aquí Java ya sabe que 'finished' NO es null.
            // El autounboxing a boolean primitivo es 100% seguro ahora.
            if ((boolean) finished) {
                Intent intent = new Intent(this, ResultActivity.class);
                intent.putExtra("SCORE", quizViewModel.getScore());
                intent.putExtra("CATEGORY_ID", categoryId);
                startActivity(intent);

                // Importante: removemos el observer o finalizamos para evitar
                // que se dispare múltiples veces si la actividad se recrea.
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

    public void nextQuestion() {
        // Le pedimos al ViewModel que avance el índice
        boolean hayMasPreguntas = quizViewModel.avanzarPregunta();

        if (hayMasPreguntas) {
            // Cargamos un NUEVO fragmento con la siguiente pregunta
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    .replace(R.id.fragment_container, new QuestionFragment())
                    .commit();
        } else {
            // El ViewModel marcará quizFinished como true y el Observer de la Activity
            // nos llevará a ResultActivity automáticamente.
            quizViewModel.finalizarTrivia();
        }
    }
}
