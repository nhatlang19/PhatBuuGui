package vn.com.vietatech.phatbuugui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import vn.com.vietatech.phatbuugui.adapter.ViewPagerAdapter;
import vn.com.vietatech.phatbuugui.fragment.DeliveryFragment;
import vn.com.vietatech.phatbuugui.fragment.NoDeliveryFragment;

public class DeliveryActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private static String temlateTitle = "Mới %d/Tổng %d";

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
    }

    private void updateTitle() {
        setTitle(String.format(temlateTitle, 0, 0));
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DeliveryFragment(), this.getString(R.string.delivery));
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
            case R.id.btnSaveDelivery:
                return true;
            case R.id.btnQuetSoLuongLon:
                return true;
            case R.id.btnXemDanhSach:
                return true;
            case R.id.btnXoaBuuGui:
                return true;
            case R.id.btnUpload:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
