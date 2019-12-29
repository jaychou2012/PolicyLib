package com.db.policylib;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PolicyAdapter extends RecyclerView.Adapter<PolicyAdapter.PolicyHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<PermissionPolicy> list;

    public PolicyAdapter(Context context, List<PermissionPolicy> lists) {
        this.context = context;
        this.list = lists;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public PolicyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PolicyHolder(layoutInflater.inflate(R.layout.item_policy, parent, false));
    }

    @Override
    public void onBindViewHolder(PolicyHolder holder, int position) {
        holder.tv_title.setText(list.get(position).getTitle());
        holder.tv_des.setText(list.get(position).getDes());
        holder.iv_icon.setImageResource(list.get(position).getIcon());
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class PolicyHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        TextView tv_des;
        ImageView iv_icon;

        PolicyHolder(View view) {
            super(view);
            tv_title = view.findViewById(R.id.tv_title);
            tv_des = view.findViewById(R.id.tv_des);
            iv_icon = view.findViewById(R.id.iv_icon);
        }
    }

}
