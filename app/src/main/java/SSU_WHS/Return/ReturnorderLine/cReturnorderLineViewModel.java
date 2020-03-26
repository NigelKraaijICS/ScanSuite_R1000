package SSU_WHS.Return.ReturnorderLine;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Webservice.cWebresult;

public class cReturnorderLineViewModel extends AndroidViewModel {
    //Region Public Properties
    private cReturnorderLineRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cReturnorderLineViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cReturnorderLineRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public void insert(cReturnorderLineEntity pvReturnorderLineEntity) {this.Repository.insert(pvReturnorderLineEntity);}
    public void delete(cReturnorderLineEntity pvReturnorderLineEntity) {this.Repository.delete(pvReturnorderLineEntity);}
    public void deleteAll() {this.Repository.deleteAll();}

    public cWebresult pSaveLineViaWebserviceWrs (){return this.Repository.pSaveLineViaWebserviceWrs();}
    public void pCreateItemVariantViaWebservice(){
        this.Repository.pCreateItemVariantViaWebservice();
    }
    public cWebresult pResetLineViaWebserviceWrs (Long pvLineNoLng){return this.Repository.pResetLineViaWebserviceWrs(pvLineNoLng);}
    public void pUpdateQuantity(){
        this.Repository.pUpdateQuantity();
    }


}

