package vn.com.vietatech.phatbuugui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import vn.com.vietatech.dto.Delivery;
import vn.com.vietatech.phatbuugui.R;
import vn.com.vietatech.phatbuugui.adapter.GiayToListAdapter;
import vn.com.vietatech.phatbuugui.adapter.ReasonListAdapter;
import vn.com.vietatech.phatbuugui.adapter.SolutionListAdapter;

public class NoDeliveryFragment extends Fragment implements IFragment{
    private Spinner spinReason;
    private Spinner spinSolution;
    private CheckBox cbPhatHoan;


    public NoDeliveryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nodelivery, container, false);

        spinReason = (Spinner) view.findViewById(R.id.spinReason);
        spinSolution = (Spinner) view.findViewById(R.id.spinSolution);
        cbPhatHoan = (CheckBox) view.findViewById(R.id.cbPhatHoan);

        ReasonListAdapter reasonListAdapter = new ReasonListAdapter(this.getContext(),  android.R.layout.simple_spinner_item);
        spinReason.setAdapter(reasonListAdapter);
        spinReason.setSelection(0);

        SolutionListAdapter solutionListAdapter = new SolutionListAdapter(this.getContext(),  android.R.layout.simple_spinner_item);
        spinSolution.setAdapter(solutionListAdapter);
        spinSolution.setSelection(0);

        return view;
    }

    @Override
    public Delivery getData() {
        return null;
    }
}
