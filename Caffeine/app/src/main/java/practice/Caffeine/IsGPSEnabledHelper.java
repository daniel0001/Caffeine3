package practice.Caffeine;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

/**
 * Created by Daniel on 24/03/2017.
 */

public class IsGPSEnabledHelper {
    Context mContext;

    public IsGPSEnabledHelper(Context mContext) {
        this.mContext = mContext;
    }

    public boolean getConnected() {
        LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER) && lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void notConnectedMessage() {
        // notify user
        AlertDialog.Builder dialogGPS = new AlertDialog.Builder(mContext);
        dialogGPS.setMessage(mContext.getResources().getString(R.string.gps_network_not_enabled));
        dialogGPS.setPositiveButton(mContext.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(myIntent);

            }
        });

        dialogGPS.setNegativeButton(mContext.getString(R.string.Cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
            }
        });
        dialogGPS.show();
    }
}
