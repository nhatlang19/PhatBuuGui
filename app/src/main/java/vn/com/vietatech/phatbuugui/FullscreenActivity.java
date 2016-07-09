package vn.com.vietatech.phatbuugui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeReader;

public class FullscreenActivity extends AppCompatActivity {
    protected Button btnNguoiDung;
    protected Button btnPhatBuuGui;
    protected Button btnCauHinh;
    protected Button btnXoaBuuGui;
    protected Button btnThoat;

    private static BarcodeReader barcodeReader;
    private AidcManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        btnNguoiDung = (Button) findViewById(R.id.btnNguoiDung);
        btnPhatBuuGui = (Button) findViewById(R.id.btnPhatBuuGui);
        btnCauHinh = (Button) findViewById(R.id.btnCauHinh);
        btnXoaBuuGui = (Button) findViewById(R.id.btnXoaBuuGui);
        btnThoat = (Button) findViewById(R.id.btnThoat);


        btnNguoiDung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadUsers();
            }
        });

        btnPhatBuuGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phatBuuGui();
            }
        });

        btnCauHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadConfig();
            }
        });

        btnXoaBuuGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xoaBuuGui();
            }
        });

        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // create the AidcManager providing a Context and a
        // CreatedCallback implementation.
        AidcManager.create(this, new AidcManager.CreatedCallback() {

            @Override
            public void onCreated(AidcManager aidcManager) {
                manager = aidcManager;
                barcodeReader = manager.createBarcodeReader();
            }
        });
    }

    public static BarcodeReader getBarcodeObject() {
        return barcodeReader;
    }

    private void loadConfig() {
        Intent intent = new Intent(FullscreenActivity.this, PrefsActivity.class);
        startActivity(intent);
    }

    private void xoaBuuGui() {

    }

    private void phatBuuGui() {
        Intent intent = new Intent(FullscreenActivity.this, DeliveryActivity.class);
        startActivity(intent);
    }

    private void loadUsers() {
        Intent intent = new Intent(FullscreenActivity.this, UserActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (barcodeReader != null) {
            // close BarcodeReader to clean up resources.
            barcodeReader.close();
            barcodeReader = null;
        }

        if (manager != null) {
            // close AidcManager to disconnect from the scanner service.
            // once closed, the object can no longer be used.
            manager.close();
        }
    }
}
