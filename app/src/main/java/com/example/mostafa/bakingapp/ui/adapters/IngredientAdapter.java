package com.example.mostafa.bakingapp.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.example.mostafa.bakingapp.R;
import com.example.mostafa.bakingapp.model.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mosta on 11/3/2018.
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private Context context;
    private List<Ingredient> ingredients;
    private String positions;


    public IngredientAdapter(Context context, List<Ingredient> ingredients) {
        this.context = context;
        this.ingredients = ingredients;
        positions = "";
    }

    @NonNull
    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_list_item, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.mIngredientTextView.setText(ingredient.getIngredient());
        String quantity = ingredient.getQuantity() + " " + ingredient.getMeasure();
        holder.mQuantityTextView.setText(quantity);
        if (!positions.contains(String.valueOf(position))) {
            ScaleAnimation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(150);
            holder.itemView.startAnimation(animation);
            positions += String.valueOf(position);
        }
    }

    @Override
    public int getItemCount() {
        if (ingredients != null)
            return ingredients.size();
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.ingredient_name)
        TextView mIngredientTextView;
        @BindView(R.id.ingredient_quantity)
        TextView mQuantityTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void add(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }
}