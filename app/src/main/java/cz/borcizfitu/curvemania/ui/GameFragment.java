package cz.borcizfitu.curvemania.ui;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.borcizfitu.curvemania.App;
import cz.borcizfitu.curvemania.R;
import cz.borcizfitu.curvemania.cast.GameControllerImpl;
import cz.borcizfitu.curvemania.cast.GameManagerMessageListener;
import cz.borcizfitu.curvemania.cast.IGameController;

/**
 * Created by Jan Stanek[jan.stanek@ackee.cz] on {8.4.16}
 **/
public class GameFragment extends Fragment implements GameManagerMessageListener.IGameMessageListener {
    public static final String TAG = GameFragment.class.getName();

    @Bind(R.id.layout_controlling)
    LinearLayout mControllingLayout;

    @Bind(R.id.btn_start_game)
    Button mStartButton;

    @Bind(R.id.btn_left)
    Button mLeftButton;

    @Bind(R.id.btn_right)
    Button mRightButton;

    @Bind(R.id.progress)
    CircularProgressView mProgress;

    @Bind(R.id.blurry_layout)
    FrameLayout mBlurryLayout;

    private boolean mIsAdmin = false;
    private boolean mGameStarted = false;
    private IGameController gameController = new GameControllerImpl();

    @Override
    public void onStart() {
        super.onStart();
        App.getInstance().getGameManagerMessageListener().setGameMessageListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        App.getInstance().getGameManagerMessageListener().clearListener();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        ButterKnife.bind(this, view);

        mLeftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        gameController.onMoveStartLeft();
                        mLeftButton.setBackgroundColor(Color.LTGRAY);
                        break;
                    case MotionEvent.ACTION_UP:
                        gameController.onMoveFinishLeft();
                        mLeftButton.setBackgroundColor(Color.WHITE);
                        break;
                }
                return true;
            }
        });

        mRightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        gameController.onMoveStartRight();
                        mRightButton.setBackgroundColor(Color.LTGRAY);
                        break;
                    case MotionEvent.ACTION_UP:
                        gameController.onMoveFinishRight();
                        mRightButton.setBackgroundColor(Color.WHITE);
                        break;
                }
                return true;
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_start_game)
    public void onStartClicked() {
        gameController.onStartGame();
    }

    public void setIsAdmin(boolean b) {
        mIsAdmin = b;
        showActualStatus();
    }

    public void showActualStatus() {
        if (mGameStarted) {
            mStartButton.setVisibility(View.GONE);
            mProgress.setVisibility(View.GONE);
            mBlurryLayout.setVisibility(View.GONE);
            mLeftButton.setClickable(true);
            mRightButton.setClickable(true);
        } else {
            if (mIsAdmin) {
                mStartButton.setVisibility(View.VISIBLE);
                mProgress.setVisibility(View.GONE);
            } else {
                mStartButton.setVisibility(View.GONE);
                mProgress.setVisibility(View.VISIBLE);
            }
            mBlurryLayout.setVisibility(View.VISIBLE);
            mLeftButton.setClickable(false);
            mRightButton.setClickable(false);
        }
    }

    @Override
    public void onGameStarted() {
        mGameStarted = true;
        showActualStatus();
        Log.i(TAG, "onGameStarted:");
    }

    @Override
    public void onGameMessageReceived(JSONObject json) {
        Log.i(TAG, "onGameMessageReceived: " + json.toString());
    }

    @Override
    public void onGamePaused() {
        mGameStarted = false;
        mIsAdmin = true;
        
        showActualStatus();
        Log.i(TAG, "onGamePaused: ");
    }
}
