package com.example.mostafa.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.mostafa.bakingapp.utils.Constants;
import com.example.mostafa.bakingapp.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mosta on 25/3/2018.
 */

public class IngredientsWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(getApplicationContext());
    }

    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context context;
        private SharedPreferences sharedPreferences;
        private List<String> mIngredients;

        public ListRemoteViewsFactory(Context context) {
            this.context = context;
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            Set<String> ingredients  = sharedPreferences.getStringSet(Constants.INGREDIENTS,new HashSet<String>());
            mIngredients = new ArrayList<>();
            for (String ingredient : ingredients) {
                mIngredients.add(ingredient);
            }
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            if(mIngredients==null)
                return 0;
            return mIngredients.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_list_item);
            remoteViews.setTextViewText(R.id.widget_ingredient, mIngredients.get(position));
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
