package SSU_WHS.Basics.Users;

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

public class cUserRepository {
    private iUserDao userDao;
    private cWebresult webResult;
    private LiveData<List<cUserEntity>> mLocalUserss;

    cUserRepository(Application application) {
        acScanSuiteDatabase db = acScanSuiteDatabase.getDatabase(application);
        userDao = db.userDao();
    }


    public List<cUserEntity> getLocalUsers() {
        List<cUserEntity> l_UserEntityObl = null;
        try {
            l_UserEntityObl = new getLocalUsersAsyncTask(userDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_UserEntityObl;
    }

    private static class getLocalUsersAsyncTask extends AsyncTask<Void, Void, List<cUserEntity>> {
        private iUserDao mAsyncTaskDao;

        getLocalUsersAsyncTask(iUserDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cUserEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getLocalUsers();
        }
    }

//    public LiveData<List<cUserEntity>> getUsers(final Boolean forceRefresh, final String branch) {
//
//        final MutableLiveData<List<cUserEntity>> data = new MutableLiveData<>();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                List<cUserEntity> usersObl = new ArrayList<>();
//                if (forceRefresh) {
//                    try {
//                        deleteAll();
//                        List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();
//
//                        PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
//                        l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_BRANCH;
//                        l_PropertyInfo1Pin.setValue(branch);
//                        l_PropertyInfoObl.add(l_PropertyInfo1Pin);
//
//                        webResult = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETUSERS, l_PropertyInfoObl);
//                        List<JSONObject> myList = webResult.getResultDtt();
//
//                        cUser.allUsers = new ArrayList<>();
//
//                        for (int i = 0; i < myList.size(); i++) {
//                            JSONObject jsonObject;
//                            jsonObject = myList.get(i);
//
//                            cUser user = new cUser(jsonObject);
//                            cUser.allUsers.add((user));
//                            usersObl.add(user.userEntity);
//                        }
//                        data.postValue(usersObl);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                else {
//                    usersObl = getAll();
//                    data.postValue(usersObl);
//                }
//            } //run
//        }).start(); //Thread
//        return data;
//    }

//    public LiveData<List<cUser>> getUsers(final Boolean forceRefresh, final String branch) {
//
//        final MutableLiveData<List<cUser>> data = new MutableLiveData<>();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if (forceRefresh) {
//                    try {
//                        deleteAll();
//                        List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();
//
//                        PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
//                        l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_BRANCH;
//                        l_PropertyInfo1Pin.setValue(branch);
//                        l_PropertyInfoObl.add(l_PropertyInfo1Pin);
//
//                        webResult = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETUSERS, l_PropertyInfoObl);
//                        List<JSONObject> myList = webResult.getResultDtt();
//
//                        cUser.allUsers = new ArrayList<>();
//
//                        for (int i = 0; i < myList.size(); i++) {
//                            JSONObject jsonObject;
//                            jsonObject = myList.get(i);
//
//                            cUser user = new cUser(jsonObject);
//                            cUser.allUsers.add((user));
//                        }
//                        data.postValue(cUser.allUsers);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                else {
////                    usersObl = getAll();
////                    data.postValue(usersObl);
//                }
//            } //run
//        }).start(); //Thread
//        return data;
//    }

    public LiveData<Boolean> gotUsers(final Boolean forceRefresh, final String branch) {

        final MutableLiveData<Boolean> data = new MutableLiveData<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (forceRefresh) {
                    try {
                        deleteAll();
                        List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

                        PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
                        l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_BRANCH;
                        l_PropertyInfo1Pin.setValue(branch);
                        l_PropertyInfoObl.add(l_PropertyInfo1Pin);

                        webResult = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_GETUSERS, l_PropertyInfoObl);
                        List<JSONObject> myList = webResult.getResultDtt();

                        cUser.allUsers = new ArrayList<>();

                        for (int i = 0; i < myList.size(); i++) {
                            JSONObject jsonObject;
                            jsonObject = myList.get(i);

                            cUser user = new cUser(jsonObject);
                            cUser.allUsers.add((user));
                        }
                        data.postValue(true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
//                    usersObl = getAll();
//                    data.postValue(usersObl);
                }
            } //run
        }).start(); //Thread
        return data;
    }

