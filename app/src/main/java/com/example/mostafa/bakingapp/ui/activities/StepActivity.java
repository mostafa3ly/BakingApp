package com.example.mostafa.bakingapp.ui.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.example.mostafa.bakingapp.utils.Constants;
import com.example.mostafa.bakingapp.R;
import com.example.mostafa.bakingapp.ui.fragments.StepFragment;
import com.example.mostafa.bakingapp.model.Recipe;
import com.example.mostafa.bakingapp.model.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepActivity extends AppCompatActivity implements View.OnClickListener{


    private int position;
    private Recipe recipe;

    @BindView(R.id.next_step)
    FloatingActionButton nextFloatingActionButton;
    @BindView(R.id.previous_step)
    FloatingActionButton previousFloatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);

        recipe = getIntent().getParcelableExtra(Constants.RECIPE);
        position = getIntent().getIntExtra(Constants.STEP,0);
        setTitle(recipe.getName());

        if (savedInstanceState == null) {
            loadStep(position);
        }


        previousFloatingActionButton.setOnClickListener(this);
        nextFloatingActionButton.setOnClickListener(this);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState!=null) {
            position = savedInstanceState.getInt(Constants.POSITION, 0);
            checkPosition();
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

    private void checkPosition()
    {
        previousFloatingActionButton.setVisibility(View.VISIBLE);
        nextFloatingActionButton.setVisibility(View.VISIBLE);
        if(position==0)
            previousFloatingActionButton.setVisibility(View.GONE);
        if(position==recipe.getSteps().size()-1)
            nextFloatingActionButton.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.next_step:
                if(position<recipe.getSteps().size()-1) {
                    position++;
                    loadStep(position);
                }
                break;
            case R.id.previous_step:
                if(position>0) {
                    position--;
                    loadStep(position);
                }
                break;
        }

    }

    private void loadStep(int position) {
        Step step = recipe.getSteps().get(position);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.STEP,step);
        StepFragment stepFragment = new StepFragment();
        stepFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.step_container, stepFragment).commit();
        checkPosition();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.POSITION,position);
    }
}
