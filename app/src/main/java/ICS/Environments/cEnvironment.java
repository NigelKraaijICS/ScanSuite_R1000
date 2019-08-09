package ICS.Environments;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;
import java.util.List;

import ICS.Utils.cSharedPreferences;
import ICS.Utils.cUserInterface;
import SSU_WHS.Webservice.cWebservice;
import SSU_WHS.cAppExtension;
import SSU_WHS.cPublicDefinitions;
import nl.icsvertex.scansuite.R;

public class cEnvironment {
    private static cEnvironmentViewModel environmentViewModel;
    public static cEnvironmentEntity currentEnvironmentEntity;

    public static void pSetEnvironment() {

        environmentViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cEnvironmentViewModel.class);
        String currentEnvironment = cSharedPreferences.getSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_ENVIRONMENT, "").trim();
        if (currentEnvironment.isEmpty()) {

            //Do we have environments at all?
            if (environmentViewModel.getNumberOfEnvironments() == 0) {
                mDoNoEnvironments();
                return;
            }
            currentEnvironmentEntity = environmentViewModel.getDefaultEnvironment();
            //we have a default environment
            if (currentEnvironmentEntity != null) {
                mSaveTheEnvironment(currentEnvironmentEntity);
                mSetWebservice(currentEnvironmentEntity);
                return;
            }
            //we don't have a default environment, set the first one
            currentEnvironmentEntity = environmentViewModel.getFirst();
            if (currentEnvironmentEntity != null) {
                mSaveTheEnvironment(currentEnvironmentEntity);
                mSetWebservice(currentEnvironmentEntity);
                return;
            }
            //something is terribly wrong!
            mDoNoEnvironments();
        } else {
            //we have a set environment!
            currentEnvironmentEntity = environmentViewModel.getEnvironmentByName(currentEnvironment);
            if (currentEnvironmentEntity != null) {
                mSetWebservice(currentEnvironmentEntity);
                return;
            }
            //something is terribly wrong!
            mDoNoEnvironments();
        }

    }

    private static void mDoNoEnvironments() {
        cUserInterface.doExplodingScreen(cAppExtension.context.getResources().getString(R.string.error_no_environments), "", true, false);
    }

    public static void mSaveTheEnvironment(cEnvironmentEntity pvEnvironmentEntity) {
        cSharedPreferences.setSharedPreferenceString(cPublicDefinitions.PREFERENCE_CURRENT_ENVIRONMENT, pvEnvironmentEntity.getName());
    }

    public static void mSetWebservice(cEnvironmentEntity pvEnvironmentEntity) {
        cWebservice.WEBSERVICE_URL = pvEnvironmentEntity.getWebserviceurl();
    }
    public static List<cEnvironmentEntity> mGetAll(FragmentActivity pvFragmentActivity) {
        if (environmentViewModel == null) {
            environmentViewModel = ViewModelProviders.of(pvFragmentActivity).get(cEnvironmentViewModel.class);
        }
        return environmentViewModel.getAll();
    }
    public static cEnvironmentEntity getEnvironmentByName(FragmentActivity pvFragmentActivity, String pvEnvironmentName) {
        if (environmentViewModel == null) {
            environmentViewModel = ViewModelProviders.of(pvFragmentActivity).get(cEnvironmentViewModel.class);
        }
        return environmentViewModel.getEnvironmentByName(pvEnvironmentName);
    }
    public static void insert(FragmentActivity pvFragmentActivity, cEnvironmentEntity pvEnvironmentEntity) {
        if (environmentViewModel == null) {
            environmentViewModel = ViewModelProviders.of(pvFragmentActivity).get(cEnvironmentViewModel.class);
        }
        environmentViewModel.insert(pvEnvironmentEntity);
    }
    public static void delete(FragmentActivity pvFragmentActivity, cEnvironmentEntity pvEnvironmentEntity) {
        if (environmentViewModel == null) {
            environmentViewModel = ViewModelProviders.of(pvFragmentActivity).get(cEnvironmentViewModel.class);
        }
        environmentViewModel.delete(pvEnvironmentEntity);
    }
}
