package SSU_WHS.Return.ReturnorderLine;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

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

    public List<cReturnorderLineEntity> pGetLinesFromDatabaseObl(String pvSourceDocumentStr){return  this.Repository.pGetReturnOrderLinesForSourceDocumentFromDatabaseObl(pvSourceDocumentStr);}
    public cWebresult pSaveLineViaWebserviceWrs (){return this.Repository.pSaveLineViaWebserviceWrs();}
    public cWebresult pCreateItemVariantViaWebserviceWrs(){return this.Repository.pCreateItemVariantViaWebserviceWrs();}
    public cWebresult pResetLineViaWebserviceWrs (Long pvLineNoLng){return this.Repository.pResetLineViaWebserviceWrs(pvLineNoLng);}
    public Boolean pUpdateQuantityBln(){return  this.Repository.pUpdateQuantityBln();}


}

