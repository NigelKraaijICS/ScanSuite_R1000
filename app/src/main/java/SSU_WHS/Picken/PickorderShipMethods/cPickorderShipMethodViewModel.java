package SSU_WHS.Picken.PickorderShipMethods;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class cPickorderShipMethodViewModel extends AndroidViewModel {
    private cPickorderShipMethodRepository mRepository;

    public cPickorderShipMethodViewModel(Application application) {
        super(application);

        mRepository = new cPickorderShipMethodRepository(application);
    }

    public void insert(cPickorderShipMethodEntity pickorderShipMethodEntity) {mRepository.insert(pickorderShipMethodEntity);}

    public LiveData<List<cPickorderShipMethodEntity>> getPickorderShipMethods(Boolean forcerefresh, String branch, String ordernumber) {return mRepository.getPickorderShipMethods(forcerefresh, branch, ordernumber);}

    public void deleteAll() {mRepository.deleteAll();}

    public cPickorderShipMethodEntity getPickorderShipMethodBySourceNo(String sourceno) {return mRepository.getPickorderShipMethodBySourceNo(sourceno);}

}
