package com.trivia.resikine.controller;

import android.app.Application;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.trivia.resikine.model.entity.Question;
import com.trivia.resikine.model.repository.TriviaRepository;
import java.util.List;

public class QuizViewModel extends AndroidViewModel {

    private final TriviaRepository repository;
    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;

    private final MutableLiveData<Integer> timeLeft = new MutableLiveData<>();
    private final MutableLiveData<Boolean> quizFinished = new MutableLiveData<>(false);
    private CountDownTimer timer;

    public QuizViewModel(@NonNull Application application) {
        super(application);
        repository = new TriviaRepository(application);
    }

    public void init(int categoryId) {
        // Obtenemos las preguntas de forma sincrónica desde el repo
        // (El repo debe manejar el hilo o llamar a Room directamente si es una lista pequeña)
        questions = repository.getQuestionsForCategory(categoryId);
        currentQuestionIndex = 0;
        score = 0;
        quizFinished.setValue(false);
    }

    public void startTimer(long seconds) {
        if (timer != null) timer.cancel();
        timer = new CountDownTimer(seconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft.setValue((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                // Si el tiempo se agota, podrías marcar la respuesta como incorrecta
                // o disparar un evento para pasar a la siguiente
                timeLeft.setValue(0);
            }
        }.start();
    }

    public void stopTimer() {
        if (timer != null) timer.cancel();
    }

    public boolean checkAnswer(int selectedIndex) {
        stopTimer();
        Question current = getCurrentQuestion();
        if (current != null && current.correctAnswer == selectedIndex) {
            score++;
            return true;
        }
        return false;
    }

    public boolean avanzarPregunta() {
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            return true;
        }
        return false;
    }

    public void finalizarTrivia() {
        quizFinished.setValue(true);
    }

    // --- Getters ---

    public Question getCurrentQuestion() {
        if (questions != null && currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }

    public LiveData<Integer> getTimeLeft() {
        return timeLeft;
    }

    public LiveData<Boolean> getQuizFinished() {
        return quizFinished;
    }

    public int getScore() {
        return score;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (timer != null) timer.cancel();
    }
}