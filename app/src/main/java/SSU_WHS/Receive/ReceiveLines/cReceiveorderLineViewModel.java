package SSU_WHS.Receive.ReceiveLines;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
import SSU_WHS.LineItemProperty.LineProperty.cLineProperty;
import SSU_WHS.Webservice.cWebresult;

public class cReceiveorderLineViewModel extends AndroidViewModel {
    //Region Public Properties
    private cReceiveorderLineRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cReceiveorderLineViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cReceiveorderLineRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods

    public void insert(cReceiveorderLineEntity pvReceiveorderLineEntity) {this.Repository.insert(pvReceiveorderLineEntity);}
    public void deleteAll() {this.Repository.deleteAll();}

    public cWebresult pResetReceiveLineViaWebserviceWrs() {return this.Repository.pResetReceiveLineViaWebserviceWrs();}
    public cWebresult pLineHandledViaWebserviceWrs(List<cIntakeorderBarcode> pvBarcodeObl ) {return this.Repository.pReceiveLineHandledViaWebserviceWrs(pvBarcodeObl);}

    public  cWebresult pReceiveAddUnknownItemViaWebserviceWrs(cBarcodeScan pvBarcodeScan) {return  this.Repository.pReceiveAddUnkownItemViaWebserviceWrs(pvBarcodeScan);}
    public  cWebresult pReceiveAddUnknownBarcodeViaWebserviceWrs(cBarcodeScan pvBarcodeScan) {return  this.Repository.pReceiveAddUnkownBarcodeViaWebserviceWrs(pvBarcodeScan);}

    public  cWebresult pLinePropertysHandledViaWebserviceWrs(List<cLineProperty> pvLinePropertieObl) {return this.Repository.pLinePropertysHandledViaWebserviceWrs(pvLinePropertieObl);}
    public  cWebresult pIntakeAddUnknownItemViaWebserviceWrs(cBarcodeScan pvBarcodeScan) {return  this.Repository.pIntakeAddUnkownItemViaWebserviceWrs(pvBarcodeScan);}
    public  cWebresult pIntakeAddUnknownBarcodeViaWebserviceWrs(cBarcodeScan pvBarcodeScan, boolean pvIsUniqueBarcodeBln) {return  this.Repository.pIntakeAddUnkownBarcodeViaWebserviceWrs(pvBarcodeScan, pvIsUniqueBarcodeBln);}

    public  cWebresult pAddERPItemViaWebserviceWrs(cBarcodeScan pvBarcodeScan) {return  this.Repository.pAddERPItemViaWebserviceWrs(pvBarcodeScan);}
    public  cWebresult pReceiveAddERPBarcodeViaWebserviceWrs(cBarcodeScan pvBarcodeScan) {return  this.Repository.pReceiveAddUnkownBarcodeViaWebserviceWrs(pvBarcodeScan);}

}
