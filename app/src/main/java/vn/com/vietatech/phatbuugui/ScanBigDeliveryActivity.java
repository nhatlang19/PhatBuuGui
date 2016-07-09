package vn.com.vietatech.phatbuugui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;
import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.ScannerNotClaimedException;
import com.honeywell.aidc.ScannerUnavailableException;
import com.honeywell.aidc.TriggerStateChangeEvent;
import com.honeywell.aidc.UnsupportedPropertyException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.com.vietatech.dto.Delivery;
import vn.com.vietatech.lib.Utils;
import vn.com.vietatech.phatbuugui.adapter.ListViewScanAdapter;

public class ScanBigDeliveryActivity extends AppCompatActivity implements BarcodeReader.BarcodeListener,
        BarcodeReader.TriggerListener {
    private ListView mListView;
    private EditText txtCode;
    private static ListViewScanAdapter mAdapter;

    private static List<String> listCodes;

    private Context mContext;

    private static BarcodeReader barcodeReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_big);

        mContext = this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mListView = (ListView) findViewById(R.id.listScanBig);
        txtCode = (EditText) findViewById(R.id.txtCodeList);

        txtCode.setFocusable(false);
        txtCode.setEnabled(false);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        if(null == mAdapter) {
            mAdapter = new ListViewScanAdapter(this, listCodes);
            listCodes = new ArrayList<>();
        } else {
            mContext = this;
            mAdapter.setContext(mContext);
            mAdapter.setListCodes(listCodes);
        }
        mListView.setAdapter(mAdapter);
        mAdapter.setMode(Attributes.Mode.Single);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((SwipeLayout)(mListView.getChildAt(position - mListView.getFirstVisiblePosition()))).open(true);
            }
        });
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("ListView", "OnTouch");
                return false;
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return true;
            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.e("ListView", "onScrollStateChanged");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("ListView", "onItemSelected:" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("ListView", "onNothingSelected:");
            }
        });

        barcodeInstance();
    }

    public void barcodeInstance() {
        // get bar code instance from MainActivity
        if (barcodeReader == null) {
            barcodeReader = FullscreenActivity.getBarcodeObject();
            if (barcodeReader != null) {

                // register bar code event listener
                barcodeReader.addBarcodeListener(this);

                // set the trigger mode to client control
                try {
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
                            BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL);
                } catch (UnsupportedPropertyException e) {
                    Toast.makeText(this, "Failed to apply properties", Toast.LENGTH_SHORT).show();
                }
                // register trigger state change listener
                barcodeReader.addTriggerListener(this);

                Map<String, Object> properties = new HashMap<String, Object>();
                // Set Symbologies On/Off
                properties.put(BarcodeReader.PROPERTY_CODE_128_ENABLED, true);
                properties.put(BarcodeReader.PROPERTY_GS1_128_ENABLED, true);
                properties.put(BarcodeReader.PROPERTY_QR_CODE_ENABLED, true);
                properties.put(BarcodeReader.PROPERTY_CODE_39_ENABLED, true);
                properties.put(BarcodeReader.PROPERTY_DATAMATRIX_ENABLED, true);
                properties.put(BarcodeReader.PROPERTY_UPC_A_ENABLE, true);
                properties.put(BarcodeReader.PROPERTY_EAN_13_ENABLED, false);
                properties.put(BarcodeReader.PROPERTY_AZTEC_ENABLED, false);
                properties.put(BarcodeReader.PROPERTY_CODABAR_ENABLED, false);
                properties.put(BarcodeReader.PROPERTY_INTERLEAVED_25_ENABLED, false);
                properties.put(BarcodeReader.PROPERTY_PDF_417_ENABLED, false);
                // Set Max Code 39 barcode length
                properties.put(BarcodeReader.PROPERTY_CODE_39_MAXIMUM_LENGTH, 10);
                // Turn on center decoding
                properties.put(BarcodeReader.PROPERTY_CENTER_DECODE, true);
                // Enable bad read response
                properties.put(BarcodeReader.PROPERTY_NOTIFICATION_BAD_READ_ENABLED, true);
                // Apply the settings
                barcodeReader.setProperties(properties);
            }
        }
    }

    @Override
    public void onBarcodeEvent(BarcodeReadEvent barcodeReadEvent) {
        final BarcodeReadEvent _event = barcodeReadEvent;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtCode.setText(_event.getBarcodeData());

                if(listCodes.indexOf(_event.getBarcodeData()) == -1) {
                    Delivery _delivery = new Delivery();
                    _delivery.setItemCode(_event.getBarcodeData());
                    mAdapter.getDeliveries().add(_delivery);
                    mAdapter.notifyDataSetChanged();

                    listCodes.add(_event.getBarcodeData());
                } else {
                    Utils.showAlert(ScanBigDeliveryActivity.this, mContext.getString(R.string.delivery_same) );
                }
            }
        });
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(ScanBigDeliveryActivity.this, "No data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent triggerStateChangeEvent) {
        try {
            // only handle trigger presses
            // turn on/off aimer, illumination and decoding
            barcodeReader.aim(triggerStateChangeEvent.getState());
            barcodeReader.light(triggerStateChangeEvent.getState());
            barcodeReader.decode(triggerStateChangeEvent.getState());

        } catch (ScannerNotClaimedException e) {
            e.printStackTrace();
            Toast.makeText(this, "Scanner is not claimed", Toast.LENGTH_SHORT).show();
        } catch (ScannerUnavailableException e) {
            e.printStackTrace();
            Toast.makeText(this, "Scanner unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (barcodeReader != null) {
            try {
                barcodeReader.claim();
            } catch (ScannerUnavailableException e) {
                e.printStackTrace();
                Toast.makeText(this, "Scanner unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (barcodeReader != null) {
            // release the scanner claim so we don't get any scanner
            // notifications while paused.
            barcodeReader.release();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (barcodeReader != null) {
//            barcodeReader.release();
            // unregister barcode event listener
//            barcodeReader.removeBarcodeListener(this);

            // unregister trigger state change listener
//            barcodeReader.removeTriggerListener(this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }
}
