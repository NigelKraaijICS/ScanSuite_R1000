package SSU_WHS.PackAndShip.PackAndShipBarcode;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcodeEntity;
import SSU_WHS.Move.MoveorderBarcodes.cMoveorderBarcodeRepository;
import SSU_WHS.Webservice.cWebresult;

public class cPackAndShipBarcodeViewModel extends AndroidViewModel {
    //Region Public Properties
    private cPackAndShipBarcodeRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cPackAndShipBarcodeViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cPackAndShipBarcodeRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public void insert(cPackAndShipBarcodeEntity pvPackAndShipBarcodeEntity) {this.Repository.insert(pvPackAndShipBarcodeEntity);}
    public void deleteAll() {this.Repository.deleteAll();}

    public cWebresult pCreateBarcodeViaWebserviceWrs() {return this.Repository.pCreateBarcodeViaWebserviceWrs();}
}
