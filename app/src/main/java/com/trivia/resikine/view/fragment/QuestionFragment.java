package com.trivia.resikine.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.trivia.resikine.controller.QuizViewModel;
import com.trivia.resikine.databinding.FragmentQuestionBinding;
import com.trivia.resikine.model.entity.Question;
import com.trivia.resikine.view.QuizActivity;
import com.trivia.resikine.view.adapter.OptionAdapter;

import java.util.ArrayList;
import java.util.List;

public class QuestionFragment extends Fragment {
    private FragmentQuestionBinding binding; // Usando ViewBinding
    private QuizViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        binding = FragmentQuestionBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(QuizViewModel.class);

        setupObservers();
        setupClickListeners();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Supongamos que viewModel.getCurrentQuestion() devuelve la entidad de Room
        Question currentQuestion = viewModel.getCurrentQuestion();

        // Convertimos las opciones de la entidad a una lista
        List<String> optionsList = new ArrayList<>(currentQuestion.options);

        // Configuramos el RecyclerView
        OptionAdapter adapter = new OptionAdapter(optionsList, (index, v) -> {
            // Lógica de respuesta (index + 1 porque en tu DB empezabas en 1)
            boolean isCorrect = viewModel.checkAnswer(index + 1);

            // Animación de feedback rápida
            v.setBackgroundColor(isCorrect ? Color.GREEN : Color.RED);

            // Deshabilitar el click para evitar doble respuesta
            binding.rv_options.setEnabled(false);

            new Handler().postDelayed(() -> {
                ((QuizActivity)requireActivity()).nextQuestion();
            }, 600);
        });

        binding.rv_options.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rv_options.setAdapter(adapter);

        // Animación dinámica para que las opciones aparezcan una por una
        binding.rv_options.scheduleLayoutAnimation();
    }

    private void setupObservers() {
        // Observar el tiempo del ViewModel
        viewModel.getTimeLeft().observe(getViewLifecycleOwner(), time -> {
            binding.txtTimer.setText(String.valueOf(time));
            if (time <= 5) binding.txtTimer.setTextColor(Color.RED);
        });

        // Cargar datos de la pregunta actual
        Question current = viewModel.getCurrentQuestion();
        binding.txtQuestion.setText(current.questionText);
        binding.btnOpt1.setText(current.option1);
        // ... setear el resto de botones
    }

    private void setupClickListeners() {
        View.OnClickListener listener = v -> {
            int selected = Integer.parseInt(v.getTag().toString());
            boolean isCorrect = viewModel.checkAnswer(selected);

            // Animación de feedback
            v.setBackgroundColor(isCorrect ? Color.GREEN : Color.RED);

            // Delay pequeño para que el usuario vea la respuesta antes de pasar
            new Handler().postDelayed(() -> {
                ((QuizActivity)requireActivity()).loadFragment();
            }, 800);
        };

        binding.btnOpt1.setOnClickListener(listener);
        // ... asignar a todos
    }
}