package com.example.duoihinhbatchu.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duoihinhbatchu.Models.CauDo;
import com.example.duoihinhbatchu.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CauDoAdapter extends RecyclerView.Adapter<CauDoAdapter.CauHoiViewHolder> {
    private Context context;
    private List<CauDo> cauDoList;
    private onItemClickListener mListener;

    public CauDoAdapter(Context context, List<CauDo> cauDoList) {
        this.context = context;
        this.cauDoList = cauDoList;
    }

    public interface onItemClickListener {
        void onItemClick(int pos, View view);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.mListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<CauDo> list) {
        this.cauDoList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CauHoiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_cauhoi,parent,false);
        return new CauHoiViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CauHoiViewHolder holder, int position) {
        CauDo cauDo = cauDoList.get(position);
        if (cauDo == null){
            return;
        }
        Picasso.get().load(cauDo.getImgUrl()).into(holder.imgView);
        holder.txtDapAn.setText(cauDo.getDapan());
    }

    @Override
    public int getItemCount() {
        if (cauDoList == null) {
            return 0;
        } else return cauDoList.size();

    }

    public class CauHoiViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgView;
        private TextView txtDapAn;

        public CauHoiViewHolder(@NonNull View itemView, onItemClickListener listener) {
            super(itemView);
            imgView = itemView.findViewById(R.id.imgAnh);
            txtDapAn = itemView.findViewById(R.id.txtDapAn);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            listener.onItemClick(pos, v);
                        }
                    }
                }
            });
        }
    }
}
