package vn.com.vietatech.phatbuugui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.vietatech.dao.SolutionsDataSource;
import vn.com.vietatech.dto.Solution;

public class SolutionListAdapter extends ArrayAdapter<Solution>{

    // Your sent context
    private Context context;
    // Your custom values for the spinner (Session)
    private ArrayList<Solution> values;

    public SolutionListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
		this.context = context;


		if(null == values) {
			SolutionsDataSource ds = SolutionsDataSource.getInstance(context);
			ds.open();
			this.values = (ArrayList<Solution>) ds.getAllSolutions();
			ds.close();
		}
	}

	public int getCount() {
		return values.size();
	}

	public Solution getItem(int position) {
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
