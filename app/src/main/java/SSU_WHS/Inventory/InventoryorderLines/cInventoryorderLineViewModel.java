package SSU_WHS.Inventory.InventoryorderLines;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import SSU_WHS.Webservice.cWebresult;

public class cInventoryorderLineViewModel extends AndroidViewModel {
    //Region Public Properties
    private cInventoryorderLineRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cInventoryorderLineViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cInventoryorderLineRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public void insert(cInventoryorderLineEntity pvInventoryorderLineEntity) {this.Repository.insert(pvInventoryorderLineEntity);}
    public void delete(cInventoryorderLineEntity pvInventoryorderLineEntity) {this.Repository.delete(pvInventoryorderLineEntity);}
    public void deleteAll() {this.Repository.deleteAll();}

    public List<cInventoryorderLineEntity> pGetLinesFromDatabaseObl(String pvBincode){return  this.Repository.pGetInventoryorderLinesForBincodeFromDatabaseObl(pvBincode);}
    public Double pGetTotalCountDbl(){return  this.Repository.pGetTotalCountDbl();}
    public Double pGetCountForBinCodeDbl(String pvBincode){return  this.Repository.pGetCountForBinCodeDbl(pvBincode);}
    public cWebresult pSaveLineViaWebserviceWrs (){return this.Repository.pSaveLineViaWebserviceWrs();}
    public cWebresult pResetLineViaWebserviceWrs (){return this.Repository.pResetLineViaWebserviceWrs();}
    public Boolean pUpdateQuantityBln(){return  this.Repository.pUpdateQuantityBln();}


}

