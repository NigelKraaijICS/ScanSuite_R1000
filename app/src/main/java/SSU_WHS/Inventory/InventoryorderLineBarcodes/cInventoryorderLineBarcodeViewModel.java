package SSU_WHS.Inventory.InventoryorderLineBarcodes;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

public class cInventoryorderLineBarcodeViewModel extends AndroidViewModel {
    public cInventoryorderLineBarcodeRepository inventoryorderLineBarcodeRepository;

    public cInventoryorderLineBarcodeViewModel(Application pvApplication) {
        super(pvApplication);
        this.inventoryorderLineBarcodeRepository = new cInventoryorderLineBarcodeRepository(pvApplication);
    }

    public void deleteAll() { this.inventoryorderLineBarcodeRepository.pDeleteAll(); }
    public void pDeleteForLineNo(Integer pvLineNoInt) {this.inventoryorderLineBarcodeRepository.pDeleteForLineNo(pvLineNoInt);}
    public void pUpdateAmountForLineNo(String pvBarcodeStr, Double pvAmoundDbl) {this.inventoryorderLineBarcodeRepository.updateBarcodeAmount(pvBarcodeStr,pvAmoundDbl);}

    public void insert(cInventoryorderLineBarcodeEntity pvInventoryorderLineBarcodeEntity) { inventoryorderLineBarcodeRepository.pInsert(pvInventoryorderLineBarcodeEntity); }
}
