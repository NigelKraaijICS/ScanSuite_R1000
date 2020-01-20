package SSU_WHS.Return.ReturnOrder;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import SSU_WHS.Basics.ArticleBarcode.cArticleBarcode;
import SSU_WHS.Webservice.cWebresult;

public class cReturnorderViewModel extends AndroidViewModel {
    //Region Public Properties
    private cReturnorderRepository Repository;
      //End Region Public Properties

    //Region Constructor
    public cReturnorderViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cReturnorderRepository(pvApplication);
    }

    //End Region Constructor

    //Region Public Methods
    public void insert(cReturnorderEntity pvReturnorderEntity) {this.Repository.insert(pvReturnorderEntity);}
    public void deleteAll() {this.Repository.deleteAll();}

    public cWebresult pCreateReturnOrderViaWebserviceWrs(String pvDocumentStr, Boolean pvMultipleDocumentsBln, String pvBincodeStr) {return this.Repository.pCreateReturnOrderViaWebserviceWrs(pvDocumentStr, pvMultipleDocumentsBln, pvBincodeStr);}

    public cWebresult pGetReturnordersFromWebserviceWrs(String pvSearchTextStr) {return this.Repository.pGetReturnordersFromWebserviceWrs(pvSearchTextStr);}

    public List<cReturnorderEntity> pGetReturnOrdersWithFilterFromDatabaseObl(String pvCurrentUserStr, Boolean pvUseFiltersBln) {return this.Repository.pGetReturnOrdersFromDatabaseWithFilterObl(pvCurrentUserStr, pvUseFiltersBln);}

    public cWebresult pAddUnknownItemViaWebserviceWrs(cBarcodeScan pvBarcodeScan) {return this.Repository.pAddUnkownItemViaWebserviceWrs(pvBarcodeScan);}

    public cWebresult pAddERPItemViaWebserviceWrs(cArticleBarcode pvArticleBarcode) {return this.Repository.pAddERPItemViaWebserviceWrs(pvArticleBarcode);}

    public cWebresult pGetLinesFromWebserviceWrs( ) {return this.Repository.pGetLinesFromWebserviceWrs();}

    public cWebresult pGetHandledLinesFromWebserviceWrs () {return this.Repository.pGetHandledLinesFromWebserviceWrs();}

    public cWebresult pGetCommentsFromWebserviceWrs() {return this.Repository.pGetCommentsFromWebservice(); }

    public cWebresult pGetBarcodesFromWebserviceWrs() {return this.Repository.pGetBarcodesFromWebserviceWrs();}

    public cWebresult pGetLineBarcodesFromWebserviceWrs() {return this.Repository.pGetLineBarcodesFromWebserviceWrs();}

    public cWebresult pHandledViaWebserviceWrs(){return this.Repository.pHandledViaWebserviceWrs();}

    public cWebresult pReturnDisposedViaWebserviceWrs(){return  this.Repository.pReturnDisposedViaWebserviceWrs();}

}
