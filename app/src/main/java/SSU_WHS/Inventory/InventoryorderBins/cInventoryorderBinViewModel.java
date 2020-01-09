package SSU_WHS.Inventory.InventoryorderBins;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import SSU_WHS.Webservice.cWebresult;

public class cInventoryorderBinViewModel extends AndroidViewModel {
    //Region Public Properties
    private cInventoryorderBinRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cInventoryorderBinViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cInventoryorderBinRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public void insert(cInventoryorderBinEntity pvInventoryorderBinEntity) {this.Repository.insert(pvInventoryorderBinEntity);}
    public void insertAll(List< cInventoryorderBinEntity> pvInventoryorderBinEntitiesObl) {this.Repository.insertAll(pvInventoryorderBinEntitiesObl);}
    public  Boolean pUpdateStatusAndTimeStampBln() {return  this.Repository.pUpdateStatusAndTimestampBln();}
    public void deleteAll() {this.Repository.deleteAll();}
    public cWebresult  pResetBinViaWebserviceWrs() {return this.Repository.pResetBinViaWebserviceWrs();}
    public cWebresult  pReopenBinViaWebserviceWrs() {return this.Repository.pReopenBinViaWebserviceWrs();}
}
