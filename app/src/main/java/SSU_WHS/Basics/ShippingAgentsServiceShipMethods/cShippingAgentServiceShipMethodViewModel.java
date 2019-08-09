package SSU_WHS.Basics.ShippingAgentsServiceShipMethods;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class cShippingAgentServiceShipMethodViewModel extends AndroidViewModel {
    private cShippingAgentServiceShipMethodRepository mRepository;

    public cShippingAgentServiceShipMethodViewModel(Application application) {
        super(application);

        mRepository = new cShippingAgentServiceShipMethodRepository(application);
    }

    public void insert(cShippingAgentServiceShipMethodEntity shippingAgentServiceShipMethodEntity) {mRepository.insert(shippingAgentServiceShipMethodEntity);}

    public LiveData<List<cShippingAgentServiceShipMethodEntity>> getShippingAgentServicesShipMethods(Boolean forcerefresh) {return mRepository.getShippingAgentServiceShipMethods(forcerefresh);}

    public void deleteAll() {mRepository.deleteAll();}

}
