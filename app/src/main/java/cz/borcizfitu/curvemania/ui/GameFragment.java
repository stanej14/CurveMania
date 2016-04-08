package cz.borcizfitu.curvemania.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.borcizfitu.curvemania.R;

/**
 * Created by Jan Stanek[jan.stanek@ackee.cz] on {8.4.16}
 **/
public class GameFragment extends Fragment {
    public static final String TAG = GameFragment.class.getName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_start_game)
    public void onGameStartClicked() {
        
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
