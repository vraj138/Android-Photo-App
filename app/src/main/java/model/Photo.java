package model;

import java.io.Serializable;
import java.util.*;

public class Photo implements Serializable {

    private static final long serialVersionUID = 1L;

    public String filePath;

    public String fileName;

    public ArrayList<String> personList;

    public ArrayList<String> locationList;

    public ArrayList<Tag> tagList = new ArrayList<>();

    public Photo(String filepath){
        this.filePath = filepath;
        personList = new ArrayList<>();
        locationList = new ArrayList<>();
    }

    public void addNewTag(String name, String value){
        tagList.add(new Tag(name, value));
    }

    public void removeTag(int index){
        tagList.remove(index);
    }

    public void removeTag(String name, String value){
        for(int i = 0; i < tagList.size(); i++) {
            Tag cur = tagList.get(i);
            if(cur.key.toLowerCase().equals(name.toLowerCase()) && cur.value.toLowerCase().equals(value.toLowerCase())) {
                tagList.remove(i);
            }
        }
    }

    public boolean checkTagExists(String name, String value) {
        for(int i = 0; i < tagList.size(); i++) {
            Tag cur = tagList.get(i);
            if(cur.key.toLowerCase().equals(name.toLowerCase()) && cur.value.toLowerCase().equals(value.toLowerCase())) {
                return true;
            }
        }
        return false;

    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public ArrayList<String> getPersonList() {
        return personList;
    }

    public ArrayList<String> getLocationList() {
        return locationList;
    }

    public ArrayList<Tag> getTaglist() {
        return tagList;
    }
}
