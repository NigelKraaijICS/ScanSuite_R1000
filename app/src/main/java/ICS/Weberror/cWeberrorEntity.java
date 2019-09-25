package ICS.Weberror;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import ICS.Utils.cDateAndTime;
import ICS.Utils.cDeviceInfo;
import ICS.cICSDatabaseDefinitions;
import nl.icsvertex.scansuite.cAppExtension;
import SSU_WHS.Webservice.cWebresult;

@Entity(tableName= cICSDatabaseDefinitions.TABLENAME_WEBERRRORS)
public class cWeberrorEntity {

    //Region Public Properties
    @PrimaryKey(autoGenerate = true)
    public Integer recordid;
    public Integer getRecordidInt() {
        return recordid;
    }

    @ColumnInfo(name = cICSDatabaseDefinitions.WEBERROR_ISSUCCESS)
    public Boolean issucess;
    public Boolean getIsSuccessBln() {
        return issucess;
    }

    @ColumnInfo(name = cICSDatabaseDefinitions.WEBERROR_ISRESULT)
    public Boolean isresult;
    public Boolean getIsresultBln() {
        return isresult;
    }

    @ColumnInfo(name = cICSDatabaseDefinitions.WEBERROR_ACTIVITY)
    public String activity;
    public String getActivityStr() {
        return activity;
    }

    @ColumnInfo(name = cICSDatabaseDefinitions.WEBERROR_WEBMETHOD)
    public String webmethod;
    public String getWebmethodStr() {
        return webmethod;
    }

    @ColumnInfo(name = cICSDatabaseDefinitions.WEBERROR_PARAMETERS)
    public String parameters;
    public String getParametersStr() {
        return parameters;
    }

    @ColumnInfo(name = cICSDatabaseDefinitions.WEBERROR_RESULT)
    public String result;
    public String getResultStr() {
        return result;
    }

    @ColumnInfo(name = cICSDatabaseDefinitions.WEBERROR_DEVICE)
    public String device;
    public String getDeviceStr() {
        return device;
    }

    @ColumnInfo(name = cICSDatabaseDefinitions.WEBERROR_DATETIME)
    public String datetime;
    public String getDatetimeStr() {
        return datetime;
    }

    @ColumnInfo(name = cICSDatabaseDefinitions.WEBERROR_LOCALSTATUS)
    public String localstatus;
    public String getLocalstatusStr() {
        return localstatus;
    }

    //Region Constructor
    public cWeberrorEntity(){

    }

    public cWeberrorEntity(String pvWebMethodStr, String pvParametersStr, String pvResultStr, cWebresult pvWebresultStr) {
        this.activity = cAppExtension.activity.getLocalClassName();
        this.webmethod = pvWebMethodStr;
        this.issucess = pvWebresultStr.getSuccessBln();
        this.isresult = pvWebresultStr.getResultBln();
        this.parameters  = pvParametersStr;
        this.result = pvResultStr;
        this.device = cDeviceInfo.getSerialnumber();
        this.datetime = cDateAndTime.pGetCurrentDateTimeForWebserviceStr();
        this.localstatus = cWeberror.WeberrorStatusEnu.NEW.toString();
    }

    //End Region Constructor

}
