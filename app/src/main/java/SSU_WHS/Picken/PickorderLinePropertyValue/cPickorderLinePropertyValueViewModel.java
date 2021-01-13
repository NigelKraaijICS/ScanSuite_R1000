package SSU_WHS.Picken.PickorderLinePropertyValue;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Picken.PickorderLineProperty.cPickorderLinePropertyEntity;
import SSU_WHS.Picken.PickorderLineProperty.cPickorderLinePropertyRepository;

public class cPickorderLinePropertyValueViewModel extends AndroidViewModel {

    //Region Public Properties
    private cPickorderLinePropertyValueRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cPickorderLinePropertyValueViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cPickorderLinePropertyValueRepository(pvApplication);
    }
    //End Region Constructor

    public void insert(cPickorderLinePropertyValueEntity pvPickorderLinePropertyValueEntity) {this.Repository.pInsert(pvPickorderLinePropertyValueEntity);}
    public void deleteAll() {this.Repository.pTruncate();}
    public void delete(cPickorderLinePropertyValueEntity pvPickorderLinePropertyValueEntity) {this.Repository.pDelete(pvPickorderLinePropertyValueEntity);}


}
