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

    public cWebresult pCreateMoveOrderViaWebserviceWrs(String pvDocumentStr, String pvBinCodeStr, boolean pvCheckBarcodesBln) {return this.Repository.pCreateMoveOrderViaWebserviceWrs(pvDocumentStr,pvBinCodeStr,pvCheckBarcodesBln);}

    public cWebresult pGetMoveordersFromWebserviceWrs(String pvSearchTextStr, String pvMainTypeStr) {return this.Repository.pGetMoveordersFromWebserviceWrs(pvSearchTextStr, pvMainTypeStr);}

    public List<cMoveorderEntity> pGetMovesWithFilterFromDatabaseObl(String pvCurrentUserStr, Boolean pvUseFiltersBln) {return this.Repository.pGetMovesFromDatabaseWithFilterObl(pvCurrentUserStr, pvUseFiltersBln);}

    public cWebresult pGetLinesFromWebserviceWrs() {return this.Repository.pGetLinesFromWebserviceWrs(); }

    public List<cMoveorderLineEntity> pGetLinesForBinFromDatabaseObl(String pvBinStr) {return Repository.pGetLinesForBinFromDatabaseObl(pvBinStr);}

    public cWebresult pGetBarcodesFromWebserviceWrs() {return this.Repository.pGetBarcodesFromWebserviceWrs();}

    public cWebresult pGetLineBarcodesFromWebserviceWrs() {return this.Repository.pGetLineBarcodesFromWebserviceWrs();}

    public cWebresult pAddUnknownItemViaWebserviceWrs(cBarcodeScan pvBarcodeScan) {return this.Repository.pAddUnkownItemViaWebserviceWrs(pvBarcodeScan);}

    public cWebresult pAddERPItemViaWebserviceWrs(cArticleBarcode pvArticleBarcode) {return this.Repository.pAddERPItemViaWebserviceWrs(pvArticleBarcode);}

    public cWebresult pGetCommentsFromWebserviceWrs() {return this.Repository.pGetCommentsFromWebservice(); }

    public cWebresult pHandledViaWebserviceWrs(){return this.Repository.pHandledViaWebserviceWrs();}


}
