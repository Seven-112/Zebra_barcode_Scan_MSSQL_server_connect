package com.connect.mssql;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.connect.mssql.Model.AllProducts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    Connection con;
    String username, password, databasename, ipaddress, portNumber, dbTableName;
    List<String> supplierNumbers = new ArrayList<>();
    List<String> csmNumbers = new ArrayList<>();
    List<String> ordNumbers = new ArrayList<>();
    List<String> ordLineNumbers = new ArrayList<>();
    List<String> artNumbers = new ArrayList<>();
    List<String> artNames = new ArrayList<>();
    List<String> undispQties = new ArrayList<>();
    List<String> barcodeQties = new ArrayList<>();
    List<String> proDates = new ArrayList<>();
    List<String> ssccs = new ArrayList<>();
    DatabaseReference connect;
    List<String> eachPro = new ArrayList<>();
    ArrayList<List>  allPro = new ArrayList<>();
    SharedPreferences pref;

    EditText ipAddress, portNo, dbname, tableName, userName, pwd;
    Button sync;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.overridePendingTransition(R.anim.animation_enter,
                R.anim.animation_leave);

        setContentView(R.layout.activity_main);

        ipAddress = findViewById(R.id.ipaddressEdit);
        portNo = findViewById(R.id.portNo);
        dbname = findViewById(R.id.dbName);
        tableName = findViewById(R.id.tabelName);
        connect = FirebaseDatabase.getInstance().getReference();
        userName = findViewById(R.id.userName);
        pwd = findViewById(R.id.password);
        sync = findViewById(R.id.syncBtn);

        setButtonEnabled(false);

        getOldData();

        if (CheckingPermissionIsEnabledOrNot())
        {
            setButtonEnabled(true);
        } else
        {
            RequestMultiplePermission();
        }

        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = userName.getText().toString();
                password = pwd.getText().toString();
                databasename = dbname.getText().toString();
                portNumber = portNo.getText().toString();
                dbTableName = tableName.getText().toString();
                ipaddress = ipAddress.getText().toString();

                if (username.matches("") || password.matches("") || databasename.matches("") || portNumber.matches("") ||
                        dbTableName.matches("") || ipaddress.matches("")) {
                    Toast.makeText(MainActivity.this, "Please fill in the blanks.", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences.Editor editor = pref.edit();

                editor.putString("ipaddress", ipaddress);
                editor.putString("portNo", portNumber);
                editor.putString("databasename", databasename);
                editor.putString("tableName", dbTableName);
                editor.putString("username", username);
                editor.putString("password", password);

                editor.commit(); // commit changes
                connect.child("connect").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String connectStatus = (String) dataSnapshot.getValue();
                        Log.i(":", connectStatus);
                        if (connectStatus.equals("true"))
                            readFromSQL();
                        else Toast.makeText(MainActivity.this, "Connection failed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }
        });
    }

    private void readFromSQL() {
        con = connectionclass(username, password, databasename, ipaddress + ":" + portNumber);

        if (con == null) {
            Log.i("connection status", "connection null");
            Toast.makeText(MainActivity.this, "connection is failed", Toast.LENGTH_SHORT).show();

        } else {
            Log.i("connection is", "successful");
            Toast.makeText(MainActivity.this, "connection is successful", Toast.LENGTH_SHORT).show();
            Statement stmt = null;
            int cnt = 0;
            try {
                stmt = con.createStatement();
                ResultSet reset = stmt.executeQuery(" select * from " + dbTableName);

                while (reset.next())
                {
                    eachPro = new ArrayList<>();
                    String supplierNo = reset.getString(1);
                    String csmNo = reset.getString(2);
                    String ordNo = reset.getString(3);
                    String ordLineNo = reset.getString(4);
                    String artNo = reset.getString(5);
                    String artName = reset.getString(6);
                    String undispQty = reset.getString(7);
                    String barcodeQty = reset.getString(8);
                    String proDate = reset.getString(9);
                    String sscc = reset.getString(10);

                    supplierNumbers.add(supplierNo);
                    csmNumbers.add(csmNo);
                    ordNumbers.add(ordNo);
                    ordLineNumbers.add(ordLineNo);
                    artNumbers.add(artNo);
                    artNames.add(artName);
                    undispQties.add(undispQty);
                    barcodeQties.add(barcodeQty);
                    proDates.add(proDate);
                    ssccs.add(sscc);

                    eachPro.add(supplierNo);
                    eachPro.add(csmNo);
                    eachPro.add(ordNo);
                    eachPro.add(ordLineNo);
                    eachPro.add(artNo);
                    eachPro.add(artName);
                    eachPro.add(undispQty);
                    eachPro.add(barcodeQty);
                    eachPro.add(proDate);
                    eachPro.add(sscc);
                    allPro.add(eachPro);
                }

                con.close();

                //TODO; all data from mssql server
//                Log.v("supplierNo:", String.valueOf(supplierNumbers));
//                Log.v("csmNo:", String.valueOf(csmNumbers));
//                Log.v("ordNo:", String.valueOf(ordNumbers));
//                Log.v("ordLineNo:", String.valueOf(ordLineNumbers));
//                Log.v("artNo:", String.valueOf(artNumbers));
//                Log.v("artName:", String.valueOf(artNames));
//                Log.v("undispQty:", String.valueOf(undispQties));
//                Log.v("barcodeQty:", String.valueOf(barcodeQties));
//                Log.v("proDate:", String.valueOf(proDates));
//                Log.v("sscc:", String.valueOf(ssccs));
//                Log.v("allPro:", String.valueOf(allPro));
//                Log.v("firstPro:", String.valueOf(allPro.get(0).get(1)));

                AllProducts allProducts = new AllProducts();

                allProducts.setAllproducts(allPro);
                Intent intent = new Intent(MainActivity.this, CsmListActivity.class);
                startActivity(intent);

            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean CheckingPermissionIsEnabledOrNot() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {
                        READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE,
                        CAMERA,
                }, 999);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 999:

                if (grantResults.length > 0) {

                    boolean ReadStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean WriteStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean CameraStorage = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (ReadStorage && WriteStorage && CameraStorage) {
                        setButtonEnabled(true);

                    }
                    else {
                        Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }

    private void setButtonEnabled(boolean isEnabled)
    {
        sync.setEnabled(isEnabled);
    }

    private void getOldData() {
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);

        // retrieve data
        ipaddress = pref.getString("ipaddress", "");
        ipAddress.setText(ipaddress);
        portNumber = pref.getString("portNo", "");
        portNo.setText(portNumber);
        databasename = pref.getString("databasename", "");
        dbname.setText(databasename);
        dbTableName = pref.getString("tableName", "");
        tableName.setText(dbTableName);
        username = pref.getString("username", "");
        userName.setText(username);
        password = pref.getString("password", "");
        pwd.setText(password);
    }

    @SuppressLint("NewApi")
    public Connection connectionclass(String username, String password, String databasename, String ipaddress) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionUrl;
        try {
//            Toast.makeText(MainActivity.this, "step_1",Toast.LENGTH_SHORT).show();
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionUrl = "jdbc:jtds:sqlserver://" + ipaddress + ";" + "databaseName=" + databasename + ";user=" + username + ";password=" + password + ";";
            connection = DriverManager.getConnection(ConnectionUrl);
//            Toast.makeText(MainActivity.this, "step_2",Toast.LENGTH_SHORT).show();

        }
        catch (SQLException se) {
//            Toast.makeText(MainActivity.this, "step_3",Toast.LENGTH_SHORT).show();

            Log.e("SQL", se.getMessage());
        }
        catch (ClassNotFoundException e) {
//            Toast.makeText(MainActivity.this, "step_4",Toast.LENGTH_SHORT).show();

            Log.e("ClassNotFound", e.getMessage());
        }
        catch (Exception e) {
//            Toast.makeText(MainActivity.this, "step_5",Toast.LENGTH_SHORT).show();

            Log.e("Exception", e.getMessage());
        }
        return connection;
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Toast.makeText(MainActivity.this, "paused",Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        Toast.makeText(MainActivity.this, "destroyed",Toast.LENGTH_SHORT).show();

    }

}
