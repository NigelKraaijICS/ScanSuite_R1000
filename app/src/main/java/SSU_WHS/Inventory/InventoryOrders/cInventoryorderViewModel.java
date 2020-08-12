package SSU_WHS.Inventory.InventoryOrders;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import SSU_WHS.Basics.ArticleBarcode.cArticleBarcode;
import SSU_WHS.Inventory.InventoryorderBins.cInventoryorderBinEntity;
import SSU_WHS.Webservice.cWebresult;

public class cInventoryorderViewModel extends AndroidViewModel {
    //Region Public Properties
    private cInventoryorderRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cInventoryorderViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cInventoryorderRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public void insert(cInventoryorderEntity pvInventoryorderEntity) {this.Repository.insert(pvInventoryorderEntity);}

    public void insertAll(List<cInventoryorderEntity>  pvInventoryorderEntities) {this.Repository.insertAll(pvInventoryorderEntities);}

    public void deleteAll() {this.Repository.deleteAll();}

    public cWebresult pCreateInventoryOrderViaWebserviceWrs(String pvDocumentStr) {return this.Repository.pCreateInventoryOrderViaWebserviceWrs(pvDocumentStr);}

    public cWebresult pGetInventoryordersFromWebserviceWrs(String pvSearchTextStr) {return this.Repository.pGetInventoryordersFromWebserviceWrs(pvSearchTextStr);}

    public List<cInventoryorderEntity> pGetInventoriesWithFilterFromDatabaseObl(String pvCurrentUserStr, Boolean pvUseFiltersBln) {return this.Repository.pGetInventoriesFromDatabaseWithFilterObl(pvCurrentUserStr, pvUseFiltersBln);}

    public cWebresult pAddBinViaWebserviceWrs(String pvBinCodeStr) {return this.Repository.pAddBinViaWebserviceWrs(pvBinCodeStr);}

    public cWebresult pCloseBinViaWebserviceWrs(String pvBinCodeStr) {return this.Repository.pCloseBinViaWebserviceWrs(pvBinCodeStr);}

    public cWebresult pAddUnknownItemViaWebserviceWrs(cBarcodeScan pvBarcodeScan) {return this.Repository.pAddUnkownItemViaWebserviceWrs(pvBarcodeScan);}

    public cWebresult pAddERPItemViaWebserviceWrs(cArticleBarcode pvArticleBarcode) {return this.Repository.pAddERPItemViaWebserviceWrs(pvArticleBarcode);}

    public cWebresult pAddLineViaWebserviceWrs() {return this.Repository.pAddLineViaWebserviceWrs();}

    public List<cInventoryorderBinEntity> pGetBinsNotDoneFromDatabaseObl(){return  this.Repository.pGetInventoryorderBinNotDoneFromDatabaseObl();}

    public List<cInventoryorderBinEntity> pGetBinsDoneFromDatabaseObl(){return  this.Repository.pGetInventoryorderBinDoneFromDatabaseObl();}

    public List<cInventoryorderBinEntity> pGetBinsTotalFromDatabaseObl(){return  this.Repository.pGetInventoryorderBinTotalFromDatabaseObl();}

    public cWebresult pGetLinesFromWebserviceWrs( ) {return this.Repository.pGetLinesFromWebserviceWrs();}

    public cWebresult pGetBinsFromWebserviceWrs( ) {return this.Repository.pGetBinsFromWebserviceWrs();}

    public cWebresult pGetPossibleBinsFromWebserviceWrs( ) {return this.Repository.pGetPossibleBinsFromWebserviceWrs();}

    public cWebresult pGetCommentsFromWebserviceWrs() {return this.Repository.pGetCommentsFromWebservice(); }

    public cWebresult pGetBarcodesFromWebserviceWrs() {return this.Repository.pGetBarcodesFromWebserviceWrs();}

    public cWebresult pGetLineBarcodesFromWebserviceWrs() {return this.Repository.pGetLineBarcodesFromWebserviceWrs();}

    public cWebresult pHandledViaWebserviceWrs(){return this.Repository.pHandledViaWebserviceWrs();}

}
