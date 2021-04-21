package SSU_WHS.LineItemProperty.LineProperty;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;


public class cLinePropertyViewModel  extends AndroidViewModel {
    //Region Public Properties
    private final cLinePropertyRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cLinePropertyViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cLinePropertyRepository(pvApplication);
    }
    //End Region Constructor

    public void insert(cLinePropertyEntity linePropertyEntity) {this.Repository.pInsert(linePropertyEntity);}
    public void deleteAll() {this.Repository.pTruncate();}
    public void delete(cLinePropertyEntity linePropertyEntity) {this.Repository.pDelete(linePropertyEntity);}
}
