package com.connect.mssql;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.connect.mssql.Adapter.CsmListAdapter;
import com.connect.mssql.Model.AllProducts;

import java.util.ArrayList;
import java.util.List;

public class CsmListActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> csmList = new ArrayList<>();
    ArrayList<Integer>  barcodeQTY = new ArrayList<>();
    ArrayList<String>  productDATE = new ArrayList<>();
    ArrayList<String>  scannedSSCC = new ArrayList<>();
    ArrayList<Integer> countList = new ArrayList<>();
    ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csmlist);
        listView = findViewById(R.id.csmList);
        backBtn = findViewById(R.id.backBtn);
        this.overridePendingTransition(R.anim.animation_enter,
                R.anim.animation_leave);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CsmListActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ArrayList<List> aaaa = new ArrayList<>();

        AllProducts allProducts = new AllProducts();
        aaaa = allProducts.getAllproducts();

        ArrayList<String> csm = new ArrayList<>();
        ArrayList<String> count = new ArrayList<>();
        final ArrayList<String> proDATE = new ArrayList<>();
        ArrayList<String> barQTY = new ArrayList<>();
        ArrayList<String> sscc = new ArrayList<>();

        for (int i = 0; i < aaaa.size(); i ++) {
            csm.add((String) aaaa.get(i).get(1));
            count.add((String) aaaa.get(i).get(6));
            barQTY.add((String) aaaa.get(i).get(7));
        }


        int j = 0;
        String compCSM = null;
        compCSM = (String) csm.get(0);
        boolean isAdded = false;
        int undispQTY_SUM = 0;
        int scannedPRODUCT_SUM = 0;
        String ccnt = "";
        Log.v("zzz", String.valueOf(barQTY.get(3)));

//        Toast.makeText(CsmListActivity.this,String.valueOf(barQTY.get(2))+"",Toast.LENGTH_SHORT).show();
        for (int i = 0; i < csm.size(); i ++) {

            String cnt = (String) count.get(i);
            if (barQTY.get(i) == null || barQTY.get(i).equals("")) {
                ccnt = "0.0";
            } else {
                ccnt = barQTY.get(i);
            }

            float cntInt = Float.parseFloat(cnt);
            int i1 = Math.round(cntInt);
            float ccntInt = Float.parseFloat(ccnt);
            int i2 = Math.round(ccntInt);
            if (!compCSM.equals(csm.get(i)))
            {
                compCSM = (String) csm.get(i);
                j++;
                isAdded = false;
                countList.add(undispQTY_SUM);
                barcodeQTY.add(scannedPRODUCT_SUM);
                undispQTY_SUM = 0;
                scannedPRODUCT_SUM = 0;
            }

            undispQTY_SUM += i1;
            scannedPRODUCT_SUM += i2;
            if (i == csm.size() -1) {
                countList.add(undispQTY_SUM);
                barcodeQTY.add(scannedPRODUCT_SUM);
            }

            if (!isAdded)
            {
                csmList.add(compCSM);

                isAdded = true;
            }



        }
//        Log.v("TAG", String.valueOf(csmList));
        final CsmListAdapter csmListAdapter = new CsmListAdapter(CsmListActivity.this, csmList, countList, barcodeQTY);
        listView.setAdapter(csmListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.v("TAG", csmList.get(i));
                if (countList.get(i) == barcodeQTY.get(i)) {
                    new AlertDialog.Builder(CsmListActivity.this)
                            .setTitle("Warning!")
                            .setMessage("Already scanned")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }else {
                    Intent intent1 = new Intent(CsmListActivity.this, ArtListActivity.class);
                    intent1.putExtra("CSM_NO", csmList.get(i));

                    startActivity(intent1);
                }
            }
        });
    }
}
