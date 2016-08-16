package br.com.v8developmentstudio.rccguarulhos.permissions;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import br.com.v8developmentstudio.rccguarulhos.activitys.DescricaoActivity;

/**
 * Created by cleiton.dantas on 16/08/2016.
 */
public class PermissionService {
    private Context context;
    private Activity activity;
    public static final String TAG = "LOG";
    public static final int REQUEST_PERMISSIONS_CODE = 128;

    public PermissionService(Context context,Activity activity){
        this.context = context;
        this.activity = activity;
    }



    public void callGetContatos(View view) {
        Log.i(TAG, "callGetContatos()");
        if( ContextCompat.checkSelfPermission( activity, Manifest.permission.GET_ACCOUNTS ) != PackageManager.PERMISSION_GRANTED ){
            if( ActivityCompat.shouldShowRequestPermissionRationale( activity, Manifest.permission.GET_ACCOUNTS ) ){
                callDialog( "É preciso a permission GET_ACCOUNTS para apresentação do content.", new String[]{Manifest.permission.GET_ACCOUNTS} );
            }
            else{
                ActivityCompat.requestPermissions( activity, new String[]{Manifest.permission.GET_ACCOUNTS}, REQUEST_PERMISSIONS_CODE );
            }
        }
        else{

        }

    }

    public void callCalendarioWRITE(View view) {
        Log.i(TAG, "callCalendarioWRITE()");
        if( ContextCompat.checkSelfPermission( activity, Manifest.permission.WRITE_CALENDAR ) != PackageManager.PERMISSION_GRANTED ){

            if( ActivityCompat.shouldShowRequestPermissionRationale( activity, Manifest.permission.WRITE_CALENDAR ) ){
                callDialog( "É preciso a permission WRITE_CALENDAR para apresentação do content.", new String[]{Manifest.permission.WRITE_CALENDAR} );
            }
            else{
                ActivityCompat.requestPermissions( activity, new String[]{Manifest.permission.WRITE_CALENDAR}, REQUEST_PERMISSIONS_CODE );
            }
        }
        else{

        }
    }

    public void callCalendarioREAD(View view) {
        Log.i(TAG, "READ_CALENDAR()");
        if( ContextCompat.checkSelfPermission( activity, Manifest.permission.READ_CALENDAR ) != PackageManager.PERMISSION_GRANTED ){

            if( ActivityCompat.shouldShowRequestPermissionRationale( activity, Manifest.permission.READ_CALENDAR ) ){
                callDialog( "É preciso a permission READ_CALENDAR para apresentação do content.", new String[]{Manifest.permission.READ_CALENDAR} );
            }
            else{
                ActivityCompat.requestPermissions( activity, new String[]{Manifest.permission.READ_CALENDAR}, REQUEST_PERMISSIONS_CODE );
            }
        }
        else{

        }
    }

    // UTIL
    private void callDialog( String message, final String[] permissions ){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                ActivityCompat.requestPermissions(activity, permissions, REQUEST_PERMISSIONS_CODE);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog

            }
        });
        AlertDialog dialog = builder.create();
    }



}
