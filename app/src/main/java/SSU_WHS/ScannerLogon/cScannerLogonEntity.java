package SSU_WHS.ScannerLogon;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import SSU_WHS.cDatabase;

@Entity(tableName = cDatabase.TABLENAME_SCANNERLOGON, primaryKeys = {"ScannerDescription"})
public class cScannerLogonEntity {
    @ColumnInfo(name = "FixedScannerBranch")
    public String fixedscannerbranch;
    @NonNull
    @ColumnInfo(name = "ScannerDescription")
    public String scannerdescription;
    @ColumnInfo(name = "RequiredScannerVersion")
    public String requiredscannerversion;
    @ColumnInfo(name = "ApplicationEnvironment")
    public String applicationenvironment;
    @ColumnInfo(name = "Languages")
    public String languages;
    @ColumnInfo(name = "RequiredScannerConfiguration")
    public String requiredscannerconfiguration;
    @ColumnInfo(name = "ReapplyScannerConfiguration")
    public String reapplyscannerconfiguration;
    @ColumnInfo(name = "VersionConfigAppCurrentScanner")
    public String versionconfigappcurrentscanner;
    @ColumnInfo(name = "VersionConfigAppRequiredScanner")
    public String versionconfigapprequiredscanner;
    @ColumnInfo(name = "ColorSet")
    public String colorset;

    //empty constructor
    public cScannerLogonEntity() {

    }
    public String getFixedscannerbranch() {return fixedscannerbranch;}
    public void setFixedscannerbranch(String pv_fixedscannerbranch) {this.fixedscannerbranch = pv_fixedscannerbranch;}
    public String getScannerdescription() {return  scannerdescription;}
    public void setScannerdescription(String pv_scannerdescription) {this.scannerdescription = pv_scannerdescription;}
    public String getRequiredscannerversion() {return requiredscannerversion;}
    public void setRequiredscannerversion(String pv_requiredscannerversion) {this.requiredscannerversion = pv_requiredscannerversion;}
    public String getApplicationenvironment() {return applicationenvironment;}
    public void setApplicationenvironment(String pv_applicationenvironment) {this.applicationenvironment = pv_applicationenvironment;}
    public String getLanguages() {return languages;}
    public void setLanguages(String pv_languages) {this.languages = pv_languages;}
    public String getRequiredscannerconfiguration() {return requiredscannerconfiguration;}
    public void setRequiredscannerconfiguration(String pv_requiredscannerconfiguration) {this.requiredscannerconfiguration = pv_requiredscannerconfiguration;}
    public String getReapplyscannerconfiguration() {return reapplyscannerconfiguration;}
    public void setReapplyscannerconfiguration(String pv_reapplyscannerconfiguration) {this.reapplyscannerconfiguration = pv_reapplyscannerconfiguration;}
    public String getVersionconfigappcurrentscanner() {return versionconfigappcurrentscanner;}
    public void setVersionconfigappcurrentscanner(String pv_versionconfigappcurrentscanner) {this.versionconfigappcurrentscanner = pv_versionconfigappcurrentscanner;}
    public String getVersionconfigapprequiredscanner() {return versionconfigapprequiredscanner;}
    public void setVersionconfigapprequiredscanner(String pv_versionconfigapprequiredscanner) {this.versionconfigapprequiredscanner = pv_versionconfigapprequiredscanner;}
    public String getColorset() {return colorset;}
    public void setColorset(String pv_colorset) {this.colorset = pv_colorset;}
}


