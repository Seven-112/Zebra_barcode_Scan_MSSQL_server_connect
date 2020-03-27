package com.connect.mssql.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.connect.mssql.R;

import java.util.List;

public class CsmListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final List<String> csmNo;
    private final List<Integer> csmCount;
    private final List<Integer> barcodeQTY;

    public CsmListAdapter(Activity context, List<String> csmNo, List<Integer> csmCount, List<Integer> barcodeQTY) {
        super(context, R.layout.csmitem, csmNo);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.csmNo=csmNo;
        this.csmCount=csmCount;
        this.barcodeQTY = barcodeQTY;
    }
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        @SuppressLint("ViewHolder") View rowView=inflater.inflate(R.layout.csmitem, null,true);
        TextView csmTitle = (TextView) rowView.findViewById(R.id.li_csmNo);
        TextView countTitle = (TextView) rowView.findViewById(R.id.li_csmCount);
        TextView barCount = (TextView) rowView.findViewById(R.id.li_barCount);
        csmTitle.setText(csmNo.get(position));
        countTitle.setText(csmCount.get(position) + "");
        barCount.setText(barcodeQTY.get(position) + "");
        return rowView;

    }
}

