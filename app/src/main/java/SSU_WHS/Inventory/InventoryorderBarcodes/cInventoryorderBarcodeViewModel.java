package SSU_WHS.Inventory.InventoryorderBarcodes;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import SSU_WHS.Webservice.cWebresult;

public class cInventoryorderBarcodeViewModel extends AndroidViewModel {
    //Region Public Properties
    private cInventoryorderBarcodeRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cInventoryorderBarcodeViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cInventoryorderBarcodeRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public void insert(cInventoryorderBarcodeEntity pvInventoryorderBarcodeEntity) {this.Repository.insert(pvInventoryorderBarcodeEntity);}
    public void insertAll(List<cInventoryorderBarcodeEntity>  pvInventoryorderBarcodeEntities) {this.Repository.insertAll(pvInventoryorderBarcodeEntities);}
    public void deleteAll() {this.Repository.deleteAll();}

    public cWebresult pCreateBarcodeViaWebserviceWrs() {return this.Repository.pCreateBarcodeViaWebserviceWrs();}
}
