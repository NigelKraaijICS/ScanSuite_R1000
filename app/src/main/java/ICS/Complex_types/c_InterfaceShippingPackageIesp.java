package ICS.Complex_types;


import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class c_InterfaceShippingPackageIesp implements KvmSerializable {

    public String g_PackagetypeStr;
    public Integer g_SequenceNumberInt;
    public Long g_WeightinGLng;
    public Double g_ItemcountDbl;
    public String g_ContainersoortStr;
    public String g_ContainerStr;

    public String getG_PackagetypeStr() {
        return g_PackagetypeStr;
    }

    public void setG_PackagetypeStr(String g_PackagetypeStr) {
        this.g_PackagetypeStr = g_PackagetypeStr;
    }

    public Integer getG_SequenceNumberInt() {
        return g_SequenceNumberInt;
    }

    public void setG_SequenceNumberInt(Integer g_SequenceNumberInt) {
        this.g_SequenceNumberInt = g_SequenceNumberInt;
    }

    public Long getG_WeightinGLng() {
        return g_WeightinGLng;
    }

    public void setG_WeightinGLng(Long g_WeightinGLng) {
        this.g_WeightinGLng = g_WeightinGLng;
    }

    public Double getG_ItemcountDbl() {
        return g_ItemcountDbl;
    }

    public void setG_ItemcountDbl(Double g_ItemcountDbl) {
        this.g_ItemcountDbl = g_ItemcountDbl;
    }

    public String getG_ContainersoortStr() {
        return g_ContainersoortStr;
    }

    public void setG_ContainersoortStr(String g_ContainersoortStr) {
        this.g_ContainersoortStr = g_ContainersoortStr;
    }

    public String getG_ContainerStr() {
        return g_ContainerStr;
    }

    public void setG_ContainerStr(String g_ContainerStr) {
        this.g_ContainerStr = g_ContainerStr;
    }

    //empty constructor
    public c_InterfaceShippingPackageIesp() {

    }
    @Override
    public Object getProperty(int arg0) {
        switch(arg0){
            case 0:
                return g_PackagetypeStr;
            case 1:
                return g_SequenceNumberInt;
            case 2:
                return g_WeightinGLng;
            case 3:
                return g_ItemcountDbl;
            case 4:
                return g_ContainersoortStr;
            case 5:
                return g_ContainerStr;
        }
        return null;
    }
    @Override
    public int getPropertyCount() {
        return 6;
    }
    @Override
    public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
        switch(arg0){
            case 0:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "g_PackagetypeStr";
                break;
            case 1:
                arg2.type = PropertyInfo.INTEGER_CLASS;
                arg2.name = "g_SequenceNumberInt";
                break;
            case 2:
                arg2.type = PropertyInfo.LONG_CLASS;
                arg2.name = "g_WeightinGLng";
                break;
            case 3:
                arg2.type = Double.class;
                arg2.name = "g_ItemcountDbl";
                break;
            case 4:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "g_ContainersoortStr";
                break;
            case 5:
                arg2.type = PropertyInfo.STRING_CLASS;
                arg2.name = "g_ContainerStr";
            default:
                break;
        }
    }
    @Override
    public void setProperty(int arg0, Object arg1) {
        // TODO Auto-generated method stub
        switch(arg0){
            case 0:
                g_PackagetypeStr = arg1.toString();
                break;
            case 1:
                g_SequenceNumberInt = Integer.parseInt(arg1.toString());
                break;
            case 2:
                g_WeightinGLng = Long.parseLong(arg1.toString());
                break;
            case 3:
                g_ItemcountDbl = Double.parseDouble(arg1.toString());
                break;
            case 4:
                g_ContainersoortStr = arg1.toString();
                break;
            case 5:
                g_ContainerStr = arg1.toString();
            default:
                break;
        }
    }
}
