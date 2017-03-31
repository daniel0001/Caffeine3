package practice.Caffeine;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by Daniel on 31/03/2017.
 */

public class BackgroundTask extends AsyncTask<String, Void, String> {

    Context mContext;

    public BackgroundTask(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected String doInBackground(String... params) {

        String method = params[0];
        SyncShopsHelper sync = new SyncShopsHelper(mContext);
        if (method.equals("sync_shops")) {
            Integer userID = Integer.parseInt(params[1]);
            sync.sync(userID);
            return "shops synced...";
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {

        Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
        // Set up new intent CoffeeShopsActivity

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }


}
