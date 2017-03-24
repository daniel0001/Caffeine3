package practice.Caffeine;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

/**
 * Created by Daniel on 24/03/2017.
 */

public class IsWifiConnectedHelper {
    Context mContext;

    public IsWifiConnectedHelper(Context mContext) {
        this.mContext = mContext;
    }

    public boolean getConnected() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void notConnectedMessage() {

        // notify user
        AlertDialog.Builder dialogConnect = new AlertDialog.Builder(mContext);
        dialogConnect.setMessage(mContext.getResources().getString(R.string.wifi_not_connected));
        dialogConnect.setPositiveButton(mContext.getResources().getString(R.string.open_wifi_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                Intent myIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                mContext.startActivity(myIntent);
                //get wifi
            }
        });
        dialogConnect.setNegativeButton(mContext.getString(R.string.Cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
            }
        });
        dialogConnect.show();
    }
}

