package vn.com.vietatech.phatbuugui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import vn.com.vietatech.dto.GiayTo;
import vn.com.vietatech.phatbuugui.R;

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
		this.values.add(new GiayTo(5, "Thẻ sinh viên"));
		this.values.add(new GiayTo(6, "[Chọn giấy tờ]"));
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
        int size = values.size();
        return size > 0 ? size - 1 : size;
	}

	public GiayTo getItem(int position) {
		return values.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.row, parent, false);

        // 3. Get the two text view from the rowView
        TextView labelView = (TextView) rowView.findViewById(R.id.label);
        labelView.setText(values.get(position).getName().trim());
        labelView.setPadding(5, 12, 5, 17);
        return rowView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.row, parent, false);

        // 3. Get the two text view from the rowView
        TextView labelView = (TextView) rowView.findViewById(R.id.label);
        labelView.setText(values.get(position).getName().trim());
        labelView.setPadding(5, 12, 5, 17);
        return rowView;
	}
}
