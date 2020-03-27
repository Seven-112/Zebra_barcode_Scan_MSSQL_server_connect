package com.connect.mssql;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.connect.mssql.Adapter.ArtListAdapter;
import com.connect.mssql.Adapter.CsmListAdapter;
import com.connect.mssql.Model.AllProducts;

import java.util.ArrayList;
import java.util.List;

public class ArtListActivity extends AppCompatActivity {

    private String CSM_NO = "";
    ListView detailList;
    ArrayList<String> art_no_list = new ArrayList<>();
    ArrayList<String> ord_no_list = new ArrayList<>();
    ArrayList<String> art_name_list = new ArrayList<>();
    ArrayList<Integer>  barcodeQTY = new ArrayList<>();
    ArrayList<String>  productDATE = new ArrayList<>();
    ArrayList<String>  scannedSSCC = new ArrayList<>();
    ArrayList<Integer> countList = new ArrayList<>();

    ArrayList<List> selectedProducts = new ArrayList<>();

    ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artlist);
        detailList = (ListView) findViewById(R.id.artList);
        backBtn = findViewById(R.id.backBtn);
        this.overridePendingTransition(R.anim.animation_enter,
                R.anim.animation_leave);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ArtListActivity.this, CsmListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Intent extra = getIntent();
        CSM_NO = extra.getStringExtra("CSM_NO");

        ArrayList<List> aaaa = new ArrayList<>();

        AllProducts allProducts = new AllProducts();
        aaaa = allProducts.getAllproducts();

        ArrayList<String> art = new ArrayList<>();
        ArrayList<String> ord = new ArrayList<>();
        ArrayList<String> art_name = new ArrayList<>();
        ArrayList<String> count = new ArrayList<>();
        ArrayList<String> proDATE = new ArrayList<>();
        ArrayList<String> barQTY = new ArrayList<>();
        ArrayList<String> sscc = new ArrayList<>();

        for (int i = 0; i < aaaa.size(); i ++) {
            if (aaaa.get(i).get(1).equals(CSM_NO)) {
                art.add((String) aaaa.get(i).get(4));
                ord.add((String) aaaa.get(i).get(2));
                art_name.add((String) aaaa.get(i).get(5));
                count.add((String) aaaa.get(i).get(6));

                barQTY.add((String) aaaa.get(i).get(7));
            }
        }

        int j = 0;
        String compCSM = null;
        String compORD = null;
        String compNAME = null;
        compCSM = (String) art.get(0);
        compORD = (String) ord.get(0);
        compNAME = (String) art_name.get(0);
        boolean isAdded = false;
        int undispQTY_SUM = 0;
        int scannedPRODUCT_SUM = 0;
        String ccnt = "";

        for (int i = 0; i < art.size(); i ++) {

            String cnt = (String) count.get(i);
            if (barQTY.get(i) == null || barQTY.get(i).equals("")) {
                ccnt = "0";
            } else {
                ccnt = barQTY.get(i);
            }

            float cntInt = Float.parseFloat(cnt);
            int i1 = Math.round(cntInt);
            float ccntInt = Float.parseFloat(ccnt);
            int i2 = Math.round(ccntInt);
            if (!compCSM.equals(art.get(i)))
            {
                compCSM = (String) art.get(i);
                compNAME = (String) art_name.get(i);
                j++;
                isAdded = false;
                countList.add(undispQTY_SUM);
                barcodeQTY.add(scannedPRODUCT_SUM);
                undispQTY_SUM = 0;
                scannedPRODUCT_SUM = 0;
            }

            undispQTY_SUM += i1;
            scannedPRODUCT_SUM += i2;
            if (i == art.size() -1) {
                countList.add(undispQTY_SUM);
                barcodeQTY.add(scannedPRODUCT_SUM);
            }

            if (!isAdded)
            {
                art_no_list.add(compCSM);
                ord_no_list.add(compORD);
                art_name_list.add(compNAME);

                isAdded = true;
            }
        }
        Log.v("artNo:", String.valueOf(art_no_list));
        Log.v("ordNo:", String.valueOf(ord_no_list));
        Log.v("artName:", String.valueOf(art_name_list));
        Log.v("countList:", String.valueOf(countList));
        Log.v("barcodeQTY:", String.valueOf(barcodeQTY));

        ArtListAdapter artListAdapter = new ArtListAdapter(ArtListActivity.this, art_no_list, ord_no_list, art_name_list, countList, barcodeQTY);
        detailList.setAdapter(artListAdapter);
        detailList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (countList.get(position) == barcodeQTY.get(position)) {
                    new AlertDialog.Builder(ArtListActivity.this)
                            .setTitle("Warning!")
                            .setMessage("Already scanned")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {
                    Intent intent = new Intent(ArtListActivity.this, ScannerActivity.class);
                    intent.putExtra("CSM_NO", CSM_NO);
                    intent.putExtra("ART_NO", art_no_list.get(position));
                    startActivity(intent);
                }
            }
        });
    }
}
