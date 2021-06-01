package ICS.Environments;

import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ICS.Utils.cResult;
import ICS.Utils.cUserInterface;
import ICS.cAppExtension;
import SSU_WHS.Webservice.cWebservice;
import nl.icsvertex.scansuite.R;

public class cEnvironment {

    //region Public Properties

    private final String nameStr;
    public String getNameStr() {
        return nameStr;
    }

    private final String descriptionStr;
    public String getDescriptionStr() {
        return descriptionStr;
    }

    private final String webserviceURLStr;
    String getWebserviceURLStr() {
        return webserviceURLStr;
    }

    private Boolean isDefaultBln;
    public Boolean isDefaultBln() {
        return getDefaultBln();
    }

    private final cEnvironmentEntity environmentEntity;

    private cEnvironmentViewModel getEnvironmentViewModel() {
        return new ViewModelProvider(cAppExtension.fragmentActivity).get(cEnvironmentViewModel.class);
    }
    public static List<cEnvironment> allEnviroments;
    public static cEnvironment currentEnvironment;
    public static cEnvironment restorableEnviroment;

    //End Region Public Properties

    //Region Private Properties

    //End Region Private Properties

    //Region Constructor
    public cEnvironment(cEnvironmentEntity pvEnviromentEntity) {
        this.environmentEntity = pvEnviromentEntity;
        this.nameStr = this.environmentEntity.getNameStr();
        this.descriptionStr = this.environmentEntity.getDescriptionStr();
        this.webserviceURLStr = this.environmentEntity.getWebserviceURLStr();
        this.setDefaultBln(this.environmentEntity.getIsdefaultBln());
    }

    public cEnvironment(String pvEnviromentBarcodeStr) {
        this.environmentEntity = new cEnvironmentEntity(pvEnviromentBarcodeStr);
        this.nameStr = this.environmentEntity.getNameStr();
        this.descriptionStr = this.environmentEntity.getDescriptionStr();
        this.webserviceURLStr = this.environmentEntity.getWebserviceURLStr();
        this.setDefaultBln(this.environmentEntity.getIsdefaultBln());
    }

    //End Region Constructor

    public static void pSetEnvironment() {

        cEnvironment.pGetEnviromentsFromDatabase();


        if (cEnvironment.currentEnvironment == null) {
            //Do we have environments at all?
            if (cEnvironment.allEnviroments.size() == 0) {
                mDoNoEnvironments();
                return;
            }

            cEnvironment defaultEnviroment = cEnvironment.getDefaultEnvironment();

            if (defaultEnviroment == null && cEnvironment.allEnviroments.size() == 1) {
                cEnvironment.pSetCurrentEnviroment(cEnvironment.allEnviroments.get(0));
            } else {
                cEnvironment.pSetCurrentEnviroment(Objects.requireNonNull(defaultEnviroment));
            }
        }
    }


    public static void pSetCurrentEnviroment(cEnvironment pvEnvironment) {
        cEnvironment.currentEnvironment = pvEnvironment;
        cWebservice.WEBSERVICE_URL = cEnvironment.currentEnvironment.webserviceURLStr;
        pvEnvironment.pSetAsDefaultBln();
    }

    private static void mDoNoEnvironments() {
        cUserInterface.pDoExplodingScreen(cAppExtension.context.getResources().getString(R.string.error_no_environments), "", true, false);
    }

    public static void pGetEnviromentsFromDatabase() {

        cEnvironment.allEnviroments = new ArrayList<>();

        cEnvironmentViewModel environmentViewModel =  new ViewModelProvider(cAppExtension.fragmentActivity).get(cEnvironmentViewModel.class);
        List<cEnvironmentEntity> EnviromentsFromdatabaseObl = environmentViewModel.getAll();

        for (cEnvironmentEntity environmentEntity : EnviromentsFromdatabaseObl) {
            cEnvironment environment = new cEnvironment(environmentEntity);
            cEnvironment.allEnviroments.add(environment);
        }
    }

    public static cEnvironment getDefaultEnvironment() {

        if (cEnvironment.allEnviroments == null) {
            return null;
        }

        for (cEnvironment environment : cEnvironment.allEnviroments) {
            if (environment.getDefaultBln()) {
                return environment;
            }
        }
        return null;
    }

    public boolean pDeleteFromDatabaseBln() {
        this.getEnvironmentViewModel().delete(this.environmentEntity);
        cEnvironment.allEnviroments.remove(this);
        return true;
    }

    private void pSetAsDefaultBln() {

        this.setDefaultBln(true);

        for (cEnvironment environment : cEnvironment.allEnviroments) {
            if (environment.nameStr.equalsIgnoreCase(this.nameStr)) {
                this.getEnvironmentViewModel().updateDefaultBln(this.getDefaultBln(), this.nameStr);
            } else {
                this.getEnvironmentViewModel().updateDefaultBln(false, environment.nameStr);
            }
        }
    }

    public boolean pInsertInDatabaseBln() {
        this.getEnvironmentViewModel().insert(this.environmentEntity);
        this.setDefaultBln(false);

        if (cEnvironment.allEnviroments == null) {
            cEnvironment.allEnviroments = new ArrayList<>();
        }

        cEnvironment.allEnviroments.add(this);
        return true;
    }

    public cResult pValidateRst() {

        cResult result = new cResult();
        result.resultBln = true;

        if (this.nameStr == null || this.nameStr.isEmpty()) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.message_empty_name));
            return result;
        }

        if (this.descriptionStr == null || this.descriptionStr.trim().isEmpty()) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.message_empty_description));
            return result;
        }

        if (this.webserviceURLStr == null || this.webserviceURLStr.isEmpty()) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.message_empty_URL));
            return result;
        }

        if (this.getDefaultBln() == null) {
            result.resultBln = false;
            result.pAddErrorMessage(cAppExtension.context.getString(R.string.message_empty_isdefault));
            return result;
        }

        return result;
    }

    private Boolean getDefaultBln() {
        return isDefaultBln;
    }

    private void setDefaultBln(Boolean defaultBln) {
        isDefaultBln = defaultBln;
    }
}
