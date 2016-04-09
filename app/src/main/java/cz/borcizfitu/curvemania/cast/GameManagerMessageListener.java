package cz.borcizfitu.curvemania.cast;

import org.json.JSONObject;

/**
 * Created by Jan Stanek[jan.stanek@ackee.cz] on {9.4.16}
 **/
public class GameManagerMessageListener {
    public static final String TAG = GameManagerMessageListener.class.getName();
    private IGameMessageListener mListener;

    public void setGameMessageListener(IGameMessageListener listener) {
        mListener = listener;
    }

    public void clearListener() {
        mListener = null;
    }

    public void onGameMessageReceived(JSONObject json) {
        mListener.onGameMessageReceived(json);
    }

    public void onGameStateChanged(int gameplayState) {
        if (mListener == null) {
            return;
        }

        if (gameplayState == 3) {
            mListener.onGameStarted();
        } else if(gameplayState == 2) {
            mListener.onGamePaused();
        }
    }

    public interface IGameMessageListener {
        void onGameStarted();

        void onGameMessageReceived(JSONObject json);

        void onGamePaused();
    }
}
