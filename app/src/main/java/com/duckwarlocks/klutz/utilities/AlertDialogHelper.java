package com.duckwarlocks.klutz.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by ngmat_000 on 7/3/2015.
 */
public class AlertDialogHelper {
    private static AlertDialog.Builder alertDialogBuilder;


    private AlertDialogHelper(){}

    public static AlertDialog buildWarningAlert(Context context, String title, String message, boolean cancelable,String negativeBtnTxt){
        alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(cancelable);
        alertDialogBuilder.setNegativeButton(negativeBtnTxt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return alertDialogBuilder.create();
    }

    public static AlertDialog.Builder buildAlert(Context context, String title, String message, boolean cancelable){
        alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(cancelable);

        return alertDialogBuilder;
    }
}
