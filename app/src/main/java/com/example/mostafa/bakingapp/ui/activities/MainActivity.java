package com.example.mostafa.bakingapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mostafa.bakingapp.R;
import com.example.mostafa.bakingapp.idlingResource.SimpleIdlingResource;
import com.example.mostafa.bakingapp.model.Recipe;
import com.example.mostafa.bakingapp.ui.adapters.RecipeAdapter;
import com.example.mostafa.bakingapp.utils.Constants;
import com.example.mostafa.bakingapp.utils.RecipeDownloader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.OnRecipeClickListener, RecipeDownloader.DelayerCallback {

    @BindView(R.id.recipes_list)
    RecyclerView recipesListRecyclerView;
    @BindView(R.id.recipes_progress_bar)
    ProgressBar mProgressBar;

    private RecipeAdapter mRecipeAdapter;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mRecipeAdapter = new RecipeAdapter(this, null, this);
        recipesListRecyclerView.setAdapter(mRecipeAdapter);
        getIdlingResource();
        RecipeDownloader.downloadRecipes(this, this, mIdlingResource);
    }

    @Override
    public void onRecipeClick(Recipe recipe) {

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(Constants.RECIPE, recipe);
        startActivity(intent);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    public void onDone(List<Recipe> recipes) {
        mProgressBar.setVisibility(View.GONE);
        mRecipeAdapter.add(recipes);

    }

    @Override
    public void onFailure() {
        Toast.makeText(this, getString(R.string.failed_to_connect), Toast.LENGTH_LONG).show();
        mProgressBar.setVisibility(View.GONE);
    }
}