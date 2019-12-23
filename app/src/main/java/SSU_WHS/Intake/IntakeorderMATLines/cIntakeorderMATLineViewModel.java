package SSU_WHS.Intake.IntakeorderMATLines;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import SSU_WHS.Webservice.cWebresult;

public class cIntakeorderMATLineViewModel extends AndroidViewModel {
    //Region Public Properties
    private cIntakeorderMATLineRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cIntakeorderMATLineViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cIntakeorderMATLineRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public void insert(cIntakeorderMATLineEntity pvIntakeorderMATLineEntity) {this.Repository.insert(pvIntakeorderMATLineEntity);}
    public void deleteAll() {this.Repository.deleteAll();}


    public List<cIntakeorderMATLineEntity> pGetIntakeorderMATLinesFromDatabaseObl() {return this.Repository.pGetIntakeorderMATLinesFromDatabaseObl();}
    public boolean pUpdateLocalStatusBln(Integer pvNewStatusInt) {return this.Repository.pUpdateLocalStatusBln(pvNewStatusInt);}
    public boolean pUpdateQuantityBln(Double pvQuantityDbl) {return this.Repository.pUpdateQuantitydBln(pvQuantityDbl);}
    public boolean pUpdateQuantityHandledBln(Double pvQuantityHandledDbl) {return this.Repository.pUpdateQuantityHandledBln(pvQuantityHandledDbl);}
    public boolean pUpdateBinCodeHandledBln(String pvBinCodeHandledStr) {return this.Repository.pUpdateBinCodeHandledBln(pvBinCodeHandledStr);}
    public cWebresult pResetViaWebserviceWrs() {return this.Repository.pResetViaWebserviceWrs();}
    public cWebresult pLineHandledViaWebserviceWrs() {return this.Repository.pLineHandledViaWebserviceWrs();}
    public cWebresult pLineSplitViaWebserviceWrs() {return this.Repository.pLineSplitViaWebserviceWrs();}
}
