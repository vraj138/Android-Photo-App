package com.example.photolibraryapp;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import model.Photo;

public class SlideshowViewActivity extends AppCompatActivity {

    public ImageButton prev, next;
    public ImageView imageView;
    public int currentindex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow_view);

        prev = (ImageButton) findViewById(R.id.previous);
        next = (ImageButton) findViewById(R.id.next);
        imageView = (ImageView) findViewById(R.id.slideshowImage);

        final int end = MainActivity.session.getCurrentAlbum().getAllPhotos().size();

        currentindex = 0;

        openFirstImage();

        next.setOnClickListener(new View.OnClickListener() {
            /**
             * Displays the next image in album
             * @param view
             */
            @Override
            public void onClick(View view) {
                currentindex++;

                if(currentindex == end){
                    currentindex = 0;
                }

                Photo photo = MainActivity.session.getCurrentAlbum().getAllPhotos().get(currentindex);
                Uri uri = Uri.parse(photo.getFilePath());
                imageView.setImageURI(uri);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * Displays the previous image in album
             */
            public void onClick(View view) {
                currentindex--;
                if(currentindex == -1){
                    currentindex = end - 1;
                }
                Photo photo = MainActivity.session.getCurrentAlbum().getAllPhotos().get(currentindex);
                Uri u = Uri.parse(photo.getFilePath());
                imageView.setImageURI(u);
            }
        });
    }

    /**
     * Display the first image of album
     */
    public void openFirstImage(){
        Uri u = Uri.parse(MainActivity.session.getCurrentAlbum().getAllPhotos().get(0).getFilePath());
        imageView.setImageURI(u);
    }
}
