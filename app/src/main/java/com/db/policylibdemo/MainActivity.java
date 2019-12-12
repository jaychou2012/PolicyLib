package com.db.policylibdemo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.db.policylib.PermissionPolicy;
import com.db.policylib.Policy;
import com.db.policylib.permission.PermissionSuit;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Policy.PolicyClick, PermissionSuit.PermissionListener, Policy.RequestTipsPermission, Policy.RuleListener {
    private TextView tv_text;
    private Button btn_request, btn_request_in, btn_request_before;
    private List<PermissionPolicy> list;
    private List<PermissionPolicy> policyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRule();
    }

    private void initRule() {
        //展示用户协议和隐私政策
        Policy.getInstance().showRuleDialog(this, "用户协议和隐私政策概要", "请阅读《用户协议》和《隐私政策》", R.color.link, this);
    }

    @Override
    public void rule(boolean agree) {
        if (agree) {
            //请求权限（先弹窗提示，后申请权限）
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
        System.out.println("已经初始化");
        tv_text = findViewById(R.id.tv_text);
        btn_request = findViewById(R.id.btn_request);
        btn_request_in = findViewById(R.id.btn_request_in);
        btn_request_before = findViewById(R.id.btn_request_before);
        tv_text.setText("必要权限已经可以使用");

        policyList = new ArrayList<>();
        PermissionPolicy permissionPolicy = new PermissionPolicy();
        permissionPolicy.setPermission(Manifest.permission.RECORD_AUDIO);
        permissionPolicy.setTitle("录音权限");
        permissionPolicy.setDes("用于语音识别和音频录制。");
        permissionPolicy.setIcon(R.mipmap.icon_record_audio);
        permissionPolicy.setRequest(true);
        policyList.add(permissionPolicy);
        // 使用自定义的权限申请框架的方法，先申请权限，拒绝后弹窗提示
        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAfterPermission();
            }
        });
        // 使用内置的权限申请方法，先申请权限，拒绝后弹窗提示
        btn_request_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Policy.getInstance().showPermissionDesSuitDialog(MainActivity.this, policyList, false, new Policy.RequestTipsPermission() {
                    @Override
                    public void hasRequest() {
                        initView();
                    }
                });
            }
        });
        // 使用内置的权限申请方法，先弹窗提示，然后申请权限
        btn_request_before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Policy.getInstance().showPermissionDesSuitDialog(MainActivity.this, policyList, true, new Policy.RequestTipsPermission() {
                    @Override
                    public void hasRequest() {
                        initView();
                    }
                });
            }
        });
    }

    // 使用自定义的权限申请框架的方法，先弹窗提示，然后申请权限
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
        list.add(permissionPolicy);
        list.add(permissionPolicy1);
        Policy.getInstance().showPermissionDesDialog(this, list, true, this);
    }

    private void showAfterPolicyDialog() {
        Policy.getInstance().showPermissionDesDialog(this, policyList, false, new Policy.PolicyClick() {
            @Override
            public void policyClick() {
                getAfterPermission();
            }
        });
    }

    private void getAfterPermission() {
        policyList = new ArrayList<>();
        PermissionPolicy permissionPolicy = new PermissionPolicy();
        permissionPolicy.setPermission(Manifest.permission.RECORD_AUDIO);
        permissionPolicy.setTitle("录音权限");
        permissionPolicy.setDes("用于语音识别和音频录制。");
        permissionPolicy.setIcon(R.mipmap.icon_record_audio);
        permissionPolicy.setRequest(true);
        policyList.add(permissionPolicy);
        PermissionSuit.with(MainActivity.this).setPermissions(Manifest.permission.RECORD_AUDIO).excute(new PermissionSuit.PermissionListener() {
            @Override
            public void getPermission(ArrayList<String> permission) {

            }

            @Override
            public void getAllPermission(ArrayList<String> permission) {

            }

            @Override
            public void noPermision(ArrayList<String> permission) {
                Policy.getInstance().getRequestPermission(permission, policyList, new Policy.RequestPermission() {
                    @Override
                    public void request(boolean showRequest) {
                        if (showRequest) {
                            showAfterPolicyDialog();
                        } else {
                            initView();
                        }
                    }
                });
            }
        });
    }

    //必要权限通过后回调的方法，这里可以正常初始化布局和数据了
    @Override
    public void hasRequest() {
        initView();
    }

    @Override
    public void policyClick() {
        PermissionSuit.with(this).setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE).excute(this);
    }

    @Override
    public void getPermission(ArrayList<String> permission) {
        showToast("有部分权限");
    }

    @Override
    public void getAllPermission(ArrayList<String> permission) {
        //必要权限通过后回调的方法，这里可以正常初始化布局和数据了
        showToast("有权限");
        initView();
    }

    @Override
    public void noPermision(ArrayList<String> permission) {
        showToast("无权限");
        Policy.getInstance().getRequestPermission(permission, list, new Policy.RequestPermission() {
            @Override
            public void request(boolean showRequest) {
                if (showRequest) {
                    showBeforePolicyDialog();
                } else {
                    //必要权限通过后回调的方法，这里可以正常初始化布局和数据了
                    initView();
                }
            }
        });
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}
