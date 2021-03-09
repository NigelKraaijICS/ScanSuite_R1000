package SSU_WHS.Picken.PickorderCompositeBarcode;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcodeEntity;
import SSU_WHS.Picken.PickorderBarcodes.cPickorderBarcodeRepository;

public class cPickorderCompositeBarcodeViewModel extends AndroidViewModel {

    //Region Public Properties
    private cPickorderCompositeBarcodeRepository pickorderCompositeBarcodeRepository;
    //End Region Public Properties

    //Region Constructor
    public cPickorderCompositeBarcodeViewModel(Application pvApplication) {
        super(pvApplication);
        this.pickorderCompositeBarcodeRepository = new cPickorderCompositeBarcodeRepository(pvApplication);
    }
    //End Region Constructor

    public void insert(cPickorderCompositeBarcodeEntity pvPickorderBarcodeEntity) {this.pickorderCompositeBarcodeRepository.pInsert(pvPickorderBarcodeEntity);}
    public void deleteAll() {this.pickorderCompositeBarcodeRepository.pDeleteAll();}
}
