package com.trivia.resikine.controller;

import android.os.CountDownTimer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trivia.resikine.model.entity.Question;

public class QuizViewModel extends ViewModel {
    private final MutableLiveData<Integer> timeLeft = new MutableLiveData<>();
    private CountDownTimer timer;

    public void startTimer(long seconds) {
        if (timer != null) timer.cancel();
        timer = new CountDownTimer(seconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft.setValue((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                // Lógica de "Tiempo agotado" -> Siguiente pregunta
            }
        }.start();
    }

    public void init(int categoryId) {
    }

    public LiveData<Object> getQuizFinished() {
    }

    public boolean getScore() {
    }

    public LiveData<Object> getTimeLeft() {
    }

    public Question getCurrentQuestion() {
    }

    public boolean checkAnswer(int selected) {
    }
}