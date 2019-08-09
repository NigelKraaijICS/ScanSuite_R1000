package SSU_WHS.Basics.Settings;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Webservice.cWebserviceDefinitions;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Webservice.cWebresult;
import ICS.Utils.cDeviceInfo;

public class cSettingsRepository {
    private iSettingsDao settingsDao;
    private cWebresult webResult;

    cSettingsRepository(Application application) {
        acScanSuiteDatabase db = acScanSuiteDatabase.getDatabase(application);
        settingsDao = db.settingsDao();
    }

    public LiveData<List<cSettingsEntity>> getSettings() {

        final MutableLiveData<List<cSettingsEntity>> data = new MutableLiveData<>();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        List<cSettingsEntity> settingsObl = new ArrayList<>();
                        List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                        PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                        l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_SCANNER;
                        l_PropertyInfo1Pin.setValue(cDeviceInfo.getSerialnumber());
                        l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                        PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                        l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_DEVICEBRAND;
                        l_PropertyInfo2Pin.setValue(cDeviceInfo.getDeviceBrand());
                        l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                        PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                        l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_DEVICETYPE;
                        l_PropertyInfo3Pin.setValue(cDeviceInfo.getDeviceModel());
                        l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                        PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                        l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_SCANNERVERSION;
                        l_PropertyInfo4Pin.setValue(cDeviceInfo.getAppVersion());
                        l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                        //We'll leave this emtpy for now
                        PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                        l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_SCANNERCONFIGURATION;
                        l_PropertyInfo5Pin.setValue("");
                        l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                        webResult = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GET14SETTINGS, l_PropertyInfoObl);

                        List<String> l_resultObl = webResult.getResultObl();
                        for(int i = 0;i <l_resultObl.size();i++) {
                            if (i==0) {
                                cSettingsEntity settingsEntity = new cSettingsEntity(cSettingsEnums.p_Settings14Enu.SCANNERFIXEDBRANCH.toString(), l_resultObl.get(i));
                                insert(settingsEntity);
                            }
                            if (i==1) {
                                cSettingsEntity settingsEntity = new cSettingsEntity(cSettingsEnums.p_Settings14Enu.SCANNERDESCRIPTION.toString(), l_resultObl.get(i));
                                insert(settingsEntity);
                            }
                            if (i==2) {
                                cSettingsEntity settingsEntity = new cSettingsEntity(cSettingsEnums.p_Settings14Enu.SCANNERREQUIREDVERSION.toString(), l_resultObl.get(i));
                                insert(settingsEntity);
                            }
                            if (i==3) {
                                cSettingsEntity settingsEntity = new cSettingsEntity(cSettingsEnums.p_Settings14Enu.SCANNERAPPLICATIONENVIRONMENT.toString(), l_resultObl.get(i));
                                insert(settingsEntity);
                            }
                            if (i==4) {
                                cSettingsEntity settingsEntity = new cSettingsEntity(cSettingsEnums.p_Settings14Enu.SCANNERCULTURE.toString(), l_resultObl.get(i));
                                insert(settingsEntity);
                            }
                            if (i==5) {
                                cSettingsEntity settingsEntity = new cSettingsEntity(cSettingsEnums.p_Settings14Enu.SCANNERREQUIREDCONFIGURATION.toString(), l_resultObl.get(i));
                                insert(settingsEntity);
                            }
                            if (i==6) {
                                cSettingsEntity settingsEntity = new cSettingsEntity(cSettingsEnums.p_Settings14Enu.SCANNERREAPPLYCONFIGURATION.toString(), l_resultObl.get(i));
                                insert(settingsEntity);
                            }
                            if (i==7) {
                                cSettingsEntity settingsEntity = new cSettingsEntity(cSettingsEnums.p_Settings14Enu.SCANNERVERSIONCONFIGVERSION.toString(), l_resultObl.get(i));
                                insert(settingsEntity);
                            }
                            if (i==8) {
                                cSettingsEntity settingsEntity = new cSettingsEntity(cSettingsEnums.p_Settings14Enu.SCANNERREQUIREDSCANNERVERSIONCONFIGVERSION.toString(), l_resultObl.get(i));
                                insert(settingsEntity);
                            }
                        }

                        List<JSONObject> myList = webResult.getResultDtt();
                        for(int i = 0;i <myList.size();i++) {
                            JSONObject jsonObject;
                            jsonObject = myList.get(i);

                            cSettingsEntity settingsEntity = new cSettingsEntity(jsonObject);
                            insert(settingsEntity);
                            settingsObl.add(settingsEntity);
                        }
                        data.postValue(settingsObl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } //run

            }).start(); //Thread
        return data;
    }

    public List<cSettingsEntity> getLocalSettings() {
        List<cSettingsEntity> l_SettingsEntityObl = null;
        try {
            l_SettingsEntityObl = new getLocalSettingsAsyncTask(settingsDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_SettingsEntityObl;
    }

    public String getSetting(String key) {
        String l_resultStr = "";
        try {
            l_resultStr = new getSettingAsyncTask(settingsDao).execute(key).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (l_resultStr == null) {
            l_resultStr = "";
        }
        return l_resultStr;
    }

    public void deleteAll () {
        new cSettingsRepository.deleteAllAsyncTask(settingsDao).execute();
    }

    private static class getLocalSettingsAsyncTask extends AsyncTask<Void, Void, List<cSettingsEntity>> {
        private iSettingsDao mAsyncTaskDao;

        getLocalSettingsAsyncTask(iSettingsDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cSettingsEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getLocalSettings();
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iSettingsDao mAsyncTaskDao;

        deleteAllAsyncTask(iSettingsDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class getSettingAsyncTask extends AsyncTask<String, Void, String> {
        private iSettingsDao mAsyncTaskDao;
        getSettingAsyncTask(iSettingsDao dao) {mAsyncTaskDao = dao; }
        @Override
        protected String doInBackground(String... params) {
            return  mAsyncTaskDao.getSetting(params[0]);
        }
    }

    private static class getLocalSettingAsyncTask extends AsyncTask<String, Void, String> {
        private iSettingsDao mAsyncTaskDao;
        getLocalSettingAsyncTask(iSettingsDao dao) {mAsyncTaskDao = dao; }
        @Override
        protected String doInBackground(String... params) {
            return  mAsyncTaskDao.getSetting(params[0]);
        }
    }

    public void insert (cSettingsEntity cSettingsEntity) {
        new insertAsyncTask(settingsDao).execute(cSettingsEntity);
    }
    private static class insertAsyncTask extends AsyncTask<cSettingsEntity, Void, Void> {
        private iSettingsDao mAsyncTaskDao;

        insertAsyncTask(iSettingsDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cSettingsEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
