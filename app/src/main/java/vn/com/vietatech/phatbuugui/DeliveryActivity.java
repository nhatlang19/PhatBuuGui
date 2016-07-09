package vn.com.vietatech.phatbuugui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.ScannerNotClaimedException;
import com.honeywell.aidc.ScannerUnavailableException;
import com.honeywell.aidc.TriggerStateChangeEvent;
import com.honeywell.aidc.UnsupportedPropertyException;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.com.vietatech.dao.DeliveryDataSource;
import vn.com.vietatech.dto.Delivery;
import vn.com.vietatech.dto.User;
import vn.com.vietatech.lib.ConfigUtils;
import vn.com.vietatech.lib.Utils;
import vn.com.vietatech.phatbuugui.adapter.ViewPagerAdapter;
import vn.com.vietatech.phatbuugui.fragment.DeliveryFragment;
import vn.com.vietatech.phatbuugui.fragment.NoDeliveryFragment;

public class DeliveryActivity extends AppCompatActivity implements BarcodeReader.BarcodeListener,
        BarcodeReader.TriggerListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private EditText txtCode;
    private CheckBox cbBatch;

    private Context context = this;

    private static String temlateTitle = "Mới %d/Tổng %d";

    public static final int REQUEST_CODE_VIEW_LIST = 1;
    public static final int REQUEST_CODE_SCAN_BIG = 2;

    private BarcodeReader barcodeReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        updateTitle();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        txtCode = (EditText) findViewById(R.id.txtCode);
        cbBatch = (CheckBox) findViewById(R.id.cbBatch);

        txtCode.setFocusable(false);
        txtCode.setEnabled(false);
        txtCode.setBackgroundColor(Color.parseColor("#FFFFFF"));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        barcodeInstance();
    }

    public void barcodeInstance() {
        // get bar code instance from MainActivity
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_UP) {
            final View view = getCurrentFocus();

            if(view != null) {
                final boolean consumed = super.dispatchTouchEvent(ev);

                final View viewTmp = getCurrentFocus();
                final View viewNew = viewTmp != null ? viewTmp : view;

                if(viewNew.equals(view)) {
                    final Rect rect = new Rect();
                    final int[] coordinates = new int[2];

                    view.getLocationOnScreen(coordinates);

                    rect.set(coordinates[0], coordinates[1], coordinates[0] + view.getWidth(), coordinates[1] + view.getHeight());

                    final int x = (int) ev.getX();
                    final int y = (int) ev.getY();

                    if(rect.contains(x, y)) {
                        return consumed;
                    }
                }
                else if(viewNew instanceof EditText) {
                    return consumed;
                }

                final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(viewNew.getWindowToken(), 0);

                viewNew.clearFocus();

                return consumed;
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    private void updateTitle() {
        DeliveryDataSource ds = DeliveryDataSource.getInstance(context);
        ds.open();
        List<Delivery> deliveries = ds.getAllDeliveries();
        ds.close();

        int count = 0;
        for(Delivery delivery : deliveries)
        {
            if(delivery.getUpload().equals(Delivery.UNUPLOADED)) {
                count++;
            }
        }

        setTitle(String.format(temlateTitle, count, deliveries.size()));
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DeliveryFragment(),  this.getString(R.string.delivery));
        adapter.addFragment(new NoDeliveryFragment(), this.getString(R.string.no_delivery));
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.delivery_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.btnShowMap:
                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                break;
            case R.id.btnSaveDelivery:
                this.save();
                break;
            case R.id.btnQuetSoLuongLon:
                Intent intentScan = new Intent(this, ScanBigDeliveryActivity.class);
                startActivityForResult(intentScan, REQUEST_CODE_SCAN_BIG);
                break;
            case R.id.btnXemDanhSach:
                Intent intentList = new Intent(this, DeliveryListActivity.class);
                startActivityForResult(intentList, REQUEST_CODE_VIEW_LIST);
                break;
            case R.id.btnXoaBuuGui:
                this.delete();
                break;
            case R.id.btnUpload:
                this.upload();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void delete() {
        if(txtCode.getText().toString().trim().length() == 0) {
            Utils.showAlert(context, this.getString(R.string.delete_error_no_code));
            return ;
        }

        DeliveryDataSource ds = DeliveryDataSource.getInstance(context);
        ds.open();
        Delivery delivery = new Delivery();
        delivery.setItemCode(txtCode.getText().toString().trim());
        ds.deleteDelivery(delivery);
        ds.close();
        Utils.showAlert(context, this.getString(R.string.delete_delivery_success) + " " + delivery.getItemCode());

        clearAllView();
        updateTitle();
    }

    protected void upload() {
        Utils.showAlert(context, "Test Uploaded");
    }

    /**
     * save delivery
     */
    protected void save() {
        int position = viewPager.getCurrentItem();
        ViewPagerAdapter adapter = (ViewPagerAdapter)viewPager.getAdapter();

        if(txtCode.getText().toString().trim().length() == 0) {
            Utils.showAlert(context, this.getString(R.string.error_no_code));
            return ;
        }

        Delivery delivery;
        if (position == 0) {
            delivery = ((DeliveryFragment)adapter.getItem(position)).getData ();
            if(delivery.getRelateWithReceive().equals(Delivery.STATUS_KHAC)) {
                if(delivery.getRealReciverName().trim().length() == 0) {
                    Utils.showAlert(context, this.getString(R.string.error_no_name));
                    return ;
                }
            }
        } else {
            delivery = ((NoDeliveryFragment)adapter.getItem(position)).getData ();
        }

        delivery.setItemCode(txtCode.getText().toString());
        if(cbBatch.isChecked()) {
            delivery.setBatchDelivery(Delivery.BATCH);
        }

        // get code
        ConfigUtils utils = ConfigUtils.getInstance(this);
        String code = utils.displaySharedPreferences().getCode();
        delivery.setToPOSCode(code);

        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateString = sdf.format(date);
        delivery.setDeliveryDate(dateString);

        MyApplication globalVariable = (MyApplication) this.getApplication();
        User user = globalVariable.getUser();
        delivery.setDeliveryUser(user.getUsername());

        DeliveryDataSource ds = DeliveryDataSource.getInstance(context);
        ds.open();
        if(!ds.existsDelivery(delivery)) {
            delivery = ds.createDelivery(delivery);
            Utils.showAlert(context, context.getString(R.string.save_delivery_success) + " " + delivery.getItemCode());
        } else {
            delivery = ds.updateDelivery(delivery);
            Utils.showAlert(context, context.getString(R.string.update_delivery_success) + " " + delivery.getItemCode());
        }
        ds.close();
        updateTitle();

        clearAllView();
    }

    /**
     * clear all text view, spinner, checkbox, radioBox to default
     */
    private void clearAllView() {
        int position = viewPager.getCurrentItem();
        ViewPagerAdapter adapter = (ViewPagerAdapter)viewPager.getAdapter();

        txtCode.getText().clear();
        cbBatch.setChecked(false);
        txtCode.setBackgroundColor(Color.parseColor("#FFFFFF"));
        if (position == 0) {
            ((DeliveryFragment)adapter.getItem(position)).clearView ();
        } else {
            ((NoDeliveryFragment)adapter.getItem(position)).clearView ();
        }
    }

    private void loadDelivery() {
        if(txtCode.getText().toString().trim().length() == 0) {
            return ;
        }
        String code = txtCode.getText().toString();
        viewPager.setCurrentItem(0);

        DeliveryDataSource ds = DeliveryDataSource.getInstance(context);
        ds.open();
        Delivery delivery = new Delivery();
        delivery.setItemCode(code);
        delivery = ds.getDelivery(delivery);
        ds.close();

        if(delivery != null) {
            if(delivery.getBatchDelivery().equals(Delivery.BATCH)) {
                cbBatch.setChecked(true);
            }
            ViewPagerAdapter adapter = (ViewPagerAdapter)viewPager.getAdapter();
            if(delivery.getIsDeliverable().equals(Delivery.PHAT_DUOC)) {
                ((DeliveryFragment)adapter.getItem(0)).setFields(delivery);
                viewPager.setCurrentItem(0);
            } else {
                ((NoDeliveryFragment)adapter.getItem(1)).setFields(delivery);
                viewPager.setCurrentItem(1);
            }
            txtCode.setBackgroundColor(Color.parseColor("#f7bc3c"));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check if the request code is same as what is passed  here it is 2
        if (requestCode == REQUEST_CODE_VIEW_LIST) {
            updateTitle();
        } else if(requestCode == REQUEST_CODE_SCAN_BIG) {
            clearAllView();
        }
    }

    @Override
    public void onBarcodeEvent(BarcodeReadEvent barcodeReadEvent) {

        final BarcodeReadEvent _event = barcodeReadEvent;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                clearAllView();
                txtCode.setText(_event.getBarcodeData());
                loadDelivery();
            }
        });
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(DeliveryActivity.this, "No data", Toast.LENGTH_SHORT).show();
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
}
