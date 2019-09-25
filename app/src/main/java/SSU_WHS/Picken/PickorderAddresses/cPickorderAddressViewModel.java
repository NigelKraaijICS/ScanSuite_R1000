package SSU_WHS.Picken.PickorderAddresses;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Webservice.cWebresult;

public class cPickorderAddressViewModel extends AndroidViewModel {

    //Region Public Properties
    public cPickorderAddressRepository repository;
    //END Region Public Properties

    //Region Constructor
    public cPickorderAddressViewModel(Application pvApplication) {
        super(pvApplication);
        this.repository = new cPickorderAddressRepository(pvApplication);
    }
    //End Region Constructor
    public void insert(cPickorderAddressEntity pvPickorderAddressEntity) {repository.pInsert(pvPickorderAddressEntity);}
    public void deleteAll() {repository.pDeleteAll();}
}
