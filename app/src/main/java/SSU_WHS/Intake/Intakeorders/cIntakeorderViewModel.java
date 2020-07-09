package SSU_WHS.Intake.Intakeorders;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import SSU_WHS.Webservice.cWebresult;

public class cIntakeorderViewModel  extends AndroidViewModel {
    //Region Public Properties
    private cIntakeorderRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cIntakeorderViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cIntakeorderRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public void insert(cIntakeorderEntity pvIntakeorderEntity) {this.Repository.pInsertInDatabase(pvIntakeorderEntity);}
    public void deleteAll() {this.Repository.pTruncateTable();}

    public cWebresult pGetIntakeordersFromWebserviceWrs(String pvSearchTextStr) {return this.Repository.pGetIntakeordersFromWebserviceWrs(pvSearchTextStr);}
    public cWebresult pGetIntakeorderBarcodesFromWebserviceWrs() {return this.Repository.pGetBarcodesFromWebserviceWrs();}

    public cWebresult pGetIntakeorderMATLineBarcodesFromWebserviceWrs() {return this.Repository.pGetMATLineBarcodesFromWebserviceWrs();}
    public cWebresult pGetIntakeorderMATLinesFromWebserviceWrs() {return this.Repository.pGetMATLinesFromWebserviceWrs();}
    public cWebresult pGetIntakeorderReceiveLinesFromWebserviceWrs(String pvScannerStr) {return this.Repository.pGetReceiveLinesFromWebserviceWrs(pvScannerStr);}
    public cWebresult pGetIntakeorderReceiveItemsFromWebserviceWrs() {return this.Repository.pGetReceiveItemsFromWebserviceWrs();}
    public cWebresult pGetIntakeorderPackagingFromWebserviceWrs() {return this.Repository.pGetPackagingFromWebserviceWrs();}
    public cWebresult pGetCommentsFromWebserviceWrs() {return this.Repository.pGetCommentsFromWebservice(); }

    public cWebresult pCreateIntakeOrderViaWebserviceWrs(String pvDocumentStr, String pvPackingSlipStr, String pvBinCodeStr, boolean pvCheckBarcodesBln) {return this.Repository.pCreateIntakeOrderViaWebserviceWrs(pvDocumentStr, pvPackingSlipStr, pvBinCodeStr, pvCheckBarcodesBln);}

    public List<cIntakeorderEntity> pGetIntakeordersFromDatabaseWithFilterObl(String pvCurrentUserStr, Boolean pvUseFiltersBln) {return this.Repository.pGetIntakeordersFromDatabaseWithFilterObl(pvCurrentUserStr, pvUseFiltersBln);}

    public Double pQuantityHandledDbl() {return this.Repository.pQuantityHandledDbl();}


    public cWebresult pMATHandledViaWebserviceWrs() { return this.Repository.pMATHandledViaWebserviceBln();}
    public cWebresult pReceiveHandledViaWebserviceWrs() { return this.Repository.pReceiveHandledViaWebserviceBln();}
    public cWebresult pPackagingHandledViaWebserviceWrs() { return this.Repository.pPackagingHandledViaWebserviceBln();}
    public cWebresult pInvalidateViaWebserviceWrs() { return this.Repository.pInvalidateViaWebserviceBln();}


}
