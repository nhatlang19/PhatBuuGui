package vn.com.vietatech.phatbuugui;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.citizen.port.android.BluetoothPort;
import com.citizen.request.android.RequestHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import vn.com.vietatech.dto.DeliveryReceive;
import vn.com.vietatech.lib.InBuuTaThu;

public class PrintBluetoothActivity extends AppCompatActivity
{
    private static final String TAG = "PrintBluetoothActivity";
    // Intent request codes
    // private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    ArrayAdapter<String> adapter;
    private BluetoothAdapter mBluetoothAdapter;
    private Vector<BluetoothDevice> remoteDevices;
    private BroadcastReceiver searchFinish;
    private BroadcastReceiver searchStart;
    private BroadcastReceiver discoveryResult;
    private Thread hThread;
    private static boolean connected;
    private Context context;
    // UI
    private EditText editText;
    private Button connectButton;
    private Button searchButton;
    private ListView list;
    private Button btnPrint;
    // BT
    private BluetoothPort bp;

    private DeliveryReceive deliveryReceive;

    /**
     * Set up Bluetooth.
     */
    private void bluetoothSetup()
    {
        // Initialize
        clearBtDevData();
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

    private void clearBtDevData()
    {
        remoteDevices = new Vector<BluetoothDevice>();
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
        new connTask().execute(btDev);
    }

    /** ======================================================================= */
    public class connTask extends AsyncTask<BluetoothDevice, Void, Integer>
    {
        private final ProgressDialog dialog = new ProgressDialog(PrintBluetoothActivity.this);

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
                connectButton.setText("Disconnect");
                list.setEnabled(false);
                editText.setEnabled(false);
                searchButton.setEnabled(false);
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
        // UI
        connectButton.setText("Connect");
        list.setEnabled(true);
        editText.setEnabled(true);
        searchButton.setEnabled(true);
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

//==================================================================================================//
//==================================================================================================//

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
        unregisterReceiver(searchFinish);
        unregisterReceiver(searchStart);
        unregisterReceiver(discoveryResult);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_menu);

        deliveryReceive = (DeliveryReceive) getIntent().getSerializableExtra("delivery_send");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Setting
        bluetoothSetup();
        editText = (EditText) findViewById(R.id.EditText01);
        connectButton = (Button) findViewById(R.id.Button02);
        searchButton = (Button) findViewById(R.id.Button01);
        list = (ListView) findViewById(R.id.ListView01);
        btnPrint = (Button) findViewById(R.id.btnPrint);
        context = this;
        // Connect, Disconnect -- Button
        connectButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Connect routine.
                if(!connected)
                {
                    connectInit();
                    try
                    {
                        String code = "0C:A6:94:3A:23:66";
                        btConn(mBluetoothAdapter.getRemoteDevice(code));
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
                }
                // Disconnect routine.
                else
                {
                    // Always run.
                    btDisconn();
                }
            }
        });
        // Search Button
        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                connectInit();
                if (!mBluetoothAdapter.isDiscovering())
                {
                    clearBtDevData();
                    adapter.clear();
                    mBluetoothAdapter.startDiscovery();
                }
                else
                {
                    mBluetoothAdapter.cancelDiscovery();
                }
            }
        });

        // Print
        btnPrint.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                if(connected)
                {
                    InBuuTaThu service = new InBuuTaThu();
                    try {
                        service.print(deliveryReceive);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, e.getMessage(), e);
                    }
                }
            }
        });

        // Bluetooth Device List
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        list.setAdapter(adapter);
        // Connect -
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                // TODO Auto-generated method stub
                BluetoothDevice btDev = remoteDevices.elementAt(arg2);
                connectInit();
                try
                {
                    if(!mBluetoothAdapter.isDiscovering())
                        btConn(btDev);
                    else
                        return;
                }
                catch (IOException e)
                {
                    Toast toast = Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
            }
        });

        // UI - Event Handler.
        // Search device, then add List.
        discoveryResult = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String key;
                BluetoothDevice remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(remoteDevice != null)
                {
                    key = remoteDevice.getName() +"\n["+remoteDevice.getAddress()+"]";

                    remoteDevices.add(remoteDevice);
                    adapter.add(key);
                }
            }
        };
        registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        searchStart = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                connectButton.setEnabled(false);
                editText.setEnabled(false);
                searchButton.setText("Stop Searching Device");
            }
        };
        registerReceiver(searchStart, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
        searchFinish = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                connectButton.setEnabled(true);
                editText.setEnabled(true);
                searchButton.setText("Search Device");
            }
        };
        registerReceiver(searchFinish, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
    }
}