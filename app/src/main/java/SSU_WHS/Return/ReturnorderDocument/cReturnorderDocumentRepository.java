package SSU_WHS.Return.ReturnorderDocument;

import android.app.Application;
import android.os.AsyncTask;

import java.util.concurrent.ExecutionException;

import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Return.ReturnorderLine.cReturnorderLine;

public class cReturnorderDocumentRepository {
    //Region Public Properties
    private iReturnorderDocumentDao returnorderDocumentDao;
    //End Region Public Properties

    //Region Private Properties


    private static class UpdateReturnDocumentStatusParams {
        String documentStr;
        int statusInt;

        UpdateReturnDocumentStatusParams(String pvBinCodeStr, int statusInt) {
            this.documentStr = pvBinCodeStr;
            this.statusInt = statusInt;
        }
    }

    //Region Constructor
    cReturnorderDocumentRepository(Application pvApplication) {
        this.returnorderDocumentDao = acScanSuiteDatabase.pGetDatabase(pvApplication).returnorderDocumentDao();
    }
    //End Region Constructor

    //Region Public Methods


    public void insert(cReturnorderDocumentEntity returnorderBinEntity) {
        new mInsertAsyncTask(returnorderDocumentDao).execute(returnorderBinEntity);
    }

    public void deleteAll() {
        new mDeleteAllAsyncTask(returnorderDocumentDao).execute();
    }

    public boolean pUpdateStatusBln() {

        Integer integerValue;
        UpdateReturnDocumentStatusParams updateReturnDocumentStatusParams = new UpdateReturnDocumentStatusParams(cReturnorderDocument.currentReturnOrderDocument.getSourceDocumentStr(),
                cReturnorderDocument.currentReturnOrderDocument.getStatusInt());


        try {
            integerValue = new updateStatusAsyncTask(returnorderDocumentDao).execute(updateReturnDocumentStatusParams).get();

            return integerValue != 0;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean pCloseBln() {

        boolean completeBln = true;
        Integer returnDocumentStatusEnu;
        for (cReturnorderLine returnorderLine: cReturnorderDocument.currentReturnOrderDocument.returnorderLineObl){
            if(returnorderLine.quantityHandledTakeDbl < returnorderLine.getQuantitytakeDbl() ){
                completeBln = false;

            }
        }
        if (cReturnorderDocument.currentReturnOrderDocument.returnorderLineObl.size() == 0 ){
            completeBln = false;
        }

        if (!completeBln){
            returnDocumentStatusEnu = cWarehouseorder.ReturnDocumentStatusEnu.InventoryPause;

        } else {
            returnDocumentStatusEnu = cWarehouseorder.ReturnDocumentStatusEnu.ReturnDone;
        }

        cReturnorderDocument.currentReturnOrderDocument.statusInt = returnDocumentStatusEnu;

        Integer integerValue;
        UpdateReturnDocumentStatusParams updateReturnDocumentStatusParams = new UpdateReturnDocumentStatusParams(cReturnorderDocument.currentReturnOrderDocument.getSourceDocumentStr(),
                returnDocumentStatusEnu);


        try {
            integerValue = new updateStatusAsyncTask(returnorderDocumentDao).execute(updateReturnDocumentStatusParams).get();

            return integerValue != 0;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static class mInsertAsyncTask extends AsyncTask<cReturnorderDocumentEntity, Void, Void> {
        private iReturnorderDocumentDao mAsyncTaskDao;

        mInsertAsyncTask(iReturnorderDocumentDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cReturnorderDocumentEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mDeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iReturnorderDocumentDao mAsyncTaskDao;

        mDeleteAllAsyncTask(iReturnorderDocumentDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class updateStatusAsyncTask extends AsyncTask<UpdateReturnDocumentStatusParams, Void, Integer> {
        private iReturnorderDocumentDao mAsyncTaskDao;

        updateStatusAsyncTask(iReturnorderDocumentDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Integer doInBackground(UpdateReturnDocumentStatusParams... params) {
            return mAsyncTaskDao.updateBinStatus(params[0].documentStr, params[0].statusInt);
        }
    }
}

