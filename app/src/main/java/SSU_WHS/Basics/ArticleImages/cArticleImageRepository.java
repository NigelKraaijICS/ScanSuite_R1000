package SSU_WHS.Basics.ArticleImages;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import SSU_WHS.General.acScanSuiteDatabase;

public class cArticleImageRepository {
    private iArticleImageDao articleImageDao;
    private cWebresult webResult;

    cArticleImageRepository(Application application) {
        acScanSuiteDatabase db = acScanSuiteDatabase.getDatabase(application);
        articleImageDao = db.articleImageDao();
    }
    private static class GetByItemnoAndVariantCodeParams {
        String itemno;
        String variantcode;

        GetByItemnoAndVariantCodeParams(String pv_itemno, String pv_variantcode) {
            this.itemno = pv_itemno;
            this.variantcode = pv_variantcode;
        }
    }

    public LiveData<List<cArticleImageEntity>> getArticleImages(final Boolean forceRefresh, final String user, final String owner, final String itemno, final String variantcode, final Boolean refresh) {

        final MutableLiveData<List<cArticleImageEntity>> data = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<cArticleImageEntity> articleImageObl = new ArrayList<>();
                if (forceRefresh) {
                    try {
                        deleteAll();
                        List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                        PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                        l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUTCH;
                        l_PropertyInfo1Pin.setValue(user);
                        l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                        PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                        l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_OWNER;
                        l_PropertyInfo2Pin.setValue(owner);
                        l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                        PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
                        l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMNO;
                        l_PropertyInfo3Pin.setValue(itemno);
                        l_PropertyInfoObl.add(l_PropertyInfo3Pin);

                        PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
                        l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_VARIANTCODE;
                        l_PropertyInfo4Pin.setValue(variantcode);
                        l_PropertyInfoObl.add(l_PropertyInfo4Pin);

                        PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
                        l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_REFRESH;
                        l_PropertyInfo5Pin.setValue(refresh);
                        l_PropertyInfoObl.add(l_PropertyInfo5Pin);

                        webResult = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETARTICLEIMAGES, l_PropertyInfoObl);
                        List<JSONObject> myList = webResult.getResultDtt();
                        for (int i = 0; i < myList.size(); i++) {
                            JSONObject jsonObject;
                            jsonObject = myList.get(i);

                            cArticleImageEntity articleImageEntity = new cArticleImageEntity(jsonObject);
                            insert(articleImageEntity);
                            articleImageObl.add(articleImageEntity);
                        }
                        data.postValue(articleImageObl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else { //no refresh, get local database
                    cArticleImageEntity articleImageEntity = getArticleImageByItemnoAndVariantCode(itemno, variantcode);
                    if (articleImageEntity != null) {
                        articleImageObl.add(getArticleImageByItemnoAndVariantCode(itemno, variantcode));
                    }
                    data.postValue(articleImageObl);
                }
            } //run
        }).start(); //Thread
        return data;
    }

    public LiveData<List<cArticleImageEntity>> getArticleImagesMultiple(final Boolean forceRefresh, final String user, final String owner, final List<cArticleImage> articleImages) {

        final MutableLiveData<List<cArticleImageEntity>> data = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<cArticleImageEntity> articleImageObl = new ArrayList<>();
                if (forceRefresh) {
                    try {
                        deleteAll();
                        List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                        PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                        l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUTCH;
                        l_PropertyInfo1Pin.setValue(user);
                        l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                        PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                        l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_OWNER;
                        l_PropertyInfo2Pin.setValue(owner);
                        l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                        SoapObject articleImagesList = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_ITEMSLIST);
                        for (cArticleImage articleImage: articleImages) {
                            SoapObject soapObject = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_ARTICLEINPUT_COMPLEX);
                            soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_ITEMNO_COMPLEX, articleImage.getItemno());
                            soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_VARIANTCODE_COMPLEX, articleImage.getVariantcode());
                            articleImagesList.addSoapObject(soapObject);
                        }

                        PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
                        l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMSLIST;
                        l_PropertyInfo7Pin.setValue(articleImagesList);
                        l_PropertyInfoObl.add(l_PropertyInfo7Pin);

                        webResult = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETARTICLEIMAGESMULTIPLE, l_PropertyInfoObl);
                        List<JSONObject> myList = webResult.getResultDtt();
                        for (int i = 0; i < myList.size(); i++) {
                            JSONObject jsonObject;
                            jsonObject = myList.get(i);

                            cArticleImageEntity articleImageEntity = new cArticleImageEntity(jsonObject);
                            insert(articleImageEntity);
                            articleImageObl.add(articleImageEntity);
                        }
                        data.postValue(articleImageObl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else { //no refresh, get local database

                }
            } //run
        }).start(); //Thread
        return data;
    }







    public List<cArticleImageEntity> getAll() {
        List<cArticleImageEntity> l_articleImageEntityObl = null;
        try {
            l_articleImageEntityObl = new getAllAsyncTask(articleImageDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_articleImageEntityObl;
    }
    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<cArticleImageEntity>> {
        private iArticleImageDao mAsyncTaskDao;

        getAllAsyncTask(iArticleImageDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cArticleImageEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }

    public void deleteAll () {
        new cArticleImageRepository.deleteAllAsyncTask(articleImageDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iArticleImageDao mAsyncTaskDao;

        deleteAllAsyncTask(iArticleImageDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    public void insert (cArticleImageEntity articleImageEntity) {
        new insertAsyncTask(articleImageDao).execute(articleImageEntity);
    }
    private static class insertAsyncTask extends AsyncTask<cArticleImageEntity, Void, Void> {
        private iArticleImageDao mAsyncTaskDao;

        insertAsyncTask(iArticleImageDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cArticleImageEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    public cArticleImageEntity getArticleImageByItemnoAndVariantCode(String pv_itemno, String pv_variantcode) {
        GetByItemnoAndVariantCodeParams getByItemnoAndVariantCodeParams = new GetByItemnoAndVariantCodeParams(pv_itemno, pv_variantcode);
        cArticleImageEntity articleImageEntity = null;
        try {
            articleImageEntity = new getArticleImageByItemnoAndVariantCodeAsyncTask(articleImageDao).execute(getByItemnoAndVariantCodeParams).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return articleImageEntity;
    }
    private static class getArticleImageByItemnoAndVariantCodeAsyncTask extends AsyncTask<GetByItemnoAndVariantCodeParams, Void, cArticleImageEntity> {
        private iArticleImageDao mAsyncTaskDao;
        getArticleImageByItemnoAndVariantCodeAsyncTask(iArticleImageDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected cArticleImageEntity doInBackground(GetByItemnoAndVariantCodeParams... params) {
            return mAsyncTaskDao.getArticleImageByItemnoAndVariantCode(params[0].itemno, params[0].variantcode);
        }
    }

}
