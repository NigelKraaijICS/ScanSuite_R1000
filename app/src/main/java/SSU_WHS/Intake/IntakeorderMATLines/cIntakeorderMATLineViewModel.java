package SSU_WHS.Intake.IntakeorderMATLines;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
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


    public boolean pUpdateLocalStatusBln(Integer pvNewStatusInt) {return this.Repository.pUpdateLocalStatusBln(pvNewStatusInt);}
    public boolean pUpdateQuantityHandledBln(Double pvQuantityHandledDbl) {return this.Repository.pUpdateQuantityHandledBln(pvQuantityHandledDbl);}
    public boolean pUpdateBinCodeHandledBln(String pvBinCodeHandledStr) {return this.Repository.pUpdateBinCodeHandledBln(pvBinCodeHandledStr);}
    public cWebresult pResetMATLineViaWebserviceWrs() {return this.Repository.pResetMATLineViaWebserviceWrs();}
    public cWebresult pMATLineHandledViaWebserviceWrs() {return this.Repository.pMATLineHandledViaWebserviceWrs();}
    public cWebresult pMATLineSplitViaWebserviceWrs() {return this.Repository.pMATLineSplitViaWebserviceWrs();}
    public cWebresult pMATCreateLineViaWebserviceWrs(String pvBarcodeStr, List<cIntakeorderBarcode> pvBarcodeObl) {return this.Repository.pCreateMATLineViaWebserviceWrs(pvBarcodeStr,pvBarcodeObl);}
}
