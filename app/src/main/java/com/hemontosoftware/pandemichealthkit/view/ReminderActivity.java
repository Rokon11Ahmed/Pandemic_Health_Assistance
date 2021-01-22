package com.hemontosoftware.pandemichealthkit.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.hemontosoftware.pandemichealthkit.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.Calendar;

import static com.airbnb.lottie.utils.Logger.debug;

public class ReminderActivity extends AppCompatActivity {

    TimePicker timePicker;
    int hour, minute;
    Button sundayButton, mondayButton, tuesdayButton, wednesdayButton, thursdayButton, fridaydayButton, saturdayButton;
    boolean sunDay = false, monDay = false, tuesDay = false, wednesDay = false, thursDay = false, friDay = false, saturDay = false;

    LinearLayout labelLayout;
    TextView labelTextview;
    String labelText = "";
    ArrayList<Integer> alarmDays = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        Toolbar toolbar = findViewById(R.id.contact_tracing_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sundayButton = findViewById(R.id.sunday_button);
        mondayButton = findViewById(R.id.monday_button);
        tuesdayButton = findViewById(R.id.tuesday_button);
        wednesdayButton = findViewById(R.id.wednesday_button);
        thursdayButton = findViewById(R.id.thursday_button);
        fridaydayButton = findViewById(R.id.friday_button);
        saturdayButton = findViewById(R.id.saturday_button);
        labelLayout = findViewById(R.id.label_layout);
        labelTextview = findViewById(R.id.label_textview);

        sundayButton.setBackground(getResources().getDrawable(R.drawable.button_not_pressed));
        mondayButton.setBackground(getResources().getDrawable(R.drawable.button_not_pressed));
        tuesdayButton.setBackground(getResources().getDrawable(R.drawable.button_not_pressed));
        wednesdayButton.setBackground(getResources().getDrawable(R.drawable.button_not_pressed));
        thursdayButton.setBackground(getResources().getDrawable(R.drawable.button_not_pressed));
        fridaydayButton.setBackground(getResources().getDrawable(R.drawable.button_not_pressed));
        saturdayButton.setBackground(getResources().getDrawable(R.drawable.button_not_pressed));


        sundayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sunDay == false) {
                    sundayButton.setBackground(getResources().getDrawable(R.drawable.button_pressed));
                    sundayButton.setTextColor(Color.WHITE);
                    alarmDays.add(Calendar.SUNDAY);
                    sunDay = true;
                } else {
                    sundayButton.setBackground(getResources().getDrawable(R.drawable.button_not_pressed));
                    sundayButton.setTextColor(Color.parseColor("#3C3C3C"));
                    alarmDays.remove(new Integer(Calendar.SUNDAY));
                    sunDay = false;
                }
            }
        });
        mondayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (monDay == false) {
                    mondayButton.setBackground(getResources().getDrawable(R.drawable.button_pressed));
                    mondayButton.setTextColor(Color.WHITE);
                    alarmDays.add(Calendar.MONDAY);
                    monDay = true;
                } else {
                    mondayButton.setBackground(getResources().getDrawable(R.drawable.button_not_pressed));
                    mondayButton.setTextColor(Color.parseColor("#3C3C3C"));
                    alarmDays.remove(new Integer(Calendar.MONDAY));
                    monDay = false;
                }
            }
        });
        tuesdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tuesDay == false) {
                    tuesdayButton.setBackground(getResources().getDrawable(R.drawable.button_pressed));
                    tuesdayButton.setTextColor(Color.WHITE);
                    alarmDays.add(Calendar.TUESDAY);
                    tuesDay = true;
                } else {
                    tuesdayButton.setBackground(getResources().getDrawable(R.drawable.button_not_pressed));
                    tuesdayButton.setTextColor(Color.parseColor("#3C3C3C"));
                    alarmDays.remove(new Integer(Calendar.TUESDAY));
                    tuesDay = false;
                }
            }
        });
        wednesdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wednesDay == false) {
                    wednesdayButton.setBackground(getResources().getDrawable(R.drawable.button_pressed));
                    wednesdayButton.setTextColor(Color.WHITE);
                    alarmDays.add(Calendar.WEDNESDAY);
                    wednesDay = true;
                } else {
                    wednesdayButton.setBackground(getResources().getDrawable(R.drawable.button_not_pressed));
                    wednesdayButton.setTextColor(Color.parseColor("#3C3C3C"));
                    alarmDays.remove(new Integer(Calendar.WEDNESDAY));
                    wednesDay = false;
                }
            }
        });
        thursdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thursDay == false) {
                    thursdayButton.setBackground(getResources().getDrawable(R.drawable.button_pressed));
                    thursdayButton.setTextColor(Color.WHITE);
                    alarmDays.add(Calendar.THURSDAY);
                    thursDay = true;
                } else {
                    thursdayButton.setBackground(getResources().getDrawable(R.drawable.button_not_pressed));
                    thursdayButton.setTextColor(Color.parseColor("#3C3C3C"));
                    alarmDays.remove(new Integer(Calendar.THURSDAY));
                    thursDay = false;
                }
            }
        });
        fridaydayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (friDay == false) {
                    fridaydayButton.setBackground(getResources().getDrawable(R.drawable.button_pressed));
                    fridaydayButton.setTextColor(Color.WHITE);
                    alarmDays.add(Calendar.FRIDAY);
                    friDay = true;
                } else {
                    fridaydayButton.setBackground(getResources().getDrawable(R.drawable.button_not_pressed));
                    fridaydayButton.setTextColor(Color.parseColor("#3C3C3C"));
                    alarmDays.remove(new Integer(Calendar.FRIDAY));
                    friDay = false;
                }
            }
        });
        saturdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saturDay == false) {
                    saturdayButton.setBackground(getResources().getDrawable(R.drawable.button_pressed));
                    saturdayButton.setTextColor(Color.WHITE);
                    alarmDays.add(Calendar.SATURDAY);
                    saturDay = true;
                } else {
                    saturdayButton.setBackground(getResources().getDrawable(R.drawable.button_not_pressed));
                    saturdayButton.setTextColor(Color.parseColor("#3C3C3C"));
                    alarmDays.remove(new Integer(Calendar.SATURDAY));
                    saturDay = false;
                }
            }
        });

        labelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(ReminderActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.label_input_layout);
                final EditText labelEditText = dialog.findViewById(R.id.lavel_editText);
                Button positiveButton = dialog.findViewById(R.id.ok_button);
                Button negetiveButton = dialog.findViewById(R.id.cancel_button);
                negetiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        labelText = labelEditText.getText().toString().trim();
                        labelTextview.setText(labelText);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        timePicker = findViewById(R.id.time_picker);
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minutes) {
                hour = hourOfDay;
                minute = minutes;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reminder_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_save:
                Dexter.withActivity(this)
                        .withPermission(Manifest.permission.SET_ALARM)
                        .withListener(new PermissionListener() {
                            @SuppressLint("RestrictedApi")
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                // permission is granted
                                Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                                intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                                intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra(AlarmClock.EXTRA_MINUTES, minute);
                                intent.putExtra(AlarmClock.EXTRA_MESSAGE, labelText);
                                intent.putExtra(AlarmClock.EXTRA_DAYS, alarmDays);
                                startActivity(intent);
                                Toast.makeText(ReminderActivity.this, "save", Toast.LENGTH_SHORT).show();


                                PackageManager packageManager = getPackageManager();
                                Intent alarmClockIntent = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER);

// Verify clock implementation
                                String clockImpls[][] = {
                                        {"HTC Alarm Clock", "com.htc.android.worldclock", "com.htc.android.worldclock.WorldClockTabControl" },
                                        {"Standar Alarm Clock", "com.android.deskclock", "com.android.deskclock.AlarmClock"},
                                        {"Froyo Nexus Alarm Clock", "com.google.android.deskclock", "com.android.deskclock.DeskClock"},
                                        {"Moto Blur Alarm Clock", "com.motorola.blur.alarmclock",  "com.motorola.blur.alarmclock.AlarmClock"},
                                        {"Samsung Galaxy Clock", "com.sec.android.app.clockpackage","com.sec.android.app.clockpackage.ClockPackage"} ,
                                        {"Sony Ericsson Xperia Z", "com.sonyericsson.organizer", "com.sonyericsson.organizer.Organizer_WorldClock" },
                                        {"ASUS Tablets", "com.asus.deskclock", "com.asus.deskclock.DeskClock"}

                                };

                                boolean foundClockImpl = false;

                                for(int i =0; i<clockImpls.length; i++) {
                                    String vendor = clockImpls[i][0];
                                    String packageName = clockImpls[i][1];
                                    String className = clockImpls[i][2];
                                    try {
                                        ComponentName cn = new ComponentName(packageName, className);
                                        ActivityInfo aInfo = packageManager.getActivityInfo(cn, PackageManager.GET_META_DATA);
                                        alarmClockIntent.setComponent(cn);
                                        debug("Found " + vendor + " --> " + packageName + "/" + className);
                                        foundClockImpl = true;
                                    } catch (PackageManager.NameNotFoundException e) {
                                        debug(vendor + " does not exists");
                                    }
                                }

                                if (foundClockImpl) {
                                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, alarmClockIntent, 0);
                                    // add pending intent to your component
                                    // ....
                                }
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                // check for permanent denial of permission
                                if (response.isPermanentlyDenied()) {
                                    showSettingsDialog();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();

                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReminderActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
}