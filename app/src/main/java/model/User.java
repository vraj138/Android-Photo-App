package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    public ArrayList<Album> albumsList;

    public Album currAlbum;

    public int numOfAlbums;

    public User(){
        albumsList = new ArrayList<Album>();
        this.currAlbum = null;
        numOfAlbums = 0;
    }

    public void addAlbumToList(Album name){
        albumsList.add(name);
        numOfAlbums++;
    }

    public void deleteAlbum(int index){
        albumsList.remove(index);
        numOfAlbums--;
    }

    public boolean checkAlbumExists(String albumname){
        for(Album album : albumsList){
            if(album.getAlbumName().equals(albumname)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Album> getAlbums() {
        return albumsList;
    }

    public Album getCurrentAlbum() {
        return currAlbum;
    }

    public void setCurrentAlbum(Album currentAlbum) {
        this.currAlbum = currentAlbum;
    }

    public int getNumberOfAlbums() {
        return numOfAlbums;
    }

    public ArrayList<Photo> searchForAllTags(String tagvalue){
        ArrayList<Photo> photolist = new ArrayList<Photo>();
        //Used to make sure no duplicates
        HashSet<Photo> photosSet = new HashSet<Photo>();
        for(Album album : albumsList) {
            for(Photo photo : album.getAllPhotos()) {
                for (Tag tag : photo.getTaglist()){
                    System.out.println(tagvalue);
                    System.out.println(tag.value.toLowerCase().contains(tagvalue.toLowerCase()));
                    if(tag.value.toLowerCase().contains(tagvalue.toLowerCase())){
                        photosSet.add(photo);
                    }
                }
            }
        }

        photolist.addAll(photosSet);
        return photolist;
    }

    public ArrayList<Photo> searchForCertainTags(String tagkey, String tagvalue){
        ArrayList<Photo> photolist = new ArrayList<Photo>();
        //Used to make sure no duplicates
        HashSet<Photo> photosSet = new HashSet<Photo>();
        for(Album album : albumsList) {
            for(Photo photo : album.getAllPhotos()) {
                for (Tag tag : photo.getTaglist()){

                    System.out.println(tagvalue);
                    System.out.println(tag.value.toLowerCase().contains(tagvalue.toLowerCase()));
                    if(tag.value.toLowerCase().contains(tagvalue.toLowerCase()) && tag.key.toLowerCase().contains(tagkey.toLowerCase())){
                        photosSet.add(photo);
                    }
                }
            }

        }

        photolist.addAll(photosSet);
        return photolist;
    }

    public static void save(User pdApp) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("/data/data/com.example.photolibraryapp/files/data.dat"));
        oos.writeObject(pdApp);
        oos.close();
    }

    public static User load() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("//data/data/com.example.photolibraryapp/files/data.dat"));
        User userList = (User) ois.readObject();
        ois.close();
        return userList;

    }
}
