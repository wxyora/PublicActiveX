package com.happyfi.publicactivex.network;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.happyfi.publicactivex.activity.AnswerQActivity;
import com.happyfi.publicactivex.activity.GetPbocPreCheckActivity;
import com.happyfi.publicactivex.activity.LoginActivity;
import com.happyfi.publicactivex.activity.LoginToAnswerActivity;
import com.happyfi.publicactivex.activity.RegUserDetailActivity;
import com.happyfi.publicactivex.activity.RegUserInfoActivity;
import com.happyfi.publicactivex.activity.ViewPbocContentActivity;
import com.happyfi.publicactivex.util.CodeUtil;
import com.happyfi.publicactivex.util.LogUtil;
import com.happyfi.publicactivex.util.ResourceUtil;


/**
 * Created by Zhengliang on 15/1/15.
 */
public class HFHandler extends Handler {
    private Activity mActivity;

    public HFHandler(Activity activity) {
        mActivity = activity;
    }

    public void handleMessage(Message message) {
        switch (message.what) {
            case CodeUtil.MSG_LOGIN_PAGE_REFRESH_VERIFY_CODE: {
                if (mActivity instanceof LoginActivity) {
                    ((LoginActivity) mActivity).refreshVerifyCode();
                } else if (mActivity instanceof RegUserInfoActivity) {
                    ((RegUserInfoActivity) mActivity).refreshVerifyCode();
                }
                break;
            }
            case CodeUtil.MSG_LOGIN_PAGE_LOGIN_OK: {
                ((LoginActivity) mActivity).loginOk();
                break;
            }

            case CodeUtil.MSG_GET_GET_QUESTION_OK: {
                ((AnswerQActivity) mActivity).resfreshUI();
                break;
            }

            case CodeUtil.MSG_GET_GET_QUESTION_FAIL: {
                ((AnswerQActivity) mActivity).getQuestionFail();
                break;
            }

            case CodeUtil.MSG_ANSWER_QUESTION_OK: {
                ((AnswerQActivity) mActivity).answerQuestionOK();
                break;
            }

            case CodeUtil.MSG_ANSWER_QUESTION_FAIL: {
                ((AnswerQActivity) mActivity).answerQuestionFAIL();
                break;
            }
            case CodeUtil.MSG_GET_VERIFY_CODE_REG_OK: {
                ((RegUserInfoActivity) mActivity).refreshVerifyCode();
                break;
            }
            case CodeUtil.MSG_VERIFY_PBOC_CODE_OK: {
                ((GetPbocPreCheckActivity) mActivity).goToPBOCPreviewPage();
                break;
            }

            case CodeUtil.MSG_VERIFY_PBOC_CODE_FAIL: {
                ((GetPbocPreCheckActivity) mActivity).checkFail();
                break;
            }

            case CodeUtil.CALL_BACK_ANSER_VERIFY_NOT_LOGIN: {
                ((GetPbocPreCheckActivity) mActivity).checkFailNotLogin();
                break;
            }

            case CodeUtil.MSG_GET_PBOC_CONTENT_OK: {
                ((ViewPbocContentActivity) mActivity).loadPbocData(message.getData().getString("pboc_info"));
                break;
            }
            case CodeUtil.MSG_GET_PBOC_CONTENT_FAIL: {
                ((ViewPbocContentActivity) mActivity).getPbocFail();
                break;
            }

            case CodeUtil.MSG_GET_HTML_OVER_TIME: {
                ((ViewPbocContentActivity) mActivity).overTime();
                break;
            }
            case CodeUtil.MSG_PRE_VERIFY_ID_OK: {
                ((RegUserInfoActivity) mActivity).addMoreInfo();
                break;
            }
            case CodeUtil.MSG_PRE_VERIFY_ID_FAIL: {
                ((RegUserInfoActivity) mActivity).checkUerInfoTips();
                break;
            }
            case CodeUtil.MSG_NET_WORK_OVER_TIME: {
                LogUtil.printLog("MainActivity", "Handler");
                if (mActivity instanceof RegUserInfoActivity) {
                    ((RegUserInfoActivity) mActivity).networkError();
                } else if (mActivity instanceof RegUserDetailActivity) {
                    ((RegUserDetailActivity) mActivity).showTips(mActivity.getResources().getString(ResourceUtil.getStringId(mActivity, "happyfi_net_work_error")));
                } else if (mActivity instanceof AnswerQActivity) {
                    ((AnswerQActivity) mActivity).showTips(mActivity.getResources().getString(ResourceUtil.getStringId(mActivity, "happyfi_net_work_error")));
                } else if (mActivity instanceof GetPbocPreCheckActivity) {
                    ((GetPbocPreCheckActivity) mActivity).showTip(mActivity.getResources().getString(ResourceUtil.getStringId(mActivity, "happyfi_net_work_error")));
                } else if (mActivity instanceof LoginToAnswerActivity) {
                    ((LoginToAnswerActivity) mActivity).netWorkError();
                } else if (mActivity instanceof ViewPbocContentActivity) {
                    ((ViewPbocContentActivity) mActivity).networkError();
                } else if (mActivity instanceof LoginActivity) {
                    ((LoginActivity) mActivity).showTips(mActivity.getResources().getString(ResourceUtil.getStringId(mActivity, "happyfi_net_work_error")));
                }
                break;
            }
            case CodeUtil.MSG_LOGIN_PAGE_LOGIN_FAIL: {
                ((LoginActivity) mActivity).loginError();
                break;
            }
            case CodeUtil.MSG_GET_REG_TOKEN_FAIL: {
                ((RegUserInfoActivity) mActivity).showTips(mActivity.getResources().getString(ResourceUtil.getStringId(mActivity, "happyfi_system_error")));
                break;
            }
            case CodeUtil.MSG_GET_MOBILE_CODE_OK: {
                ((RegUserDetailActivity) mActivity).passMobileBack(message.getData().getString("backvalue"));
                break;
            }
            case CodeUtil.MSG_GET_MOBILE_CODE_FAIL: {
                ((RegUserDetailActivity) mActivity).showTips(mActivity.getResources().getString(ResourceUtil.getStringId(mActivity, "happyfi_message_send_fail")));
                break;
            }
            case CodeUtil.MSG_POST_ALL_INFO_OK: {
                ((RegUserDetailActivity) mActivity).postOk();
                break;
            }
            case CodeUtil.MSG_POST_ALL_INFO_FAIL: {
                ((RegUserDetailActivity) mActivity).postFAIL();
                break;
            }
            case CodeUtil.UPDATE_TIMER: {
                ((AnswerQActivity) mActivity).upDateTimer(message.getData().getInt("timer_value"));
                break;
            }
            case CodeUtil.MSG_POST_HTML_OK: {
                if (mActivity instanceof ViewPbocContentActivity) {
                    ((ViewPbocContentActivity) mActivity).postHtmlOK();
                }
                break;
            }
            case CodeUtil.MSG_POST_HTML_FAIL: {
                if (mActivity instanceof ViewPbocContentActivity) {
                    ((ViewPbocContentActivity) mActivity).postHtmlFAIL();
                }
                break;

            }
            case CodeUtil.MSG_DISPATCH_OK: {
                ((LoginToAnswerActivity) mActivity).checkSuccess();
                break;
            }
            case CodeUtil.MSG_CHECK_USER_NICK_NAME: {
                ((RegUserDetailActivity) mActivity).nickNameOK();
                break;
            }
            case CodeUtil.MSG_CHECK_USER_NICK_NAME_FAIL: {
                ((RegUserDetailActivity) mActivity).nickNameFail();
                break;
            }
            case CodeUtil.MSG_REFRESH_UPLOAD_IMAGE: {
//                ((UploadActivity)mActivity).refreshLoadImage(message.getData().getString("path"));
                break;
            }
            case CodeUtil.MSG_UPDATE_HF_STATUS_OK: {
                ((GetPbocPreCheckActivity) mActivity).havelogin();
                break;
            }
            case CodeUtil.MSG_UPDATE_HF_STATUS_FAIL: {
                ((GetPbocPreCheckActivity) mActivity).notLogin();
                break;
            }
            case CodeUtil.CALL_BACK_GET_HAS_REPORT_OK:
            case CodeUtil.CALL_BACK_GET_HAS_REPORT_FAIL:
                ((LoginToAnswerActivity) mActivity).jump();
                break;
            default: {
                break;
            }
        }
    }
}
