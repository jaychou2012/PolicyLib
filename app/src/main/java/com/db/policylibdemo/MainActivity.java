package com.db.policylibdemo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.db.policylib.PermissionPolicy;
import com.db.policylib.Policy;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements Policy.RuleListener, EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks, Policy.PolicyClick {
    private TextView tv_text;
    private List<PermissionPolicy> list;
    private static final String[] STORAGE_AND_PHONE =
            {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};
    private static final int RC_STORAGE_PHONE_PERM = 125;
    private String text = "欢迎使用XX应用！我们将通过XXXXXX《用户协议》和《隐私政策》帮助您了解我们收集、使用、存储和共享个人信息的情况，以及您所享有的相关权利。\n\n" +
            "• 为了向您提供XX音频文件生成存储、语音转文字、头像上传、用户注册等功能服务，我们需要使用您的一些存储权限、音视频录制权限、相机权限、获取设备信息等权限及信息。\n" +
            "• 您可以在个人中心修改、更正您的信息，也可以自己注销账户。\n" +
            "• 我们会采用业界领先的安全技术保护好您的个人信息。\n\n" +
            "您可以通过阅读完整版用户隐私政策，了解个人信息类型与用途的对应关系等更加详尽的个人信息处理规则。\n" +
            "如您同意，请点击“同意”开始接受我们的服务。";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRule();
    }

    private void initRule() {
        //展示用户协议和隐私政策
        Policy.getInstance().showRuleDialog(this, "用户协议和隐私政策概要", text, R.color.link, this);
    }

    @Override
    public void rule(boolean agree) {
        if (agree) {
            showBeforePolicyDialog();
        } else {
            MainActivity.this.finish();
        }
    }

    @Override
    public void oneClick() {
        Intent intent = new Intent(this, RuleActivity.class);
        intent.putExtra("privateRule", false);
        intent.putExtra("url", "file:////android_asset/userRule.html");
        startActivity(intent);
    }

    @Override
    public void twoClick() {
        Intent intent = new Intent(this, RuleActivity.class);
        intent.putExtra("privateRule", true);
        intent.putExtra("url", "file:////android_asset/privateRule.html");
        startActivity(intent);
    }

    private void initView() {
        tv_text = findViewById(R.id.tv_text);
        tv_text.setText("必要权限已经可以使用");
    }

    private void showBeforePolicyDialog() {
        list = new ArrayList<>();
        PermissionPolicy permissionPolicy = new PermissionPolicy();
        permissionPolicy.setPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionPolicy.setTitle("存储权限");
        permissionPolicy.setDes("缓存图片和视频，降低流量消耗。");
        permissionPolicy.setIcon(R.mipmap.icon_storage);
        permissionPolicy.setRequest(true);

        PermissionPolicy permissionPolicy1 = new PermissionPolicy();
        permissionPolicy1.setPermission(Manifest.permission.READ_PHONE_STATE);
        permissionPolicy1.setTitle("手机/电话权限");
        permissionPolicy1.setDes("校验IMEI&IMSI码，防止账号被盗。");
        permissionPolicy1.setIcon(R.mipmap.icon_tel);
        permissionPolicy1.setRequest(false);
        if (!Policy.getInstance().hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            list.add(permissionPolicy);
        }
        if (!Policy.getInstance().hasPermission(this, Manifest.permission.READ_PHONE_STATE)) {
            list.add(permissionPolicy1);
        }
        if (list.size() == 0) {
            initView();
            return;
        }
        getPermission();
    }

    @AfterPermissionGranted(RC_STORAGE_PHONE_PERM)
    public void getPermission() {
        if (EasyPermissions.hasPermissions(this, STORAGE_AND_PHONE)) {
            initView();
        } else {
            EasyPermissions.requestPermissions(
                    MainActivity.this, "权限",
                    RC_STORAGE_PHONE_PERM, list,
                    STORAGE_AND_PHONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            getPermission();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show(list, this);
        } else {
            getPermission();
        }
    }

    @Override
    public void onRationaleAccepted(int requestCode) {

    }

    @Override
    public void onRationaleDenied(int requestCode) {
        showToast("必要的权限被禁止，无法正常使用");
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void policyCancelClick() {

    }
}
