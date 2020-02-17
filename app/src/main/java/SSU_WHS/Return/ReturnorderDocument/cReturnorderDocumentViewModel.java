package SSU_WHS.Return.ReturnorderDocument;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

public class cReturnorderDocumentViewModel extends AndroidViewModel {
    //Region Public Properties
    private cReturnorderDocumentRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cReturnorderDocumentViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cReturnorderDocumentRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods
    public void insert(cReturnorderDocumentEntity pvReturnorderDocumentEntity) {this.Repository.insert(pvReturnorderDocumentEntity);}
    public void deleteAll() {this.Repository.deleteAll();}
    public Boolean pCloseBln() {return  this.Repository.pCloseBln();}

}
