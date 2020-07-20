package com.example.leafapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.leafapp.R;
import com.example.leafapp.utils.TextSearch;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends AppCompatActivity {

    private SearchView mSearchField;
    private ListView mSearchList;
    private TextSearch mTextSearch;
    private HashMap<String, ArrayList<String>> mSearchResult;
    private FloatingActionButton mExitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Setup Widgets
        mSearchField = findViewById(R.id.search_field);
        mSearchList = findViewById(R.id.search_list);
        mExitBtn = findViewById(R.id.search_exit_btn);
        mSearchResult = null;
        mTextSearch = new TextSearch();

        // Set listeners
        mSearchField.setOnQueryTextListener(search_listener);
        mSearchList.setOnItemClickListener(search_click_listener);
        mExitBtn.setOnClickListener(exit_listener);
    }

    SearchView.OnQueryTextListener search_listener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        // TODO try search with loading data and using filter to get right text maybe faster
        @Override
        public boolean onQueryTextChange(String newText) {
            InputStream file_stream = getResources().openRawResource(R.raw.booklist);
            mSearchResult = mTextSearch.getResults(newText, file_stream);
            if (mSearchResult == null){
                return false;
            }

            ArrayAdapter<String> search_adapter = new ArrayAdapter<>(SearchActivity.this,
                    android.R.layout.simple_list_item_1, new ArrayList<>(mSearchResult.keySet()));

            mSearchList.setAdapter(search_adapter);
            return true;
        }
    };

    AdapterView.OnItemClickListener search_click_listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String chosen_title = mSearchList.getItemAtPosition(i).toString();
                Intent resultIntent = new Intent();

                resultIntent.putExtra("result", chosen_title);
                setResult(Activity.RESULT_OK, resultIntent);

                finish();
            }
    };

    View.OnClickListener exit_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent resultIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, resultIntent);
            finish();
        }
    };
}