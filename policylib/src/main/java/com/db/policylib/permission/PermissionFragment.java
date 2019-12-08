package com.db.policylib.permission;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class PermissionFragment extends Fragment {
    private static String permissions = "permissions";
    private static String request = "request";
    private static PermissionSuit suit;

    public static PermissionFragment newInstance(PermissionSuit permissionSuit, ArrayList<String> permission, int requestCode) {
        suit = permissionSuit;
        Bundle args = new Bundle();
        args.putStringArrayList(permissions, permission);
        args.putInt(request, requestCode);
        PermissionFragment fragment = new PermissionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> list = getArguments().getStringArrayList(permissions);
            requestPermissions(list.toArray(new String[list.size() - 1]), getArguments().getInt(request));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        suit.onRequestPermissionsResult(requestCode, permissions, grantResults);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.remove(this);
        ft.commit();
    }

    public void request(FragmentManager manager) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, this.getClass().getName());
        ft.commit();
    }
}
