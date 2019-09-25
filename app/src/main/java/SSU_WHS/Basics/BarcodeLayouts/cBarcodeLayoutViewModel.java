package SSU_WHS.Basics.BarcodeLayouts;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import SSU_WHS.Webservice.cWebresult;

public class cBarcodeLayoutViewModel extends AndroidViewModel{
    //Region Public Properties
    public cBarcodeLayoutRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cBarcodeLayoutViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cBarcodeLayoutRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public cWebresult pGetBarcodeLayoutsFromWebserviceWrs() {return this.Repository.pGetBarcodeLayoutsFromWebserviceWrs(); }
    public void insert(cBarcodeLayoutEntity pvBarcodeLayoutEntity) {this.Repository.pInsert(pvBarcodeLayoutEntity);}
    public void deleteAll() {Repository.pDeleteAll();}
    //End Region Public Methods

}



