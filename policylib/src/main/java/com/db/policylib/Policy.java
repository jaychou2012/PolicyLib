package com.db.policylib;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.db.policylib.permission.PermissionSuit;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class Policy {

    private static volatile Policy instance = null;
    private String title = "应用需要下列权限才可以正常使用";
    public static final String TITLE = "应用需要下列权限才可以正常使用";

    private Policy() {
    }

    public static Policy getInstance() {
        if (instance == null) {
            synchronized (Policy.class) {
                if (instance == null) {
                    instance = new Policy();
                }
            }
        }
        return instance;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void showPermissionDesDialog(Context context, int requestCode, List<PermissionPolicy> list, boolean before,
                                        final PolicyClick policyClick) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (policyClick != null) {
                policyClick.policyCancelClick(requestCode);
            }
            return;
        }
        List<PermissionPolicy> listRequest = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (!EasyPermissions.hasPermissions(context, list.get(i).getPermission())) {
                listRequest.add(list.get(i));
            }
        }
        if (listRequest.size() == 0) {
            if (policyClick != null) {
                policyClick.policyCancelClick(requestCode);
            }
            return;
        }
        if (!before) {
            if (policyClick != null) {
                policyClick.policyCancelClick(requestCode);
            }
            return;
        }
        final Dialog dialog = new Dialog(context, R.style.POLICY_DIALOG);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_policy);
        dialog.show();
        RecyclerView rv_list = dialog.findViewById(R.id.rv_list);
        TextView tv_ok = dialog.findViewById(R.id.tv_ok);
        PolicyAdapter adapter = new PolicyAdapter(context, listRequest);
        rv_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rv_list.setAdapter(adapter);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (policyClick != null) {
                    policyClick.policyCancelClick(requestCode);
                }
            }
        });
    }

    public interface PolicyClick {
        void policyCancelClick(int reqeustCode);
    }

    public boolean hasPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_GRANTED;
    }

    public void getRequestPermission(ArrayList<String> permissions, List<PermissionPolicy> list, RequestPermission requestPermission) {
        int k = 0;
        if (permissions != null && list != null && permissions.size() > 0) {
            for (int i = 0; i < permissions.size(); i++) {
                for (int j = 0; j < list.size(); j++) {
                    if (permissions.get(i).equals(list.get(j).getPermission()) && list.get(j).isRequest()) {
                        k++;
                    }
                }
            }
        }
        if (requestPermission != null && k > 0) {//被拒绝的是必须权限
            requestPermission.request(true);
            k = 0;
        } else {
            requestPermission.request(false);
        }
    }

    public interface RequestPermission {
        void request(boolean showRequest);
    }

    public void showSettingDesDialog(final Context context, int requestCode, List<PermissionPolicy> list,
                                     final PolicyClick policyClick) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (policyClick != null) {
                policyClick.policyCancelClick(requestCode);
            }
            return;
        }
        final Dialog dialog = new Dialog(context, R.style.POLICY_DIALOG);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_policy);
        dialog.show();
        RecyclerView rv_list = dialog.findViewById(R.id.rv_list);
        TextView tv_ok = dialog.findViewById(R.id.tv_ok);
        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
        LinearLayout ll_bottom = dialog.findViewById(R.id.ll_bottom);
        TextView tv_request = dialog.findViewById(R.id.tv_request);
        TextView tv_title = dialog.findViewById(R.id.tv_title);
        TextView tv_tips = dialog.findViewById(R.id.tv_tips);
        ll_bottom.setVisibility(View.VISIBLE);
        tv_request.setVisibility(View.GONE);
        tv_title.setText("应用选择了不再提示，请手动授权");
        tv_tips.setVisibility(View.VISIBLE);
        tv_tips.setText("您已经选择了不再提示，请去应用详情设置->权限里手动授权。");
        PolicyAdapter adapter = new PolicyAdapter(context, list);
        rv_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rv_list.setAdapter(adapter);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        .setData(Uri.fromParts("package", context.getPackageName(), null));
                ((Activity) context).startActivityForResult(intent, requestCode > 0 ? requestCode :
                        AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE);
            }
        });
        tv_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        .setData(Uri.fromParts("package", context.getPackageName(), null));
                ((Activity) context).startActivityForResult(intent, requestCode > 0 ? requestCode :
                        AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (policyClick != null) {
                    policyClick.policyCancelClick(requestCode);
                }
            }
        });
    }

    public void showPermissionDesSuitDialog(final Context context, final List<PermissionPolicy> list, boolean before,
                                            final RequestTipsPermission requestTipsPermission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (requestTipsPermission != null) {
                requestTipsPermission.hasRequest();
            }
            return;
        }
        List<PermissionPolicy> listRequest = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (!EasyPermissions.hasPermissions(context, list.get(i).getPermission())) {
                listRequest.add(list.get(i));
            }
        }
        if (listRequest.size() == 0) {
            if (requestTipsPermission != null) {
                requestTipsPermission.hasRequest();
            }
            return;
        }
        final ArrayList<String> stringLists = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).getPermission().length; j++) {
                stringLists.add(list.get(i).getPermission()[j]);
            }
        }
        if (!before) {
            PermissionSuit.with((Activity) context).setPermissions(stringLists).excute(new PermissionListener(context, list, requestTipsPermission));
            return;
        }
        final Dialog dialog = new Dialog(context, R.style.POLICY_DIALOG);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_policy);
        dialog.show();
        RecyclerView rv_list = dialog.findViewById(R.id.rv_list);
        TextView tv_ok = dialog.findViewById(R.id.tv_ok);
        PolicyAdapter adapter = new PolicyAdapter(context, listRequest);
        rv_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rv_list.setAdapter(adapter);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                PermissionSuit.with((Activity) context).setPermissions(stringLists).excute(new PermissionListener(context, list, requestTipsPermission));
            }
        });
    }

    class PermissionListener implements PermissionSuit.PermissionListener {
        List<PermissionPolicy> list;
        RequestTipsPermission requestTipsPermission;
        Context context;

        public PermissionListener(Context context, List<PermissionPolicy> list, RequestTipsPermission requestTipsPermission) {
            this.list = list;
            this.requestTipsPermission = requestTipsPermission;
            this.context = context;
        }

        @Override
        public void getPermission(ArrayList<String> permission) {
            if (requestTipsPermission != null) {
                requestTipsPermission.hasRequest();
            }
        }

        @Override
        public void getAllPermission(ArrayList<String> permission) {
            if (requestTipsPermission != null) {
                requestTipsPermission.hasRequest();
            }
        }

        @Override
        public void noPermision(ArrayList<String> permission) {
            getRequestPermission(permission, list, new Policy.RequestPermission() {
                @Override
                public void request(boolean showRequest) {
                    if (showRequest) {
                        showPermissionDesSuitDialog(context, list, true, requestTipsPermission);
                    } else {
                        if (requestTipsPermission != null) {
                            requestTipsPermission.hasRequest();
                        }
                    }
                }
            });
        }
    }

    public interface RequestTipsPermission {
        void hasRequest();
    }

    public void showRuleDialog(final Context context, String title, String text, int tagColor, final RuleListener ruleListener) {
        if (hasShowRule(context)) {
            if (ruleListener != null) {
                ruleListener.rule(true);
            }
            return;
        }
        final Dialog dialog = new Dialog(context, R.style.POLICY_DIALOG);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_rule);
        dialog.show();
        TextView tv_ok = dialog.findViewById(R.id.tv_ok);
        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
        TextView tv_title = dialog.findViewById(R.id.tv_title);
        TextView tv_text = dialog.findViewById(R.id.tv_text);
        tv_title.setText(title);
        String tag1 = "《";
        String tag2 = "》";
        int firstIndex = text.indexOf(tag1);
        int secondIndex = text.indexOf(tag2) + 1;
        int thirdIndex = text.indexOf(tag1, firstIndex + 1);

        SpannableStringBuilder style = new SpannableStringBuilder();
        style.append(text);
        ClickableSpan clickableSpanOne = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (ruleListener != null) {
                    ruleListener.oneClick();
                }
            }
        };

        style.setSpan(clickableSpanOne, firstIndex, secondIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_text.setText(style);

        ForegroundColorSpan foregroundColorSpanOne = new ForegroundColorSpan(context.getResources().getColor(tagColor));
        style.setSpan(foregroundColorSpanOne, firstIndex, secondIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_text.setMovementMethod(LinkMovementMethod.getInstance());
        tv_text.setText(style);

        if (thirdIndex != -1) {
            ClickableSpan clickableSpanTwo = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    if (ruleListener != null) {
                        ruleListener.twoClick();
                    }
                }
            };
            style.setSpan(clickableSpanTwo, thirdIndex, text.lastIndexOf(tag2) + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_text.setText(style);

            ForegroundColorSpan foregroundColorSpanTwo = new ForegroundColorSpan(context.getResources().getColor(tagColor));
            style.setSpan(foregroundColorSpanTwo, thirdIndex, text.lastIndexOf(tag2) + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_text.setMovementMethod(LinkMovementMethod.getInstance());
            tv_text.setText(style);
        }

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (ruleListener != null) {
                    ruleListener.rule(true);
                    putShowRule(context);
                }
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (ruleListener != null) {
                    ruleListener.rule(false);
                }
            }
        });
    }

    public interface RuleListener {
        void rule(boolean agree);

        void oneClick();

        void twoClick();
    }

    public boolean hasRefusedPermission(Context mContexts, String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContexts, permission);
    }

    public boolean getRefusedList(Context context, ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            String permissionItem = list.get(i);
            if (!hasPermission(context, permissionItem) &&
                    hasRefusedPermission(context, permissionItem)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasShowRule(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("rule", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("rule", false);
    }

    public void putShowRule(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("rule", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("rule", true).commit();
    }

    public void clearShowRule(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("rule", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("rule", false).commit();
    }

    public void putString(Context context, String permission) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("rule", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(permission, true).commit();
    }

    public boolean getString(Context context, String permission) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("rule", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(permission, false);
    }

    public boolean getStringList(Context context, @NonNull String... perms) {
        for (int i = 0; i < perms.length; i++) {
            if (!getString(context, perms[i])) {
                return false;
            }
        }
        return true;
    }
}
