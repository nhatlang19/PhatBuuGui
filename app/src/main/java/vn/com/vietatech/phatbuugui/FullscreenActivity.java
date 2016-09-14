package vn.com.vietatech.phatbuugui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeReader;

import vn.com.vietatech.dao.DeliveryDataSource;
import vn.com.vietatech.dto.User;
import vn.com.vietatech.lib.Utils;
import vn.com.vietatech.phatbuugui.dialog.DialogConfirm;

public class FullscreenActivity extends AppCompatActivity {
    protected Button btnNguoiDung;
    protected Button btnPhatBuuGui;
    protected Button btnBuuTaThu;
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
        btnBuuTaThu = (Button) findViewById(R.id.btnBuuTaThu);
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

        btnBuuTaThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buuTaThu();
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

        MyApplication globalVariable = (MyApplication) this.getApplication();
        User user = globalVariable.getUser();
        if(!user.getRole().toLowerCase().equals("admin")) {
            btnNguoiDung.setVisibility(View.GONE);
            btnCauHinh.setVisibility(View.GONE);
        }

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
        DeliveryDataSource ds = DeliveryDataSource.getInstance(this);
        ds.open();
        ds.deleteDeliveryByUser();
        ds.close();
        Utils.showAlert(this, this.getString(R.string.xoa_thanh_cong_buu_gui));
    }

    private void phatBuuGui() {
        MyApplication globalVariable = (MyApplication) this.getApplication();
        User user = globalVariable.getUser();
        if(user.getRole().toLowerCase().equals("admin")) {
            new DialogConfirm(this, this.getString(R.string.phat_buu_gui_for_admin)) {
                public void run() {
                    Intent intent = new Intent(FullscreenActivity.this, DeliveryActivity.class);
                    startActivity(intent);
                }
            };
        } else {
            Intent intent = new Intent(FullscreenActivity.this, DeliveryActivity.class);
            startActivity(intent);
        }
    }

    private void buuTaThu() {
        Intent intent = new Intent(FullscreenActivity.this, DeliverySendActivity.class);
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
