package SSU_WHS.Basics.PrintItemLabel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Webservice.cWebresult;

public class cPrintItemLabelViewModel extends AndroidViewModel {

    //Region Public Properties
    public cPrintItemLabelRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public  cPrintItemLabelViewModel(Application pvApplication){
        super(pvApplication);
        this.Repository = new cPrintItemLabelRepository();
    }
    //End Region Constructor

    //Region Public Methods
    public cWebresult pPrintLineItemLabelViaWebserviceWrs() {return this.Repository.pPrintLineItemLabelViaWebserviceWrs();}
    public cWebresult pPrintItemLabelViaWebserviceWrs(){return this.Repository.pPrintItemLabelViaWebserviceWrs();}

    //END Region Public Methods
}
