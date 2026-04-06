package com.trivia.resikine.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.trivia.resikine.R;
import com.trivia.resikine.model.entity.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categories = new ArrayList<>();
    private final OnCategoryClickListener listener;

    // Interface para manejar el click desde la Activity
    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }

    public CategoryAdapter(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflamos el layout de cada item (asegúrate de que el nombre coincida)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category current = categories.get(position);

        // Seteamos los textos usando los Getters de la entidad
        holder.title.setText(current.name);
        holder.summaryPreview.setText("Último puntaje: " + current.lastScore);

        // Configuramos el click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCategoryClick(current);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    // Este método lo llama el LiveData del HomeViewModel
    public void submitList(List<Category> newList) {
        this.categories = newList;
        notifyDataSetChanged(); // Podrías usar DiffUtil para más eficiencia luego
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView title, summaryPreview;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            // Asegúrate de que estos IDs coincidan con item_category.xml
            title = itemView.findViewById(R.id.txt_category_name);
            summaryPreview = itemView.findViewById(R.id.txt_last_score);
        }
    }
}