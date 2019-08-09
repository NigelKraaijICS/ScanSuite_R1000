package SSU_WHS.ScannerLogon;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import SSU_WHS.acScanSuiteDatabase;
import ICS.Utils.cDeviceInfo;

public class cScannerLogonRepository {
    private iScannerLogonDao scannerLogonDao;
    private cWebresult webResult;

    cScannerLogonRepository(Application application) {
        acScanSuiteDatabase db = acScanSuiteDatabase.getDatabase(application);
        scannerLogonDao = db.scannerLogonDao();
    }
    public LiveData<List<cScannerLogonEntity>> getScannerLogon(final Boolean forcerefresh) {

        final MutableLiveData<List<cScannerLogonEntity>> data = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<cScannerLogonEntity> scannerLogonObl = new ArrayList<>();
                if (forcerefresh) {
                    try {
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

                        webResult = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_SCANNERLOGON, l_PropertyInfoObl);

                        List<String> l_resultObl = webResult.getResultObl();
                        cScannerLogonEntity scannerLogonEntity = new cScannerLogonEntity();
                        for (int i = 0; i < l_resultObl.size(); i++) {
                            if (i == 0) {
                                scannerLogonEntity.setFixedscannerbranch(l_resultObl.get(i));
                            }
                            if (i == 1) {
                                scannerLogonEntity.setScannerdescription(l_resultObl.get(i));
                            }
                            if (i == 2) {
                                scannerLogonEntity.setRequiredscannerversion(l_resultObl.get(i));
                            }
                            if (i == 3) {
                                scannerLogonEntity.setApplicationenvironment(l_resultObl.get(i));
                            }
                            if (i == 4) {
                                scannerLogonEntity.setLanguages(l_resultObl.get(i));
                            }
                            if (i == 5) {
                                scannerLogonEntity.setRequiredscannerconfiguration(l_resultObl.get(i));
                            }
                            if (i == 6) {
                                scannerLogonEntity.setReapplyscannerconfiguration(l_resultObl.get(i));
                            }
                            if (i == 7) {
                                scannerLogonEntity.setVersionconfigappcurrentscanner(l_resultObl.get(i));
                            }
                            if (i == 8) {
                                scannerLogonEntity.setVersionconfigapprequiredscanner(l_resultObl.get(i));
                            }
                            if (i == 9) {
                                scannerLogonEntity.setColorset(l_resultObl.get(i));
                            }
                        }
                        deleteAll();
                        scannerLogonObl.add(scannerLogonEntity);
                        insert(scannerLogonEntity);
                        data.postValue(scannerLogonObl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    scannerLogonObl = getAll();
                    data.postValue(scannerLogonObl);
                }
            }//run
        }).start(); //Thread
        return data;
    }

    public void deleteAll () {
        new cScannerLogonRepository.deleteAllAsyncTask(scannerLogonDao).execute();
    }
    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iScannerLogonDao mAsyncTaskDao;

        deleteAllAsyncTask(iScannerLogonDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    public List<cScannerLogonEntity> getAll() {
        List<cScannerLogonEntity> l_ScannerLogonEntityObl = null;
        try {
            l_ScannerLogonEntityObl = new getAllAsyncTask(scannerLogonDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_ScannerLogonEntityObl;
    }
    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<cScannerLogonEntity>> {
        private iScannerLogonDao mAsyncTaskDao;

        getAllAsyncTask(iScannerLogonDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cScannerLogonEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }


    public void insert (cScannerLogonEntity scannerLogonEntity) {
        new insertAsyncTask(scannerLogonDao).execute(scannerLogonEntity);
    }
    private static class insertAsyncTask extends AsyncTask<cScannerLogonEntity, Void, Void> {
        private iScannerLogonDao mAsyncTaskDao;

        insertAsyncTask(iScannerLogonDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cScannerLogonEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}
