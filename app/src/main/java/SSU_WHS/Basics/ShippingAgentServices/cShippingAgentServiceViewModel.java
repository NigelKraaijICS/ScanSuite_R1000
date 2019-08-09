package SSU_WHS.Basics.ShippingAgentServices;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class cShippingAgentServiceViewModel extends AndroidViewModel {
    private cShippingAgentServiceRepository mRepository;

    public cShippingAgentServiceViewModel(Application application) {
        super(application);

        mRepository = new cShippingAgentServiceRepository(application);
    }

    public void insert(cShippingAgentServiceEntity shippingAgentServiceEntity) {mRepository.insert(shippingAgentServiceEntity);}

    public LiveData<List<cShippingAgentServiceEntity>> getShippingAgentServicess(Boolean forcerefresh) {return mRepository.getShippingAgentServices(forcerefresh);}

    public void deleteAll() {mRepository.deleteAll();}

    public cShippingAgentServiceEntity getShippingAgentServiceByServiceCode(String servicecode) {return mRepository.getShippingAgentServiceByServiceCode(servicecode);}

}
