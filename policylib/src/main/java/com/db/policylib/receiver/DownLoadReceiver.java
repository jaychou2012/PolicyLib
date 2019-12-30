package com.db.policylib.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.db.policylib.UpdateUtils;

import java.io.File;

public class DownLoadReceiver extends BroadcastReceiver {
    private long id = 0;
    private DownloadManager manager;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            id = UpdateUtils.getInstance().getDownloadId(context);
            installFile(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void installFile(final Context context) {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);
        manager = (DownloadManager) context.getSystemService(
                Context.DOWNLOAD_SERVICE);
        Cursor cursor = manager.query(query);
        if (cursor.moveToFirst()) {
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                //下载完成
                case DownloadManager.STATUS_SUCCESSFUL:
                    //下载完成安装APK
                    installAPK(context);
                    cursor.close();
                    break;
                //下载失败
                case DownloadManager.STATUS_FAILED:
                    Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
                    cursor.close();
                    context.unregisterReceiver(this);
                    break;
            }
        }
    }

    private void installAPK(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            File fileLocation = new File(context.getExternalFilesDir(null).getPath() + "/app.apk");
            Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", fileLocation);
            Intent installIntent = new Intent(Intent.ACTION_VIEW);
            installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
            context.startActivity(installIntent);
        } else {
            File fileLocation = new File(context.getExternalFilesDir(null).getPath() + "/app.apk");
            Intent installIntent = new Intent(Intent.ACTION_VIEW);
            installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            installIntent.setDataAndType(Uri.fromFile(fileLocation),
                    "application/vnd.android.package-archive");
            context.startActivity(installIntent);
        }
    }
}
