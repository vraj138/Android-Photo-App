package com.example.photolibraryapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import model.Photo;
import model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import model.Adapter;

public class AlbumViewActivity extends AppCompatActivity {

    public final int REQUEST_CODE = 0;
    public int pos;
    public GridView gridView;
    public ArrayList<Photo> photoList = new ArrayList<>();
    public Adapter imageAdapter;
    public FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_view);

        gridView = (GridView) findViewById(R.id.gridview_album);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton_album);
        imageAdapter = new Adapter(AlbumViewActivity.this, photoList);
        gridView.setAdapter(imageAdapter);
        registerForContextMenu(gridView);

        update();

        fab.setOnClickListener(new View.OnClickListener() {
            /**
             * By clicking the float-able button we execute adding photo to album
             * @param view
             */
            @Override
            public void onClick(View view) {
                Intent addPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                addPhoto.addCategory(Intent.CATEGORY_OPENABLE);
                addPhoto.setType("image/*");
                startActivityForResult(addPhoto, REQUEST_CODE);
            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Switch Scene to Single Photo activity
             * @param adapterView
             * @param view
             * @param index
             * @param l
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                Photo photo = MainActivity.session.getCurrentAlbum().getAllPhotos().get(index);
                MainActivity.session.getCurrentAlbum().setCurrPhoto(photo);
                Intent viewFullImage = new Intent(AlbumViewActivity.this, SinglePhotoActivity.class);
                viewFullImage.putExtra("index", index);
                startActivity(viewFullImage);
            }
        });
    }

    /**
     * Display the drop down menu by holding on any photo
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.photo_operations, menu);
    }

    /**
     * Execute the operation by selecting delete or move operations
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//        final int pos = (int) info.id;
        pos = info.position;
        System.out.println("===============================================" + pos);

        switch (item.getItemId()){
            case R.id.deletePhoto:
                MainActivity.session.getCurrentAlbum().deletePhoto(pos);
                try {
                    User.save(MainActivity.session);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                gridView = (GridView) findViewById(R.id.gridview_album);
                update();
                imageAdapter.notifyDataSetChanged();
                gridView.invalidateViews();
                gridView.setAdapter(imageAdapter);
                Toast.makeText(getApplicationContext(), "Photo Deleted", Toast.LENGTH_SHORT).show();

                return true;
            case R.id.movePhoto:
                final PopupMenu popupMenu = new PopupMenu(AlbumViewActivity.this, gridView);
                for(int i = 0; i < MainActivity.session.getAlbums().size(); i++){
                    popupMenu.getMenu().add(Menu.NONE, i, Menu.NONE,MainActivity.session.getAlbums().get(i).albumName);
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    /**
                     * Move the photo the appropriate album and update the gridview
                     * @param menuItem
                     * @return
                     */
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
//                        System.out.println(menuItem.getItemId());
                        Photo photo = MainActivity.session.getCurrentAlbum().getAllPhotos().get(pos);
                        MainActivity.session.getAlbums().get(menuItem.getItemId()).addPhoto(photo);
                        MainActivity.session.getCurrentAlbum().deletePhoto(pos);

                        try {
                            User.save(MainActivity.session);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        gridView = (GridView) findViewById(R.id.gridview_album);
                        update();
                        imageAdapter.notifyDataSetChanged();
                        gridView.invalidateViews();
                        gridView.setAdapter(imageAdapter);

                        return true;
                    }
                });
                popupMenu.show();

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * Display all the photos which are stored in the particular album
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && data != null){
            Uri u = null;
            if(data != null){
                u = data.getData();
            }
            String photopath = u.toString();
            MainActivity.session.getCurrentAlbum().addPhoto(photopath);

            try {
                User.save(MainActivity.session);
            } catch (IOException e) {
                e.printStackTrace();
            }
            gridView = (GridView) findViewById(R.id.gridview_album);
            update();

            imageAdapter.notifyDataSetChanged();
            gridView.setAdapter(imageAdapter);
        }
        else{

        }
    }

    /**
     * Update method to refresh the gridview after any operation
     */
    public void update(){
        photoList.clear();
        photoList.addAll(MainActivity.session.getCurrentAlbum().getAllPhotos());
    }

    /**
     * Display the menu bar with slide show icon
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.album_menu_slideshow, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Switch to the slideshow view activity only if there are photos in the album otherwise the alertdialog will popup
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.slideshow) {
            if(MainActivity.session.getCurrentAlbum().getAllPhotos().size() == 0){
                AlertDialog alertDialog = new AlertDialog.Builder(AlbumViewActivity.this).create();
                alertDialog.setTitle("Empty Album");
                alertDialog.setMessage("Add a photo to an album to view a slideshow");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            else {
                Intent goToSlideshow = new Intent(AlbumViewActivity.this, SlideshowViewActivity.class);
                startActivity(goToSlideshow);
                Toast.makeText(getApplicationContext(), "Slideshow", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}