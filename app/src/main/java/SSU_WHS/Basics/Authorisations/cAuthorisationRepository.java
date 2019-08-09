package SSU_WHS.Basics.Authorisations;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Basics.Settings.iSettingsDao;
import SSU_WHS.Basics.Settings.iSettingsDao_Impl;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.General.cAppExtension;
import ICS.Utils.cText;

import static SSU_WHS.Basics.Settings.cSettingsEnums.p_Settings14Enu.PICK_PACK_AND_SHIP_FASE_AVAILABLE;
import static SSU_WHS.Basics.Settings.cSettingsEnums.p_Settings14Enu.PICK_SORT_FASE_AVAILABLE;

public class cAuthorisationRepository {
    private iAuthorisationDao authorisationDao;
    private cWebresult webResult;

    cAuthorisationRepository(Application application) {
        acScanSuiteDatabase db = acScanSuiteDatabase.getDatabase(application);
        authorisationDao = db.authorisationDao();
    }

    public LiveData<List<cAuthorisationEntity>> getAuthorisations(final Boolean forceRefresh, final String username, final String branch) {

        final MutableLiveData<List<cAuthorisationEntity>> data = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<cAuthorisationEntity> authorisationsObl = new ArrayList<>();
                if (forceRefresh) {
                    try {
                        deleteAll();
                        List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                        PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                        l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAME;
                        l_PropertyInfo1Pin.setValue(username);
                        l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                        PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                        l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_BRANCH;
                        l_PropertyInfo2Pin.setValue(branch);
                        l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                        webResult = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETAUTHORISATIONS, l_PropertyInfoObl);
                        List<JSONObject> myList = webResult.getResultDtt();

                        Context context = cAppExtension.context;
                        iSettingsDao settingsDao = new iSettingsDao_Impl(acScanSuiteDatabase.getDatabase(context));
                        String pickSortAvailableStr = settingsDao.getSetting(PICK_SORT_FASE_AVAILABLE.toString());
                        Boolean pickSortAvailable = cText.stringToBoolean(pickSortAvailableStr, false);
                        String pickPackAndShipAvailableStr = settingsDao.getSetting(PICK_PACK_AND_SHIP_FASE_AVAILABLE.toString());
                        Boolean pickPackAndShipAvailable = cText.stringToBoolean(pickPackAndShipAvailableStr, false);
                        for (int i = 0; i < myList.size(); i++) {
                            JSONObject jsonObject;
                            jsonObject = myList.get(i);

                            cAuthorisationEntity authorisationEntity = new cAuthorisationEntity(jsonObject);

                            insert(authorisationEntity);
                            authorisationsObl.add(authorisationEntity);

                            if (pickSortAvailable) {
                                cAuthorisationEntity sortingEntity = new cAuthorisationEntity();
                                sortingEntity.authorisation = cAuthorisation.g_eAuthorisation.SORTING.toString();
                                sortingEntity.order = authorisationEntity.getOrderInt() + 1;
                                sortingEntity.license = cAuthorisation.g_eAuthorisation.SORTING.toString();
                                authorisationsObl.add(sortingEntity);
                            }

                            if (pickPackAndShipAvailable) {
                                cAuthorisationEntity shippingEntity = new cAuthorisationEntity();
                                shippingEntity.authorisation = cAuthorisation.g_eAuthorisation.SHIPPING.toString();
                                shippingEntity.order = authorisationEntity.getOrderInt() + 2;
                                shippingEntity.license = cAuthorisation.g_eAuthorisation.SHIPPING.toString();
                                authorisationsObl.add(shippingEntity);
                            }
                        }
                        data.postValue(authorisationsObl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else { //no refresh, get local database
                    authorisationsObl = getAll();
                    data.postValue(authorisationsObl);
                }
            } //run
        }).start(); //Thread
        return data;
    }

    public List<cAuthorisationEntity> getAll() {
        List<cAuthorisationEntity> l_UserEntityObl = null;
        try {
            l_UserEntityObl = new getAllAsyncTask(authorisationDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_UserEntityObl;
    }
    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<cAuthorisationEntity>> {
        private iAuthorisationDao mAsyncTaskDao;

        getAllAsyncTask(iAuthorisationDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cAuthorisationEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }

    public void deleteAll () {
        new cAuthorisationRepository.deleteAllAsyncTask(authorisationDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iAuthorisationDao mAsyncTaskDao;

        deleteAllAsyncTask(iAuthorisationDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    public void insert (cAuthorisationEntity authorisationEntity) {
        new insertAsyncTask(authorisationDao).execute(authorisationEntity);
    }
    private static class insertAsyncTask extends AsyncTask<cAuthorisationEntity, Void, Void> {
        private iAuthorisationDao mAsyncTaskDao;

        insertAsyncTask(iAuthorisationDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cAuthorisationEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
