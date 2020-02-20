package SSU_WHS.Receive.ReceiveLines;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import ICS.Utils.Scanning.cBarcodeScan;
import SSU_WHS.Intake.IntakeorderBarcodes.cIntakeorderBarcode;
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

    public boolean pUpdateLocalStatusBln(Integer pvNewStatusInt) {return this.Repository.pUpdateLocalStatusBln(pvNewStatusInt);}
    public boolean pUpdateQuantityHandledBln(Double pvQuantityHandledDbl) {return this.Repository.pUpdateQuantityHandledBln(pvQuantityHandledDbl);}

    public cWebresult pResetReceiveLineViaWebserviceWrs() {return this.Repository.pResetReceiveLineViaWebserviceWrs();}
    public cWebresult pLineHandledViaWebserviceWrs(List<cIntakeorderBarcode> pvBarcodeObl ) {return this.Repository.pReceiveLineHandledViaWebserviceWrs(pvBarcodeObl);}


    public  cWebresult pAddUnknownItemViaWebserviceWrs(cBarcodeScan pvBarcodeScan) {return  this.Repository.pAddUnkownItemViaWebserviceWrs(pvBarcodeScan);}
    public  cWebresult pAddUnknownBarcodeViaWebserviceWrs(cBarcodeScan pvBarcodeScan) {return  this.Repository.pAddUnkownBarcodeViaWebserviceWrs(pvBarcodeScan);}

    public  cWebresult pAddERPItemViaWebserviceWrs(cBarcodeScan pvBarcodeScan) {return  this.Repository.pAddERPItemViaWebserviceWrs(pvBarcodeScan);}
    public  cWebresult pAddERPBarcodeViaWebserviceWrs(cBarcodeScan pvBarcodeScan) {return  this.Repository.pAddUnkownBarcodeViaWebserviceWrs(pvBarcodeScan);}

}
