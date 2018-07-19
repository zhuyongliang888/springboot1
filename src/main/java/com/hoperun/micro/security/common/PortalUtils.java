package com.hoperun.micro.security.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Jimmy
 */
public class PortalUtils
{

    public static final String RESPONSE_IS_OK = "statusOK";

    public static final String PRE_ADMIN_ACCESS_TOKEN = "HRAT_";

    public static final String PRE_TENANT_ACCESS_TOKEN = "HTENAT_";

    public static final String PRE_BRANCH_ACCESS_TOKEN = "HBRAAT_";

    public static final String PRE_DEP_ACCESS_TOKEN = "HDEPAT_";

    public static final String PRE_JOB_ACCESS_TOKEN = "HJOBAT_";

    public static final String PRE_REFRESH_TOKEN = "HRRT_";

    /**
     * Decode json string to Map according to key value of Map
     * 
     * @param source
     *            -- Json String, such as
     *            {"account":"test_account","password":"test_password"}
     * @param valueMap
     *            -- The map has some key values
     * @return result map
     */
    public static Map<String, String> decodeJsonToMap(String source, String[] keys)
    {

	if (keys == null)
	{
	    return null;
	}
	if (null == source || source.isEmpty())
	{
	    source = "{}";
	}
	Map<String, String> valueMap = new HashMap<String, String>();
	try
	{
	    for (int i = 0; i < keys.length; i++)
	    {
		if (JSONObject.parseObject(source) != null)
		{
		    String value = JSONObject.parseObject(source).getString(keys[i]);
		    valueMap.put(keys[i], value);
		}
	    }
	} catch (Exception e)
	{
	    e.printStackTrace();
	    return valueMap;
	}
	return valueMap;
    }

    /**
     * 1, Get status code from Map 2, Set status code to response 3, Remove
     * status code from Map 4, Return Json string according to the other Map
     * values
     * 
     * @param values
     *            -- result map which includes status code
     * @param response
     * @return RESPONSE_IS_OK -- it means status code is 200 or there is no
     *         status code
     */
    // set status code for response
    public static String dealResponseStatus(HttpServletResponse response, Map<String, Object> values)
    {
	if (values.get(ErrorCode.KEYWORD_STATUS) != null)
	{
	    // get stauts code
	    int statusCode = Integer.parseInt(values.get(ErrorCode.KEYWORD_STATUS).toString());

	    // set status code to response
	    response.setStatus(statusCode);

	    // remove status code, it was set into response
	    values.remove(ErrorCode.KEYWORD_STATUS);

	    // status code is not OK, it means that error happens, then return
	    // directly
	    if (statusCode != ErrorCode.CODE_OK)
	    {
		try
		{
		    ObjectMapper mapper = new ObjectMapper();
		    return mapper.writeValueAsString(values);
		} catch (Exception e)
		{
		    System.out.println(e.toString());
		    return "Server exception";
		}
	    }
	}
	return RESPONSE_IS_OK;
    }

    /**
     * 1, Get status code from Map 2, Set status code to response 3, Remove
     * status code from Map
     * 
     * @param values
     *            -- result map which includes status code
     * @param response
     * @return
     */
    public static void setResponseStatus(HttpServletResponse response, Map<String, Object> values)
    {
	// set status code for response
	if (values.get(ErrorCode.KEYWORD_STATUS) != null)
	{
	    int statusCode = Integer.parseInt(values.get(ErrorCode.KEYWORD_STATUS).toString());
	    response.setStatus(statusCode);
	    values.remove(ErrorCode.KEYWORD_STATUS);
	} else
	{
	    values.put(ErrorCode.KEYWORD_CODE, 0);
	    values.put(ErrorCode.KEYWORD_MSG, ErrorCode.MSG_OK);
	    response.setStatus(200);
	}
    }

