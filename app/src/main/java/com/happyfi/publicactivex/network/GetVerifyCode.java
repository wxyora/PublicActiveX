package com.happyfi.publicactivex.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.happyfi.publicactivex.common.PBOCCommonHeader;
import com.happyfi.publicactivex.common.PBOCUrlUtil;
import com.happyfi.publicactivex.util.CodeUtil;
import com.happyfi.publicactivex.util.CommonUtil;
import com.happyfi.publicactivex.util.Constants;
import com.happyfi.publicactivex.util.LogUtil;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Zhengliang on 15/1/21.
 */
public class GetVerifyCode extends HttpEngine {
    public interface GetVerifyCodeListener extends BaseListener {
        public void getVerifyCodeCallBack(int status, String message, Bitmap bitmap);

    }

    private GetVerifyCodeListener mCallBack;

    public GetVerifyCode(BaseListener baseListener) {
        super();
        mCallBack = (GetVerifyCodeListener) baseListener;
        mAPI = Constants.GET_PBOC_VERIFY_CODE + CommonUtil.getRadom();
        startEngine(null, true, PBOCCommonHeader.getHeader());
    }

    public void getStream(int status, InputStream is, long length) {
        Bitmap mBitmap = null;
        int status_deal = status;
        if (200 == status) {
            try {
                byte[] data = readStream(is);
                mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                status_deal = CodeUtil.CALL_BACK_GET_VERIFY_CODE_OK;
            } catch (Exception ex) {
                status_deal = CodeUtil.CALL_BACK_GET_VERIFY_CODE_FAIL;
            }
        } else {
            status_deal = CodeUtil.CALL_BACK_OVER_TIME;
        }
        mCallBack.getVerifyCodeCallBack(status_deal, "", mBitmap);
    }

    private byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

}
