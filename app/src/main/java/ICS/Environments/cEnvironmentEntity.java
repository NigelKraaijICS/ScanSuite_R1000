package ICS.Environments;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.annotation.NonNull;

import ICS.Utils.cText;
import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_ENVIRONMENTS, primaryKeys = {cDatabase.ENVIRONMENT_NAME})
public class cEnvironmentEntity {

    //Region Public Properties
    @NonNull
    @ColumnInfo(name = cDatabase.ENVIRONMENT_NAME)
    public String name;
    @NonNull
    public String getNameStr() {
        return name;
    }

    @ColumnInfo(name = cDatabase.ENVIRONMENT_DESCRIPTION)
    public String description;
    public String getDescriptionStr() {
        return description;
    }

    @ColumnInfo(name = cDatabase.ENVIRONMENT_WEBSERVICEURL)
    public String webserviceurl;
    public String getWebserviceURLStr() {
        return webserviceurl;
    }

    @ColumnInfo(name = cDatabase.ENVIRONMENT_DEFAULT)
    public boolean isdefault;
    public boolean getIsdefaultBln() {
        return isdefault;
    }

    //end region Public Propties

    //Region Constructor
    public cEnvironmentEntity() {

    }

    public cEnvironmentEntity(String pvEnviromentBarcodeStr) {
        if (pvEnviromentBarcodeStr.startsWith("\\000026")) {
            pvEnviromentBarcodeStr =  pvEnviromentBarcodeStr.replace("\\000026","");
        }

        String[] fieldsObl = pvEnviromentBarcodeStr.split("\\|");

        if (fieldsObl.length != 4) {
            return;
        }

        if (!fieldsObl[0].isEmpty() && !fieldsObl[0].toUpperCase().startsWith("NAME")) {
            return;
        }

        String[] nameFields = fieldsObl[0].split("=");
        if (nameFields.length != 2) {
            return;
        }

        this.name = nameFields[1];

        if (!fieldsObl[1].isEmpty() && !fieldsObl[1].toUpperCase().startsWith("DESCRIPTION")) {
            return;
        }

        String[] nameFields2 = fieldsObl[1].split("=");
        if (nameFields2.length != 2) {
            return;
        }

        this.description = nameFields2[1];

        if (!fieldsObl[2].isEmpty() && !fieldsObl[2].toUpperCase().startsWith("URL")) {
            return;
        }

        String[] nameFields3 = fieldsObl[2].split("=");
        if (nameFields3.length != 2) {
            return;
        }

        this.webserviceurl = nameFields3[1];


        if (!fieldsObl[3].isEmpty() && !fieldsObl[3].toUpperCase().startsWith("DEFAULT")) {
            return;
        }

        String[] nameFields4 = fieldsObl[3].split("=");
        if (nameFields4.length != 2) {
            return;
        }

        this.isdefault = cText.pStringToBooleanBln(nameFields4[1], false);
    }



    //End Region Constructor



}
