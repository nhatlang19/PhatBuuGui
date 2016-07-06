package vn.com.vietatech.phatbuugui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.ArrayList;

import vn.com.vietatech.dto.Delivery;
import vn.com.vietatech.phatbuugui.R;
import vn.com.vietatech.phatbuugui.adapter.GiayToListAdapter;
import vn.com.vietatech.phatbuugui.adapter.NoiCapListAdapter;

public class DeliveryFragment extends Fragment implements IFragment  {

    private EditText txtHoten;
    private RadioButton rbDuongSu;
    private RadioButton rbKhac;
    private Spinner spinnerGiayTo;
    private EditText txtSoGiayTo;
    private EditText txtNgayCap;
    private Spinner spinnerNoiCap;
    private CheckBox rbPhatHoan;

    public DeliveryFragment() {
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
        View view = inflater.inflate(R.layout.fragment_delivery, container, false);

        txtHoten = (EditText) view.findViewById(R.id.txtHoTen);
        rbDuongSu = (RadioButton) view.findViewById(R.id.rbDuongSu);
        rbKhac = (RadioButton) view.findViewById(R.id.rbKhac);
        spinnerGiayTo = (Spinner) view.findViewById(R.id.spinnerGiayTo);
        txtSoGiayTo = (EditText) view.findViewById(R.id.txtSoGiayTo);
        txtNgayCap = (EditText) view.findViewById(R.id.txtNgayCap);
        spinnerNoiCap = (Spinner) view.findViewById(R.id.spinnerNoiCap);
        rbPhatHoan = (CheckBox) view.findViewById(R.id.rbPhatHoan1);

        GiayToListAdapter tableListAdapter = new GiayToListAdapter(this.getContext(),
                android.R.layout.simple_spinner_item);
        spinnerGiayTo.setAdapter(tableListAdapter);

        NoiCapListAdapter noiCapListAdapter = new NoiCapListAdapter(this.getContext(),
                android.R.layout.simple_spinner_item);
        spinnerNoiCap.setAdapter(noiCapListAdapter);

        // txtNgayCap click
        txtNgayCap.setClickable(true);
        final LayoutInflater layoutInflater = inflater;
        txtNgayCap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadDatePickerDialog(layoutInflater);
            }
        });
        return view;
    }

    protected void loadDatePickerDialog(LayoutInflater layoutInflater) {
        View promptView = layoutInflater.inflate(R.layout.date_picker_dialog,
                null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this.getContext());

        final DatePicker datePicker = (DatePicker) promptView
                .findViewById(R.id.npDate);
        String dateString = txtNgayCap.getText().toString().trim();
        if(!dateString.isEmpty()) {
            String[] dates = dateString.split("/");
            datePicker.init(Integer.parseInt(dates[2]), Integer.parseInt(dates[1]) - 1, Integer.parseInt(dates[0]), null);
        }

        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Chọn ngày");
        alertDialogBuilder.setPositiveButton("Chọn",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int month = datePicker.getMonth();
                        int day = datePicker.getDayOfMonth();
                        int year = datePicker.getYear();
                        txtNgayCap.setText(new StringBuilder().append(day)
                                .append("/").append(month + 1).append("/").append(year)
                                .append(" "));
                        dialog.cancel();
                    }
                }).setNegativeButton("Tắt",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
    }

    @Override
    public Delivery getData() {
//        return null;

//
//
//        rbDuongSu = (RadioButton) view.findViewById(R.id.rbDuongSu);
//        rbKhac = (RadioButton) view.findViewById(R.id.rbKhac);
//        spinnerGiayTo = (Spinner) view.findViewById(R.id.spinnerGiayTo);
//        txtSoGiayTo = (EditText) view.findViewById(R.id.txtSoGiayTo);
//        txtNgayCap = (EditText) view.findViewById(R.id.txtNgayCap);
//        spinnerNoiCap = (Spinner) view.findViewById(R.id.spinnerNoiCap);
//        rbPhatHoan = (CheckBox) view.findViewById(R.id.rbPhatHoan1);

        Delivery _delivery = new Delivery();
        _delivery.setDeliveryCertificateName(spinnerGiayTo.getSelectedItem().toString());
        _delivery.setDeliveryCertificateNumber(txtSoGiayTo.getText().toString());
        _delivery.setDeliveryCertificateDateOfIssue(txtNgayCap.getText().toString());
        _delivery.setDeliveryCertificatePlaceOfIssue(spinnerNoiCap.getSelectedItem().toString());
        _delivery.setIsDeliverable("1");

        return _delivery;

//        _delivery.setItemCode(cursor.getString(indexItemCode));
//        _delivery.setToPOSCode(cursor.getString(indexPosCode));
//        _delivery.setDeliveryDate(cursor.getString(indexDeliveryDate));
//        _delivery.setRelateWithReceive(cursor.getString(indexRelateWithReceive));
//        _delivery.setRealReciverName(cursor.getString(indexRealReceverName));
//        _delivery.setRealReceiverIdentification(cursor.getString(indexRealReceivertIdent));
//        _delivery.setDeliveryUser(cursor.getString(indexDeliveryUser));
//        _delivery.setBatchDelivery(cursor.getString(indexBatchDelivery));
//        _delivery.setUpload(cursor.getString(indexUpload));
    }
}
