package com.example.photolibraryapp;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import model.Adapter;
import model.CustomListner;
import model.Photo;

public class SearchPageActivity extends AppCompatActivity {

    public CustomListner customSpinner;
    public GridView gridView;
    public SearchView searchView;
    public ArrayList<Photo> photoList = new ArrayList<>();
    public Adapter albumImageAdapter;
    public String[] spinneroptions = {"Person", "Location"};
    public int option;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        customSpinner = (CustomListner) findViewById(R.id.tagspinner);
        gridView = (GridView) findViewById(R.id.gridview_search);
        searchView = (SearchView) findViewById(R.id.searchField);
        albumImageAdapter = new Adapter(SearchPageActivity.this, photoList);
        gridView.setAdapter(albumImageAdapter);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, spinneroptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customSpinner.setAdapter(spinnerAdapter);

        customSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                option = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                photoList.clear();
                //Search for photos by person
                if(option == 0){
                    photoList.addAll(MainActivity.session.searchForCertainTags(spinneroptions[option],s));
                    gridView = (GridView) findViewById(R.id.gridview_search);
                    albumImageAdapter = new Adapter(SearchPageActivity.this, photoList);
                    gridView.setAdapter(albumImageAdapter);
                }
                //Search for photos by location
                else if(option == 1){
                    photoList.addAll(MainActivity.session.searchForCertainTags(spinneroptions[option],s));
                    gridView = (GridView) findViewById(R.id.gridview_search);
                    albumImageAdapter = new Adapter(SearchPageActivity.this, photoList);
                    gridView.setAdapter(albumImageAdapter);
                }
                return true;
            }
        });



        gridView = (GridView) findViewById(R.id.gridview_search);
        albumImageAdapter = new Adapter(SearchPageActivity.this, photoList);
        gridView.setAdapter(albumImageAdapter);
    }
}