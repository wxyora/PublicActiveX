package com.happyfi.publicactivex.util;

/**
 * Created by wanglijuan on 15/7/2.
 */
public class CodeUtil {
    /*
    *************************
    ******Handle  Message****
    *************************
    */
    //start page
    public static final int MSG_START_PAGE = 10000;


    public static final int MSG_LOGIN_PAGE_REFRESH_VERIFY_CODE = 20000;
    public static final int MSG_LOGIN_PAGE_LOGIN_OK = 20001;
    public static final int MSG_LOGIN_PAGE_LOGIN_FAIL = 20002;

    public static final int MSG_DISPATCH_OK = 20010;
    public static final int MSG_DISPATCH_FAIL = 20011;

    public static final int MSG_GET_GET_QUESTION_OK = 20020;
    public static final int MSG_GET_GET_QUESTION_FAIL = 20021;

    public static final int MSG_GET_VERIFY_CODE_REG_OK = 30000;
    public static final int MSG_GET_VERIFY_CODE_REG_FAIL = 30001;

    public static final int MSG_VERIFY_PBOC_CODE_OK = 40000;
    public static final int MSG_VERIFY_PBOC_CODE_FAIL = 40001;


    public static final int MSG_GET_PBOC_CONTENT_OK = 50000;
    public static final int MSG_GET_PBOC_CONTENT_FAIL = 50001;

    public static final int MSG_PRE_VERIFY_ID_OK = 60001;
    public static final int MSG_PRE_VERIFY_ID_FAIL = 60002;

    public static final int MSG_GET_REG_TOKEN_OK = 70000;
    public static final int MSG_GET_REG_TOKEN_FAIL = 70001;

    public static final int MSG_GET_MOBILE_CODE_OK = 80000;
    public static final int MSG_GET_MOBILE_CODE_FAIL = 80001;

    public static final int MSG_POST_ALL_INFO_OK = 81000;
    public static final int MSG_POST_ALL_INFO_FAIL = 81001;

    public static final int MSG_ANSWER_QUESTION_OK = 82000;
    public static final int MSG_ANSWER_QUESTION_FAIL = 82001;

    public static final int MSG_POST_HTML_OK = 90000;
    public static final int MSG_POST_HTML_FAIL = 90001;
    public static final int MSG_GET_HTML_OVER_TIME = 90002;

    public static final int MSG_NET_WORK_OVER_TIME = 99999;

    public static final int MSG_CHECK_VERSION_OK = 91000;
    public static final int MSG_CHECK_VERSION_FAIL = 91001;

    public static final int MSG_CHECK_USER_NICK_NAME = 92000;
    public static final int MSG_CHECK_USER_NICK_NAME_FAIL = 92001;

    public static final int MSG_UPLOAD_PIC_OK = 94000;
    public static final int MSG_UPLOAD_PIC_FAIL = 94001;

    public static final int MSG_CHECK_UPLAOD = 95000;
    public static final int MSG_CHECK_UPLAOD_FAIL = 95001;
    public static final int MSG_NO_LONGIN = 95002;

    public static final int MSG_UPDATE_HF_STATUS_OK = 96000;
    public static final int MSG_UPDATE_HF_STATUS_FAIL = 96001;

    public static final int MSG_REFRESH_UPLOAD_IMAGE = 93000;

    /*
*************************
***Call Back Listener****
*************************
*/
    public static final int CALL_BACK_STATUS_OK = 1000;
    public static final int CALL_BACK_SYSTEM_ERROR = 1001;

    //Login
    public static final int CALL_BACK_PW_ERROR = 2000;
    public static final int CALL_BACK_VC_ERROR = 2001;

    //GoDirector
    public static final int CALL_BACK_DISPATCH = 2010;
    public static final int CALL_BACK_DISPATCH_ERROR = 2011;

    //GoQuestion
    public static final int CALL_BACK_COULD_GO_OK = 2021;
    public static final int CALL_BACK_COULD_GO_FAIL = 2020;

    //GetQuestion
    public static final int CALL_BACK_GET_QUESTION_OK = 2030;
    public static final int CALL_BACK_GET_QUESTION_FAIL = 2031;

