package com.example.duoihinhbatchu.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.duoihinhbatchu.R;

import java.util.ArrayList;
import java.util.List;

public class DapAnAdapter extends BaseAdapter {

    private Context context;
    int layout;
    List<String> arrDapAn;

    public DapAnAdapter(Context context, int layout, List<String> arrDapAn) {
        this.context = context;
        this.layout = layout;
        this.arrDapAn = arrDapAn;
    }

    @Override
    public int getCount() {
        if (arrDapAn.isEmpty()){
            return 0;
        }
        return arrDapAn.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        private TextView tvWord;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            viewHolder = new ViewHolder();
            viewHolder.tvWord = convertView.findViewById(R.id.tvWord);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvWord.setText(arrDapAn.get(position).toString());
        return convertView;
    }
}
