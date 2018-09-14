package com.example.denishaamrutiya.demoscanbarcode.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.denishaamrutiya.demoscanbarcode.MainActivity;
import com.example.denishaamrutiya.demoscanbarcode.R;
import com.example.denishaamrutiya.demoscanbarcode.model.ScanningItemPojo;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class ScanningItemAdapter extends RealmRecyclerViewAdapter<ScanningItemPojo,
        ScanningItemAdapter.ScanningItemPojoViewHolder> {

    private Context context;
    private OrderedRealmCollection<ScanningItemPojo> itemPojos;



    public ScanningItemAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<ScanningItemPojo> scanningItemPojos) {
        super(scanningItemPojos, true);
        this.context = context;
        this.itemPojos = scanningItemPojos;
    }

    @Override
    public ScanningItemAdapter.ScanningItemPojoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_scan_item_value, parent, false);
        return new ScanningItemPojoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ScanningItemAdapter.ScanningItemPojoViewHolder holder, int position) {
        final ScanningItemPojo pojoViewHolder = itemPojos.get(position);
        //holder.id.setText(String.valueOf(pojoViewHolder.getId()));
        holder.title.setText(pojoViewHolder.getTitle());
        holder.quntity.setText(pojoViewHolder.getQuntity());
    }

    public class ScanningItemPojoViewHolder extends RecyclerView.ViewHolder {
        public TextView id, title, quntity;
        public ScanningItemPojoViewHolder(View itemView) {
            super(itemView);

            //id = (TextView) view.findViewById(R.id.text_view_id);
            title = (TextView) itemView.findViewById(R.id.txtItemName);
            quntity = (TextView) itemView.findViewById(R.id.txtTotalItem);
        }
    }
}
