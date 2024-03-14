package model;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import java.util.ArrayList;
import com.example.photolibraryapp.R;

public class Adapter extends BaseAdapter {

    public Context mContext;
    public ArrayList<Photo> photolist;
    private View view;
    private LayoutInflater layoutInflater;


    public Adapter(Context mContext, ArrayList<Photo> photolist){
        this.mContext = mContext;
        this.photolist = photolist;
    }

    @Override
    public int getCount() {
        return photolist.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            view = new View(mContext);
            view = layoutInflater.inflate(R.layout.activity_photo, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
            Uri u = Uri.parse(photolist.get(pos).getFilePath());
            imageView.setImageURI(u);

        } else {

        }
        return view;
    }
}
