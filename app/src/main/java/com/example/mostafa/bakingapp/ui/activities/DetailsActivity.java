package com.example.mostafa.bakingapp.ui.activities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mostafa.bakingapp.R;
import com.example.mostafa.bakingapp.model.Ingredient;
import com.example.mostafa.bakingapp.model.Recipe;
import com.example.mostafa.bakingapp.model.Step;
import com.example.mostafa.bakingapp.ui.adapters.StepAdapter;
import com.example.mostafa.bakingapp.ui.fragments.IngredientsFragment;
import com.example.mostafa.bakingapp.ui.fragments.StepFragment;
import com.example.mostafa.bakingapp.utils.Constants;
import com.example.mostafa.bakingapp.widget.IngredientsWidgetProvider;

import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity implements StepAdapter.OnStepClickListener {


    @BindView(R.id.recipe_ingredients)
    TextView mIngredientTextView;
    @BindView(R.id.steps_list)
    RecyclerView mStepsRecyclerView;

    private boolean mIsTwoPane;
    private StepAdapter mStepAdapter;
    private Recipe mRecipe;

    private int currentPosition = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        mIsTwoPane = findViewById(R.id.step_container) != null;

        mStepAdapter = new StepAdapter(this, null, this);
        mStepsRecyclerView.setAdapter(mStepAdapter);

        mRecipe = getIntent().getParcelableExtra(Constants.RECIPE);
        setTitle(mRecipe.getName());

        loadSteps(mRecipe);

        if (savedInstanceState == null && mIsTwoPane)
            showIngredients(mRecipe);

        mIngredientTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIngredients(mRecipe);
            }
        });

    }


    private void loadSteps(Recipe recipe) {
        mStepAdapter.add(recipe.getSteps());
    }

    private void showIngredients(Recipe recipe) {
        if (mIsTwoPane) {
            if (!mIngredientTextView.isSelected()) {
                IngredientsFragment ingredientsFragment = new IngredientsFragment();
                ingredientsFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().replace(R.id.step_container, ingredientsFragment).commit();

                mIngredientTextView.setSelected(true);
                currentPosition = -1;
                mStepAdapter.select(currentPosition);
            }
        } else {
            Intent intent = new Intent(this, IngredientsActivity.class);
            intent.putExtra(Constants.RECIPE, recipe);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStepClick(int position) {
        if (mIsTwoPane) {
            if (currentPosition != position) {
                Step step = mRecipe.getSteps().get(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.STEP, step);
                StepFragment stepFragment = new StepFragment();
                stepFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.step_container, stepFragment).commit();

                currentPosition = position;
                mStepAdapter.select(currentPosition);
            }

            if (mIngredientTextView.isSelected())
                mIngredientTextView.setSelected(false);

        } else {
            Intent intent = new Intent(this, StepActivity.class);
            intent.putExtra(Constants.RECIPE, mRecipe);
            intent.putExtra(Constants.STEP, position);
            startActivity(intent);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {

            if (mIsTwoPane) {
                currentPosition = savedInstanceState.getInt(Constants.POSITION);

                mStepAdapter.select(currentPosition);
                if (currentPosition == -1)
                    showIngredients(mRecipe);

            }

                mStepsRecyclerView.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(Constants.STATE));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.POSITION, currentPosition);
        outState.putParcelable(Constants.STATE, mStepsRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    public void pinIngredients(View view) {
        saveCurrentRecipeIngredients(mRecipe);
        updateWidget();
        Toast.makeText(this,getString(R.string.pinned_to_home_screen),Toast.LENGTH_SHORT).show();
    }
    private void saveCurrentRecipeIngredients(Recipe recipe) {
        Set<String> ingredients = new HashSet<>();
        for (int i = 0; i <recipe.getIngredients().size(); i++) {
            Ingredient ingredient = recipe.getIngredients().get(i);
            ingredients.add(ingredient.getQuantity() + " " + ingredient.getMeasure() + " " + ingredient.getIngredient());
        }
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                .putStringSet(Constants.INGREDIENTS,ingredients)
                .apply();
    }

    private void updateWidget() {
        Intent intent = new Intent(this, IngredientsWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), IngredientsWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }
}
