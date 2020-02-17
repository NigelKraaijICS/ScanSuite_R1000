package ICS.Utils;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import java.io.File;

import ICS.cAppExtension;
import nl.icsvertex.scansuite.R;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class cUpdate {
    private static IntentFilter downloadIntentFilter;

    public static void mUpdateBln(View pvErrorView,String pvURLStr) {

        String updateFileStr =   Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/AppName.apk";

         UpdateHandler updateHandler = new UpdateHandler(updateFileStr,pvURLStr,pvErrorView);
         updateHandler.run();

    }

    private static class UpdateHandler /** Whichever class you extend */ {

        String updateFileNameStr;
        String URLStr;
        File updateFile;
        Uri uri;
        Uri uri2;
        View errorView;


        private UpdateHandler(String pvUpdateFileNameStr, String pvUrlStr, View pvErrorView) {
            this.updateFileNameStr = pvUpdateFileNameStr;
            this.URLStr = pvUrlStr;
            this.errorView = pvErrorView;

            this.updateFile = new File(this.updateFileNameStr);
            this.uri = Uri.parse("file://" +    this.updateFileNameStr);
            this.uri2 = FileProvider.getUriForFile(cAppExtension.context.getApplicationContext(), cAppExtension.context.getApplicationContext().getPackageName() +".provider", updateFile);

            // Required empty public constructor
        }


        public void run() {
            cAppExtension.activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    //Close all dialogs
                    cUserInterface.pCheckAndCloseOpenDialogs();

                    //Show download dialog
                    final Dialog dialog = new Dialog(cAppExtension.context);
                    dialog.setContentView(R.layout.default_progressdialog);

                    TextView messageText = dialog.findViewById(R.id.progressTextView);
                    if (messageText != null) {
                        messageText.setText(R.string.message_downloading_update);
                    }
                    dialog.setCancelable(false);
                    dialog.show();

                    //Delete update file if exists
                    final File file = new File(updateFileNameStr);
                    if (file.exists())
                        //file.pDelete() - test this, I think sometimes it doesnt work
                        file.delete();

                    //get url of app on server
                    final String url = URLStr;

                    //set downloadmanager
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.setDescription(cAppExtension.context.getString(R.string.app_name));
                    request.setTitle(cAppExtension.context.getString(R.string.app_name));

                    //set destination
                    request.setDestinationUri(uri);

                    // get download ShippingAgentServiceStr and enqueue file
                    final DownloadManager manager = (DownloadManager) cAppExtension.context.getSystemService(Context.DOWNLOAD_SERVICE);
                    final long downloadId = manager.enqueue(request);

                    //set BroadcastReceiver to install app when .apk is downloaded
                    BroadcastReceiver onComplete = new BroadcastReceiver() {
                        public void onReceive(Context context, Intent intent) {
                            //ctxt.grantUriPermission(getPackageName(), uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            //have we downloaded something?

                            if (!file.exists()) {
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                                String message = context.getResources().getString(R.string.update_file_not_found) + cText.NEWLINE + url;
                                //cUserInterface.pShowSnackbarMessage(context,errorView,message, R.raw.badsound, true);
                                cUserInterface.pShowActionSnackbar(errorView,message, R.raw.badsound, false);
                                return;
                            }
                            Intent install = new Intent(Intent.ACTION_VIEW);
                            install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            install.addFlags(FLAG_GRANT_READ_URI_PERMISSION|FLAG_GRANT_WRITE_URI_PERMISSION);
                            install.setDataAndType(uri2,
                                    manager.getMimeTypeForDownloadedFile(downloadId));
                            context.startActivity(install);

                            context.unregisterReceiver(this);
                            cAppExtension.activity.finish();
                        }
                    };

                    //register receiver for when .apk download is complete
                    downloadIntentFilter = new IntentFilter();
                    downloadIntentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);

                    cAppExtension.context.registerReceiver(onComplete, downloadIntentFilter);

                }

            });
        }
    }

}
