package com.example.photolibraryapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import model.*;

public class MainActivity extends AppCompatActivity {

    public FloatingActionButton fab;

    public ArrayList<String> albumnames = new ArrayList<String>();
    public ArrayAdapter adapter;
    public ListView listview;
    public File filename = new File("/data/data/com.example.photolibraryapp/files/data.dat");
    public int pos;
    public static User session = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            session = User.load();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(!filename.exists()){
            Context context = this;
            File file = new File(context.getFilesDir(), "data.dat");
            try {
                file.createNewFile();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        update();

        listview = (ListView) findViewById(R.id.listview);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        adapter = new ArrayAdapter(this, R.layout.album_name, albumnames);
        listview.setAdapter(adapter);
        registerForContextMenu(listview);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addAlbum(v);
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Album currentAlbum = session.getAlbums().get(i);
                session.setCurrentAlbum(currentAlbum);
                Intent goToCurrentAlbum = new Intent(MainActivity.this, AlbumViewActivity.class);
                startActivity(goToCurrentAlbum);
            }
        });

    }

    /**
     * Display the menu bar with app title and search icon
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Display the drop down menu with rename album and delete album options
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.rename_delete_options, menu);
    }

    /**
     * Display alert dialog to rename album or to delete album
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        final int pos = (int) info.id;
        pos = info.position;
        System.out.println("======================" + pos);
        switch (item.getItemId()){
            case R.id.renameAlbum:
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Rename");
                alert.setMessage("Rename me");

                final EditText input = new EditText(MainActivity.this);
                alert.setView(input);


                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String renamed = input.getText().toString();
                        if(session.checkAlbumExists(renamed)){
                            Context context = getApplicationContext();
                            CharSequence text = "Album already exists. Try another name.";
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(context, text, duration).show();
                            return;
                        }
                        if(renamed.isEmpty()){
                            Context context = getApplicationContext();
                            CharSequence text = "Field cannot be blank";
                            int duration = Toast.LENGTH_SHORT;
                            Toast.makeText(context, text, duration).show();
                            return;
                        }
                        session.getAlbums().get(pos).setAlbumName(renamed);

                        try {
                            User.save(session);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        update();
                        adapter.notifyDataSetChanged();
                    }
                });


                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
                return true;
            case R.id.deleteAlbum:
                session.deleteAlbum(pos);
                try {
                    User.save(session);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                update();
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Album Deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Display search page activity when click on search icon
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.search_icon) {
            Intent goToSearch = new Intent(MainActivity.this, SearchPageActivity.class);
            startActivity(goToSearch);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Gets executed when the activity starts
     */
    @Override
    public void onStart() {
        super.onStart();

        try {
            session = User.load();
            adapter.notifyDataSetChanged();
            listview.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save the user when activity is in pause state
     */
    @Override
    public void onPause(){
        super.onPause();

        try {
            User.save(session);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resume the application and load the user data back
     */
    @Override
    public void onResume() {
        super.onResume();

        try {
            session = User.load();
            adapter.notifyDataSetChanged();
            listview.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Display an alert dialog to allow user to add a new album
     * @param view
     */
    public void addAlbum(View view){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Enter the name of the album: ");

        final EditText input = new EditText(this);
        alertDialogBuilder.setView(input);
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //close the alert dialog
            }
        });
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            /**
             * Save the album to the arraylist and update the list after clicking OK
             * @param dialog
             * @param which
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String albumname = input.getText().toString().trim();
                if(albumname.isEmpty() || albumname == null){
                    Context context = getApplicationContext();
                    CharSequence text = "Field cannot be blank";
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(context, text, duration).show();
                    return;
                }

                if(session.checkAlbumExists(albumname)){
                    Context context = getApplicationContext();
                    CharSequence text = "Album already exists!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(context, text, duration).show();
                    return;
                }

                try {
                    User.save(session);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Album album = new Album(albumname);
                session.addAlbumToList(album);

                try {
                    User.save(session);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                update();
                adapter.notifyDataSetChanged();
//                etInput.setText("");
            }
        });
        alertDialogBuilder.show();
    }

    /**
     * Update method to refresh the list
     */
    public void update(){
        albumnames.clear();
        for(int i=0; i <session.getAlbums().size(); i++){
            String albumname = session.getAlbums().get(i).getAlbumName();
            albumnames.add(albumname);
        }
    }
}
