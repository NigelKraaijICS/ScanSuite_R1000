package SSU_WHS.Basics.ShippingAgentServiceShippingUnits;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class cShippingAgentServiceShippingUnitViewModel extends AndroidViewModel {
    private cShippingAgentServiceShippingUnitRepository mRepository;

    public cShippingAgentServiceShippingUnitViewModel(Application application) {
        super(application);

        mRepository = new cShippingAgentServiceShippingUnitRepository(application);
    }

    public void insert(cShippingAgentServiceShippingUnitEntity shippingAgentServiceShippingUnitEntity) {mRepository.insert(shippingAgentServiceShippingUnitEntity);}

    public LiveData<List<cShippingAgentServiceShippingUnitEntity>> getShippingAgentServicesShippingUnits(Boolean forcerefresh) {return mRepository.getShippingAgentServiceShippingUnits(forcerefresh);}

    public void deleteAll() {mRepository.deleteAll();}

    public List<cShippingAgentServiceShippingUnitEntity> getShippingUnitsByAgentAndService(String shippingAgent, String shippingService) {return mRepository.getShippingUnitsByAgentAndService(shippingAgent, shippingService);}

    public LiveData<List<cShippingAgentServiceShippingUnitEntity>> getShippingUnitsByAgentAndServiceLive(String shippingAgent, String shippingService) {return mRepository.getShippingUnitsByAgentAndServiceLive(shippingAgent, shippingService);}

    public void updateShippingUnitQuantityUsed(Integer newQuantity,cShippingAgentServiceShippingUnitEntity shippingAgentServiceShippingUnitEntity) {mRepository.updateShippingUnitQuantityUsed(newQuantity,shippingAgentServiceShippingUnitEntity);}

    public void resetShippingUnitQuantityUsed() {mRepository.resetShippingUnitQuantityUsed();}

    public cShippingAgentServiceShippingUnitEntity getShippingUnitsByAgentAndServiceAndShippingUnit(String shippingAgent, String shippingService, String shippingUnit) {return mRepository.getShippingUnitsByAgentAndServiceAndShippingUnit(shippingAgent, shippingService, shippingUnit);}
}