    public static void setWebResponseStatus(HttpServletResponse response, Map<String, Object> values)
    {
	if (values.get(ErrorCode.KEYWORD_STATUS) != null)
	{
	    int statusCode = Integer.parseInt(values.get(ErrorCode.KEYWORD_STATUS).toString());
	    response.setStatus(statusCode);
	    values.remove(ErrorCode.KEYWORD_STATUS);
	    if (statusCode != ErrorCode.CODE_OK)
	    {
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(values);
		values.clear();
		values.put("error", map);
	    }
	}
    }

    public static void setResponseAttribute(HttpServletResponse response)
    {
	response.addHeader("Access-Control-Allow-Credentials", "true");
	response.setHeader("Access-Control-Allow-Headers", "Access-Token,Corp-ID,Content-Type");
	response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
	response.setHeader("Access-Control-Allow-Origin", "*");
	response.addHeader("Connection", "keep-alive");
    }

    public static Cookie creatCookieByString(String source)
    {
	String uuidAccount = UUID.nameUUIDFromBytes(source.getBytes()).toString().replaceAll("-", "");
	Long unixTimestamp = new Date().getTime() / 1000;

	Cookie cookie = new Cookie("SERVERID",
		uuidAccount + "|" + unixTimestamp.toString() + "|" + unixTimestamp.toString());
	cookie.setPath("/");
	return cookie;
    }

    public static Cookie findCookieFromRequest(HttpServletRequest request, String name)
    {
	Cookie[] cookies = request.getCookies();
	Cookie findCookie = null;
	if (cookies != null)
	{
	    // 找出名称（键）为“cool”的Cookie
	    for (int i = 0; i < cookies.length; i++)
	    {
		if (name.equals(cookies[i].getName()))
		{
		    findCookie = cookies[i];
		    break;
		}
	    }
	}
	return findCookie;
    }

    @SuppressWarnings("restriction")
    public static String createUserToken()
    {
	String result = UUID.randomUUID().toString().replaceAll("-", "");
	result += UUID.randomUUID().toString().replaceAll("-", "");
	return (new sun.misc.BASE64Encoder()).encode(result.getBytes()).replaceAll("\r|\n", "");
    }

    /**
     * get the ip
     * 
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request)
    {
	String ip = request.getHeader("x-forwarded-for");
	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
	{
	    ip = request.getHeader("Proxy-Client-IP");
	}
	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
	{
	    ip = request.getHeader("WL-Proxy-Client-IP");
	}
	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
	{
	    ip = request.getHeader("HTTP_CLIENT_IP");
	}
	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
	{
	    ip = request.getHeader("HTTP_X_FORWARDED_FOR");
	}
	if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
	{
	    ip = request.getRemoteAddr();
	}
	return ip;
    }

    public static String resultMapToString(Map<String, Object> resultMap)
    {
	try
	{
	    ObjectMapper mapper = new ObjectMapper();
	    return mapper.writeValueAsString(resultMap);
	} catch (Exception e)
	{
	    System.out.println(e.toString());
	    return "Server exception";
	}
    }

    public static String resultListToString(List<Map<String, Object>> resultMap)
    {
	try
	{
	    ObjectMapper mapper = new ObjectMapper();
	    return mapper.writeValueAsString(resultMap);
	} catch (Exception e)
	{
	    System.out.println(e.toString());
	    return "Server exception";
	}
    }

    public static final String PATTERN_MAC = "^[A-Fa-f0-9]{2,64}$";

    /**
     * MAC address check
     */
    public static boolean isValidMacAddress(String mac)
    {

	if (mac != null && !mac.isEmpty())
	{
	    if (Pattern.compile(PATTERN_MAC).matcher(mac).find())
	    {
		return true;
	    }
	}
	return false;
    }

    public static final String PATTERN_SN = "^[a-zA-Z0-9][a-zA-Z-0-9]{0,31}$";

    /**
     * SN check
     */
    public static boolean isValidSn(String sn)
    {

	if (sn != null && !sn.isEmpty())
	{
	    if (Pattern.compile(PATTERN_SN).matcher(sn).find())
	    {
		return true;
	    }
	}
	return false;
    }

