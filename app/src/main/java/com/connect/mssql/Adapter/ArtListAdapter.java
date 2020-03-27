package com.connect.mssql.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.connect.mssql.R;

import java.util.ArrayList;
import java.util.List;

public class ArtListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final List<String> artNo;
    private final List<String> ordNo;
    private final List<String> artName;
    private final List<Integer> artCount;
    private final List<Integer> scannedPro;

    public ArtListAdapter (Activity context, List<String> artNo, List<String> ordNo, List<String> artName, List<Integer> artCount, List<Integer> scannedPro) {
        super(context, R.layout.artitem, artNo);
        this.context = context;
        this.artNo = artNo;
        this.ordNo = ordNo;
        this.artName = artName;
        this.artCount = artCount;
        this.scannedPro = scannedPro;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.artitem, null,true);
        TextView art_no = (TextView) rowView.findViewById(R.id.li_artNo);
        TextView ord_no = (TextView) rowView.findViewById(R.id.li_ordNo);
        TextView art_name = (TextView) rowView.findViewById(R.id.li_artName);
        TextView art_count = (TextView) rowView.findViewById(R.id.li_artCount);
        TextView scanned_product = (TextView) rowView.findViewById(R.id.li_artBar);

        art_no.setText(artNo.get(position));
        ord_no.setText(ordNo.get(position));
        art_name.setText(artName.get(position));
        art_count.setText(artCount.get(position) + "");
        scanned_product.setText(scannedPro.get(position) + "");
        return rowView;
    }
}
