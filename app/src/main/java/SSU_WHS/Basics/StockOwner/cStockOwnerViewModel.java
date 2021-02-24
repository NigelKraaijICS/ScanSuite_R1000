package SSU_WHS.Basics.StockOwner;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Webservice.cWebresult;

public class cStockOwnerViewModel extends AndroidViewModel {
    //Region Public Properties
    public cStockOwnerRepository Repository;
    //End Region Public Properties

    //Region Constructor
public cStockOwnerViewModel(Application pvApplication){
    super(pvApplication);
    this.Repository = new cStockOwnerRepository(pvApplication);
}

    //End Region Constructor
    //Region Public Methods
    public cWebresult pStockOwnerFromWebserviceWrs() {return this.Repository.pGetStockOwnerFromWebserviceWrs(); }
    public void insert(cStockOwnerEntity pvStockownerEntity) {this.Repository.pInsert(pvStockownerEntity);}
    public void deleteAll() {Repository.pDeleteAll();}
    //End Region Public Methods
}
