package SSU_WHS.Picken.Pickorders;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import java.util.List;

import SSU_WHS.General.Warehouseorder.cWarehouseorder;
import SSU_WHS.Picken.PickorderLines.cPickorderLineEntity;
import SSU_WHS.Picken.PickorderShipPackages.cPickorderShipPackageEntity;
import SSU_WHS.Webservice.cWebresult;

public class cPickorderViewModel extends AndroidViewModel {

    //Region Public Properties
    public cPickorderRepository PickorderRepository;
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
    public List<cPickorderEntity> pGetPickordersWithFilterFromDatabaseObl(String pvCurrentUserStr, Boolean pvUseFiltersBln, Boolean pvShowProcessedWaitBln, Boolean pvShowSingleArticlesBln, Boolean pvShowAssignedToMeBln, Boolean pvShowAssignedToOthersBln, Boolean pvShowNotAssignedBln) {return this.PickorderRepository.pGetPickordersFromDatabaseWithFilterObl(pvCurrentUserStr, pvUseFiltersBln, pvShowProcessedWaitBln,pvShowSingleArticlesBln,  pvShowAssignedToMeBln, pvShowAssignedToOthersBln, pvShowNotAssignedBln);}

    public Boolean pPickenHandledViaWebserviceBln(String pvWorkplaceStr) { return this.PickorderRepository.pPickenHandledViaWebserviceBln(pvWorkplaceStr);}
    public Boolean pPickorderSourceDocumentShippedViaWebserviceBln(String pvSourceNoStr, String pvShippingAgentStr, String pvShippingServiceStr, List<cPickorderShipPackageEntity> pvPackagesObl) { return this.PickorderRepository.pPickorderSourceDocumentShippedViaWebserviceBln(pvSourceNoStr, pvShippingAgentStr, pvShippingServiceStr, pvPackagesObl);}
    public Boolean pPickorderUpdateWorkplaceViaWebserviceBln() { return this.PickorderRepository.pPickorderUpdateWorkplaceViaWebserviceBln();}
    public Boolean pUpdatePickorderWorkplaceInDatabaseInt() {return this.PickorderRepository.pPickorderUpdateWorkplaceInDatabaseBln();}


    public cWebresult pUpdateCurrentLocationViaWebserviceWrs(String pvCurrentLocationStr) {return this.PickorderRepository.pUpdateCurrentLocationViaWebserviceWrs(pvCurrentLocationStr);}
    public Boolean pUpdatePickorderCurrentLocationInDatabaseBln(String pvCurrentLocationStr) {return this.PickorderRepository.pPickorderUpdatCurrentLocationInDatabaseBln(pvCurrentLocationStr);}

    public Double pQuantityNotHandledDbl() {return this.PickorderRepository.pNumberNotHandledDbl();}
    public Double pQuantityHandledCounterDbl() {return this.PickorderRepository.pNumberHandledDbl();}
    public Double pGetNumberTotalForCounterDbl() {return this.PickorderRepository.pGetTotalQuantityDbl();}

    public cWebresult pGetLinesFromWebserviceWrs(cWarehouseorder.ActionTypeEnu pvActionTypeEnu ) {return this.PickorderRepository.pGetLinesFromWebserviceWrs(pvActionTypeEnu);}
    public List<cPickorderLineEntity> pGetAllLinesFromDatabaseObl(){return  this.PickorderRepository.pGetAllLinesFromDatabaseObl();}
    public List<cPickorderLineEntity> pGetPickorderLinesToSendFromDatabaseObl(){return  this.PickorderRepository.pGetPickorderLinesToSendFromDatabaseObl();}
    public List<cPickorderLineEntity> pGetLinesNotHandledFromDatabaseObl(){return  this.PickorderRepository.pGetLinesNotHandledFromDatabaseObl();}
    public List<cPickorderLineEntity> pGetLinesHandledFromDatabaseObl(){return  this.PickorderRepository.pGetLinesHandledFromDatabaseObl();}

    public cWebresult pGetAdressesFromWebserviceWrs() {return this.PickorderRepository.pGetAddressesFromWebserviceWrs();}

    public cWebresult pGetArticleImagesFromWebserviceWrs(List<String> pvItemNoAndVariantObl) {return this.PickorderRepository.pGetArticleImagesFromWebserviceWrs(pvItemNoAndVariantObl);}

    public cWebresult pGetBarcodesFromWebserviceWrs() {return this.PickorderRepository.pGetBarcodesFromWebservice(); }

    public cWebresult pGetCommentsFromWebserviceWrs() {return this.PickorderRepository.pGetCommentsFromWebservice(); }

    //End Region Public Methods
}
