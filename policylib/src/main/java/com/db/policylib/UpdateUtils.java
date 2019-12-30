package com.db.policylib;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class UpdateUtils {
    private static volatile UpdateUtils instance = null;
    private Context context;
    public String SP_NAME = "update";

    private UpdateUtils() {
    }

    public static UpdateUtils getInstance() {
        if (instance == null) {
            synchronized (UpdateUtils.class) {
                if (instance == null) {
                    instance = new UpdateUtils();
                }
            }
        }
        return instance;
    }

    public void showUpdate(Context context, String content, String url, boolean force) {
        this.context = context;
        final Dialog dialog = new Dialog(context, R.style.POLICY_DIALOG);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_update);
        dialog.show();
        TextView tv_ok = dialog.findViewById(R.id.tv_ok);
        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
        TextView tv_title = dialog.findViewById(R.id.tv_title);
        TextView tv_text = dialog.findViewById(R.id.tv_text);
        LinearLayout ll_bottom = dialog.findViewById(R.id.ll_bottom);
        TextView tv_update = dialog.findViewById(R.id.tv_update);
        tv_text.setText(content);
        if (force) {
            ll_bottom.setVisibility(View.GONE);
            tv_update.setVisibility(View.VISIBLE);
        } else {
            ll_bottom.setVisibility(View.VISIBLE);
            tv_update.setVisibility(View.GONE);
        }
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                downLoadApk(context, url);
            }
        });
        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                downLoadApk(context, url);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void showUpdate(Context context, String title, String content, String url, boolean force) {
        this.context = context;
        final Dialog dialog = new Dialog(context, R.style.POLICY_DIALOG);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_update);
        dialog.show();
        TextView tv_ok = dialog.findViewById(R.id.tv_ok);
        TextView tv_cancel = dialog.findViewById(R.id.tv_cancel);
        TextView tv_title = dialog.findViewById(R.id.tv_title);
        TextView tv_text = dialog.findViewById(R.id.tv_text);
        LinearLayout ll_bottom = dialog.findViewById(R.id.ll_bottom);
        TextView tv_update = dialog.findViewById(R.id.tv_update);
        tv_title.setText(title);
        tv_text.setText(content);
        if (force) {
            ll_bottom.setVisibility(View.GONE);
            tv_update.setVisibility(View.VISIBLE);
        } else {
            ll_bottom.setVisibility(View.VISIBLE);
            tv_update.setVisibility(View.GONE);
        }
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                downLoadApk(context, url);
            }
        });
        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                downLoadApk(context, url);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void downLoadApk(Context context, String url) {
        Toast.makeText(context, "请在通知栏查看下载进度", Toast.LENGTH_SHORT).show();
        File file = new File(context.getExternalFilesDir(null).getPath() + "/app.apk");
        if (file.exists()) {
            file.delete();
        }
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(
                Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        downloadManager.remove(getDownloadId(context));
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setVisibleInDownloadsUi(true);
        request.setTitle("正在下载");
        request.setDestinationInExternalFilesDir(context, null, "app.apk");
        long id = downloadManager.enqueue(request);
        putDownloadId(context, id);
    }

    public void putDownloadId(Context context, long id) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putLong("downloadId", id).commit();
    }

    public long getDownloadId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return sp.getLong("downloadId", 0);
    }

}
