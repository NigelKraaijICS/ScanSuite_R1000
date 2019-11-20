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

    public void insert(cInventoryorderLineBarcodeEntity pvInventoryorderLineBarcodeEntity) { inventoryorderLineBarcodeRepository.pInsert(pvInventoryorderLineBarcodeEntity); }
    public void delete(cInventoryorderLineBarcodeEntity pvInventoryorderLineBarcodeEntity) { inventoryorderLineBarcodeRepository.pDelete(pvInventoryorderLineBarcodeEntity); }

}
