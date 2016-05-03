package com.fr.general;

import com.fr.json.JSONObject;
import com.fr.stable.CodeUtils;
import com.fr.stable.LicUtils;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public abstract class FUNC
{
  public static final int BASIC = 0;
  public static final int DEV = 1;
  public static final int STAND = 2;
  public static final int PROFESS = 3;
  public static final int ENTER = 4;
  public static final int GROUP = 5;
  public static final int MAX_FUN_NUMBER = 81;
  public static final String[] VERSION = { "706561", "706605", "17484909", "34864023661", "265735294061", "4355081031277" };
  private static List<FUNC> FUNC_LIST = new ArrayList();
  private static BigInteger functions = BigInteger.ONE.shiftLeft(81).subtract(BigInteger.ONE);
  
  public FUNC()
  {
    if (marker() < 81) {
      FUNC_LIST.add(this);
    }
  }
  
  public String getLocaleKey()
  {
    return "";
  }
  
  public String toString()
  {
    return Inter.getLocText(getLocaleKey());
  }
  
  public static Iterator<FUNC> getAllFunctionIterator()
  {
    return FUNC_LIST.iterator();
  }
  
  public static String getEditionByFunc()
  {
	  // always return "Registration-Enterprise_Edition"
	  return "Registration-Enterprise_Edition";
	  /*
    int i = -1;
    int j = 0;
    int k = VERSION.length;
    while (j < k)
    {
      BigInteger localBigInteger = new BigInteger(VERSION[j]);
      if (localBigInteger.subtract(functions) == BigInteger.ZERO)
      {
        i = j;
        break;
      }
      j++;
    }
    switch (i)
    {
    case 0: 
      return "Registration-Basic_Edition";
    case 1: 
      return "Registration-Develop_Edition";
    case 2: 
      return "Registration-Standard_Edition";
    case 3: 
      return "Registration-Professional_Edition";
    case 4: 
      return "Registration-Enterprise_Edition";
    }
    return "Registration-Customize_Edition";
    */
  }
  
  public static void refreshFuntions()
  {
	  /*
    initFunction();
    byte[] arrayOfByte = StableUtils.getBytes();
    try
    {
      if (arrayOfByte == null) {
        return;
      }
      String str = LicUtils.getLicAsStr();
      if (invalidLic(str))
      {
        resetFunctions();
        return;
      }
      JSONObject localJSONObject = new JSONObject(str);
      if ((localJSONObject.getLong("DEADLINE") > Calendar.getInstance().getTimeInMillis()) && (localJSONObject.has("FUNCTION")))
      {
        functions = new BigInteger(String.valueOf(localJSONObject.get("FUNCTION")));
        if (!localJSONObject.has("ISAFTER701")) {
          functions = functions.add(BigInteger.ONE.shiftLeft(15));
        }
      }
    }
    catch (Exception localException)
    {
      FRLogger.getLogger().error(localException.getMessage());
      resetFunctions();
    }
    finally
    {
      VT4FR.fireLicChangeListener();
    }
    */
	  VT4FR.fireLicChangeListener();
  }
  
  private static boolean invalidLic(String paramString)
  {
    return (StringUtils.contains(LicUtils.getSoftLockContent(), CodeUtils.passwordEncode(paramString))) || (ComparatorUtils.equals(functions, BigInteger.ONE.shiftLeft(1)));
  }
  
  private static void initFunction()
  {
    functions = BigInteger.ONE.shiftLeft(81).subtract(BigInteger.ONE);
  }
  
  public static void resetFunctions()
  {
    functions = BigInteger.ONE.shiftLeft(1);
    VT4FR.fireLicChangeListener();
  }
  
  public boolean support()
  {
    return functions.and(getNum()).compareTo(BigInteger.ZERO) == 1;
  }
  
  private BigInteger getNum()
  {
    return BigInteger.ONE.shiftLeft(marker());
  }
  
  public abstract int marker();
  
  public boolean groupSupported()
  {
    return true;
  }
  
  public boolean enterpriseSupported()
  {
    return true;
  }
  
  public boolean professionalSupported()
  {
    return true;
  }
  
  public boolean standardSupported()
  {
    return true;
  }
  
  public boolean developmentSupported()
  {
    return true;
  }
  
  public boolean basicSupported()
  {
    return true;
  }
  
  static
  {
    refreshFuntions();
  }
}
