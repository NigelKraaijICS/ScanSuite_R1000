package SSU_WHS.Picken.PickorderAddresses;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class cPickorderAddressViewModel extends AndroidViewModel {
    private cPickorderAddressRepository mRepository;

    public cPickorderAddressViewModel(Application application) {
        super(application);

        mRepository = new cPickorderAddressRepository(application);
    }

    public void insert(cPickorderAddressEntity pickorderAddressEntity) {mRepository.insert(pickorderAddressEntity);}

    public LiveData<List<cPickorderAddressEntity>> getPickorderAddresses(Boolean forcerefresh, String branchStr, String ordernumberStr) {return mRepository.getPickorderAddresses(forcerefresh, branchStr, ordernumberStr);}

    public void deleteAll() {mRepository.deleteAll();}

    public cPickorderAddressEntity getAddressByAddresCode(String addresscode) {return mRepository.getAddressByAddresCode(addresscode);}
}
