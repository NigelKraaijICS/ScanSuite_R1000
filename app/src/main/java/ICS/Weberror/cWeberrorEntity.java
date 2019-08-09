package ICS.Weberror;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import ICS.cICSDatabaseDefinitions;

@Entity(tableName= cICSDatabaseDefinitions.TABLENAME_WEBERRRORS)
public class cWeberrorEntity {
    @PrimaryKey(autoGenerate = true)
    public Integer recordid;
    @ColumnInfo(name = cICSDatabaseDefinitions.WEBERROR_ISSUCCESS)
    public Boolean issucess;
    @ColumnInfo(name = cICSDatabaseDefinitions.WEBERROR_ISRESULT)
    public Boolean isresult;
    @ColumnInfo(name = cICSDatabaseDefinitions.WEBERROR_ACTIVITY)
    public String activity;
    @ColumnInfo(name = cICSDatabaseDefinitions.WEBERROR_WEBMETHOD)
    public String webmethod;
    @ColumnInfo(name = cICSDatabaseDefinitions.WEBERROR_PARAMETERS)
    public String parameters;
    @ColumnInfo(name = cICSDatabaseDefinitions.WEBERROR_RESULT)
    public String result;
    @ColumnInfo(name = cICSDatabaseDefinitions.WEBERROR_DEVICE)
    public String device;
    @ColumnInfo(name = cICSDatabaseDefinitions.WEBERROR_DATETIME)
    public String datetime;
    @ColumnInfo(name = cICSDatabaseDefinitions.WEBERROR_LOCALSTATUS)
    public String localstatus;

    public Boolean getIssucess() {
        return issucess;
    }

    public void setIssucess(Boolean issucess) {
        this.issucess = issucess;
    }

    public Boolean getIsresult() {
        return isresult;
    }

    public void setIsresult(Boolean isresult) {
        this.isresult = isresult;
    }

    public Integer getRecordid() {
        return recordid;
    }

    public void setRecordid(Integer recordid) {
        this.recordid = recordid;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getWebmethod() {
        return webmethod;
    }

    public void setWebmethod(String webmethod) {
        this.webmethod = webmethod;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getLocalstatus() {
        return localstatus;
    }

    public void setLocalstatus(String localstatus) {
        this.localstatus = localstatus;
    }
}
