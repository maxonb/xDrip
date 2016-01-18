package com.eveningoutpost.dexdrip;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.eveningoutpost.dexdrip.UtilityModels.NightscoutUploader;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;

import io.fabric.sdk.android.Fabric;
import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender;

/**
 * Created by stephenblack on 3/21/15.
 */
@ReportsCrashes(
        formUri = "https://yoursolace.cloudant.com/acra-xdrip/_design/acra-storage/_update/report",
        reportType = HttpSender.Type.JSON,
        httpMethod = HttpSender.Method.POST,
        formUriBasicAuthLogin = "nateriverldstiondrephery",
        formUriBasicAuthPassword = "GEK5Nv7NtMkloAkufNvFgast",
        formKey = "", // This is required for backward compatibility but not used
        customReportContent = {
                ReportField.APP_VERSION_CODE,
                ReportField.APP_VERSION_NAME,
                ReportField.ANDROID_VERSION,
                ReportField.PACKAGE_NAME,
                ReportField.REPORT_ID,
                ReportField.BUILD,
                ReportField.STACK_TRACE
        },
        mode = ReportingInteractionMode.TOAST,
        logcatArguments = {"-t", "500", "-v", "time"},
        resToastText = R.string.toast_crash
)

public class xdrip extends Application {

    private static final String TAG = xdrip.class.getSimpleName();

    private static MongoClient sMongoClient;
    public static DB sDb;

    private void openMongoConnection() {
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            boolean enableMongoUpload = prefs.getBoolean("cloud_storage_mongodb_enable", false);
            String dbURI = prefs.getString("cloud_storage_mongodb_uri", null);

            if (enableMongoUpload && dbURI != null) {

                // connect to db
                MongoClientOptions.Builder builder = new MongoClientOptions.Builder();

                builder.connectionsPerHost(1)
                        .heartbeatFrequency(24 * 60 * 60 * 1000)
                        .cursorFinalizerEnabled(false)
                        .socketTimeout(7 * 60 * 1000)
                        .maxConnectionIdleTime(22 * 60 * 1000);

                MongoClientURI uri = new MongoClientURI(dbURI.trim(), builder);
                sMongoClient = new MongoClient(uri);

                // get db
                sDb = sMongoClient.getDB(uri.getDatabase());
            }
        } catch (Exception e) {

            if (sMongoClient != null) {
                sMongoClient.close();
            }
            sDb = null;
            sMongoClient = null;

            Log.e(TAG, "Failed to open MongoDB connection.", e);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        // The following line triggers the initialization of ACRA
        //ACRA.init(this);

        openMongoConnection();
    }
}
