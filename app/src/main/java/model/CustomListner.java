package model;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AdapterView;
import androidx.appcompat.widget.AppCompatSpinner;

public class CustomListner extends AppCompatSpinner {
    AdapterView.OnItemSelectedListener listener;

    public CustomListner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
        if (listener != null)
            listener.onItemSelected(null, null, position, 0);
    }

    public void setOnItemSelectedEvenIfUnchangedListener(
            AdapterView.OnItemSelectedListener listener) {
        this.listener = listener;
    }
}