package com.app.qrcodeapplication.ui.scan;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

public class PermissionsDelegate {

    private static final int REQUEST_CODE = 10;
    private final Fragment fragment;

    public PermissionsDelegate(Fragment fragment) {
        this.fragment = fragment;
    }

    public boolean hasCameraPermission() {
        int permissionCheckResult = ContextCompat.checkSelfPermission(
                fragment.getContext(),
                Manifest.permission.CAMERA
        );
        return permissionCheckResult == PackageManager.PERMISSION_GRANTED;
    }

    public void requestCameraPermission() {
        fragment.requestPermissions(
                new String[]{Manifest.permission.CAMERA},
                REQUEST_CODE
        );
    }

    public boolean resultGranted(int requestCode,
                          String[] permissions,
                          int[] grantResults) {

        if (requestCode != REQUEST_CODE) {
            return false;
        }

        if (grantResults.length < 1) {
            return false;
        }
        if (!(permissions[0].equals(Manifest.permission.CAMERA))) {
            return false;
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        return false;
    }
}
