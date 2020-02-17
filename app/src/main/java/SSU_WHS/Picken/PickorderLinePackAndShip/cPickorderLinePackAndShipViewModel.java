package SSU_WHS.Picken.PickorderLinePackAndShip;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

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

    //End Region Public Methods

}
