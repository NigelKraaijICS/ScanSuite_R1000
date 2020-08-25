package SSU_WHS.Move.MoveorderLines;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcode;
import SSU_WHS.Webservice.cWebresult;

public class cMoveorderLineViewModel extends AndroidViewModel {
    //Region Public Properties
    private cMoveorderLineRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cMoveorderLineViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cMoveorderLineRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public void insert(cMoveorderLineEntity pvMoveorderLineEntity) {this.Repository.insert(pvMoveorderLineEntity);}
    public void delete(cMoveorderLineEntity pvMoveorderLineEntity) {this.Repository.delete(pvMoveorderLineEntity);}
    public void deleteAll() {this.Repository.deleteAll();}

    public cWebresult pAddUnknownBarcodeViaWebserviceWrs(cBarcodeScan pvBarcodeScan) {return  this.Repository.pAddUnkownBarcodeViaWebserviceWrs(pvBarcodeScan);}
    public cWebresult pAddERPBarcodeViaWebserviceWrs(cBarcodeScan pvBarcodeScan) {return  this.Repository.pAddERPBarcodeViaWebserviceWrs(pvBarcodeScan);}

    public cWebresult pMoveItemTakeHandledViaWebserviceWrs(List<cMoveorderBarcode> pvScannedBarcodesObl) {return this.Repository.pMoveItemTakeHandledViaWebserviceWrs(pvScannedBarcodesObl);}
    public cWebresult pMoveItemPlaceHandledViaWebserviceWrs(List<cMoveorderBarcode> pvScannedBarcodesObl) {return this.Repository.pMoveItemPlaceHandledViaWebserviceWrs(pvScannedBarcodesObl);}

    public cWebresult pMoveItemTakeMTHandledViaWebserviceWrs(List<cMoveorderBarcode> pvScannedBarcodesObl) {return this.Repository.pMoveItemTakeMTHandledViaWebserviceWrs(pvScannedBarcodesObl);}
    public cWebresult pMoveItemPlaceMTHandledViaWebserviceWrs(List<cMoveorderBarcode> pvScannedBarcodesObl) {return this.Repository.pMoveItemPlaceMTHandledViaWebserviceWrs(pvScannedBarcodesObl);}

    public cWebresult pResetLineViaWebserviceWrs() {return  this.Repository.pResetLineViaWebserviceWrs();}


}

