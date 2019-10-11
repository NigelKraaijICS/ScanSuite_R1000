package SSU_WHS.Picken.PickorderLinePackAndShip;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class cPickorderLinePackAndShipViewModel extends AndroidViewModel {

    //Region Public Properties
    public cPickorderLinePackAndShipRepository repository;
    //End Region Public Properties

    //Region Constructor

    public cPickorderLinePackAndShipViewModel(Application pvApplication) {
        super(pvApplication);
        this.repository = new cPickorderLinePackAndShipRepository(pvApplication);
    }

    //End Region Constructor

    //Region Public Methods

    public void insert(cPickorderLinePackAndShipEntity pickorderLinePackAndShipEntity) {
        repository.insert(pickorderLinePackAndShipEntity);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    //End Region Public Methods

}
