package vn.com.vietatech.phatbuugui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class FullscreenActivity extends AppCompatActivity {
    protected Button btnNguoiDung;
    protected Button btnPhatBuuGui;
    protected Button btnCauHinh;
    protected Button btnXoaBuuGui;
    protected Button btnThoat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
}
