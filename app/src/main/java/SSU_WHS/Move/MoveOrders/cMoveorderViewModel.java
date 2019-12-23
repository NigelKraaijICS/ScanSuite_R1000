package SSU_WHS.Move.MoveOrders;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import SSU_WHS.Basics.ArticleBarcode.cArticleBarcode;
import SSU_WHS.Move.MoveorderLines.cMoveorderLineEntity;
import SSU_WHS.Webservice.cWebresult;

public class cMoveorderViewModel extends AndroidViewModel {
    //Region Public Properties
    private cMoveorderRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cMoveorderViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cMoveorderRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public void insert(cMoveorderEntity pvMoveorderEntity) {this.Repository.insert(pvMoveorderEntity);}
    public void deleteAll() {this.Repository.deleteAll();}

    public cWebresult pCreateMoveOrderViaWebserviceWrs(String pvDocumentStr) {return this.Repository.pCreateMoveOrderViaWebserviceWrs(pvDocumentStr);}

    public cWebresult pGetMoveordersFromWebserviceWrs(String pvSearchTextStr, String pvMainTypeStr) {return this.Repository.pGetMoveordersFromWebserviceWrs(pvSearchTextStr, pvMainTypeStr);}

    public List<cMoveorderEntity> pGetMoveordersFromDatabaseObl() {return this.Repository.pGetMoveordersFromDatabaseObl();}

    public List<cMoveorderEntity> pGetMovesWithFilterFromDatabaseObl(String pvCurrentUserStr, Boolean pvUseFiltersBln) {return this.Repository.pGetMovesFromDatabaseWithFilterObl(pvCurrentUserStr, pvUseFiltersBln);}

    public cWebresult pAddUnknownItemViaWebserviceWrs(cBarcodeScan pvBarcodeScan) {return this.Repository.pAddUnkownItemViaWebserviceWrs(pvBarcodeScan);}

    public cWebresult pAddERPItemViaWebserviceWrs(cArticleBarcode pvArticleBarcode) {return this.Repository.pAddERPItemViaWebserviceWrs(pvArticleBarcode);}

    public cWebresult pGetLinesFromWebserviceWrs( ) {return this.Repository.pGetLinesFromWebserviceWrs();}

    public cWebresult pGetCommentsFromWebserviceWrs() {return this.Repository.pGetCommentsFromWebservice(); }

    public cWebresult pGetBarcodesFromWebserviceWrs() {return this.Repository.pGetBarcodesFromWebserviceWrs();}

    public cWebresult pGetLineBarcodesFromWebserviceWrs() {return this.Repository.pGetLineBarcodesFromWebserviceWrs();}

    public cWebresult pHandledViaWebserviceWrs(){return this.Repository.pHandledViaWebserviceWrs();}

    public cWebresult pUpdateCurrentLocationViaWebserviceWrs(String pvCurrentLocationStr) {return this.Repository.pUpdateCurrentLocationViaWebserviceWrs(pvCurrentLocationStr);}
    public Boolean pUpdateMoveorderCurrentLocationInDatabaseBln(String pvCurrentLocationStr) {return this.Repository.pPickorderUpdatCurrentLocationInDatabaseBln(pvCurrentLocationStr);}

    public Double pQuantityNotHandledDbl() {return this.Repository.pQuantityNotHandledDbl();}
    public Double pQuantityHandledDbl() {return this.Repository.pQuantityHandledDbl();}
    public Double pGetQuantityTotalDbl() {return this.Repository.pGetTotalQuantityDbl();}

    public List<cMoveorderLineEntity> pGetLinesHandledFromDatabaseObl(){return  this.Repository.pGetLinesHandledFromDatabaseObl();}
    public List<cMoveorderLineEntity> pGetLinesNotHandledFromDatabaseObl(){return  this.Repository.pGetLinesNotHandledFromDatabaseObl();}

    public List<cMoveorderLineEntity> pGetLinesForBinFromDatabaseObl(String pvBinStr) {return Repository.pGetLinesForBinFromDatabaseObl(pvBinStr);}
}
