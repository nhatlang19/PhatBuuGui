package vn.com.vietatech.phatbuugui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.vietatech.dto.City;
import vn.com.vietatech.dto.GiayTo;

public class GiayToListAdapter extends ArrayAdapter<GiayTo>{

    // Your sent context
    private Context context;
    // Your custom values for the spinner (Session)
    private ArrayList<GiayTo> values;

    public GiayToListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
		this.context = context;

		this.values = new ArrayList<>();
		this.values.add(new GiayTo(1, "CMND"));
		this.values.add(new GiayTo(2, "CMND quân đội"));
		this.values.add(new GiayTo(3, "Bằng lái xe"));
		this.values.add(new GiayTo(4, "Thẻ học sinh"));
		this.values.add(new GiayTo(4, "Thẻ sinh viên"));
	}

	public int getItemIndexByName(String name) {
		int index = 0;
		for(GiayTo giayto : values) {
			if(giayto.getName().equals(name)) {
				return index;
			}
			index++;
		}
		return -1;
	}

	public int getCount() {
		return values.size();
	}

	public GiayTo getItem(int position) {
		return values.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView label;
		
		if (convertView != null){
			label = (TextView) convertView;
        } else {
        	LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        	label = (TextView) inflater.inflate(
                    android.R.layout.simple_dropdown_item_1line, parent, false
            );
        }
        label.setTextSize(20);
		label.setText(values.get(position).getName().trim());
		return label;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		TextView label;
		if (convertView != null){
			label = (TextView) convertView;
        } else {
        	LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        	label = (TextView) inflater.inflate(
                    android.R.layout.simple_dropdown_item_1line, parent, false
            );
        }
        label.setTextSize(20);
		label.setText(values.get(position).getName().trim());
		return label;
	}
}
