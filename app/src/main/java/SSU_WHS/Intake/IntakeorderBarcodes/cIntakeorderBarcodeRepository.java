package SSU_WHS.Intake.IntakeorderBarcodes;

import android.app.Application;
import android.os.AsyncTask;

import SSU_WHS.General.acScanSuiteDatabase;

public class cIntakeorderBarcodeRepository {
    //Region Public Properties
    public iIntakeorderBarcodeDao Repository;
    //End Region Public Properties

    //Region Private Properties


    //Region Constructor
    cIntakeorderBarcodeRepository(Application pvApplication) {
        acScanSuiteDatabase db =  acScanSuiteDatabase.pGetDatabase(pvApplication);
        this.Repository = db.intakeorderBarcodeDao();
    }
    //End Region Constructor

    //Region Public Methods

    public void insert(cIntakeorderBarcodeEntity intakeorderBarcodeEntity) {
        new mInsertAsyncTask(Repository).execute(intakeorderBarcodeEntity);
    }

    public void deleteAll() {
        new mDeleteAllAsyncTask(Repository).execute();
    }

    private static class mInsertAsyncTask extends AsyncTask<cIntakeorderBarcodeEntity, Void, Void> {
        private iIntakeorderBarcodeDao mAsyncTaskDao;

        mInsertAsyncTask(iIntakeorderBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final cIntakeorderBarcodeEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class mDeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private iIntakeorderBarcodeDao mAsyncTaskDao;

        mDeleteAllAsyncTask(iIntakeorderBarcodeDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

}
