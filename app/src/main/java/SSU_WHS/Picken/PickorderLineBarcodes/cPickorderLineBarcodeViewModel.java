package SSU_WHS.Picken.PickorderLineBarcodes;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;

import ICS.Utils.cText;

public class cPickorderLineBarcodeViewModel extends AndroidViewModel {
    public cPickorderLineBarcodeRepository pickorderLineBarcodeRepository;

    public cPickorderLineBarcodeViewModel(Application pvApplication) {
        super(pvApplication);
        this.pickorderLineBarcodeRepository = new cPickorderLineBarcodeRepository(pvApplication);
    }

    public void deleteAll() { this.pickorderLineBarcodeRepository.pDeleteAll(); }
    public void pDeleteForLineNo(Integer pvLineNoInt) {this.pickorderLineBarcodeRepository.pDeleteForLineNo(pvLineNoInt);}
    public void pUpdateAmountForLineNo(String pvBarcodeStr, Double pvAmoundDbl) {this.pickorderLineBarcodeRepository.updateBarcodeAmount(pvBarcodeStr,pvAmoundDbl);}

    public void insert(cPickorderLineBarcodeEntity pvPickorderLineBarcodeEntity) { pickorderLineBarcodeRepository.pInsert(pvPickorderLineBarcodeEntity); }
}
