package com.happyfi.publicactivex.network;

import java.util.HashMap;

/**
 * Created by Zhengliang on 15/1/15.
 */
public class NetworkWraper {
    public static void getIsHasReport(final HashMap<String, String> params, final BaseListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new CheckIsHasReport(params, listener);
            }
        }).start();
    }

//    public static void initPay(final HashMap<String, String> params, final BaseListener listener) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                new InitPay(listener, params);
//            }
//        }).start();
//    }

    public static void initPbocLogin(final HashMap<String, String> params, final BaseListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new InitLoginPBOC(listener, params);
            }
        }).start();
    }

    public static void getVerifyCode(final HashMap<String, String> params, final BaseListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new GetVerifyCode(listener);
            }
        }).start();
    }

    public static void loginPBOC(final HashMap<String, String> params, final BaseListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new LoginPBOC(listener, params);
            }
        }).start();
    }

    public static void getDiection(final HashMap<String, String> params, final BaseListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new GetPbocChooseInfo(params, listener);
            }
        }).start();
    }

    public static void getQuestion(final HashMap<String, String> params, final BaseListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new GetQuestionList(params, listener);
            }
        }).start();
    }

    public static void postQuestionResult(final HashMap<String, String> params, final BaseListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new PostQuestionResult(params, listener);
            }
        }).start();
    }

    public static void getRegToken(final HashMap<String, String> params, final BaseListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new GetPbocRegToken(params, listener);
            }
        }).start();
    }

    public static void verifyUserInfo(final HashMap<String, String> params, final BaseListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new VerifyUserInfo(params, listener);
            }
        }).start();
    }

    public static void getMobileCode(final HashMap<String, String> params, final BaseListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new GetMobileCode(params, listener);
            }
        }).start();
    }

    public static void postAllRegInfo(final HashMap<String, String> params, final BaseListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new SubmitAllRegInfo(params, listener);
            }
        }).start();
    }

    public static void VerifyPbocCode(final HashMap<String, String> params, final BaseListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new VerifyPbocCode(params, listener);
            }
        }).start();
    }

    public static void getPbocContent(final HashMap<String, String> params, final BaseListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new GetPbocContent(params, listener);
            }
        }).start();
    }

    public static void checkUserNameAva(final HashMap<String, String> params, final BaseListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new CheckUserNameAvaliable(params, listener);
            }
        }).start();
    }
}
