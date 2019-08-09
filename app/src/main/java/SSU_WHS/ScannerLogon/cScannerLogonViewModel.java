package SSU_WHS.ScannerLogon;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class cScannerLogonViewModel extends AndroidViewModel {
    private cScannerLogonRepository mRepository;

    public cScannerLogonViewModel(Application application) {
        super(application);
        mRepository = new cScannerLogonRepository(application);
    }

    public LiveData<List<cScannerLogonEntity>> getScannerLogons(Boolean forcerefresh) {return mRepository.getScannerLogon(forcerefresh);}

    public void insert(cScannerLogonEntity scannerLogonEntity) {mRepository.insert(scannerLogonEntity);}

    public void deleteAll() {mRepository.deleteAll();}

}
