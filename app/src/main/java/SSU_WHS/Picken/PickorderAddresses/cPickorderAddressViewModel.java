package SSU_WHS.Picken.PickorderAddresses;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;

public class cPickorderAddressViewModel extends AndroidViewModel {

    //Region Public Properties
    public cPickorderAddressRepository repository;
    //Eend Region Public Properties

    //Region Constructor
    public cPickorderAddressViewModel(Application pvApplication) {
        super(pvApplication);
        this.repository = new cPickorderAddressRepository(pvApplication);
    }
    //End Region Constructor

    //Region Public Methods

    public void insert(cPickorderAddressEntity pvPickorderAddressEntity) {
        repository.pInsert(pvPickorderAddressEntity);
    }

    public void deleteAll() {
        repository.pDeleteAll();
    }

    //End Region Public Methods


}
