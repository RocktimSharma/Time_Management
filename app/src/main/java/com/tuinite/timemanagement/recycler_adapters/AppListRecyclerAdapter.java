package com.tuinite.timemanagement.recycler_adapters;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuinite.timemanagement.R;

import java.util.List;

public class AppListRecyclerAdapter extends RecyclerView.Adapter<AppListRecyclerAdapter.AppListViewHolder> {

    private List<ApplicationInfo> applicationInfos;

    private Context context;


    public AppListRecyclerAdapter(List<ApplicationInfo> applicationInfos) {
        this.applicationInfos = applicationInfos;
    }


    @NonNull
    @Override
    public AppListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_list_recycler_layout, parent, false);
        context = itemView.getContext();
        return new AppListRecyclerAdapter.AppListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AppListViewHolder holder, int position) {
        PackageManager pm = context.getPackageManager();
        String appName = applicationInfos.get(position).loadLabel(pm).toString();
        holder.appName_tv.setText(appName);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class AppListViewHolder extends RecyclerView.ViewHolder {

        public ImageView appIcon_imv;
        private TextView appName_tv;
        private Switch enableApp_sw;

        public AppListViewHolder(@NonNull View itemView) {
            super(itemView);

            appIcon_imv = (ImageView) itemView.findViewById(R.id.app_list_recycler_app_icon_imv);
            appName_tv = (TextView) itemView.findViewById(R.id.app_list_recycler_app_name_tv);
            enableApp_sw = (Switch) itemView.findViewById(R.id.app_list_recycler_enable_sw);

        }
    }
}
