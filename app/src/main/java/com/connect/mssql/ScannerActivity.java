package com.connect.mssql;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.connect.mssql.Adapter.ScanListAdapter;
import com.connect.mssql.Model.AllProducts;
import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKManager.EMDKListener;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.barcode.BarcodeManager;
import com.symbol.emdk.barcode.ScanDataCollection;
import com.symbol.emdk.barcode.ScanDataCollection.ScanData;
import com.symbol.emdk.barcode.Scanner;
import com.symbol.emdk.barcode.Scanner.DataListener;
import com.symbol.emdk.barcode.Scanner.StatusListener;
import com.symbol.emdk.barcode.Scanner.TriggerType;
import com.symbol.emdk.barcode.ScannerConfig;
import com.symbol.emdk.barcode.ScannerException;
import com.symbol.emdk.barcode.ScannerResults;
import com.symbol.emdk.barcode.StatusData;
import com.symbol.emdk.barcode.StatusData.ScannerStates;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static com.symbol.emdk.barcode.StatusData.ScannerStates.DISABLED;
import static com.symbol.emdk.barcode.StatusData.ScannerStates.ERROR;
import static com.symbol.emdk.barcode.StatusData.ScannerStates.SCANNING;

public class ScannerActivity extends AppCompatActivity implements EMDKListener, StatusListener, DataListener{
    private Boolean isClicked = false;
    TextView product_date, barcode_QTY, sscc_info;
    ImageView backBtn;
    Button scanBtn, exportBtn;
    private ListView listView;
    ArrayList<String> ordLineNo = new ArrayList<>();
    ArrayList<List> selectedProducts = new ArrayList<>();
    ArrayList<List> InitPro = new ArrayList<>();
    ArrayList<List> scannedPro = new ArrayList<>();
    ArrayList<String> scannedEachPro = new ArrayList<>();
    ArrayList<Integer> images = new ArrayList<>();
    private String CSM_NO = "";
    private String ART_NO = "";
    AllProducts allProducts = new AllProducts();
    //TODO; emdk parts
    //Assign the profile name used in EMDKConfig.xml
    private String profileName = "DataCaptureProfile";

    private EMDKManager emdkManager = null;
    private BarcodeManager barcodeManager = null;
    private Scanner scanner = null;
    private int dataLength=  0;

    String scannedData = "";
    int len;
    String compPRODATE = "";
    String compBARQTY_1 = "";
    String compBARQTY_2 = "";
    String compSSCC = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        backBtn = findViewById(R.id.backBtn);
        listView = findViewById(R.id.ordLineNoList);

        product_date = findViewById(R.id.proDateDisp);
        barcode_QTY = findViewById(R.id.barcodeQtyDisp);
        sscc_info = findViewById(R.id.ssccDisp);

        scanBtn = findViewById(R.id.scanStartBtn);
        exportBtn = findViewById(R.id.scanStopBtn);


        this.overridePendingTransition(R.anim.animation_enter,
                R.anim.animation_leave);

