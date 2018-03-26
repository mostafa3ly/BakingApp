package com.example.mostafa.bakingapp.api;

import com.example.mostafa.bakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by mosta on 10/3/2018.
 */

public interface RecipesService {

    @GET("baking.json")
    Call<List<Recipe>> getRecipes();

}
