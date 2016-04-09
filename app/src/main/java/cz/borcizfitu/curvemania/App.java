package cz.borcizfitu.curvemania;

import android.app.Application;

import cz.borcizfitu.curvemania.cast.CastConnectionManager;
import cz.borcizfitu.curvemania.cast.SendMessageHandler;

/**
 * Created by Jan Stanek on {8.4.16}
 **/
public class App extends Application {
    public static final String TAG = App.class.getName();

    private static App sInstance;

    private CastConnectionManager mCastConnectionManager;
    private SendMessageHandler mSendMessageHandler;

    public static App getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mCastConnectionManager = new CastConnectionManager(this);
        mSendMessageHandler = new SendMessageHandler(mCastConnectionManager);
    }

    public CastConnectionManager getCastConnectionManager() {
        return mCastConnectionManager;
    }

    public SendMessageHandler getSendMessageHandler() {
        return mSendMessageHandler;
    }
}
