package cz.borcizfitu.curvemania.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import cz.borcizfitu.curvemania.R;

/**
 * Created by Jan Stanek[jan.stanek@ackee.cz] on {8.4.16}
 **/
public class GameFragment extends Fragment {
    public static final String TAG = GameFragment.class.getName();

    @Bind(R.id.layout_controlling)
    LinearLayout mControllingLayout;

    @Bind(R.id.btn_start_game)
    Button mStartButton;

    @Bind(R.id.btn_left)
    ImageButton mLeftButton;

    @Bind(R.id.btn_right)
    ImageButton mRightButton;

    @Bind(R.id.progress)
    CircularProgressView mProgress;

    private boolean mAdmin = false;
    private boolean mGameStarted = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        ButterKnife.bind(this, view);

        mLeftButton.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return true;
        });

        mRightButton.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            return true;
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void showActualStatus() {
        if(mGameStarted){
            mStartButton.setVisibility(View.GONE);
            mProgress.setVisibility(View.GONE);
            mControllingLayout.setVisibility(View.VISIBLE);
        } else {
            if(mAdmin){
                mStartButton.setVisibility(View.VISIBLE);
                mProgress.setVisibility(View.GONE);
                mControllingLayout.setVisibility(View.GONE);
            } else {
                mStartButton.setVisibility(View.GONE);
                mProgress.setVisibility(View.VISIBLE);
                mControllingLayout.setVisibility(View.GONE);
            }
        }
    }
}
