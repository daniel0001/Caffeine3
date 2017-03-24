package practice.Caffeine;

import android.content.Context;

/**
 * Created by Daniel on 24/03/2017.
 */

public class CheckConnectedHelper {
    Context mContext;

    public CheckConnectedHelper(Context mContext) {
        this.mContext = mContext;
    }

    public boolean checkConnected(boolean gpsEnabled, boolean wifiConnected) {
        // Get helper method to check if internet connected
        IsWifiConnectedHelper conHelper = new IsWifiConnectedHelper(mContext);

        // Get helper method to check if GPS && network enabled
        IsGPSEnabledHelper gpsHelper = new IsGPSEnabledHelper(mContext);
        try {
            gpsEnabled = gpsHelper.getConnected();
        } catch (Exception ex) {
        }
        if (!gpsEnabled) {
            gpsHelper.notConnectedMessage();
            return false;
        }
        try {
            wifiConnected = conHelper.getConnected();
        } catch (Exception ex) {
        }
        if (!wifiConnected) {
            conHelper.notConnectedMessage();
            return false;
        }
        return true;
    }
}
