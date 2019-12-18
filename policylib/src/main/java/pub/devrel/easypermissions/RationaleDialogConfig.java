package pub.devrel.easypermissions;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.db.policylib.PermissionPolicy;
import com.db.policylib.PolicyAdapter;
import com.db.policylib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration for either {@link RationaleDialogFragment} or {@link RationaleDialogFragmentCompat}.
 */
class RationaleDialogConfig {

    private static final String KEY_POSITIVE_BUTTON = "positiveButton";
    private static final String KEY_NEGATIVE_BUTTON = "negativeButton";
    private static final String KEY_RATIONALE_MESSAGE = "rationaleMsg";
    private static final String KEY_THEME = "theme";
    private static final String KEY_REQUEST_CODE = "requestCode";
    private static final String KEY_PERMISSIONS = "permissions";
    private static final String KEY_PERMISSION_LIST = "permissionList";
    String positiveButton;
    String negativeButton;
    int theme;
    int requestCode;
    String rationaleMsg;
    String[] permissions;
    List<PermissionPolicy> lists;

    RationaleDialogConfig(@NonNull String positiveButton,
                          @NonNull String negativeButton,
                          @NonNull String rationaleMsg,
                          @StyleRes int theme,
                          int requestCode, List<PermissionPolicy> list,
                          @NonNull String[] permissions) {

        this.positiveButton = positiveButton;
        this.negativeButton = negativeButton;
        this.rationaleMsg = rationaleMsg;
        this.theme = theme;
        this.lists = list;
        this.requestCode = requestCode;
        this.permissions = permissions;
    }

    RationaleDialogConfig(Bundle bundle) {
        positiveButton = bundle.getString(KEY_POSITIVE_BUTTON);
        negativeButton = bundle.getString(KEY_NEGATIVE_BUTTON);
        rationaleMsg = bundle.getString(KEY_RATIONALE_MESSAGE);
        theme = bundle.getInt(KEY_THEME);
        requestCode = bundle.getInt(KEY_REQUEST_CODE);
        lists = (List<PermissionPolicy>) bundle.getSerializable(KEY_PERMISSION_LIST);
        permissions = bundle.getStringArray(KEY_PERMISSIONS);
    }

    Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_POSITIVE_BUTTON, positiveButton);
        bundle.putString(KEY_NEGATIVE_BUTTON, negativeButton);
        bundle.putString(KEY_RATIONALE_MESSAGE, rationaleMsg);
        bundle.putInt(KEY_THEME, theme);
        bundle.putInt(KEY_REQUEST_CODE, requestCode);
        bundle.putSerializable(KEY_PERMISSION_LIST, (ArrayList<PermissionPolicy>) lists);
        bundle.putStringArray(KEY_PERMISSIONS, permissions);

        return bundle;
    }

    Dialog createSupportDialog(Context context, final RationaleClickListener listener) {
        final Dialog dialog = new Dialog(context, R.style.POLICY_DIALOG);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_policy);
        dialog.show();
        LinearLayout ll_bottom = dialog.findViewById(R.id.ll_bottom);
        RecyclerView rv_list = dialog.findViewById(R.id.rv_list);
        TextView tv_request = dialog.findViewById(R.id.tv_request);
        TextView tv_ok = dialog.findViewById(R.id.tv_ok);
        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
        ll_bottom.setVisibility(View.VISIBLE);
        tv_request.setVisibility(View.GONE);
        PolicyAdapter adapter = new PolicyAdapter(context, lists);
        rv_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rv_list.setAdapter(adapter);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onClick(0);
                }
            }
        });
        tv_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onClick(0);
                }
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onClick(1);
                }
            }
        });
        return dialog;
    }

    Dialog createFrameworkDialog(Context context, final RationaleClickListener listener) {
        final Dialog dialog = new Dialog(context, R.style.POLICY_DIALOG);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_policy);
        dialog.show();
        LinearLayout ll_bottom = dialog.findViewById(R.id.ll_bottom);
        RecyclerView rv_list = dialog.findViewById(R.id.rv_list);
        TextView tv_request = dialog.findViewById(R.id.tv_request);
        TextView tv_ok = dialog.findViewById(R.id.tv_ok);
        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
        ll_bottom.setVisibility(View.VISIBLE);
        tv_request.setVisibility(View.GONE);
        PolicyAdapter adapter = new PolicyAdapter(context, lists);
        rv_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rv_list.setAdapter(adapter);
        tv_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onClick(0);
                }
            }
        });
        tv_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onClick(0);
                }
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onClick(1);
                }
            }
        });
        return dialog;
    }

}
