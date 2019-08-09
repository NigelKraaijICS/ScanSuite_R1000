package ICS.Environments;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import SSU_WHS.General.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_ENVIRONMENTS, primaryKeys = {cDatabase.ENVIRONMENT_NAME})
public class cEnvironmentEntity {
    @NonNull
    @ColumnInfo(name = cDatabase.ENVIRONMENT_NAME)
    public String name;
    @ColumnInfo(name = cDatabase.ENVIRONMENT_DESCRIPTION)
    public String description;
    @ColumnInfo(name = cDatabase.ENVIRONMENT_WEBSERVICEURL)
    public String webserviceurl;
    @ColumnInfo(name = cDatabase.ENVIRONMENT_DEFAULT)
    public boolean isdefault;

    //empty constructor
    public cEnvironmentEntity() {

    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebserviceurl() {
        return webserviceurl;
    }

    public void setWebserviceurl(String webserviceurl) {
        this.webserviceurl = webserviceurl;
    }

    public boolean getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(boolean isdefault) {
        this.isdefault = isdefault;
    }

}
