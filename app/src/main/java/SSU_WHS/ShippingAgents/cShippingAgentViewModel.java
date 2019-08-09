package SSU_WHS.ShippingAgents;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class cShippingAgentViewModel extends AndroidViewModel {
    private cShippingAgentRepository mRepository;

    public cShippingAgentViewModel(Application application) {
        super(application);

        mRepository = new cShippingAgentRepository(application);
    }

    public void insert(cShippingAgentEntity shippingAgentEntity) {mRepository.insert(shippingAgentEntity);}

    public LiveData<List<cShippingAgentEntity>> getShippingAgents(Boolean forcerefresh) {return mRepository.getShippingAgents(forcerefresh);}

    public void deleteAll() {mRepository.deleteAll();}

    public cShippingAgentEntity getShippingAgentByAgentName(String agentname) {return mRepository.getShippingAgentByAgentName(agentname);}

}
