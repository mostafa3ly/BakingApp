package com.example.mostafa.bakingapp.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.mostafa.bakingapp.api.APIClient;
import com.example.mostafa.bakingapp.api.RecipesService;
import com.example.mostafa.bakingapp.idlingResource.SimpleIdlingResource;
import com.example.mostafa.bakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mosta on 26/3/2018.
 */

public class RecipeDownloader {


    public interface DelayerCallback{
        void onDone(List<Recipe> recipes);
        void onFailure();
    }

    public static void downloadRecipes(Context context, final DelayerCallback callback,
                                @Nullable final SimpleIdlingResource idlingResource) {


        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }


        RecipesService recipesService = APIClient.getClient().create(RecipesService.class);
        Call<List<Recipe>> call = recipesService.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                List<Recipe> recipes = response.body();
                if (recipes != null) {
                    if (callback != null) {
                        callback.onDone(recipes);
                        if (idlingResource != null) {
                            idlingResource.setIdleState(true);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable throwable) {
                callback.onFailure();
            }
        });

    }
    }
