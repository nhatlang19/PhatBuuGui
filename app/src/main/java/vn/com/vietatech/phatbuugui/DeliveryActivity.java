package vn.com.vietatech.phatbuugui;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.List;

import vn.com.vietatech.dao.DeliveryDataSource;
import vn.com.vietatech.dto.Delivery;
import vn.com.vietatech.dto.User;
import vn.com.vietatech.lib.ConfigUtils;
import vn.com.vietatech.lib.Utils;
import vn.com.vietatech.phatbuugui.adapter.ViewPagerAdapter;
import vn.com.vietatech.phatbuugui.fragment.DeliveryFragment;
import vn.com.vietatech.phatbuugui.fragment.NoDeliveryFragment;

public class DeliveryActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private EditText txtCode;
    private CheckBox cbBatch;

    private Context context = this;

    private static String temlateTitle = "Mới %d/Tổng %d";

    public static final int REQUEST_CODE_VIEW_LIST = 1;

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
                this.scanMany();
                break;
            case R.id.btnXemDanhSach:
                Intent intentList = new Intent(this, DeliveryListActivity.class);
                startActivityForResult(intentList, REQUEST_CODE_VIEW_LIST);
                break;
            case R.id.btnXoaBuuGui:
                this.delete();
                break;
            case R.id.btnUpload:
                upload();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void delete() {

    }

    protected void upload() {

    }

    protected void scanMany() {

    }


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
        } else {
            delivery = ds.updateDelivery(delivery);
        }
        ds.close();

        Utils.showAlert(context, context.getString(R.string.save_delivery_success) + " " + delivery.getItemCode());

        updateTitle();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check if the request code is same as what is passed  here it is 2
        if (requestCode == REQUEST_CODE_VIEW_LIST) {
            updateTitle();
        }
    }
}
