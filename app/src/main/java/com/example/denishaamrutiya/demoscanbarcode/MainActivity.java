package com.example.denishaamrutiya.demoscanbarcode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.denishaamrutiya.demoscanbarcode.adapter.ScanningItemAdapter;
import com.example.denishaamrutiya.demoscanbarcode.helper.DbHelper;
import com.example.denishaamrutiya.demoscanbarcode.helper.MarshMallowPermission;
import com.example.denishaamrutiya.demoscanbarcode.model.ScanningItemPojo;
import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private Class<?> mClss;
    private TextView txtData;
    Bundle bundle;
    private boolean isUpdate;
    private RecyclerView recyclerView;
    private ScanningItemAdapter mRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<ScanningItemPojo> myList = new ArrayList<>();
    private String message;
    private Realm realm;
    private Integer nextId, i;
    private DbHelper dbHelper;
    private ScanningItemPojo scanningItemPojo;
    MarshMallowPermission permission;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        /*btnCustomScan = (Button) findViewById(R.id.btnCustomScan);*/
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_item);
        fabAdd = (FloatingActionButton) findViewById(R.id.fab_add);


        realm = Realm.getDefaultInstance();
        dbHelper = new DbHelper(this, realm);
        permission = new MarshMallowPermission(this);

        RealmResults<ScanningItemPojo> scanningItem = realm.where(ScanningItemPojo.class).findAllAsync();
        Log.e("DATA",scanningItem.toString());
        mRecyclerAdapter = new ScanningItemAdapter(this, scanningItem);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mRecyclerAdapter);


        /*realm.executeTransaction(new Realm.Transaction() { // must be in transaction for this to work
            @Override
            public void execute(Realm realm) {
                // Get the current max id in the users table
                Number maxId = realm.where(ScanningItemPojo.class).max("discription");
                // If there are no rows, currentId is null, so the next id must be 1
                // If currentId is not null, increment it by 1
                nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
                // User object created with the new Primary key
                // RegisterPojo user = bgRealm.createObject(users.class, nextId);
                // Now you can update your object with your data. The object will be
                // automatically saved in the database when the execute method ends
                // ...
                // ...
            }
        });*/


        /*btnCustomScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                launchActivity(CustomViewFinderScannerActivity.class);

             *//*   launchActivity(MainActivity.this);
                startActivity(new Intent(MainActivity.this,CustomViewFinderScannerActivity.class));*//*
            }
        });*/

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchActivity(CustomViewFinderScannerActivity.class);
            }
        });
    }



    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);

            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
