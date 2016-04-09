package cz.borcizfitu.curvemania.cast;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import cz.borcizfitu.curvemania.App;

/**
 * TODO add class description *
 * Created by daniel.pina@ackee.cz
 * 4/9/2016
 */
public class GameControllerImpl implements IGameController {
    private static final String TAG = "GameControllerImpl";

    public static final String DIRECTION_TYPE_LEFT = "left";
    public static final String DIRECTION_TYPE_RIGHT = "right";
    public static final String PRESSED_TYPE_FALSE = "false";
    public static final String PRESSED_TYPE_TRUE = "true";
    public static final String ACTION = "action";
    public static final String START_ACTION = "start";

    private static final int PRESSED_LEFT = 1;
    private static final int PRESSED_RIGHT = 2;
    private static final int UNPRESSED_LEFT = 3;
    private static final int UNPRESSED_RIGHT = 4;
    private static final String DIRECTION = "direction";
    private static final String PRESSED = "pressed";

    @Override
    public void onMoveStartLeft() {
        App.getInstance().getSendMessageHandler().enqueueMessage(
                PRESSED_LEFT, createMoveMessage(DIRECTION_TYPE_LEFT, PRESSED_TYPE_TRUE));
    }

    @Override
    public void onMoveFinishLeft() {
        App.getInstance().getSendMessageHandler().enqueueMessage(
                UNPRESSED_LEFT, createMoveMessage(DIRECTION_TYPE_LEFT, PRESSED_TYPE_FALSE));
    }

    @Override
    public void onMoveStartRight() {
        App.getInstance().getSendMessageHandler().enqueueMessage(
                PRESSED_RIGHT, createMoveMessage(DIRECTION_TYPE_RIGHT, PRESSED_TYPE_TRUE));
    }

    @Override
    public void onMoveFinishRight() {
        App.getInstance().getSendMessageHandler().enqueueMessage(
                UNPRESSED_RIGHT, createMoveMessage(DIRECTION_TYPE_RIGHT, PRESSED_TYPE_FALSE));
    }

    @Override
    public void onStartGame() {
        App.getInstance().getSendMessageHandler().enqueueMessage(
                PRESSED_LEFT, createStartMessage());
    }

    private JSONObject createStartMessage() {
        JSONObject startMessage = new JSONObject();
        try {
            startMessage.put(ACTION, START_ACTION);
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON move message", e);
        }
        return startMessage;
    }

    public static JSONObject createMoveMessage(String directionType, String pressedType) {
        JSONObject moveMessage = new JSONObject();
        try {
            moveMessage.put(DIRECTION, directionType);
            moveMessage.put(PRESSED, pressedType);
        } catch (JSONException e) {
            Log.e(TAG, "Error creating JSON move message", e);
        }
        return moveMessage;
    }
}
