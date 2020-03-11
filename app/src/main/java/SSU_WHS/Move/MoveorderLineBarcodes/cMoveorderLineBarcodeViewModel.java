package SSU_WHS.Move.MoveorderLineBarcodes;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

public class cMoveorderLineBarcodeViewModel extends AndroidViewModel {
    public cMoveorderLineBarcodeRepository moveorderLineBarcodeRepository;

    public cMoveorderLineBarcodeViewModel(Application pvApplication) {
        super(pvApplication);
        this.moveorderLineBarcodeRepository = new cMoveorderLineBarcodeRepository(pvApplication);
    }

    public void deleteAll() { this.moveorderLineBarcodeRepository.pDeleteAll(); }
    public void pDeleteForLineNo(Integer pvLineNoInt) {this.moveorderLineBarcodeRepository.pDeleteForLineNo(pvLineNoInt);}
    public void pUpdateAmountForLineNo(String pvBarcodeStr, Double pvAmoundDbl) {this.moveorderLineBarcodeRepository.updateBarcodeAmount(pvBarcodeStr,pvAmoundDbl);}

    public void insert(cMoveorderLineBarcodeEntity pvMoveorderLineBarcodeEntity) { moveorderLineBarcodeRepository.pInsert(pvMoveorderLineBarcodeEntity); }

}
