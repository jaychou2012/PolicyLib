package com.db.policylibdemo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.db.policylib.PermissionPolicy;
import com.db.policylib.Policy;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class MainFragment extends Fragment implements View.OnClickListener, EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks, Policy.PolicyClick {
    public static final int RC_RECORD_AUDIO_PERM = 122;
    private static final String[] RECORD_AUDIO =
            {Manifest.permission.RECORD_AUDIO};
    private List<PermissionPolicy> list;
    private Button btn_audio;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn_audio = getView().findViewById(R.id.btn_audio);
        btn_audio.setOnClickListener(this);
        list = new ArrayList<>();
        PermissionPolicy permissionPolicy = new PermissionPolicy();
        permissionPolicy.setPermission(Manifest.permission.RECORD_AUDIO);
        permissionPolicy.setTitle("录音权限");
        permissionPolicy.setDes("用于录制音频功能。");
        permissionPolicy.setIcon(R.mipmap.icon_record_audio);
        permissionPolicy.setRequest(true);
        list.add(permissionPolicy);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_RECORD_AUDIO_PERM) {
            showToast("设置回调");
            getPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_RECORD_AUDIO_PERM)
    private void getPermissions() {
        if (EasyPermissions.hasPermissions(getContext(), RECORD_AUDIO)) {
            // Have permission, do the thing!
            showToast("已获取权限");
        } else {
            // Request one permission
            EasyPermissions.requestPermissions(this, "权限",
                    RC_RECORD_AUDIO_PERM, list, RECORD_AUDIO);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show(requestCode, list, this);
        } else {
            getPermissions();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_audio:
                getPermissions();
                break;
        }
    }

    @Override
    public void onRationaleAccepted(int requestCode) {

    }

    @Override
    public void onRationaleDenied(int requestCode) {
        showToast("取消授权");
    }

    @Override
    public void policyCancelClick(int reqeustCode) {
        showToast("必要的授权权限被禁止，无法正常使用");
    }

    private void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
}
