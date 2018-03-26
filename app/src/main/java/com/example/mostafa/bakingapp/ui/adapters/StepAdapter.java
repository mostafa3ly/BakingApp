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
import com.example.mostafa.bakingapp.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mosta on 11/3/2018.
 */
public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {

    private Context context;
    private List<Step> steps;
    private OnStepClickListener onStepClickListener;
    private String positions;
    private int currentPosition = -1;


    public StepAdapter(Context context, List<Step> steps, OnStepClickListener onStepClickListener) {
        this.context = context;
        this.steps = steps;
        this.onStepClickListener = onStepClickListener;
        positions = "";
    }

    @NonNull
    @Override
    public StepAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StepAdapter.ViewHolder holder, int position) {
        final Step step = steps.get(position);
        holder.stepDescriptionTextView.setText(step.getShortDescription());
        if(holder.getAdapterPosition()==currentPosition)
            holder.stepDescriptionTextView.setSelected(true);
        else
            holder.stepDescriptionTextView.setSelected(false);
        if (!positions.contains(String.valueOf(position))) {
            ScaleAnimation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(150);
            holder.itemView.startAnimation(animation);
            positions += String.valueOf(position);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStepClickListener.onStepClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (steps != null)
            return steps.size();
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.step_description)
        TextView stepDescriptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void add(List<Step> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    public interface OnStepClickListener {
        void onStepClick( int position);

    }
    public void select(int position)
    {
        currentPosition = position;
        notifyDataSetChanged();
    }
}

