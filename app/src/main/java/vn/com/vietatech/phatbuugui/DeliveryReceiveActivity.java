package vn.com.vietatech.phatbuugui;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.citizen.port.android.BluetoothPort;
import com.citizen.request.android.RequestHandler;
import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.ScannerNotClaimedException;
import com.honeywell.aidc.ScannerUnavailableException;
import com.honeywell.aidc.TriggerStateChangeEvent;
import com.honeywell.aidc.UnsupportedPropertyException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.com.vietatech.dao.DeliveryReceiveDataSource;
import vn.com.vietatech.dto.DeliveryReceive;
import vn.com.vietatech.lib.ConfigUtils;
import vn.com.vietatech.lib.InBuuTaThu;
import vn.com.vietatech.lib.UploadHandler;
import vn.com.vietatech.lib.Utils;
import vn.com.vietatech.phatbuugui.dialog.TransparentProgressDialog;

public class DeliveryReceiveActivity extends AppCompatActivity implements BarcodeReader.BarcodeListener,
        BarcodeReader.TriggerListener {

    private static final String TAG = "PrintBluetoothActivity";

    // Intent request codes
    // private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    private EditText txtCodeSend;
    private EditText txtSendName;
    private EditText txtSendIdentity;
    private EditText txtSendAddress;

    private BarcodeReader barcodeReader;

    private BluetoothAdapter mBluetoothAdapter;
    private Thread hThread;
    private static boolean connected;
    private Context context;
    // BT
    private BluetoothPort bp;

    private DeliveryReceive deliveryReceive;

    /**
     * Set up Bluetooth.
     */
    private void bluetoothSetup()
    {
        // Initialize
        bp = BluetoothPort.getInstance();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null)
        {
            // Device does not support Bluetooth
            return;
        }
        if (!mBluetoothAdapter.isEnabled())
        {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    // For the Desire Bluetooth close() bug.
    private void connectInit()
    {
        if(!mBluetoothAdapter.isEnabled())
        {
            mBluetoothAdapter.enable();
            try
            {
                Thread.sleep(3600);
            }
            catch(Exception e)
            {
                Log.e(TAG,e.getMessage(),e);
            }
        }
    }

    // Bluetooth Connection method.
    private void btConn(final BluetoothDevice btDev) throws IOException
    {
        new DeliveryReceiveActivity.connTask().execute(btDev);
    }

    /** ======================================================================= */
    public class connTask extends AsyncTask<BluetoothDevice, Void, Integer>
    {
        private final ProgressDialog dialog = new ProgressDialog(DeliveryReceiveActivity.this);

        @Override
        protected void onPreExecute()
        {
            dialog.setTitle("Bluetooth");
            dialog.setMessage("Connecting");
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(BluetoothDevice... params)
        {
            Integer retVal = null;
            try
            {
                bp.connect(params[0]);
                retVal = new Integer(0);
            }
            catch (IOException e)
            {
                retVal = new Integer(-1);
            }
            return retVal;
        }

        @Override
        protected void onPostExecute(Integer result)
        {
            if(result.intValue() == 0)
            {
                RequestHandler rh = new RequestHandler();
                hThread = new Thread(rh);
                hThread.start();
                connected = true;
                // UI
            }
            if(dialog.isShowing())
            {
                String cmsg = "Bluetooth Not Connected.";
                dialog.dismiss();
                if(connected)
                {
                    cmsg = "Bluetooth Connected";
                }
                Toast toast = Toast.makeText(context, cmsg, Toast.LENGTH_SHORT);
                toast.show();

                InBuuTaThu service = new InBuuTaThu();
                try {
                    service.print(deliveryReceive);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

//                btDisconn();
            }
            super.onPostExecute(result);
        }
    }
    /** ============================================================================ */

    private void btDisconn()
    {
        try
        {
            bp.disconnect();
            Thread.sleep(1200);
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getMessage(), e);
        }

        if((hThread != null) && (hThread.isAlive()))
            hThread.interrupt();
        connected = false;
        Toast toast = Toast.makeText(context, "Bluetooth Disconnected", Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Bluetooth connection status.
     * @return connected - boolean
     */
    public static boolean isConnected()
    {
        return connected;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_send);

        txtCodeSend = (EditText) findViewById(R.id.txtCodeSend);
        txtSendName = (EditText) findViewById(R.id.txtSendName);
        txtSendIdentity = (EditText) findViewById(R.id.txtSendIdentity);
        txtSendAddress = (EditText) findViewById(R.id.txtSendAddress);

//        txtCodeSend.setFocusable(false);
//        txtCodeSend.setEnabled(false);
        txtCodeSend.setBackgroundColor(Color.parseColor("#FFFFFF"));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        context = this;

        barcodeInstance();

        bluetoothSetup();
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
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            final View view = getCurrentFocus();

            if (view != null) {
                final boolean consumed = super.dispatchTouchEvent(ev);

                final View viewTmp = getCurrentFocus();
                final View viewNew = viewTmp != null ? viewTmp : view;

                if (viewNew.equals(view)) {
                    final Rect rect = new Rect();
                    final int[] coordinates = new int[2];

                    view.getLocationOnScreen(coordinates);

                    rect.set(coordinates[0], coordinates[1], coordinates[0] + view.getWidth(), coordinates[1] + view.getHeight());

                    final int x = (int) ev.getX();
                    final int y = (int) ev.getY();

                    if (rect.contains(x, y)) {
                        return consumed;
                    }
                } else if (viewNew instanceof EditText) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.delivery_send_menu, menu);
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
            case R.id.btnInBienNhan:
                this.inBienNhan();
                break;
            case R.id.btnSaveDeliverySend:
                this.save();
                break;
            case R.id.btnXemDanhSachSend:
                break;
            case R.id.btnXoaBuuGuiSend:
                break;
            case R.id.btnUploadSend:
                this.upload();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void upload()
    {
        TransparentProgressDialog pd = new TransparentProgressDialog(context, R.drawable.spinner);
        pd.show();
        try {
            DeliveryReceiveDataSource ds = DeliveryReceiveDataSource.getInstance(context);
            UploadHandler uploadHandler = UploadHandler.getInstance(context);

            ds.open();
            List<DeliveryReceive> lists = ds.getAllDeliveriesUnupload();
            ds.close();
            List<String> itemCodes;

            if (lists.isEmpty()) {
                Utils.showAlert(context, "Không có data bưu thu để upload");
            } else {
                itemCodes = uploadHandler.uploadReceive(context, lists);
                int size = itemCodes.size();
                ds.open();
                ds.updatesMulti(itemCodes);
                ds.close();

                if(size != 0) {
                    Utils.showAlert(context, "Upload thành công: " + size + " bưu thu");
                } else {
                    Utils.showAlert(context, "Có lỗi trong quá trình upload");
                }
            }
        } catch (Exception e) {
            Utils.showAlert(context, e.getMessage());
        } finally {
            pd.dismiss();
        }
    }

    private void save()
    {
        String code = txtCodeSend.getText().toString().trim();
        String name = txtSendName.getText().toString().trim();
        String identity = txtSendIdentity.getText().toString().trim();
        String address = txtSendAddress.getText().toString().trim();

        DeliveryReceive _deliveryReceive = new DeliveryReceive();
        _deliveryReceive.setItemCode(code);
        _deliveryReceive.setName(name);
        _deliveryReceive.setAddress(address);
        _deliveryReceive.setIdentity(identity);

        // get code
        ConfigUtils utils = ConfigUtils.getInstance(this);
        String _code = utils.displaySharedPreferences().getCode();
        _deliveryReceive.setToPOSCode(_code);

        DeliveryReceiveDataSource ds = DeliveryReceiveDataSource.getInstance(context);
        ds.open();

        if (!ds.existsDelivery(_deliveryReceive)) {
            _deliveryReceive = ds.createDelivery(_deliveryReceive);
            Utils.showAlert(context, context.getString(R.string.save_delivery_success) + " " + _deliveryReceive.getItemCode());
        } else {
            _deliveryReceive = ds.updateDelivery(_deliveryReceive);
            Utils.showAlert(context, context.getString(R.string.update_delivery_success) + " " + _deliveryReceive.getItemCode());
        }
        ds.close();

        txtCodeSend.setText("");
        txtSendName.setText("");
        txtSendIdentity.setText("");
        txtSendAddress.setText("");
    }

    private void inBienNhan()
    {
        String code = txtCodeSend.getText().toString().trim();
        String name = txtSendName.getText().toString().trim();
        String identity = txtSendIdentity.getText().toString().trim();
        String address = txtSendAddress.getText().toString().trim();

        if(code.length() == 0) {
            Toast toast = Toast.makeText(this, "Vui lòng điền đủ thông tin", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            deliveryReceive = new DeliveryReceive();
            deliveryReceive.setItemCode(code);
            deliveryReceive.setName(name);
            deliveryReceive.setAddress(address);
            deliveryReceive.setIdentity(identity);

            print();
        }
    }

    private void print()
    {
        if(!connected)
        {
            connectInit();
            try
            {
                ConfigUtils utils = ConfigUtils.getInstance(this);
                String bdaCode = utils.displaySharedPreferences().getBda();
                btConn(mBluetoothAdapter.getRemoteDevice(bdaCode));
            }
            catch(IllegalArgumentException e)
            {
                // Bluetooth Address Format [OO:OO:OO:OO:OO:OO]
                Log.e(TAG,e.getMessage(),e);
                Toast toast = Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            catch (IOException e)
            {
                Log.e(TAG,e.getMessage(),e);
                Toast toast = Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
        } else {
            InBuuTaThu service = new InBuuTaThu();
            try {
                service.print(deliveryReceive);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }


    ///////////// BARCODE /////////////
    @Override
    public void onBarcodeEvent(BarcodeReadEvent barcodeReadEvent) {
        final BarcodeReadEvent _event = barcodeReadEvent;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtCodeSend.setText(_event.getBarcodeData());
            }
        });
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(DeliveryReceiveActivity.this, "No data", Toast.LENGTH_SHORT).show();
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
    protected void onDestroy()
    {
        super.onDestroy();
        try
        {
            bp.disconnect();
        }
        catch (IOException e)
        {
            Log.e(TAG, e.getMessage(), e);
        }
        if((hThread != null) && (hThread.isAlive()))
        {
            hThread.interrupt();
            hThread = null;
        }

        btDisconn();
    }
}
