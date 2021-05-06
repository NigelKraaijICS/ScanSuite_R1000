package SSU_WHS.Picken.Pickorders;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Picken.PickorderLines.cPickorderLineEntity;
import SSU_WHS.Webservice.cWebresult;

public class cPickorderViewModel extends AndroidViewModel {

    //Region Public Properties
    private cPickorderRepository Repository;
    //End Region Public Properties


    //Region Constructor
    public cPickorderViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cPickorderRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public void insert(cPickorderEntity pvPickorderEntity) {this.Repository.insert(pvPickorderEntity);}
    public void deleteAll() {this.Repository.deleteAll();}
    public void pAbortOrder() {this.Repository.pAbortOrder();}

    public cWebresult pGetPickordersFromWebserviceWrs(Boolean pvProcessingOrParkedBln, String pvSearchTextStr) {return this.Repository.pGetPickordersFromWebserviceWrs(pvProcessingOrParkedBln,pvSearchTextStr);}
    public cWebresult pGetPickordersNextStepFromWebserviceWrs(String pvUserStr, cWarehouseorder.StepCodeEnu pvStepCodeEnu, String pvSearchTextStr) {return this.Repository.pGetPickorderstPickordersNextStepFromWebserviceWrs(pvUserStr,pvStepCodeEnu,pvSearchTextStr);}
    public List<cPickorderEntity> pGetPickordersWithFilterFromDatabaseObl(String pvCurrentUserStr, Boolean pvUseFiltersBln) {return this.Repository.pGetPickordersFromDatabaseWithFilterObl(pvCurrentUserStr, pvUseFiltersBln);}

    public cWebresult pPickingHandledViaWebserviceWrs(String pvWorkplaceStr) { return this.Repository.pPickHandledViaWebserviceBln(pvWorkplaceStr);}
    public cWebresult pPickingGeneratedHandledViaWebserviceWrs() { return this.Repository.pPickGeneratedHandledViaWebserviceBln();}
    public cWebresult pStoreHandledViaWebserviceWrs() { return this.Repository.pStoreHandledViaWebserviceBln();}
    public cWebresult pSortHandledViaWebserviceWrs(String pvWorkplaceStr) { return this.Repository.pSortHandledViaWebserviceBln(pvWorkplaceStr);}
    public cWebresult pQCHandledViaWebserviceWrs(String pvWorkplaceStr) { return this.Repository.pQCHandledViaWebserviceBln(pvWorkplaceStr);}
    public cWebresult pShipHandledViaWebserviceWrs(String pvWorkplaceStr) { return this.Repository.pShipHandledViaWebserviceWrs(pvWorkplaceStr);}
    public cWebresult pFinishSinglePiecesHandledViaWebserviceWrs(String pvWorkplaceStr) { return this.Repository.pFinishSinglePiecesHandledViaWebserviceWrs(pvWorkplaceStr);}
    public cWebresult pPickorderSourceDocumentShippedViaWebserviceBln() { return this.Repository.pPickorderSourceDocumentShippedViaWebserviceWrs();}
    public cWebresult pPickorderSourceDocumentStoredViaWebserviceBln() { return this.Repository.pPickorderSourceDocumentStoredViaWebserviceWrs();}

    public cWebresult pCreatePickOrderViaWebserviceWrs(String pvDocumentstr,  boolean pvCheckBarcodesBln) {return this.Repository.pCreatePickOrderViaWebserviceWrs(pvDocumentstr, pvCheckBarcodesBln);}

    public cWebresult pUpdateCurrentLocationViaWebserviceWrs(String pvCurrentLocationStr) {return this.Repository.pUpdateCurrentLocationViaWebserviceWrs(pvCurrentLocationStr);}
    public Boolean pUpdatePickorderCurrentLocationInDatabaseBln(String pvCurrentLocationStr) {return this.Repository.pPickorderUpdatCurrentLocationInDatabaseBln(pvCurrentLocationStr);}
    public boolean pUpdateIsSelectedBln() {return this.Repository.pPickorderUpdatIsSelectedInDatabaseBln();}
    public Boolean pPickorderUpdateWorkplaceViaWebserviceBln(String pvWorkplaceStr) {return this.Repository.pPickorderUpdateWorkplaceViaWebserviceBln(pvWorkplaceStr);}

