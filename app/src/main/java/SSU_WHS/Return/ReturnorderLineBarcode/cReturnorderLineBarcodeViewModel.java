package SSU_WHS.Return.ReturnorderLineBarcode;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

public class cReturnorderLineBarcodeViewModel extends AndroidViewModel {
    private cReturnorderLineBarcodeRepository returnorderLineBarcodeRepository;

    public cReturnorderLineBarcodeViewModel(Application pvApplication) {
        super(pvApplication);
        this.returnorderLineBarcodeRepository = new cReturnorderLineBarcodeRepository(pvApplication);
    }

    public void deleteAll() { this.returnorderLineBarcodeRepository.pDeleteAll(); }
    public void pDeleteForLineNo(Integer pvLineNoInt) {this.returnorderLineBarcodeRepository.pDeleteForLineNo(pvLineNoInt);}

    public void insert(cReturnorderLineBarcodeEntity pvReturnorderLineBarcodeEntity) { returnorderLineBarcodeRepository.pInsert(pvReturnorderLineBarcodeEntity); }
}
