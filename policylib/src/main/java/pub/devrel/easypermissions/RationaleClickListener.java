package pub.devrel.easypermissions;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import java.util.Arrays;

import pub.devrel.easypermissions.helper.PermissionHelper;

public class RationaleClickListener {

    private Object mHost;
    private RationaleDialogConfig mConfig;
    private EasyPermissions.PermissionCallbacks mCallbacks;
    private EasyPermissions.RationaleCallbacks mRationaleCallbacks;

    RationaleClickListener(RationaleDialogFragmentCompat compatDialogFragment,
                           RationaleDialogConfig config,
                           EasyPermissions.PermissionCallbacks callbacks,
                           EasyPermissions.RationaleCallbacks rationaleCallbacks) {

        mHost = compatDialogFragment.getParentFragment() != null
                ? compatDialogFragment.getParentFragment()
                : compatDialogFragment.getActivity();

        mConfig = config;
        mCallbacks = callbacks;
        mRationaleCallbacks = rationaleCallbacks;

    }

    RationaleClickListener(RationaleDialogFragment dialogFragment,
                           RationaleDialogConfig config,
                           EasyPermissions.PermissionCallbacks callbacks,
                           EasyPermissions.RationaleCallbacks dialogCallback) {

        mHost = dialogFragment.getActivity();

        mConfig = config;
        mCallbacks = callbacks;
        mRationaleCallbacks = dialogCallback;
    }

    public void onClick(int which) {
        int requestCode = mConfig.requestCode;
        if (which == 0) {
            String[] permissions = mConfig.permissions;
            if (mRationaleCallbacks != null) {
                mRationaleCallbacks.onRationaleAccepted(requestCode);
            }
            if (mHost instanceof Fragment) {
                PermissionHelper.newInstance((Fragment) mHost).directRequestPermissions(requestCode, permissions);
            } else if (mHost instanceof Activity) {
                PermissionHelper.newInstance((Activity) mHost).directRequestPermissions(requestCode, permissions);
            } else {
                throw new RuntimeException("Host must be an Activity or Fragment!");
            }
        } else {
            if (mRationaleCallbacks != null) {
                mRationaleCallbacks.onRationaleDenied(requestCode);
            }
//            notifyPermissionDenied();
        }
    }

    private void notifyPermissionDenied() {
        if (mCallbacks != null) {
            mCallbacks.onPermissionsDenied(mConfig.requestCode, Arrays.asList(mConfig.permissions));
        }
    }
}
