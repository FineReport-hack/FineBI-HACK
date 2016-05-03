package com.fr.data;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.fr.general.ComparatorUtils;
import com.fr.general.FUNC;
import com.fr.general.GeneralContext;
import com.fr.general.GeneralUtils;
import com.fr.general.Inter;
import com.fr.general.data.TableDataException;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.fr.stable.LicUtils;
//import com.fr.stable.ProductConsts;
import com.fr.stable.StringUtils;

@SuppressWarnings("deprecation")
public class VersionInfoTableData extends AbstractTableData {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static Object CONCURRENCY_IP = "5";
    
    private static String VERSION = "8.0";
    private String[] columnNames;
    private Object[][] rowData;
    
    private JSONObject licJSONObject;

    public VersionInfoTableData() {
        licJSONObject = null;
        String[] arrayOfString = { "Key", "Value" };
        this.columnNames = arrayOfString;
        this.rowData = initDatas();
    }
    
    public Object[][] initDatas() {
        generateJSONFromLic();
        
        String version = getVersion();
        String versionInLic = getVersionNumberInLic();
        String bindingMac = getMacAddressInLic();
        String appNameInLic = getAppNameInLic();
        String macAddresses = getMacAddresses();
        String firstMacAddress = getFirstMacAddress();
        String webAppName = getWebAppName();
        String deadDate = getDeadDate();
        String serialNumber = getSerialNumberInLic();
        String concurrency = getConcurrency();
        String jarInfo = getJarInfo();
        String uuidInLic = getUUIDInLic();
        String uuidInSystem = getUUIDInSystem();
        
        boolean isLicOutOfDate = isLicOutOfDate();
        boolean isVersionNumberMatch = isVersionNumberMatch(versionInLic);
        boolean isMacAddressMatch = isMacAddressMatch(bindingMac);
        boolean isUuidMatch = isUuidMatch(uuidInLic);
        boolean isAppNameMatch = isAppNameMatch(webAppName, appNameInLic);
        boolean isAppContentMatch = true;
        
        Object[][] arrayOfObject = { 
                { "About-Version", version }, 
                { "Re-version-in-lic", versionInLic },
                { "Re-binding-mac", bindingMac }, 
                { "Re-appname-in-lic", appNameInLic }, 
                { "Re-all-server-mac", macAddresses },
                { "Re-first-mac", firstMacAddress }, 
                { "Re-AppName", webAppName }, 
                { "Re-system-version", VERSION },
                { "Re-deadline", deadDate }, 
                { "Re-lock-serial", serialNumber }, 
                { "Re-sametime-ip", concurrency },
                { "Re-Jar", jarInfo }, 
                { "Re-lic-out-date", isLicOutOfDate }, 
                { "Re-uuid-in-lic", uuidInLic },
                { "Re-uuid-in-system", uuidInSystem }, 
                { "Re-uuid-match", isUuidMatch },
                { "Re-version-match", isVersionNumberMatch }, 
                { "Re-mac-match", isMacAddressMatch },
                { "Re-appname-match", isAppNameMatch },
                { "Re-appcontent-match", isAppContentMatch }};
        return arrayOfObject;
    }
    
    private String getVersion() {
        if (licJSONObject == null) {
            return Inter.getLocText("Unregistered");
        }
        return FUNC.getEditionByFunc();
    }
    
    private String getVersionNumberInLic(){
        if (licJSONObject == null) {
            return "";
        }
        try {
            return licJSONObject.getString(LicUtils.VERSION);
        } catch (JSONException e) {
            return "";
        }
    }
    
    private Long getDeadline() {
        if (licJSONObject == null) {
            return null;
        }
        try {
            return licJSONObject.getLong(LicUtils.DEADLINE);
        } catch (JSONException e) {
            return null;
        }
    }
    
    private String getMacAddresses() {
        try {
            return StringUtils.join("\n", GeneralUtils.getMacAddresses());
        } catch (IOException e) {
            return "";
        }
    }
    
