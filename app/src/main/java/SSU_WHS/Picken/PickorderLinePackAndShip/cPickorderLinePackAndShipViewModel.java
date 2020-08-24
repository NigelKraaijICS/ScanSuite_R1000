package SSU_WHS.Picken.PickorderLinePackAndShip;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcodeEntity;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcodeRepository;
import SSU_WHS.Webservice.cWebresult;

public class cPickorderLinePackAndShipViewModel extends AndroidViewModel {

    //Region Public Properties
    private cPickorderLinePackAndShipRepository pickorderLinePackAndShipRepository;
    //End Region Public Properties

    //Region Constructor
    public cPickorderLinePackAndShipViewModel(Application pvApplication) {
        super(pvApplication);
        this.pickorderLinePackAndShipRepository = new cPickorderLinePackAndShipRepository();
    }
    //End Region Constructor


    public cWebresult pLineCheckedViaWebserviceWrs() {return this.pickorderLinePackAndShipRepository.pLineCheckedViaWebserviceWrs();}



}
