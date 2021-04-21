package SSU_WHS.LineItemProperty.LinePropertyValue;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;


public class cLinePropertyValueViewModel extends AndroidViewModel {
    //Region Public Properties
    private final cLinePropertyValueRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cLinePropertyValueViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cLinePropertyValueRepository(pvApplication);
    }

    //End Region Constructor

    public void insert(cLinePropertyValueEntity linePropertyValueEntity) {this.Repository.pInsert(linePropertyValueEntity);}
    public void deleteAll() {this.Repository.pTruncate();}
    public void delete(cLinePropertyValueEntity linePropertyValueEntity) {this.Repository.pDelete(linePropertyValueEntity);}
}
