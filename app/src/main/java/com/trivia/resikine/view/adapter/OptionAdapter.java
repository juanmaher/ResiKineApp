package com.trivia.resikine.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trivia.resikine.R;

import java.util.List;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.OptionViewHolder> {

    private List<String> options;
    private OnOptionClickListener listener;

    public interface OnOptionClickListener {
        void onOptionClick(int index, View view);
    }

    public OptionAdapter(List<String> options, OnOptionClickListener listener) {
        this.options = options;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_option, parent, false);
        return new OptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionViewHolder holder, int position) {
        String optionText = options.get(position);
        holder.button.setText(optionText);
        holder.button.setOnClickListener(v -> listener.onOptionClick(position, v));
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    static class OptionViewHolder extends RecyclerView.ViewHolder {
        Button button;
        OptionViewHolder(View itemView) {
            super(itemView);
            button = (Button) itemView;
        }
    }
}
