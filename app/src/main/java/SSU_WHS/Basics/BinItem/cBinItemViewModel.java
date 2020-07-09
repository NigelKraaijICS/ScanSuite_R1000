package SSU_WHS.Basics.BinItem;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Webservice.cWebresult;

public class cBinItemViewModel extends AndroidViewModel {

    //Region Public Properties
    public cBinItemRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cBinItemViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cBinItemRepository();
    }
    //End Region Constructor

    //Region Public Methods
    public cWebresult pBinItemsFromWebserviceWrs(String pvBinCodeStr) {return this.Repository.pBinItemsFromWebserviceWrs(pvBinCodeStr); }

    //End Region Public Methods

}