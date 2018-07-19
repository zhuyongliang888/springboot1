package com.hoperun.micro.security.common;

/**
 * System Error Code
 * 
 * @author Jimmy
 *
 */
public class ErrorCode
{
    public static final String KEYWORD_ERROR = "error";
    public static final String KEYWORD_CODE = "code";
    public static final String KEYWORD_MSG = "message";
    public static final String KEYWORD_STATUS = "responese_status";// 200、400、403、404、503

    public static final int CODE_OK = 200;
    public static final int CODE_BAD_REQUEST = 400;
    public static final int CODE_UNAUTHORIZED = 401;
    public static final int CODE_FORBIDDEN = 403;
    public static final int CODE_NOT_FOUND = 404;
    public static final int CODE_SERICE_UNAVAIABLE = 503;

    public static final String MSG_OK = "SUCCESS!";

    public static final Integer ERROR_CODE_PARAMETER_ERROR = 4001001;
    public static final String ERROR_MSG_PARAMETER_ERROR = "parameter error";
    
    public static final Integer ERR_CODE_PARAMETER_ERROR = 4001001;
    public static final String ERR_MSG_PARAMETER_ERROR = "parameter error";
    
    public static final Integer ERR_CODE_PARAMETER_NOTNULL = 4001002;
    public static final String ERR_MSG_PARAMETER_NOTNULL = "parameter must not null ";
    
    public static final Integer ERR_CODE_PARAMETER_NULL = 4001003;
    public static final String ERR_MSG_PARAMETER_NULL = "parameter is null ";

    public static final Integer ERR_CODE_TENANT_ID_IS_NULL = 4001004;
    public static final String ERR_MSG_TENANT_ID_IS_NULL = "tenant_id is null";

    // 参数超出范围
    public static final Integer ERR_CODE_PARAMETER_BEYOND_RANGE = 4001005;
    public static final String ERR_MSG_PARAMETER_BEYOND_RANGE = "parameter beyond range";

    public static final Integer ERR_CODE_NO_TOKEN = 4031001;
    public static final String ERR_MSG_NO_TOKEN = "have not Access-Token";

    public static final Integer ERR_CODE_EXPIRED_TOKEN = 4031002;
    public static final String ERR_MSG_EXPIRED_TOKEN = "Access-Token Expired";

    public static final Integer ERR_CODE_REFRESHED_TOKEN = 4031003;
    public static final String ERR_MSG_REFRESHED_TOKEN = "Access-Token Refresh";
    
    
    public static final Integer ERR_CODE_USER_NOT_EXISTS = 4041001;
    public static final String ERR_MSG_USER_NOT_EXISTS = "this uer not exists";
    
    public static final Integer ERR_CODE_DEVICE_NOT_EXISTS = 4041001;
    public static final String ERR_MSG_DEVICE_NOT_EXISTS = "this device not exists";
    
    public static final Integer ERR_CODE_SERVER_EXCEPTION = 5031001;
    public static final String ERR_MSG_SERVER_EXCEPTION = "server has exception";

    public static final Integer ERR_CODE_SELECT = 5030004;
    public static final String ERR_MSG_SELECT = "Query database failure";

}
