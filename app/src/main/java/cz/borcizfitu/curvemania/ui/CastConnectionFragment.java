
package cz.borcizfitu.curvemania.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Observable;
import java.util.Observer;

import cz.borcizfitu.curvemania.App;
import cz.borcizfitu.curvemania.R;
import cz.borcizfitu.curvemania.cast.CastConnectionManager;

/**
 * A fragment displayed while this application is not yet connected to a cast device.
 */
public class CastConnectionFragment extends Fragment implements Observer {
    public static final String TAG = "CastConnectionFragment";

    private CastConnectionManager mCastConnectionManager;

    private View mConnectLabel;
    private View mSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mCastConnectionManager = App.getInstance().getCastConnectionManager();
        mCastConnectionManager.addObserver(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCastConnectionManager.deleteObserver(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cast_connection_fragment, container, false);
        mConnectLabel = view.findViewById(R.id.connect_label);
        mSpinner = view.findViewById(R.id.spinner);
        return view;
    }

    @Override
    public void update(Observable object, Object data) {
        if (getView() == null) {
            return;
        }
        if (mCastConnectionManager.getSelectedDevice() != null) {
            mConnectLabel.setVisibility(View.GONE);
            mSpinner.setVisibility(View.VISIBLE);
        } else {
            mConnectLabel.setVisibility(View.VISIBLE);
            mSpinner.setVisibility(View.GONE);
        }
    }
}
