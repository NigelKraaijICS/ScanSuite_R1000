package SSU_WHS.Picken.Comments;

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

import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;
import SSU_WHS.General.acScanSuiteDatabase;

public class cCommentRepository {
    private iCommentDao commentDao;
    private cWebresult webResult;

    cCommentRepository(Application application) {
        acScanSuiteDatabase db = acScanSuiteDatabase.getDatabase(application);
        commentDao = db.commentDao();
    }

    private static class CommentParams {
        String branch;
        String ordernumber;

        CommentParams(String pv_branch, String pv_ordernumber) {
            this.branch = pv_branch;
            this.ordernumber = pv_ordernumber;
        }
    }

    public void insert (cCommentEntity commentEntity) {
        new insertAsyncTask(commentDao).execute(commentEntity);
    }
    private static class insertAsyncTask extends AsyncTask<cCommentEntity, Void, Void> {
        private iCommentDao mAsyncTaskDao;

        insertAsyncTask(iCommentDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cCommentEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    LiveData<List<cCommentEntity>> getComments(final Boolean forcerefresh, final String branchStr, final String ordernumberStr) {

        final MutableLiveData<List<cCommentEntity>> data = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<cCommentEntity> commentEntities = new ArrayList<>();
                if (forcerefresh) {
                    try {
                        deleteAll();
                        List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                        PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                        l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_BRANCH;
                        l_PropertyInfo1Pin.setValue(branchStr);
                        l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                        PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
                        l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_ORDERNUMBER;
                        l_PropertyInfo2Pin.setValue(ordernumberStr);
                        l_PropertyInfoObl.add(l_PropertyInfo2Pin);

                        webResult = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETPICKORDERCOMMENTS, l_PropertyInfoObl);
                        List<JSONObject> myList = webResult.getResultDtt();
                        for (int i = 0; i < myList.size(); i++) {
                            JSONObject jsonObject;
                            jsonObject = myList.get(i);

                            cCommentEntity commentEntity = new cCommentEntity(jsonObject);
                            insert(commentEntity);
                            commentEntities.add(commentEntity);
                        }
                        data.postValue(commentEntities);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }//forcerefresh
                else {
                    commentEntities = getAll();
                    data.postValue(commentEntities);
                }
            } //run

        }).start(); //Thread
        return data;
    }

    public void deleteAll () {
        new deleteAllAsyncTask(commentDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iCommentDao mAsyncTaskDao;

        deleteAllAsyncTask(iCommentDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    public List<cCommentEntity> getAll() {
        List<cCommentEntity> commentEntities = null;
        try {
            commentEntities = new getAllAsyncTask(commentDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return commentEntities;
    }
    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<cCommentEntity>> {
        private iCommentDao mAsyncTaskDao;

        getAllAsyncTask(iCommentDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cCommentEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }




}
