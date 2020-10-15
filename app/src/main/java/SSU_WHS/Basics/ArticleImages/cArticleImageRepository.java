package SSU_WHS.Basics.ArticleImages;

import android.app.Application;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cArticleImageRepository {
    //Region Public Properties

    private iArticleImageDao articleImageDao;
    //End Region Public Properties

    //Region Private Properties


    private static class GetByItemnoAndVariantCodeParams {
        String itemNoStr;
        String variantCodeStr;

        GetByItemnoAndVariantCodeParams(String pvItemNoStr, String pvVariantCodeStr) {
            this.itemNoStr = pvItemNoStr;
            this.variantCodeStr = pvVariantCodeStr;
        }
    }

    //End Region Private Properties

    //Region Constructor
    cArticleImageRepository(Application pvApplication) {
         acScanSuiteDatabase db =  acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.articleImageDao = db.articleImageDao();
    }
    //End Region Constructor

    //Region Public Methods

    public void insert (cArticleImageEntity articleImageEntity) {
        new insertAsyncTask(articleImageDao).execute(articleImageEntity);
    }

    public void deleteAll () {
        new cArticleImageRepository.deleteAllAsyncTask(articleImageDao).execute();
    }

    public cWebresult pGetArticleImageFromWebserviceWrs(String pvItemNoStr, String pvVariantCodeStr) {

        ArrayList<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        cArticleImageRepository.GetByItemnoAndVariantCodeParams getByItemnoAndVariantCodeParams = new cArticleImageRepository.GetByItemnoAndVariantCodeParams(pvItemNoStr,pvVariantCodeStr);

        try {
            webResultWrs = new cArticleImageRepository.mGetArticleImageFromWebserviceGetAsyncTask().execute(getByItemnoAndVariantCodeParams).get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    public cWebresult pGetArticleImagesFromWebserviceWrs(List<String> pvItemAmdVariantsObl) {

        List<String> resultObl = new ArrayList<>();
        cWebresult webResultWrs = new cWebresult();

        try {
            webResultWrs = new mGetImagesFromWebserviceTask().execute(pvItemAmdVariantsObl).get();
        } catch (ExecutionException | InterruptedException e) {
            webResultWrs.setResultBln(false);
            webResultWrs.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            webResultWrs.setResultObl(resultObl);
            e.printStackTrace();
        }
        return webResultWrs;
    }

    //End Region Public Methods

    //Region Private Methods

    //End Region Private Methods

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

    private static class mGetArticleImageFromWebserviceGetAsyncTask extends AsyncTask<GetByItemnoAndVariantCodeParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(final GetByItemnoAndVariantCodeParams... params) {
            cWebresult WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUTCH;
            l_PropertyInfo1Pin.setValue(cUser.currentUser.getUsernameStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
            l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_OWNER;
            l_PropertyInfo2Pin.setValue("");
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
            l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMNO;
            l_PropertyInfo3Pin.setValue(params[0].itemNoStr);
            l_PropertyInfoObl.add(l_PropertyInfo3Pin);

            PropertyInfo l_PropertyInfo4Pin = new PropertyInfo();
            l_PropertyInfo4Pin.name = cWebserviceDefinitions.WEBPROPERTY_VARIANTCODE;
            l_PropertyInfo4Pin.setValue(params[0].variantCodeStr);
            l_PropertyInfoObl.add(l_PropertyInfo4Pin);

            PropertyInfo l_PropertyInfo5Pin = new PropertyInfo();
            l_PropertyInfo5Pin.name = cWebserviceDefinitions.WEBPROPERTY_REFRESH;
            l_PropertyInfo5Pin.setValue(false);
            l_PropertyInfoObl.add(l_PropertyInfo5Pin);

            try {
                WebresultWrs = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETARTICLEIMAGE, l_PropertyInfoObl);

            } catch (JSONException e) {
                WebresultWrs.setResultBln(false);
                WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return WebresultWrs;
        }
    }

    private static class mGetImagesFromWebserviceTask extends AsyncTask<List<String>, Void, cWebresult> {
        @SafeVarargs
        @Override
        protected final cWebresult doInBackground(final List<String>... params) {
            cWebresult webResult = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAMEDUTCH;
            l_PropertyInfo1Pin.setValue(cUser.currentUser.getUsernameStr());
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
            l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_OWNER;
            l_PropertyInfo2Pin.setValue("");
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            SoapObject articleImagesList = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_ITEMSLIST);

            for (String itemNoAndVariantCodeStr: params[0]) {
                String [] itemNoAndVariantObl = itemNoAndVariantCodeStr.split(";");

                SoapObject soapObject = new SoapObject(cWebservice.WEBSERVICE_NAMESPACE, cWebserviceDefinitions.WEBPROPERTY_ARTICLEINPUT_COMPLEX);
                soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_ITEMNO_COMPLEX,itemNoAndVariantObl[0]);

                if (itemNoAndVariantObl.length > 1) {
                    soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_VARIANTCODE_COMPLEX, itemNoAndVariantObl[1]);

                }
                else{
                    soapObject.addProperty(cWebserviceDefinitions.WEBPROPERTY_VARIANTCODE_COMPLEX, "");
                }

                articleImagesList.addSoapObject(soapObject);
            }

            PropertyInfo l_PropertyInfo7Pin = new PropertyInfo();
            l_PropertyInfo7Pin.name = cWebserviceDefinitions.WEBPROPERTY_ITEMSLIST;
            l_PropertyInfo7Pin.setValue(articleImagesList);
            l_PropertyInfoObl.add(l_PropertyInfo7Pin);

            try {
                webResult = cWebresult.pGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETARTICLEIMAGESMULTIPLE, l_PropertyInfoObl);

            } catch (JSONException e) {
                webResult.setResultBln(false);
                webResult.setSuccessBln(false);
                e.printStackTrace();
            }

            return webResult;
        }
    }


}
