package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {

        public String albumName;

        public ArrayList<Photo> photosList;

        public Photo currPhoto;

        public int numOfPhotos;

        public Album(String albumName){
                this.albumName = albumName;
                photosList = new ArrayList<Photo>();
        }

        public void addPhoto(Photo photo){
                photosList.add(photo);
                numOfPhotos++;
        }

        public void addPhoto(String filepath){
                Photo photo = new Photo(filepath);
                photosList.add(photo);
                numOfPhotos++;
        }

        public void deletePhoto(int index){
                photosList.remove(index);
                numOfPhotos--;
        }

        public String getAlbumName() {
                return albumName;
        }

        public void setAlbumName(String albumName) {
                this.albumName = albumName;
        }

        public ArrayList<Photo> getAllPhotos() {
                return photosList;
        }

        public Photo getCurrPhoto() {
                return currPhoto;
        }

        public void setCurrPhoto(Photo currentPhoto) {
                this.currPhoto = currentPhoto;
        }

        public int getNumOfPhotos() {
                return numOfPhotos;
        }

}
