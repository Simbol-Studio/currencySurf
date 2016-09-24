package simbolstudio.projectcurrencysurf.controller;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import simbolstudio.projectcurrencysurf.common.ConstantHelper;

/**
 * Created by Marcus on 22-Aug-2016.
 */
public class OkHttpClientSingleton {
    private static OkHttpClient mClient = null;
    private Gson mGson = null;
    private String mUrl;
    private Call mCall;
    private Callback mCallback;


    public OkHttpClientSingleton() {
    }

    public OkHttpClientSingleton getInstance() {
        if (mClient == null) {
            mClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(ConstantHelper.TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .build();
        }
        if (mGson == null)
            mGson = new Gson();
        return this;
    }

    public void setParameter(String url, Callback callback) {
        this.mUrl = url;
        this.mCallback = callback;
    }

    public Gson getmGson() {
        return mGson;
    }

    public void run() throws Exception {
        Request request = new Request.Builder()
                .url(mUrl)
                .build();

        if (mCall != null)
            mCall.cancel();
        mCall = mClient.newCall(request);
        mCall.enqueue(mCallback);
        mCall = null;
    }
}
