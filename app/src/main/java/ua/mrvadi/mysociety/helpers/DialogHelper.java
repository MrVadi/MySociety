package ua.mrvadi.mysociety.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;

import ua.mrvadi.mysociety.R;

/**
 * Created by mrvadi on 07.10.16.
 */
public class DialogHelper {

    public static void showRationale(Context context, int stringRes,
                                     DialogInterface.OnClickListener positive) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(stringRes)
                .setPositiveButton("OK", positive)
                .show();
    }

    public static void showConfirmationDialog(Context context, int resMessage,
                                     DialogInterface.OnClickListener positive) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(resMessage)
                .setPositiveButton("Delete", positive)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }
    public static void showConfirmationDialog(Context context, String message,
                                     DialogInterface.OnClickListener positive) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setPositiveButton("Delete", positive)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }
    public static void goToSettingsSnackbar(View view) {
        Resources resources = view.getContext().getResources();
        Snackbar.make(view, resources.getText(R.string.access_permission), Snackbar.LENGTH_LONG)
                .setAction(resources.getString(R.string.action_settings),
                        new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                             goToSettings(view.getContext());
                    }
                }).show();
    }

    private static void goToSettings(Context context) {

        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + context.getPackageName()));
        context.startActivity(appSettingsIntent);
    }
}

