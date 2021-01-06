package SSU_WHS.Picken.PickorderLineProperty;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Picken.PickorderLines.cPickorderLineEntity;
import SSU_WHS.Picken.PickorderLines.cPickorderLineRepository;
import SSU_WHS.Webservice.cWebresult;

public class cPickorderLinePropertyViewModel extends AndroidViewModel {

    //Region Public Properties
    private cPickorderLinePropertyRepository Repository;
    //End Region Public Properties

    //Region Constructor
    public cPickorderLinePropertyViewModel(Application pvApplication) {
        super(pvApplication);
        this.Repository = new cPickorderLinePropertyRepository(pvApplication);
    }
    //End Region Constructor

    public void insert(cPickorderLinePropertyEntity pvPickorderLinePropertyEntity) {this.Repository.pInsert(pvPickorderLinePropertyEntity);}
    public void deleteAll() {this.Repository.pTruncate();}
    public void delete(cPickorderLinePropertyEntity pvPickorderLinePropertyEntity) {this.Repository.pDelete(pvPickorderLinePropertyEntity);}

}
