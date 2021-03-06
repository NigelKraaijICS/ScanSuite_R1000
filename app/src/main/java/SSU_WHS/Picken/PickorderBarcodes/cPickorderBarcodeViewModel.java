package SSU_WHS.Picken.PickorderBarcodes;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

public class cPickorderBarcodeViewModel extends AndroidViewModel {

    //Region Public Properties
    private cPickorderBarcodeRepository pickorderBarcodeRepository;
    //End Region Public Properties

    //Region Constructor
    public cPickorderBarcodeViewModel(Application pvApplication) {
        super(pvApplication);
        this.pickorderBarcodeRepository = new cPickorderBarcodeRepository(pvApplication);
    }
    //End Region Constructor


    public void insert(cPickorderBarcodeEntity pvPickorderBarcodeEntity) {this.pickorderBarcodeRepository.pInsert(pvPickorderBarcodeEntity);}
    public void delete(cPickorderBarcodeEntity pvPickorderBarcodeEntity) {this.pickorderBarcodeRepository.pDelete(pvPickorderBarcodeEntity);}
    public void deleteAll() {this.pickorderBarcodeRepository .pDeleteAll();}




}
