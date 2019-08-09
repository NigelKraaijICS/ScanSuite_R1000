package SSU_WHS.Picken.PickorderBarcodes;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class cPickorderBarcodeViewModel extends AndroidViewModel {
    private cPickorderBarcodeRepository mRepository;

    public cPickorderBarcodeViewModel(Application application) {
        super(application);

        mRepository = new cPickorderBarcodeRepository(application);
    }

    public List<cPickorderBarcodeEntity> getPickorderBarcodesForItemVariantCode(String itemno, String variantcode) {return mRepository.getPickorderBarcodesForItemVariantCode(itemno, variantcode);}

    public void insert(cPickorderBarcodeEntity pickorderBarcodeEntity) {mRepository.insert(pickorderBarcodeEntity);}

    public LiveData<List<cPickorderBarcodeEntity>> getPickorderBarcodes(Boolean forcerefresh, String branchStr, String ordernumberStr) {return mRepository.getPickorderBarcodes(forcerefresh, branchStr, ordernumberStr);}

    //public List<cPickorderBarcodeEntity> getPickorderBarcodesLocal(String branchStr, String ordernumberStr) {return mRepository.getPickorderBarcodesLocal(branchStr, ordernumberStr);}

    public void deleteAll() {mRepository.deleteAll();}

    public cPickorderBarcodeEntity getPickOrderBarcodeByBarcode(String barcode) {return mRepository.getPickOrderBarcodeByBarcode(barcode); }


}