    public List<cUserEntity> getAll() {
        List<cUserEntity> l_UserEntityObl = null;
        try {
            l_UserEntityObl = new getAllAsyncTask(userDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return l_UserEntityObl;
    }
    private static class getAllAsyncTask extends AsyncTask<Void, Void, List<cUserEntity>> {
        private iUserDao mAsyncTaskDao;

        getAllAsyncTask(iUserDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected List<cUserEntity> doInBackground(final Void... params) {
            return mAsyncTaskDao.getAll();
        }
    }

    public void deleteAll () {
        new cUserRepository.deleteAllAsyncTask(userDao).execute();
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iUserDao mAsyncTaskDao;

        deleteAllAsyncTask(iUserDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    public void insert (cUserEntity userEntity) {
        new insertAsyncTask(userDao).execute(userEntity);
    }
    private static class insertAsyncTask extends AsyncTask<cUserEntity, Void, Void> {
        private iUserDao mAsyncTaskDao;

        insertAsyncTask(iUserDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cUserEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
    public cUserEntity getUserByCode(String username) {
        cUserEntity userEntity = null;
        try {
            userEntity = new getUserByCodeAsyncTask(userDao).execute(username).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return userEntity;
    }
    private static class getUserByCodeAsyncTask extends AsyncTask<String, Void, cUserEntity> {
        private iUserDao mAsyncTaskDao;

        getUserByCodeAsyncTask(iUserDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected cUserEntity doInBackground(final String... params) {
            return mAsyncTaskDao.getUserByCode(params[0]);
        }
    }

    private static class UserLoginParams{
        String user;
        String password;
        String culture;

        UserLoginParams(String pv_user, String pv_password, String pv_culture) {
            this.user = pv_user;
            this.password = pv_password;
            this.culture = pv_culture;
        }
    }

    public cWebresult userLogonWrs (String user, String password, String culture) {
        UserLoginParams userLoginParams = new UserLoginParams(user, password, culture);
        List<String> resultObl = new ArrayList<>();
        cWebresult l_webResult = new cWebresult();
        try {
            l_webResult = new userLogonAsyncTask().execute(userLoginParams).get();
        } catch (ExecutionException e) {
            l_webResult.setResultBln(false);
            l_webResult.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            l_webResult.setResultObl(resultObl);
            e.printStackTrace();
        } catch (InterruptedException e) {
            l_webResult.setResultBln(false);
            l_webResult.setSuccessBln(false);
            resultObl.add(e.getLocalizedMessage());
            l_webResult.setResultObl(resultObl);
            e.printStackTrace();
        }
        return l_webResult;
    }

    private static class userLogonAsyncTask extends AsyncTask<UserLoginParams, Void, cWebresult> {
        @Override
        protected cWebresult doInBackground(UserLoginParams... params) {
            cWebresult l_WebresultWrs = new cWebresult();

            List<PropertyInfo> l_PropertyInfoObl = new ArrayList<>();

            PropertyInfo l_PropertyInfo1Pin = new PropertyInfo();
            l_PropertyInfo1Pin.name = cWebserviceDefinitions.WEBPROPERTY_USERNAME;
            l_PropertyInfo1Pin.setValue(params[0].user);
            l_PropertyInfoObl.add(l_PropertyInfo1Pin);

            PropertyInfo l_PropertyInfo2Pin = new PropertyInfo();
            l_PropertyInfo2Pin.name = cWebserviceDefinitions.WEBPROPERTY_PASSWORD;
            l_PropertyInfo2Pin.setValue(params[0].password);
            l_PropertyInfoObl.add(l_PropertyInfo2Pin);

            PropertyInfo l_PropertyInfo3Pin = new PropertyInfo();
            l_PropertyInfo3Pin.name = cWebserviceDefinitions.WEBPROPERTY_LANGUAGEASCULTURE;
            l_PropertyInfo3Pin.setValue(params[0].culture);
            l_PropertyInfoObl.add(l_PropertyInfo3Pin);

            try {
                l_WebresultWrs = new cWebresult().mGetwebresultWrs(cWebserviceDefinitions.WEBMETHOD_USERLOGIN, l_PropertyInfoObl);
            } catch (JSONException e) {
                l_WebresultWrs.setResultBln(false);
                l_WebresultWrs.setSuccessBln(false);
                e.printStackTrace();
            }

            return l_WebresultWrs;
        }

    }


}
