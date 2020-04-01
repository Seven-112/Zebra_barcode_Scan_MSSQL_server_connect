package com.connect.mssql.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.connect.mssql.R;

import java.util.ArrayList;
import java.util.List;

public class ScanListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> ordLineNo;
//    private final List<Integer> images;

    public ScanListAdapter(Activity context, ArrayList<String> ordLineNo) {
        super(context, R.layout.scanitem, ordLineNo);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.ordLineNo = ordLineNo;
//        this.images = images;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.scanitem, null,true);
        TextView ord_line_no = (TextView) rowView.findViewById(R.id.li_ordLineNo);
        ImageView scannedStatus = (ImageView) rowView.findViewById(R.id.li_isScannedImg);
        ord_line_no.setText(ordLineNo.get(position));
//        scannedStatus.setImageResource(images.get(position));
        return rowView;

    }
}
