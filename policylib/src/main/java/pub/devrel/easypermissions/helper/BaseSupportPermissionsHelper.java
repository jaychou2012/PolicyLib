package pub.devrel.easypermissions.helper;

import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.db.policylib.PermissionPolicy;

import java.util.List;

import pub.devrel.easypermissions.RationaleDialogFragmentCompat;

/**
 * Implementation of {@link PermissionHelper} for Support Library host classes.
 */
public abstract class BaseSupportPermissionsHelper<T> extends PermissionHelper<T> {

    private static final String TAG = "BSPermissionsHelper";

    public BaseSupportPermissionsHelper(@NonNull T host) {
        super(host);
    }

    public abstract FragmentManager getSupportFragmentManager();

    @Override
    public void showRequestPermissionRationale(@NonNull String rationale,
                                               @NonNull String positiveButton,
                                               @NonNull String negativeButton,
                                               @StyleRes int theme,
                                               int requestCode, List<PermissionPolicy> list,
                                               @NonNull String... perms) {

        FragmentManager fm = getSupportFragmentManager();

        // Check if fragment is already showing
        Fragment fragment = fm.findFragmentByTag(RationaleDialogFragmentCompat.TAG);
        if (fragment instanceof RationaleDialogFragmentCompat) {
            Log.d(TAG, "Found existing fragment, not showing rationale.");
            return;
        }

        RationaleDialogFragmentCompat
                .newInstance(rationale, positiveButton, negativeButton, theme, requestCode, list, perms)
                .showAllowingStateLoss(fm, RationaleDialogFragmentCompat.TAG);
    }
}