    //PostQuestion


    //GetRegToken
    public static final int CALL_BACK_GET_REG_TOKEN_OK = 3000;
    public static final int CALL_BACK_GET_REG_TOKEN_FAIL = 3001;

    //GEtRegToken
    public static final int CALL_BACK_VERIFY_USERINFO_OK = 4000;
    public static final int CALL_BACK_VERIFY_USERINFO_FAIL = 4001;

    //Get Question Answer Result

    public static final int CALL_BACK_ANSER_QUESTION_OK = 5000;
    public static final int CALL_BACK_ANSER_QUESTION_FAIL = 5001;

    //Verify proc get code
    public static final int CALL_BACK_ANSER_VERIFY_OK = 6000;
    public static final int CALL_BACK_ANSER_VERIFY_FAIL = 6001;
    public static final int CALL_BACK_ANSER_VERIFY_NOT_LOGIN = 6002;


    //Get pboc content
    public static final int CALL_BACK_GET_POBC_CONTENT_OK = 7000;
    public static final int CALL_BACK_GET_PBOC_CONTENT_FAIL = 7001;
    public static final int CALL_BACK_GET_PBOC_OVER_TIME = 7002;

    //Get Verify code
    public static final int CALL_BACK_GET_VERIFY_CODE_OK = 8000;
    public static final int CALL_BACK_GET_VERIFY_CODE_FAIL = 8001;

    //Get MOBILE Code
    public static final int CALL_BACK_GET_MOIDLE_CODE_OK = 9000;
    public static final int CALL_BACK_GET_MOIDLE_CODE_FAIL = 9001;

    //POST  All Userinfo
    public static final int CALL_BACK_POST_ALL_INFO_OK = 9100;
    public static final int CALL_BACK_POST_ALL_INFO_FAIL = 9101;

    //POST USER PBOC
    public static final int CALL_BACK_POST_PBOC_OK = 9200;
    public static final int CALL_BACK_POST_PBOC_FAIL = 9201;

    //CHECK VERSION
    public static final int CALL_BACK_CHECK_VERSION_OK = 9300;
    public static final int CALL_BACK_CHECK_VERSION_FAIL = 9301;

    //CHECK USESR  NAME
    public static final int CALL_BACK_USER_NAME_AVA = 9400;
    public static final int CALL_BACK_USER_NAME_FAIL = 9401;

    //upload pic
    public static final int CALL_BACK_UPLOAD_PIC = 9500;
    public static final int CALL_BACK_UPLOAD_PIC_FAIL = 9501;

    //check upload status
    public static final int CALL_BACK_UPLOAD_STATUAS = 9600;
    public static final int CALL_BACK_UPLOAD_STATUAS_FAIL = 9601;
    public static final int CALL_BACK_UPLOAD_STATUAS_NO_LOGIN = 9602;

    public static final int CALL_BACK_UPDATE_HF_STATUS_OK = 9700;
    public static final int CALL_BACK_UPDATE_HF_STATUS_FAIL = 9701;

    //init pboc login
    public static final int CALL_BACK_INIT_POBC_LOGIN_OK = 9800;
    public static final int CALL_BACK_INIT_POBC_LOGIN_FAIL = 9801;

    //init pay
    public static final int CALL_BACK_INIT_PAY_OK = 9900;
    public static final int CALL_BACK_INIT_PAY_FAIL = 9901;

    //getShareContent
    public static final int CALL_BACK_GET_SHARE_CONTENT_OK = 9902;
    public static final int CALL_BACK_GET_SHARE_CONTENT_FAIL = 9903;

    public static final int CALL_BACK_GET_HAS_REPORT_OK = 9904;
    public static final int CALL_BACK_GET_HAS_REPORT_FAIL = 9905;

    //NETwork error
    public static final int CALL_BACK_OVER_TIME = 9999;
    public static final int CALL_BACK_SUCCESS = 910001;
    public static final int CALL_BACK_FAIL = 910002;
    public static final int CALL_CALL_NOT_LOGIN = 910003;
    //Timer
    public static final int UPDATE_TIMER = 989898;
}
