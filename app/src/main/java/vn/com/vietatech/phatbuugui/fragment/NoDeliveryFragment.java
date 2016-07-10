package vn.com.vietatech.phatbuugui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import vn.com.vietatech.dto.Delivery;
import vn.com.vietatech.dto.Reason;
import vn.com.vietatech.dto.Solution;
import vn.com.vietatech.phatbuugui.R;
import vn.com.vietatech.phatbuugui.adapter.GiayToListAdapter;
import vn.com.vietatech.phatbuugui.adapter.ReasonListAdapter;
import vn.com.vietatech.phatbuugui.adapter.SolutionListAdapter;

public class NoDeliveryFragment extends Fragment implements IFragment{
    private Spinner spinReason;
    private Spinner spinSolution;
    private CheckBox cbPhatHoan;

    private ReasonListAdapter reasonListAdapter;
    private SolutionListAdapter solutionListAdapter;

    private String solutionCode;

    public NoDeliveryFragment() {
        super();
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

        reasonListAdapter = new ReasonListAdapter(this.getContext(),  android.R.layout.simple_spinner_item);
        spinReason.setAdapter(reasonListAdapter);
        spinReason.setSelection(0);

        final Context context = this.getContext();
        spinReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                List<Solution> solutions = ((Reason) spinReason.getSelectedItem()).getSolutions();
                solutionListAdapter = new SolutionListAdapter(context,  android.R.layout.simple_spinner_item, solutions);
                spinSolution.setAdapter(solutionListAdapter);

                if(solutionCode != null && solutionCode.length() != 0) {
                    int indexSolution = solutionListAdapter.getItemIndexByCode(solutionCode);
                    spinSolution.setSelection(indexSolution);
                } else {
                    spinSolution.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return view;
    }

    @Override
    public Delivery getData() {
        Delivery _delivery = new Delivery();

        Reason reason = reasonListAdapter.getItem(spinReason.getSelectedItemPosition());
        _delivery.setCauseCode(String.valueOf(reason.getId()));

        Solution solution = solutionListAdapter.getItem(spinSolution.getSelectedItemPosition());
        _delivery.setSolutionCode(String.valueOf(solution.getId()));

        _delivery.setIsDeliverable(Delivery.KHONG_PHAT_DUOC);

        if(cbPhatHoan.isChecked()) {
            _delivery.setDeliveryReturn(Delivery.PHAT_HOAN);
        }
        return _delivery;
    }

    @Override
    public void clearView() {
        spinReason.setSelection(0);
        spinSolution.setSelection(0);
        cbPhatHoan.setSelected(false);
    }

    @Override
    public void setFields(Delivery delivery) {
        if(delivery.getDeliveryReturn().equals(Delivery.PHAT_HOAN)) {
            cbPhatHoan.setChecked(true);
        }

        String causeCode = delivery.getCauseCode();
        int indexReason = reasonListAdapter.getItemIndexByCode(causeCode);
        spinReason.setSelection(indexReason, true);

        solutionCode = delivery.getSolutionCode();
    }
}
