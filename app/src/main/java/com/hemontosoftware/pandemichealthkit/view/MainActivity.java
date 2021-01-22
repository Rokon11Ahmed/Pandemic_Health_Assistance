package com.hemontosoftware.pandemichealthkit.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.hemontosoftware.pandemichealthkit.R;
import com.hemontosoftware.pandemichealthkit.loginRegister.LoginActivity;
import com.hemontosoftware.pandemichealthkit.uitlity.SessaionManager;
import com.potyvideo.slider.library.Animations.DescriptionAnimation;
import com.potyvideo.slider.library.SliderLayout;
import com.potyvideo.slider.library.SliderTypes.BaseSliderView;
import com.potyvideo.slider.library.SliderTypes.TextSliderViewCurve;
import com.potyvideo.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private static final String TAG = "MainActivity";
    NavigationView navigationView;
    SliderLayout mainSlider;
    CardView hospitalLayout, contactTracingLayout, ambulanceLayout, medicineShopLayout, bloodDonorLayout, reminderLayout;
    SessaionManager sessaionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        hospitalLayout = findViewById(R.id.hospital_cardView);
        contactTracingLayout = findViewById(R.id.contact_tracing_layout);
        ambulanceLayout = findViewById(R.id.ambulance_layout);
        medicineShopLayout = findViewById(R.id.medicine_shop_layout);
        bloodDonorLayout = findViewById(R.id.blood_donor_layout);
        reminderLayout = findViewById(R.id.reminder_layout);
        sessaionManager = new SessaionManager(getApplicationContext());

        mainSlider = findViewById(R.id.main_slider);
        initMainSlider();

        contactTracingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessaionManager.get_user_login_status().equals("true") && sessaionManager.get_user_profile_update_status().equals("true")){
                    startActivity(new Intent(MainActivity.this, ContactTracingActivity.class));
                }else {
                    checkVisibility();
                }
            }
        });
        hospitalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HospitalMapsActivity.class));
            }
        });
        bloodDonorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessaionManager.get_user_login_status().equals("true") && sessaionManager.get_user_profile_update_status().equals("true")){
                    startActivity(new Intent(MainActivity.this, BloodDonorActivity.class));
                }else {
                    checkVisibility();
                }
            }
        });
        ambulanceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AmbulanceActivity.class));
            }
        });
        medicineShopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MedicineShopActivity.class));
            }
        });
        reminderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ReminderActivity.class));
            }
        });

        checkUpdateTime();
    }

    private void checkUpdateTime() {
        if (System.currentTimeMillis()>Long.parseLong(sessaionManager.getNextReportTime())){
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this).create();
            alertDialog.setTitle("Report time");
            alertDialog.setMessage("Your last report was before the 24 hour.\n It's time to update your current status");
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getApplicationContext(), MyReportActivity.class));
                            finish();
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    private void checkVisibility() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("You need to login or update your profile to continue");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (!sessaionManager.get_user_login_status().equals("true")){
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                            dialog.dismiss();
                        }else if (!sessaionManager.get_user_profile_update_status().equals("true")){
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            finish();
                            dialog.dismiss();
                        }

                    }
                });
        alertDialog.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.navigation_home:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.navigation_my_report:
                if (sessaionManager.get_user_login_status().equals("true") && sessaionManager.get_user_profile_update_status().equals("true")){
                    startActivity(new Intent(MainActivity.this, MyReportActivity.class));
                }else {
                    checkVisibility();
                }
                break;
            case R.id.navigation_profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;
            case R.id.navigation_policy:
                Toast.makeText(this, "Policy", Toast.LENGTH_SHORT).show();
                break;
            case R.id.navigation_terms_and_conditions:
                Toast.makeText(this, "Terms & Condition", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }
    private void initMainSlider() {
        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Image 1", R.drawable.cover_image_2);
        file_maps.put("Image 2", R.drawable.cover_image_3);
        file_maps.put("Image 3", R.drawable.cover_image_1);
        file_maps.put("Image 4", R.drawable.cover_image_4);
        file_maps.put("Image 5", R.drawable.cover_image_5);
        file_maps.put("Image 6", R.drawable.cover_image_6);

        for (String name : file_maps.keySet()) {
            TextSliderViewCurve textSliderView = new TextSliderViewCurve(this, false);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mainSlider.addSlider(textSliderView);
            mainSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            mainSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mainSlider.setCustomAnimation(new DescriptionAnimation(false));
            mainSlider.setDuration(4000);
            mainSlider.addOnPageChangeListener(this);
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;
            case R.id.share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rate_us:
                Toast.makeText(this, "Rate Us", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about_us:
                Toast.makeText(this, "About Us", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                sessaionManager.set_user_login_status("false");
                Toast.makeText(this, "Successfully Logout", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}