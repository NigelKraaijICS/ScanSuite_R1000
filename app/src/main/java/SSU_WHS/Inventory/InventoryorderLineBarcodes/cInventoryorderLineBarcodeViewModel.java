package SSU_WHS.Inventory.InventoryorderLineBarcodes;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.List;

public class cInventoryorderLineBarcodeViewModel extends AndroidViewModel {
    public cInventoryorderLineBarcodeRepository repository;

    public cInventoryorderLineBarcodeViewModel(Application pvApplication) {
        super(pvApplication);
        this.repository = new cInventoryorderLineBarcodeRepository(pvApplication);
    }

    public void deleteAll() { this.repository.pDeleteAll(); }

    public void insert(cInventoryorderLineBarcodeEntity pvInventoryorderLineBarcodeEntity) { repository.pInsert(pvInventoryorderLineBarcodeEntity); }
    public void insertAll(List<cInventoryorderLineBarcodeEntity> pvInventoryorderLineEntities) {this.repository.insertAll(pvInventoryorderLineEntities);}

    public void delete(cInventoryorderLineBarcodeEntity pvInventoryorderLineBarcodeEntity) { repository.pDelete(pvInventoryorderLineBarcodeEntity); }

}