    private String getFirstMacAddress() {
        try {
            return GeneralUtils.getMacAddress();
        } catch (IOException e) {
            return "";
        }
    }
    
    private String getUUIDInSystem() {
        return GeneralUtils.getUUID();
    }
    
    private String getWebAppName() {
        return GeneralContext.getCurrentAppNameOfEnv();
    }
    
    private boolean isLicOutOfDate() {
        Long deadline = getDeadline();
        if (deadline == null) {
            return true;
        }
        return deadline < Calendar.getInstance().getTimeInMillis();
    }
    
    private String getMacAddressInLic() {
        if (licJSONObject == null) {
            return "";
        }
        try {
            return licJSONObject.getString(LicUtils.MACADDRESS);
        } catch (JSONException e) {
            return "";
        }
    }
    
    private String getAppNameInLic() {
        if (licJSONObject == null) {
            return "";
        }
        try {
            return licJSONObject.getString(LicUtils.APPNAME);
        } catch (JSONException e) {
            return "";
        }
    }
    
    private String getUUIDInLic() {
        if (licJSONObject == null) {
            return "";
        }
        try {
            return licJSONObject.getString(LicUtils.UUID);
        } catch (JSONException e) {
            return "";
        }
    }
    
    private String getDeadDate() {
        Long deadline = getDeadline();
        if (deadline == null) {
            return null;
        }
        Date deadDate = new Date(deadline);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年 MM月dd日");
        return dateFormat.format(deadDate);
    }
    
    private String getSerialNumberInLic() {
        if (licJSONObject == null) {
            return "";
        }
        try {
            return licJSONObject.getString(LicUtils.LOCKSERIAL);
        } catch (JSONException e) {
            return "";
        }
    }
    
    private String getConcurrencyInLic() {
        if (licJSONObject == null) {
            return "";
        }
        try {
            return licJSONObject.getString(LicUtils.CONCURRENCY);
        } catch (JSONException e) {
            return "";
        }
    }
    
    private String getConcurrency() {
        String concurrency = getConcurrencyInLic();
        if (ComparatorUtils.equals("0", concurrency)) {
            concurrency = Inter.getLocText("Not_limited_IP");
        }
        return concurrency;
    }
    
    private boolean isVersionNumberMatch(String versionNumberInLic) {
        return ComparatorUtils.equals(VERSION, versionNumberInLic);
    }
    
    private boolean isMacAddressMatch(String bindingMacInLic) {
        try {
            return GeneralUtils.isMacAddressMatch(bindingMacInLic);
        } catch (IOException e) {
            return false;
        }
    }
    
    private boolean isUuidMatch(String uuidInLic) {
        return GeneralUtils.isUUIDMatch(uuidInLic);
    }
    
    private boolean isAppNameMatch(String webAppName, String appNameInLic) {
        return webAppName.equalsIgnoreCase(appNameInLic) 
                || ComparatorUtils.equals(appNameInLic, "");
    }
    
    private String getJarInfo() {
        String jarInfo = GeneralUtils.readBuildNO();
        if (StringUtils.isEmpty(jarInfo)) {
            jarInfo = Inter.getLocText("Re-not-start-in-jar");
        } else {
            jarInfo = "Build #" + jarInfo;
        }
        return jarInfo;
    }
    
    private void generateJSONFromLic() {
        licJSONObject = LicUtils.getJsonFromLic();
    }

    @Override
    public int getColumnCount() throws TableDataException {
        return this.columnNames.length;
    }

    @Override
    public String getColumnName(int arg0) throws TableDataException {
        return this.columnNames[arg0];
    }

    @Override
    public int getRowCount() throws TableDataException {
        return this.rowData.length;
    }

    @Override
    public Object getValueAt(int arg0, int arg1) {
        return this.rowData[arg0][arg1];
    }

}
