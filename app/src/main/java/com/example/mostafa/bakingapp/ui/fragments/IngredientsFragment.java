package com.example.mostafa.bakingapp.ui.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mostafa.bakingapp.utils.Constants;
import com.example.mostafa.bakingapp.ui.adapters.IngredientAdapter;
import com.example.mostafa.bakingapp.R;
import com.example.mostafa.bakingapp.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientsFragment extends Fragment {

    @BindView(R.id.ingredient_list)
    RecyclerView mIngredientsRecyclerView;

    private IngredientAdapter mIngredientAdapter;

    public IngredientsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);
        ButterKnife.bind(this, view);
        mIngredientAdapter = new IngredientAdapter(getContext(), null);
        mIngredientsRecyclerView.setAdapter(mIngredientAdapter);
        if (getArguments() != null) {
            Recipe recipe = getArguments().getParcelable(Constants.RECIPE);
            loadIngredients(recipe);
        }
        return view;
    }

    private void loadIngredients(Recipe recipe) {
        mIngredientAdapter.add(recipe.getIngredients());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.STATE, mIngredientsRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mIngredientsRecyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getBundle(Constants.STATE));
        }
    }
}