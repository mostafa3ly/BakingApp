package com.example.mostafa.bakingapp.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.mostafa.bakingapp.utils.Constants;
import com.example.mostafa.bakingapp.ui.fragments.IngredientsFragment;
import com.example.mostafa.bakingapp.R;
import com.example.mostafa.bakingapp.model.Recipe;

public class IngredientsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        Recipe recipe = getIntent().getParcelableExtra(Constants.RECIPE);
        setTitle(recipe.getName());
        if (savedInstanceState == null) {
            IngredientsFragment ingredientsFragment = new IngredientsFragment();
            ingredientsFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().replace(R.id.ingredient_container, ingredientsFragment).commit();
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
}