    public Double pQuantityNotHandledDbl() {return this.Repository.pQuantityNotHandledDbl();}
    public Double pQuantityHandledDbl() {return this.Repository.pQuantityHandledDbl();}
    public Double pGetQuantityTotalDbl() {return this.Repository.pGetTotalQuantityDbl();}

    public cWebresult pGetLinesFromWebserviceWrs(cWarehouseorder.ActionTypeEnu pvActionTypeEnu ) {return this.Repository.pGetLinesFromWebserviceWrs(pvActionTypeEnu);}
    public List<cPickorderLineEntity> pGetAllLinesFromDatabaseObl(){return  this.Repository.pGetAllLinesFromDatabaseObl();}
    public List<cPickorderLineEntity> pGetPickorderLinesToSendFromDatabaseObl(){return  this.Repository.pGetPickorderLinesToSendFromDatabaseObl();}
    public List<cPickorderLineEntity> pGetLinesNotHandledFromDatabaseObl(){return  this.Repository.pGetLinesNotHandledFromDatabaseObl();}
    public List<cPickorderLineEntity> pGetLinesNotHandledForBranchFromDatabaseObl(String pvDestinationNoStr){return  this.Repository.pGetLinesNotHandledForBranchFromDatabaseObl(pvDestinationNoStr);}
    public List<cPickorderLineEntity> pGetLinesBusyFromDatabaseObl(){return  this.Repository.pGetLinesBusyFromDatabaseObl();}
    public List<cPickorderLineEntity> pGetLinesHandledFromDatabaseObl(){return  this.Repository.pGetLinesHandledFromDatabaseObl();}

    public cWebresult pGetStorageLinesFromWebserviceWrs() {return this.Repository.pGetStorageLinesFromWebserviceWrs();}

    public cWebresult pGetPackAndShipLinesFromWebserviceWrs( ) {return this.Repository.pGetPackAndShipLinesFromWebserviceWrs();}

    public cWebresult pGetFinishPackSinglePieceLinesViaWebserviceWrs( ) {return this.Repository.pGetFinishPackSinglePieceLinesViaWebserviceWrsWrs();}

    public cWebresult pGetAdressesFromWebserviceWrs() {return this.Repository.pGetAddressesFromWebserviceWrs();}

    public cWebresult pGetBarcodesFromWebserviceWrs() {return this.Repository.pGetBarcodesFromWebservice(); }

    public cWebresult pGetCompositeBarcodesFromWebserviceWrs() {return this.Repository.pGetCompositeBarcodesFromWebservice(); }

    public cWebresult pGetLineBarcodesFromWebserviceWrs(cWarehouseorder.ActionTypeEnu pvActionTypeEnu) {return this.Repository.pGetLineBarcodesFromWebservice(pvActionTypeEnu); }

    public cWebresult pGetLinePropertysViaWebserviceWrs() {return this.Repository.pGetLinePropertysViaWebserviceWrs(); }

    public cWebresult pGetLinePropertyValuesViaWebserviceWrs() {return this.Repository.pGetLinePropertyValuesViaWebserviceWrs(); }

    public cWebresult pGetCommentsFromWebserviceWrs() {return this.Repository.pGetCommentsFromWebservice(); }

    public cWebresult pGetSettingsFromWebserviceWrs() {return this.Repository.pGetSettingsFromWebserviceWrs();}

    public cWebresult pGetPackagesFromWebserviceWrs() {return this.Repository.pGetPackagesFromWebserviceWrs(); }

    public cWebresult pCreateCombinedPickViaWebserviceWrs( ) {return this.Repository.pCreateCombinedPickViaWebserviceWrs();}

    public cWebresult pAddOrderToCombinedPickViaWebserviceWrs( ) {return this.Repository.pAddOrderToCombinedPickViaWebserviceWrs();}

    public cWebresult pRemoveOrderFromCombinedPickViaWebserviceWrs( ) {return this.Repository.pRemoveOrderFromCombinedPickViaWebserviceWrs();}

    public cWebresult pRemoveCombinedPickViaWebserviceWrs( ) {return this.Repository.pRemoveCombinedPickViaWebserviceWrs();}

    //End Region Public Methods
}
