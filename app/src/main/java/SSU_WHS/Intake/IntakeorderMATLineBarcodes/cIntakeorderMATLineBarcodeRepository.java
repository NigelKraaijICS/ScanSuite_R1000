package SSU_WHS.Intake.IntakeorderMATLineBarcodes;

import android.app.Application;
import android.os.AsyncTask;

import org.json.JSONException;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import SSU_WHS.Basics.Users.cUser;
import SSU_WHS.General.acScanSuiteDatabase;
import SSU_WHS.Intake.IntakeorderMATLines.cIntakeorderMATLineRepository;
import SSU_WHS.Intake.Intakeorders.cIntakeorder;
import SSU_WHS.Webservice.cWebresult;
import SSU_WHS.Webservice.cWebserviceDefinitions;

public class cIntakeorderMATLineBarcodeRepository {
    //Public Properties

    public iIntakeorderMATLineBarcodeDao intakeorderMATLineBarcodeDao;

    //End Public Properties

    //Private properties
    private acScanSuiteDatabase db;
    //End Private properties

    //Region Constructor
    cIntakeorderMATLineBarcodeRepository(Application application) {
        this.db = acScanSuiteDatabase.getDatabase(application);
        this.intakeorderMATLineBarcodeDao = db.intakeorderMATLineBarcodeDao();
    }

    //End Region Constructor

    //Region public methods

    public void pInsert (cIntakeorderMATLineBarcodeEntity intakeorderMATLineBarcodeEntity) {
        new insertAsyncTask(intakeorderMATLineBarcodeDao).execute(intakeorderMATLineBarcodeEntity);
    }

    public void pDeleteAll () {
        new deleteAllAsyncTask(intakeorderMATLineBarcodeDao).execute();
    }

    public void pDeleteForLineNo(Integer pvLineNoInt) {
        new mDeleteAllForLineNoAsyncTask(intakeorderMATLineBarcodeDao).execute(pvLineNoInt);
    }

    public void updateBarcodeAmount(String pvBarcodeStr, double pvAmountDbl) {
        UpdateBarcodeAmountParams updateBarcodeAmountParams = new UpdateBarcodeAmountParams(pvBarcodeStr, pvAmountDbl);
        new updateBarcodeAmountAsyncTask(intakeorderMATLineBarcodeDao).execute(updateBarcodeAmountParams);
    }

    //End Region Public Methods

    //Region Private methods


    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iIntakeorderMATLineBarcodeDao mAsyncTaskDao;

        deleteAllAsyncTask(iIntakeorderMATLineBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class mDeleteAllForLineNoAsyncTask extends AsyncTask<Integer, Void, Void> {
        private iIntakeorderMATLineBarcodeDao mAsyncTaskDao;

        mDeleteAllForLineNoAsyncTask(iIntakeorderMATLineBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final Integer... params) {
            mAsyncTaskDao.deleteLinesForLineNo(params[0]);
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<cIntakeorderMATLineBarcodeEntity, Void, Void> {
        private iIntakeorderMATLineBarcodeDao mAsyncTaskDao;

        insertAsyncTask(iIntakeorderMATLineBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final cIntakeorderMATLineBarcodeEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class UpdateBarcodeAmountParams {
        String barcodeStr;
        double amountDbl;

        UpdateBarcodeAmountParams(String pvBarcodeStr, double pvAmountDbl) {
            this.barcodeStr = pvBarcodeStr;
            this.amountDbl = pvAmountDbl;
        }
    }

    private static class updateBarcodeAmountAsyncTask extends AsyncTask<UpdateBarcodeAmountParams, Void, Void> {
        private iIntakeorderMATLineBarcodeDao mAsyncTaskDao;
        updateBarcodeAmountAsyncTask(iIntakeorderMATLineBarcodeDao dao) { mAsyncTaskDao = dao; }
        @Override
        protected Void doInBackground(UpdateBarcodeAmountParams... params) {
            mAsyncTaskDao.updateBarcodeAmount(params[0].barcodeStr, params[0].amountDbl);
            return null;
        }
    }



}
