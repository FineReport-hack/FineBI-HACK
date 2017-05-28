package com.fr.stable;

import com.fr.base.Env;
import com.fr.base.FRContext;
import com.fr.base.Utils;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Calendar;

public class LicUtils {
    public static final String VERSION = "VERSION";
    public static final String MACADDRESS = "MACADDRESS";
    public static final String LOCKSERIAL = "SERIALNUMBER";
    public static final String DEADLINE = "DEADLINE";
    public static final String APPNAME = "APPNAME";
    public static final String UUID = "UUID";
    public static final String FS_USER = "FS_USER";
    public static final String MOBILE_FS_USER = "MOBILE_FS_USER";
    public static final String PROJECTNAME = "PROJECTNAME";
    public static final String COMPANYNAME = "COMPANYNAME";
    public static final String CONCURRENCY = "CONCURRENCY";
    public static final String FUNCTION = "FUNCTION";
    public static final String ENCODE_KEY = "KEY";
    public static final String REPORTLETCOUNTS = "reportletscount";
    public static final String MUTICONNECTION = "MUTICONNECTION";
    public static final String CHECKFAIL = "FAILPASS";
    public static final String ADDRESSDELIMIER = ",";
    public static final String CORE_CONTEXT = "com.fr.base.FRCoreContext";
    public static final String FILE_NAME = "FineReport.lic";
    private static final long ONE_YEAR_MILLISECOND = 31536000000L;
    

    public static byte[] getBytes() {
        //return (byte[]) StableUtils.invokeMethod("com.fr.base.FRCoreContext", "getBytes");
        //File file = new File("C:/FineReport_7.1/WebReport/WEB-INF/resources/FineReport.lic");
        try {
            //InputStream localInputStream = new FileInputStream(file);
            return Utils.inputStream2Bytes(getLicFileStream());
        } catch (Exception e) {
            return null;
        }
    }
    
    private static InputStream getLicFileStream() {
        try
        {
          Env localEnv = FRContext.getCurrentEnv();
          if (localEnv == null) {
        	  System.out.println("Env is null.");
        	  return null;
          }
          String str = localEnv.getLicName();
          //printStackTrace();
          System.out.println("Lic Name : " + str);
          InputStream localInputStream = localEnv.readBean(str, "resources");
          return localInputStream;
        }
        catch (Exception e) {
        	e.printStackTrace();
            return null;
        }
    }
    
    // hack purpose to print the stack trace
    private static void printStackTrace() {
    	try {
      	  throw new Exception();
        } catch(Exception e) {
      	  e.printStackTrace();
        }
    }

    public static void resetBytes() {
        //StableUtils.invokeMethod("com.fr.base.FRCoreContext", "resetBytes");
    }

    public static void retryLicLock() {
        //StableUtils.invokeMethod("com.fr.base.FRCoreContext", "retryLicLock");
    }

    public static void decode(byte[] paramArrayOfByte, OutputStream paramOutputStream) {
        //StableUtils.invokeMethod("com.fr.base.FRCoreContext", "decode",
        //        new Class[] { byte[].class, OutputStream.class }, new Object[] { paramArrayOfByte, paramOutputStream });
    	loadLic(paramOutputStream);
    }
    
    private static void loadLic(OutputStream paramOutputStream) {
    	byte[] buffer = new byte[1024];
    	int read = 0;
    	InputStream inputStream = getLicFileStream();
    	try {
			while ((read = inputStream.read(buffer)) != -1) {
				paramOutputStream.write(buffer, 0, read);
			}
			paramOutputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static String getItemFromLic(String paramString) {
    	System.out.println("getItemFromLic -- paramString : " + paramString);
        JSONObject localJSONObject = getJsonFromLic();
        if (localJSONObject != null) {
        	try {
                return localJSONObject.has(paramString) ? (String) localJSONObject.get(paramString) : "";
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }
        
        return "";
         
    }

    public static JSONObject getJsonFromLic() {
        JSONObject localJSONObject = null;
        try {
            localJSONObject = new JSONObject(getLicAsStr());
        } catch (JSONException e) {
        }
        return localJSONObject;
    }

    public static String getLicAsStr() {
        String result = "";
        //File file = new File("C:/FineReport_7.1/WebReport/WEB-INF/resources/FineReport.lic");
        try {
            //InputStream localInputStream = new FileInputStream(file);
            InputStream localInputStream = getLicFileStream();
            result = Utils.inputStream2String(localInputStream, "UTF-8");
        } catch (Exception e) {
        }
        return result;
    }

    public static boolean isTempLic() {
        
        JSONObject localJSONObject = getJsonFromLic();
        if (!localJSONObject.has("DEADLINE")) {
            return true;
        }
        try {
            return localJSONObject.getLong("DEADLINE") - Calendar.getInstance().getTimeInMillis() < 31536000000L;
        } catch (JSONException localJSONException) {
        }
        return true;
        
    }

    public static String getLicBytesAsStr(byte[] paramArrayOfByte) {
        return getLicAsStr();
    }

    public static JSONObject getJsonFromBytes(byte[] paramArrayOfByte) {
        return getJsonFromLic();
    }

    public static String getItemFromBytes(byte[] paramArrayOfByte, String paramString) {
        JSONObject localJSONObject = getJsonFromBytes(paramArrayOfByte);
        try {
            return localJSONObject.has(paramString) ? (String) localJSONObject.get(paramString) : "";
        } catch (JSONException localJSONException) {
        }
        return "";
    }

    public static String getSoftLockContent() {
    	return "";
        //return getSoftLockContent("");
    }

    public static String getSoftLockContent(String paramString) {
        StringBuffer localStringBuffer = new StringBuffer();
        File localFile = new File("" + File.separator + "System");
        if (localFile.exists()) {
            try {
                FileInputStream localFileInputStream = new FileInputStream(localFile);
                InputStreamReader localInputStreamReader = new InputStreamReader(localFileInputStream);
                BufferedReader localBufferedReader = new BufferedReader(localInputStreamReader);
                for (String str = localBufferedReader.readLine(); str != null; str = localBufferedReader.readLine()) {
                    localStringBuffer.append(str);
                }
                localBufferedReader.close();
            } catch (Exception localException) {
            }
        }
        if (!StringUtils.contains(localStringBuffer.toString(), paramString)) {
            localStringBuffer.append(paramString);
        }
        return localStringBuffer.toString();
    }
}
