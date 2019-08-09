package SSU_WHS.Picken.WarehouseLocations;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class cWarehouseLocationViewModel extends AndroidViewModel {
    private cWarehouseLocationRepository mRepository;

    public cWarehouseLocationViewModel(Application application) {
        super(application);
        mRepository = new cWarehouseLocationRepository(application);
    }
    public void insert(cWarehouseLocationEntity warehouseLocationEntity) {mRepository.insert(warehouseLocationEntity);}

    public LiveData<List<cWarehouseLocationEntity>> getWarehouseLocations(Boolean forcerefresh, String branch, List<String> bins) {return mRepository.getWarehouseLocations(forcerefresh, branch,bins);}

    public void deleteAll() {mRepository.deleteAll();}
}