    /**
     * get com.alibaba.fastjson.JSONArray from string
     * 
     * @param str
     * @return --null or jsonArray
     */
    public static JSONArray getJSONArrayFromStr(String str)
    {
	JSONArray jsonArray = null;
	if (str != null && !str.equals(""))
	{
	    try
	    {
		jsonArray = JSON.parseArray(str);
	    } catch (Exception e)
	    {
		e.printStackTrace();
		return null;
	    }
	}
	return jsonArray;
    }

    public static Date convertStringToDate(String dateStr)
    {
	if (dateStr == null)
	{
	    return null;
	}
	Date date = null;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	try
	{
	    date = sdf.parse(dateStr);
	} catch (Exception e)
	{
	    return null;
	}
	return date;
    }

    /**
     * change Date to format string ("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" )
     * 
     * @param date
     * @return --"" or string
     */
    public static String convertDateToString(Date date)
    {
	if (date == null)
	{
	    return "";
	}
	String s = "";
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	try
	{
	    s = sdf.format(date);
	} catch (Exception e)
	{
	    return "";
	}
	return s;
    }

    public static Date convertStringToBirthday(String dateStr)
    {
	if (dateStr == null)
	{
	    return null;
	}
	Date date = null;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	try
	{
	    date = sdf.parse(dateStr);
	} catch (Exception e)
	{
	    return null;
	}
	return date;
    }

    public static String getAgeByBirth(Date birthday)
    {
	int age = 0;
	try
	{
	    Calendar now = Calendar.getInstance();
	    now.setTime(new Date());// 当前时间

	    Calendar birth = Calendar.getInstance();
	    birth.setTime(birthday);

	    if (birth.after(now))
	    {// 如果传入的时间，在当前时间的后面，返回0岁
		age = 0;
	    } else
	    {
		age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
		if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR))
		{
		    age += 1;
		}
	    }
	    return age + "";
	} catch (Exception e)
	{// 兼容性更强,异常后返回数据
	    return "";
	}
    }

    /**
     * paramter must not null
     * 
     * @param object
     * @param response
     * @return
     */
    public static String paramterMustNotNull(Object object, HttpServletResponse response)
    {
	Map<String, Object> resultMap = new HashMap<String, Object>();
	object = String.format(ErrorCode.ERR_MSG_PARAMETER_NULL, object);
	resultMap.put(ErrorCode.KEYWORD_CODE, ErrorCode.ERR_CODE_PARAMETER_NULL);
	resultMap.put(ErrorCode.KEYWORD_MSG, object);
	response.setStatus(ErrorCode.CODE_BAD_REQUEST);
	return PortalUtils.resultMapToString(resultMap);
    }

    /**
     * paramter error
     * 
     * @param object
     * @param msg
     * @param response
     * @return
     */
    public static String paramterError(Object object, String msg, HttpServletResponse response)
    {
	if (msg == null || msg.trim().length() == 0)
	{
	    return null;
	}
	Map<String, Object> resultMap = new HashMap<String, Object>();
	msg = String.format(ErrorCode.ERR_MSG_PARAMETER_ERROR, object, msg);
	resultMap.put(ErrorCode.KEYWORD_CODE, ErrorCode.ERR_CODE_PARAMETER_ERROR);
	resultMap.put(ErrorCode.KEYWORD_MSG, msg);
	response.setStatus(ErrorCode.CODE_BAD_REQUEST);
	return PortalUtils.resultMapToString(resultMap);
    }

    /**
     * access-token error
     * 
     * @param response
     * @return
     */
    public static String accessTokenError(HttpServletResponse response)
    {
	Map<String, Object> resultMap = new HashMap<String, Object>();
	resultMap.put(ErrorCode.KEYWORD_CODE, ErrorCode.ERR_CODE_EXPIRED_TOKEN);
	resultMap.put(ErrorCode.KEYWORD_MSG, ErrorCode.ERR_MSG_EXPIRED_TOKEN);
	resultMap.put(ErrorCode.KEYWORD_STATUS, ErrorCode.CODE_FORBIDDEN);
	return PortalUtils.resultMapToString(resultMap);
    }

}
