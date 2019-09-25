package ICS.Environments;

import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

import ICS.Utils.cUserInterface;
import SSU_WHS.Webservice.cWebservice;
import nl.icsvertex.scansuite.cAppExtension;
import nl.icsvertex.scansuite.R;

public class cEnvironment {

    //region Public Properties

    public String nameStr;
    public String getNameStr() {
        return nameStr;
    }

    public String descriptionStr;
    public String getDescriptionStr() {
        return descriptionStr;
    }

    public String webserviceURLStr;
    public String getWebserviceURLStr() {
        return webserviceURLStr;
    }

    public boolean isDefaultBln;
    public boolean isDefaultBln() {
        return isDefaultBln;
    }

    public cEnvironmentEntity environmentEntity;
    public boolean inDatabaseBln;

    public static cEnvironmentViewModel gEnviromentViewModel;

    public static cEnvironmentViewModel getEnviromementViewModel() {
        if (gEnviromentViewModel == null) {
            gEnviromentViewModel = ViewModelProviders.of(cAppExtension.fragmentActivity ).get(cEnvironmentViewModel.class);
        }
        return gEnviromentViewModel;
    }

    public static List<cEnvironment> allEnviroments;
    public static cEnvironment currentEnvironment;

    //Region Constructor
    public cEnvironment(cEnvironmentEntity pvEnviromentEntity) {
        this.environmentEntity = pvEnviromentEntity;
        this.nameStr = this.environmentEntity.getNameStr();
        this.descriptionStr =  this.environmentEntity.getDescriptionStr();
        this.webserviceURLStr = this.environmentEntity.getWebserviceURLStr();
        this.isDefaultBln = this.environmentEntity.getIsdefaultBln();
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
                return;
            }
            else
                {
                 cEnvironment.pSetCurrentEnviroment(defaultEnviroment);
            }
        }
    }


    public static void pSetCurrentEnviroment(cEnvironment pvEnvironment){
        cEnvironment.currentEnvironment = pvEnvironment;
        cWebservice.WEBSERVICE_URL = cEnvironment.currentEnvironment.webserviceURLStr;
        pvEnvironment.pSetAsDefaultBln();
     }

    private static void mDoNoEnvironments() {
        cUserInterface.pDoExplodingScreen(cAppExtension.context.getResources().getString(R.string.error_no_environments), "", true, false);
    }

    public static void pGetEnviromentsFromDatabase() {

        cEnvironment.allEnviroments = new ArrayList<>();

        List<cEnvironmentEntity> EnviromentsFromdatabaseObl =   cEnvironment.getEnviromementViewModel().getAll();

        for (cEnvironmentEntity environmentEntity : EnviromentsFromdatabaseObl) {
            cEnvironment environment = new cEnvironment(environmentEntity);
            environment.inDatabaseBln = true;
            cEnvironment.allEnviroments.add(environment);
        }
    }

    public static cEnvironment getEnvironmentByName(String pvEnvironmentName) {

        if (cEnvironment.allEnviroments == null){
            return null;
        }

        for (cEnvironment environment : cEnvironment.allEnviroments) {
            if (environment.nameStr.equalsIgnoreCase(pvEnvironmentName)) {
                return  environment;
            }
        }
                return  null;
     }

    public static cEnvironment getDefaultEnvironment() {

        if (cEnvironment.allEnviroments == null){
            return null;
        }

        for (cEnvironment environment : cEnvironment.allEnviroments) {
            if (environment.isDefaultBln == true) {
                return  environment;
            }
        }
        return  null;
    }

    public boolean pDeleteFromDatabaseBln() {
        cEnvironment.getEnviromementViewModel().delete(this.environmentEntity);
        this.inDatabaseBln = false;
        cEnvironment.allEnviroments.remove(this);
        return true;
    }

    public boolean pSetAsDefaultBln() {

        this.isDefaultBln = true;

        for (cEnvironment environment : cEnvironment.allEnviroments) {
            if (environment.nameStr.equalsIgnoreCase(this.nameStr)) {
                cEnvironment.getEnviromementViewModel().updateDefaultBln( this.isDefaultBln, this.nameStr);
            }  else {
                cEnvironment.getEnviromementViewModel().updateDefaultBln(false, environment.nameStr);
            }
        }
        return true;
    }


    public boolean pInsertInDatabaseBln() {
        cEnvironment.getEnviromementViewModel().insert(this.environmentEntity);
        this.inDatabaseBln = true;
        this.isDefaultBln = false;

        if (cEnvironment.allEnviroments == null){
            cEnvironment.allEnviroments = new ArrayList<>();
        }

        cEnvironment.allEnviroments.add(this);
        return true;
    }
}
