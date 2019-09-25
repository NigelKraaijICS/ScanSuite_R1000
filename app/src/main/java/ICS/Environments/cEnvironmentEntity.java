package ICS.Environments;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.annotation.NonNull;

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
    //End Region Constructor



}
