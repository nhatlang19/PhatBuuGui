package vn.com.vietatech.phatbuugui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import vn.com.vietatech.dto.Solution;

public class SolutionListAdapter extends ArrayAdapter<Solution>{

    // Your sent context
    private Context context;
    // Your custom values for the spinner (Session)
    private List<Solution> values;

    public SolutionListAdapter(Context context, int textViewResourceId, List<Solution> solutions) {
        super(context, textViewResourceId, solutions);
		setValues(solutions);
		this.context = context;

	}

	public int getCount() {
		return getValues().size();
	}

	public Solution getItem(int position) {
		return getValues().get(position);
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
		label.setText(getValues().get(position).getName().trim());
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
		label.setText(getValues().get(position).getName().trim());
		return label;
	}

    public List<Solution> getValues() {
        return values;
    }

    public void setValues(List<Solution> values) {
        this.values = values;
    }
}
