package com.trivia.resikine.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.trivia.resikine.controller.QuizViewModel;
import com.trivia.resikine.databinding.FragmentQuestionBinding;
import com.trivia.resikine.model.entity.Question;
import com.trivia.resikine.view.QuizActivity;
import com.trivia.resikine.view.adapter.OptionAdapter;

import java.util.ArrayList;
import java.util.List;

public class QuestionFragment extends Fragment {
    private FragmentQuestionBinding binding;
    private QuizViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentQuestionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar ViewModel compartido con la Activity
        viewModel = new ViewModelProvider(requireActivity()).get(QuizViewModel.class);

        setupObservers();
        loadQuestionData();
    }

    private void setupObservers() {
        viewModel.getTimeLeft().observe(getViewLifecycleOwner(), time -> {
            if (time == null) return;

            binding.txtTimer.setText(String.valueOf(time));
            binding.txtTimer.setTextColor(time <= 5 ? Color.RED : Color.BLACK);

            // Opcional: si el tiempo llega a 0, podrías forzar el salto de pregunta aquí
            if (time == 0) handleAnswerSelection(-1, null);
        });
    }

    private void loadQuestionData() {
        Question current = viewModel.getCurrentQuestion();
        if (current == null) return;

        binding.txtQuestion.setText(current.questionText);

        // Preparar lista de opciones dinámicamente
        List<String> options = current.options;

        setupRecyclerView(options);

        // Iniciar timer para esta pregunta específica
        viewModel.startTimer(15);
    }

    private void setupRecyclerView(List<String> options) {
        OptionAdapter adapter = new OptionAdapter(options, this::handleAnswerSelection);

        binding.rvOptions.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvOptions.setAdapter(adapter);
        binding.rvOptions.scheduleLayoutAnimation();
    }

    private void handleAnswerSelection(int index, View view) {
        // Deshabilitar interacción inmediata
        binding.rvOptions.setEnabled(false);
        for (int i = 0; i < binding.rvOptions.getChildCount(); i++) {
            binding.rvOptions.getChildAt(i).setEnabled(false);
        }

        // Lógica de validación (index -1 significa tiempo agotado)
        if (index != -1 && view != null) {
            boolean isCorrect = viewModel.checkAnswer(index + 1);
            view.setBackgroundColor(isCorrect ? Color.GREEN : Color.RED);
        } else {
            viewModel.stopTimer(); // Detener si fue por tiempo
        }

        // Transición a la siguiente pantalla
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (isAdded() && getActivity() instanceof QuizActivity) {
                ((QuizActivity) getActivity()).nextQuestion();
            }
        }, 800);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Limpieza de ViewBinding
    }
}