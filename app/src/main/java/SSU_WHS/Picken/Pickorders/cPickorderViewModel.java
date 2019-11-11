package SSU_WHS.Picken.Pickorders;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import java.util.List;

import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Picken.PickorderLinePackAndShip.cPickorderLinePackAndShipEntity;
import SSU_WHS.Picken.PickorderLines.cPickorderLineEntity;
import SSU_WHS.Webservice.cWebresult;

public class cPickorderViewModel extends AndroidViewModel {

    //Region Public Properties
    private cPickorderRepository PickorderRepository;
    //End Region Public Properties


    //Region Constructor
    public cPickorderViewModel(Application pvApplication) {
        super(pvApplication);
        this.PickorderRepository = new cPickorderRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public void insert(cPickorderEntity pvPickorderEntity) {this.PickorderRepository.insert(pvPickorderEntity);}
    public void deleteAll() {this.PickorderRepository.deleteAll();}
    public void pAbortOrder() {this.PickorderRepository.pAbortOrder();}

    public cWebresult pGetPickordersFromWebserviceWrs(Boolean pvProcessingOrParkedBln, String pvSearchTextStr) {return this.PickorderRepository.pGetPickordersFromWebserviceWrs(pvProcessingOrParkedBln,pvSearchTextStr);}
    public List<cPickorderEntity> pGetPickordersFromDatabaseObl() {return this.PickorderRepository.pGetPickordersFromDatabaseObl();}
    public cWebresult pGetSortOrShipordersFromWebserviceWrs(String pvUserStr, cWarehouseorder.StepCodeEnu pvStepCodeEnu, String pvSearchTextStr) {return this.PickorderRepository.pGetPickordersToShipFromWebserviceWrs(pvUserStr,pvStepCodeEnu,pvSearchTextStr);}
    public List<cPickorderEntity> pGetPickordersWithFilterFromDatabaseObl(String pvCurrentUserStr, Boolean pvUseFiltersBln) {return this.PickorderRepository.pGetPickordersFromDatabaseWithFilterObl(pvCurrentUserStr, pvUseFiltersBln);}

    public cWebresult pPickenHandledViaWebserviceWrs(String pvWorkplaceStr) { return this.PickorderRepository.pPickHandledViaWebserviceBln(pvWorkplaceStr);}
    public cWebresult pSortHandledViaWebserviceWrs(String pvWorkplaceStr) { return this.PickorderRepository.pSortHandledViaWebserviceBln(pvWorkplaceStr);}
    public cWebresult pShipHandledViaWebserviceWrs(String pvWorkplaceStr) { return this.PickorderRepository.pShipHandledViaWebserviceWrs(pvWorkplaceStr);}
    public cWebresult pPickorderSourceDocumentShippedViaWebserviceBln() { return this.PickorderRepository.pPickorderSourceDocumentShippedViaWebserviceWrs();}

    public cWebresult pUpdateCurrentLocationViaWebserviceWrs(String pvCurrentLocationStr) {return this.PickorderRepository.pUpdateCurrentLocationViaWebserviceWrs(pvCurrentLocationStr);}
    public Boolean pUpdatePickorderCurrentLocationInDatabaseBln(String pvCurrentLocationStr) {return this.PickorderRepository.pPickorderUpdatCurrentLocationInDatabaseBln(pvCurrentLocationStr);}
    public Boolean pPickorderUpdateWorkplaceViaWebserviceBln(String pvWorkplaceStr) {return this.PickorderRepository.pPickorderUpdateWorkplaceViaWebserviceBln(pvWorkplaceStr);}

    public Double pQuantityNotHandledDbl() {return this.PickorderRepository.pQuantityNotHandledDbl();}
    public Double pQuantityHandledDbl() {return this.PickorderRepository.pQuantityHandledDbl();}
    public Double pGetQuantityTotalDbl() {return this.PickorderRepository.pGetTotalQuantityDbl();}


    public cWebresult pGetLinesFromWebserviceWrs(cWarehouseorder.ActionTypeEnu pvActionTypeEnu ) {return this.PickorderRepository.pGetLinesFromWebserviceWrs(pvActionTypeEnu);}
    public List<cPickorderLineEntity> pGetAllLinesFromDatabaseObl(){return  this.PickorderRepository.pGetAllLinesFromDatabaseObl();}
    public List<cPickorderLineEntity> pGetPickorderLinesToSendFromDatabaseObl(){return  this.PickorderRepository.pGetPickorderLinesToSendFromDatabaseObl();}
    public List<cPickorderLineEntity> pGetLinesNotHandledFromDatabaseObl(){return  this.PickorderRepository.pGetLinesNotHandledFromDatabaseObl();}
    public List<cPickorderLineEntity> pGetLinesBusyFromDatabaseObl(){return  this.PickorderRepository.pGetLinesBusyFromDatabaseObl();}
    public List<cPickorderLineEntity> pGetLinesHandledFromDatabaseObl(){return  this.PickorderRepository.pGetLinesHandledFromDatabaseObl();}

    public cWebresult pGetPackAndShipLinesFromWebserviceWrs( ) {return this.PickorderRepository.pGetPackAndShipLinesFromWebserviceWrs();}
    public List<cPickorderLinePackAndShipEntity> pGetPackAndShipLinesNotHandledFromDatabaseObl(){return  this.PickorderRepository.pGetPackAndShipLinesNotHandledFromDatabaseObl();}

    public cWebresult pGetAdressesFromWebserviceWrs() {return this.PickorderRepository.pGetAddressesFromWebserviceWrs();}

    public cWebresult pGetArticleImagesFromWebserviceWrs(List<String> pvItemNoAndVariantObl) {return this.PickorderRepository.pGetArticleImagesFromWebserviceWrs(pvItemNoAndVariantObl);}

    public cWebresult pGetBarcodesFromWebserviceWrs() {return this.PickorderRepository.pGetBarcodesFromWebservice(); }

    public cWebresult pGetLineBarcodesFromWebserviceWrs(cWarehouseorder.ActionTypeEnu pvActionTypeEnu) {return this.PickorderRepository.pGetLineBarcodesFromWebservice(pvActionTypeEnu); }

    public cWebresult pGetCommentsFromWebserviceWrs() {return this.PickorderRepository.pGetCommentsFromWebservice(); }

    public cWebresult pGetPackagesFromWebserviceWrs() {return this.PickorderRepository.pGetPackagesFromWebserviceWrs(); }

    //End Region Public Methods
}
