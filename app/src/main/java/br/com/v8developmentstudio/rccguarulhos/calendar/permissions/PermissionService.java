package br.com.v8developmentstudio.rccguarulhos.calendar.permissions;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cleiton.dantas on 16/08/2016.
 */
public class PermissionService {
    private Context context;
    private Activity activity;
    public static final int REQUEST_PERMISSIONS_CODE = 128;
    List<String> permissoesList  = new ArrayList<>();
    public PermissionService(Context context,Activity activity){
        this.context = context;
        this.activity = activity;
    }

    public void callGetPermissions() {
        boolean controler =false;
        int i=0;
        if(ContextCompat.checkSelfPermission( activity, Manifest.permission.GET_ACCOUNTS ) != PackageManager.PERMISSION_GRANTED ){
            controler=true;
            permissoesList.add(Manifest.permission.GET_ACCOUNTS);
        }
        if(ContextCompat.checkSelfPermission( activity, Manifest.permission.WRITE_CALENDAR ) != PackageManager.PERMISSION_GRANTED ){
            controler=true;
            permissoesList.add(Manifest.permission.WRITE_CALENDAR);
        }
        if((!ActivityCompat.shouldShowRequestPermissionRationale( activity, Manifest.permission.GET_ACCOUNTS)) ||(!ActivityCompat.shouldShowRequestPermissionRationale( activity, Manifest.permission.WRITE_CALENDAR ) ) ){
            if(controler){
                String[] permiss = new String[permissoesList.size()];
                permiss = permissoesList.toArray(permiss);
                ActivityCompat.requestPermissions( activity,permiss,REQUEST_PERMISSIONS_CODE );
            }
        }

    }

    public boolean getPermission(){
        boolean controler =true;
        if(ContextCompat.checkSelfPermission( activity, Manifest.permission.GET_ACCOUNTS ) != PackageManager.PERMISSION_GRANTED ) {
            controler = false;
        }
        if(ContextCompat.checkSelfPermission( activity, Manifest.permission.WRITE_CALENDAR ) != PackageManager.PERMISSION_GRANTED ) {
            controler = false;
        }
        return controler;
    }
}
