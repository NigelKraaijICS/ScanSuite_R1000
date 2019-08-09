package SSU_WHS.Basics.BarcodeLayouts;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class cBarcodeLayoutViewModel extends AndroidViewModel{
    private cBarcodeLayoutRepository mRepository;
    private LiveData<List<cBarcodeLayoutEntity>> mAllBarcodeLayouts;

    public cBarcodeLayoutViewModel(Application application) {
        super(application);

        mRepository = new cBarcodeLayoutRepository(application);
    }

    public void insert(cBarcodeLayoutEntity barcodeLayoutEntity) {mRepository.insert(barcodeLayoutEntity);}

    public LiveData<List<cBarcodeLayoutEntity>> getBarcodeLayouts() {return mRepository.getBarcodeLayouts();}

    public List<cBarcodeLayoutEntity> getLocalBarcodeLAyouts() {return mRepository.getLocalBarcodeLayouts();}

    public void deleteAll() {mRepository.deleteAll();}

    public List<cBarcodeLayoutEntity> getBarcodeLayoutsOfType(String barcodeLayoutType) {return mRepository.getBarcodeLayoutsOfType(barcodeLayoutType);}
}
