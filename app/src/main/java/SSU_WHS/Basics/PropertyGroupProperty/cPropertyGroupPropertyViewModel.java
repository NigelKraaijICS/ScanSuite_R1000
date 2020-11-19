package SSU_WHS.Basics.PropertyGroupProperty;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Basics.PropertyGroup.cPropertyGroupEntity;
import SSU_WHS.Basics.PropertyGroup.cPropertyGroupRepository;
import SSU_WHS.Webservice.cWebresult;

public class cPropertyGroupPropertyViewModel extends AndroidViewModel {
    //Region Public Properties
    public cPropertyGroupPropertyRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cPropertyGroupPropertyViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cPropertyGroupPropertyRepository(pvApplication);
    }
    //End Region Constructor


    //Region Public Methods
    public void insert(cPropertyGroupPropertyEntity pvPropertyGroupPropertyEntity) {this.Repository.pInsert(pvPropertyGroupPropertyEntity);}
    public void deleteAll() {Repository.pDeleteAll();}

    //End Region Public Methods
}
