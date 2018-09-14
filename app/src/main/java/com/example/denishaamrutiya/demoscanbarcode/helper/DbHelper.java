package com.example.denishaamrutiya.demoscanbarcode.helper;

import android.content.Context;
import android.os.Environment;


import com.afollestad.materialdialogs.MaterialDialog;
import com.example.denishaamrutiya.demoscanbarcode.model.ScanningItemPojo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import io.realm.Realm;

public class DbHelper {
    private File EXPORT_REALM_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);;
    private String EXPORT_REALM_FILE_NAME = "barcodescanner.realm";
    private String IMPORT_REALM_FILE_NAME = "barcodescanner.realm";
    private Context context;
    private Realm realm;


    public DbHelper(Context context, Realm realm) {
        this.context = context;
        this.realm = realm;
    }

    public int getNextScanningItemPojoKey() {
        if (realm.where(ScanningItemPojo.class).count() > 0)
            return realm.where(ScanningItemPojo.class).max("id").intValue() + 1;
        else
            return 0;
    }

    public boolean addScanningItemPojo(String title, String quntity) {
        realm.beginTransaction();
        ScanningItemPojo ScanningItemPojo = realm.createObject(ScanningItemPojo.class, getNextScanningItemPojoKey());
        ScanningItemPojo.setTitle(title);
        ScanningItemPojo.setQuntity(quntity);
        realm.commitTransaction();
        return true;
    }

    public void deleteScanningItemPojo(final ScanningItemPojo ScanningItemPojo) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ScanningItemPojo.deleteFromRealm();
            }
        });
    }

    public boolean updateScanningItemPojo(int id, String title, String quntity){
        ScanningItemPojo ScanningItemPojo = new ScanningItemPojo();
        ScanningItemPojo.setId(id);
        ScanningItemPojo.setTitle(title);
        ScanningItemPojo.setQuntity(quntity);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(ScanningItemPojo);
        realm.commitTransaction();
        return true;
    }

    public void backup() {
        File exportRealmFile;
        exportRealmFile = new File(EXPORT_REALM_PATH, EXPORT_REALM_FILE_NAME);
        exportRealmFile.delete();
        realm.writeCopyTo(exportRealmFile);
        String msg = "File exported to Path: " + EXPORT_REALM_PATH + "/" + EXPORT_REALM_FILE_NAME;
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title("Done")
                .content(msg)
                .positiveText("Ok")
                .show();

    }

    public void restore(String restoreFilePath){
        copyBundledRealmFile(restoreFilePath, IMPORT_REALM_FILE_NAME);
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title("Done")
                .content("Next time you open application, data will be updated! :)")
                .positiveText("Ok")
                .show();


    }

    private String copyBundledRealmFile(String oldFilePath, String outFileName) {
        try {
            File file = new File(context.getApplicationContext().getFilesDir(), outFileName);

            FileOutputStream outputStream = new FileOutputStream(file);

            FileInputStream inputStream = new FileInputStream(new File(oldFilePath));

            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, bytesRead);
            }
            outputStream.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
