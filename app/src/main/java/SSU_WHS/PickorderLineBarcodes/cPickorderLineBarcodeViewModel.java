package SSU_WHS.PickorderLineBarcodes;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class cPickorderLineBarcodeViewModel extends AndroidViewModel {
    private cPickorderLineBarcodeRepository mRepository;

    public cPickorderLineBarcodeViewModel(Application application) {
        super(application);

        mRepository = new cPickorderLineBarcodeRepository(application);
    }


    public void delete(cPickorderLineBarcodeEntity pickorderLineBarcodeEntity) {mRepository.delete(pickorderLineBarcodeEntity);}

    public void insert(cPickorderLineBarcodeEntity pickorderLineBarcodeEntity) {mRepository.insert(pickorderLineBarcodeEntity);}

    public LiveData<List<cPickorderLineBarcodeEntity>> getPickorderLineBarcodes(Boolean forcerefresh, String branchStr, String ordernumberStr, String actiontypeStr) {return mRepository.getPickorderLineBarcodes(forcerefresh, branchStr, ordernumberStr, actiontypeStr);}

    public List<cPickorderLineBarcodeEntity> getPickorderLineBarcodesForLineNo(Integer lineno) {return mRepository.getPickorderLineBarcodesForLineNo(lineno);}

    public void deleteAll() {mRepository.deleteAll();}

    public cPickorderLineBarcodeEntity getPickorderLineBarcodeByBarcode(String barcode) {return mRepository.getPickorderLineBarcodeByBarcode(barcode);}

    public void updateBarcodeAmount(String barcode, int newAmount) { mRepository.updateBarcodeAmount(barcode, newAmount);}

}
