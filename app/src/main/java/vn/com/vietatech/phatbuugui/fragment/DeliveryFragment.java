package vn.com.vietatech.phatbuugui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.io.File;

import vn.com.vietatech.dto.City;
import vn.com.vietatech.dto.Delivery;
import vn.com.vietatech.dto.GiayTo;
import vn.com.vietatech.lib.Utils;
import vn.com.vietatech.phatbuugui.GPSTracker;
import vn.com.vietatech.phatbuugui.R;
import vn.com.vietatech.phatbuugui.SignatureActivity;
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
    private EditText txtGiaTien;
    private Button btnSign;
    private ImageView signImage;

    private NoiCapListAdapter noiCapListAdapter;
    private GiayToListAdapter tableListAdapter;

    public DeliveryFragment() {
        // Required empty public constructor
        super();
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
        txtGiaTien = (EditText) view.findViewById(R.id.txtPrice);
        btnSign = (Button) view.findViewById(R.id.btnSign);
        signImage = (ImageView) view.findViewById(R.id.imageView2);

        tableListAdapter = new GiayToListAdapter(this.getContext(),
                android.R.layout.simple_spinner_item);
        spinnerGiayTo.setAdapter(tableListAdapter);
        spinnerGiayTo.setSelection(tableListAdapter.getCount());

        noiCapListAdapter = new NoiCapListAdapter(this.getContext(),
                android.R.layout.simple_spinner_item);
        spinnerNoiCap.setAdapter(noiCapListAdapter);
        spinnerNoiCap.setSelection(noiCapListAdapter.getCount());

        // txtNgayCap click
        txtNgayCap.setClickable(true);
        final LayoutInflater layoutInflater = inflater;
        txtNgayCap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadDatePickerDialog(layoutInflater);
            }
        });


        final Context context = this.getContext();
        btnSign.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SignatureActivity.class);
                startActivityForResult(intent, SignatureActivity.GET_IMAGE);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();

        if (requestCode == SignatureActivity.GET_IMAGE) {
            if(resultCode == Activity.RESULT_OK){
                String signImageStr = data.getStringExtra("signImage");
                File photo = new File(Utils.getAlbumStorageDir("SignaturePad"), signImageStr);
                Uri contentUri = Uri.fromFile(photo);
                signImage.setImageURI(contentUri);
            }
        }
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
        Delivery _delivery = new Delivery();
        _delivery.setDeliveryCertificateName(String.valueOf(((GiayTo)spinnerGiayTo.getSelectedItem()).getName()));
        _delivery.setDeliveryCertificateNumber(txtSoGiayTo.getText().toString());
        _delivery.setDeliveryCertificateDateOfIssue(txtNgayCap.getText().toString());
        _delivery.setDeliveryCertificatePlaceOfIssue(String.valueOf(((City)spinnerNoiCap.getSelectedItem()).getDesc()));
        _delivery.setIsDeliverable(Delivery.PHAT_DUOC);
        if(txtGiaTien.getText().toString().trim().length() != 0) {
            _delivery.setPrice(txtGiaTien.getText().toString());
        }

        GPSTracker gps = new GPSTracker(this.getContext());
        // check if GPS enabled
        if(gps.canGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            _delivery.setLatitude(String.valueOf(latitude));
            _delivery.setLongtitude(String.valueOf(longitude));
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        if(rbPhatHoan.isChecked()) {
            _delivery.setDeliveryReturn(Delivery.PHAT_HOAN);
        }

        if(rbDuongSu.isChecked()) {
            _delivery.setRelateWithReceive(Delivery.STATUS_DUONGSU);
        }

        if(rbKhac.isChecked()) {
            _delivery.setRelateWithReceive(Delivery.STATUS_KHAC);
        }

        _delivery.setRealReciverName(txtHoten.getText().toString());
        return _delivery;
    }

    @Override
    public void clearView() {
        txtHoten.getText().clear();
        rbDuongSu.setSelected(true);
        spinnerGiayTo.setSelection(0);
        txtSoGiayTo.getText().clear();
        txtNgayCap.getText().clear();
        spinnerNoiCap.setSelection(0);
        rbPhatHoan.setSelected(false);
        txtGiaTien.getText().clear();
    }

    @Override
    public void setFields(Delivery delivery) {
        txtHoten.setText(delivery.getRealReciverName());

        if(delivery.getRelateWithReceive().equals(Delivery.STATUS_DUONGSU)) {
            rbDuongSu.setChecked(true);
        } else {
            rbKhac.setChecked(true);
        }

        txtSoGiayTo.setText(delivery.getDeliveryCertificateNumber());
        txtNgayCap.setText(delivery.getDeliveryCertificateDateOfIssue());

        int indexGiayTo = tableListAdapter.getItemIndexByName(delivery.getDeliveryCertificateName());
        spinnerGiayTo.setSelection(indexGiayTo);

        int indexNoiCap = noiCapListAdapter.getItemIndexByDesc(delivery.getDeliveryCertificatePlaceOfIssue());
        spinnerNoiCap.setSelection(indexNoiCap);

        if(delivery.getDeliveryReturn().equals(Delivery.PHAT_HOAN)) {
            rbPhatHoan.setChecked(true);
        }
    }
}