        Intent extra = getIntent();
        CSM_NO = extra.getStringExtra("CSM_NO");
        ART_NO = extra.getStringExtra("ART_NO");
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ScannerActivity.this, ArtListActivity.class);
                intent.putExtra("CSM_NO", CSM_NO);

                finish();
            }
        });


        selectedProducts = allProducts.getAllproducts();

        for (int i = 0; i < selectedProducts.size(); i ++) {
            if (selectedProducts.get(i).get(1).equals(CSM_NO) && selectedProducts.get(i).get(4).equals(ART_NO)) {
                if (selectedProducts.get(i).get(7).equals("") || selectedProducts.get(i).get(7) == null) {
                    if (selectedProducts.get(i).get(8).equals("") || selectedProducts.get(i).get(8) == null) {
                        if (selectedProducts.get(i).get(9).equals("") || selectedProducts.get(i).get(9) == null) {
                            ordLineNo.add((String) selectedProducts.get(i).get(3));
                        }
                    }
                }
//                ordLineNo.add((String) selectedProducts.get(i).get(3));

            }

        }

        ScanListAdapter scanListAdapter = new ScanListAdapter(ScannerActivity.this, ordLineNo);
        listView.setAdapter(scanListAdapter);

        if (!product_date.getText().toString().equals("") && !barcode_QTY.getText().toString().equals("") && !sscc_info.getText().toString().equals("")) {
            listView.setVisibility(View.VISIBLE);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    product_date.setText("");
//                    barcode_QTY.setText("");
//                    sscc_info.setText("");
                    AllProducts products = new AllProducts();
                    AllProducts newProducts = new AllProducts();
                    InitPro = products.getAllproducts();
                    for (int i = 0; i < InitPro.size(); i ++) {
                        if (InitPro.get(i).get(1).equals(CSM_NO) && InitPro.get(i).get(4).equals(ART_NO) && InitPro.get(i).get(3).equals(ordLineNo.get(position))) {
                            scannedEachPro.add((String) InitPro.get(i).get(0));
                            scannedEachPro.add(CSM_NO);
                            scannedEachPro.add((String) InitPro.get(i).get(2));
                            scannedEachPro.add(ordLineNo.get(position));
                            scannedEachPro.add(ART_NO);
                            scannedEachPro.add((String) InitPro.get(i).get(5));
                            scannedEachPro.add((String) InitPro.get(i).get(6));
                            scannedEachPro.add(barcode_QTY.getText().toString());
                            scannedEachPro.add(product_date.getText().toString());
                            scannedEachPro.add(sscc_info.getText().toString());
                            scannedPro.add(scannedEachPro);
                        } else {
                            scannedEachPro.add((String) InitPro.get(i).get(0));
                            scannedEachPro.add((String) InitPro.get(i).get(1));
                            scannedEachPro.add((String) InitPro.get(i).get(2));
                            scannedEachPro.add((String) InitPro.get(i).get(3));
                            scannedEachPro.add((String) InitPro.get(i).get(4));
                            scannedEachPro.add((String) InitPro.get(i).get(5));
                            scannedEachPro.add((String) InitPro.get(i).get(6));
                            scannedEachPro.add((String) InitPro.get(i).get(7));
                            scannedEachPro.add((String) InitPro.get(i).get(8));
                            scannedEachPro.add((String) InitPro.get(i).get(9));
                            scannedPro.add(scannedEachPro);
                        }
                    }
                    newProducts.setAllproducts(scannedPro);

                    Intent intent = new Intent(ScannerActivity.this, ArtListActivity.class);
                    intent.putExtra("CSM_NO", CSM_NO);

                    finish();
                }
            });
        }
        EMDKResults results = EMDKManager.getEMDKManager(getApplicationContext(), this);

        // Check the return status of getEMDKManager() and update the status TextView accordingly.
        if (results.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
            updateStatus("EMDKManager object request failed!");
            return;
        } else {
            updateStatus("EMDKManager object initialization is   in   progress.......");
        }

    }

    private void initBarcodeManager() {
        // Get the feature object such as BarcodeManager object for accessing the feature.
        barcodeManager =  (BarcodeManager)emdkManager.getInstance(EMDKManager.FEATURE_TYPE.BARCODE);
        // Add external scanner connection listener.
        if (barcodeManager == null) {
            Toast.makeText(this, "Barcode scanning is not supported.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initScanner() {
        if (scanner == null) {
            // Get default scanner defined on the device
            scanner = barcodeManager.getDevice(BarcodeManager.DeviceIdentifier.DEFAULT);
            if(scanner != null) {
                // Implement the DataListener interface and pass the pointer of this object to get the data callbacks.
                scanner.addDataListener(this);

                // Implement the StatusListener interface and pass the pointer of this object to get the status callbacks.
                scanner.addStatusListener(this);

                // Hard trigger. When this mode is set, the user has to manually
                // press the trigger on the device after issuing the read call.
                // NOTE: For devices without a hard trigger, use TriggerType.SOFT_ALWAYS.
                scanner.triggerType =  TriggerType.HARD;

                try{
                    // Enable the scanner
                    // NOTE: After calling enable(), wait for IDLE status before calling other scanner APIs
                    // such as setConfig() or read().
                    scanner.enable();

                } catch (ScannerException e) {
                    updateStatus(e.getMessage());
                    deInitScanner();
                }
            } else {
                updateStatus("Failed to   initialize the scanner device.");
            }
        }


    }

    private void deInitScanner() {
        if (scanner != null) {
            try {
                // Release the scanner
                scanner.release();
            } catch (Exception e)   {
                updateStatus(e.getMessage());
            }
            scanner = null;
        }
    }

    @Override
    public void onOpened(EMDKManager emdkManager) {

        // Get a reference to EMDKManager
        this.emdkManager =  emdkManager;

        // Get a  reference to the BarcodeManager feature object
        initBarcodeManager();

        // Initialize the scanner
        initScanner();


    }
    public void onData(ScanDataCollection scanDataCollection) {

        String dataStr = "";
        if ((scanDataCollection != null) &&   (scanDataCollection.getResult() == ScannerResults.SUCCESS)) {
            ArrayList<ScanData> scanData =  scanDataCollection.getScanData();
            // Iterate through scanned data and prepare the data.
            for (ScanData data :  scanData) {
                // Get the scanned dataString barcodeData =  data.getData();
                // Get the type of label being scanned
                ScanDataCollection.LabelType labelType = data.getLabelType();
                // Concatenate barcode data and label type
                String barcodeData = data.getData();
//                product_date.setText(barcodeData);
                dataStr =  barcodeData;
//                dataStr =  barcodeData + "  " +  labelType;
            }
            // Updates EditText with scanned data and type of label on UI thread.
            updateData(dataStr);
        }
    }

    private void updateData(final String result) {
        runOnUiThread(new Runnable() {
            @Override public void run() {
                // Update the dataView EditText on UI thread with barcode data and its label type.
                if (dataLength++ >= 50) {
                    // Clear the cache after 50 scans
//                    dataView.getText().clear();
                    dataLength = 0;
                }
//                dataView.append(result + "\n"); // editText
                Toast.makeText(ScannerActivity.this, "Scanned barcode is " + result, Toast.LENGTH_SHORT).show();
                product_date.setText(result);
                handleBarcode(result);
            }
        });
    }

    //TODO; barcode scaning result compare process here
    private void handleBarcode(String result) {
        len = result.length();
        compPRODATE = result.substring(0,3);
        compBARQTY_1 = result.substring(0,5);
        compBARQTY_1 = result.substring(len-4);
        compSSCC = result.substring(0,3);
        if (compPRODATE.contains("91")) {
            product_date.setText(result);// product date (91)2009
        }
        if (compBARQTY_1.contains("240") && compBARQTY_2.contains("30")) {
            barcode_QTY.setText(result);//barcodeQTY (240)1234565433(30)1
        }
        if (compSSCC.contains("00")) {
            sscc_info.setText(result);//sscc (00)1234567890
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
//        if (emdkManager != null) {
//            emdkManager.release();
//            emdkManager= null;
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (emdkManager != null) {
            emdkManager.release();
            emdkManager= null;
        }
    }

    @Override
    public void onClosed() {
        // The EMDK closed unexpectedly. Release all the resources.
        if (emdkManager != null) {
            emdkManager.release();
            emdkManager= null;
        }
        updateStatus("EMDK closed unexpectedly! Please close and restart the application.");
    }

    @Override
    public void onStatus(StatusData statusData) {
        // The status will be returned on multiple cases. Check the state and take the action.
// Get the current state of scanner in background
        ScannerStates state =  statusData.getState();
        String statusStr = "";
        switch (state){
        case IDLE:
        // Scanner is idle and ready to change configuration and submit read.
//        statusStr = statusData.getFriendlyName()+" is   enabled and idle...";
        // Change scanner configuration. This should be done while the scanner is in IDLE state.
        setConfig();
        try {
            // Starts an asynchronous Scan. The method will NOT turn ON the scanner beam,
            //but puts it in a  state in which the scanner can be turned on automatically or by pressing a hardware trigger.
            scanner.read();
        }
        catch (ScannerException e)   {
            updateStatus(e.getMessage());
        }
        break;
        case WAITING:
        // Scanner is waiting for trigger press to scan...
        statusStr = "Scanner is waiting for trigger press...";
        break;
        case SCANNING:
        // Scanning is in progress...
        statusStr = "Scanning...";
        break;
        case DISABLED:
        // Scanner is disabledstatusStr = statusData.getFriendlyName()+" is disabled.";
        break;
        case ERROR:
        // Error has occurred during scanning
        statusStr = "An error has occurred.";
        break;
        default:
        break;
    }
        // Updates TextView with scanner state on UI thread.
        updateStatus(statusStr);
    }

    private void updateStatus(final String status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Update the status text view on UI thread with current scanner state
//                statusTextView.setText(""+  status);
                Toast.makeText(ScannerActivity.this, "" + status, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setConfig() {
        if (scanner != null) {try {
            // Get scanner config
            ScannerConfig config = scanner.getConfig();
            // Enable haptic feedback
            if (config.isParamSupported("config.scanParams.decodeHapticFeedback")) {
                config.scanParams.decodeHapticFeedback = true;
            }
            // Set scanner config
            scanner.setConfig(config);
        } catch (ScannerException e)   {
            updateStatus(e.getMessage());
        }
        }
    }
}
