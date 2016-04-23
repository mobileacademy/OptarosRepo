package receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mobileacademy.NewsReader.services.ListPackagesService;

/**
 * Created by danielastamati on 22/04/16.
 */
public class PackageInstalledReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isReplacing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false);

        if(!isReplacing){
            context.startService(new Intent(context, ListPackagesService.class));
        }

    }
}
