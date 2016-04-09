package cz.borcizfitu.curvemania.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.cast.games.GameManagerClient;
import com.google.android.gms.cast.games.GameManagerState;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;

import java.util.Observable;
import java.util.Observer;

import cz.borcizfitu.curvemania.App;
import cz.borcizfitu.curvemania.R;
import cz.borcizfitu.curvemania.cast.CastConnectionManager;

public class MainActivity extends AppCompatActivity implements Observer {
    private static final String TAG = MainActivity.class.getName();
    private CastConnectionFragment mCastConnectionFragment;
    private GameFragment mGameFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (mCastConnectionFragment == null) {
            mCastConnectionFragment = new CastConnectionFragment();
        }
        if (mGameFragment == null) {
            mGameFragment = new GameFragment();
        }
//
//        if (savedInstanceState == null) {
//            getFragmentManager().beginTransaction()
//                    .add(R.id.container, new CastConnectionFragment(), CastConnectionFragment.TAG)
//                    .commit();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CastConnectionManager manager =
                App.getInstance().getCastConnectionManager();
        manager.startScan();
        manager.addObserver(this);
        App.getInstance().getSendMessageHandler().resumeSendingMessages();
        updateFragments();
    }

    @Override
    protected void onPause() {
        CastConnectionManager manager =
                App.getInstance().getCastConnectionManager();
        manager.stopScan();
        manager.deleteObserver(this);
        App.getInstance().getSendMessageHandler().flushMessages();
        super.onPause();
    }

    /**
     * Called when the options menu is first created.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem mediaRouteMenuItem = menu.findItem(R.id.media_route_menu_item);
        MediaRouteActionProvider mediaRouteActionProvider =
                (MediaRouteActionProvider) MenuItemCompat.getActionProvider(mediaRouteMenuItem);
        if (mediaRouteActionProvider == null) {
            Log.w(TAG, "mediaRouteActionProvider is null!");
            return false;
        }
        mediaRouteActionProvider.setRouteSelector(App.getInstance().
                getCastConnectionManager().getMediaRouteSelector());
        return true;
    }

    /**
     * Called when the cast connection changes.
     */
    @Override
    public void update(Observable object, Object data) {
        CastConnectionManager manager =
                App.getInstance().getCastConnectionManager();
        if (manager.isConnectedToReceiver() && !hasPlayerConnected()) {
            final GameManagerClient gameManagerClient = manager.getGameManagerClient();
            PendingResult<GameManagerClient.GameManagerResult> result =
                    gameManagerClient.sendPlayerAvailableRequest(null);

            result.setResultCallback(new ResultCallback<GameManagerClient.GameManagerResult>() {
                @Override
                public void onResult(@NonNull GameManagerClient.GameManagerResult gameManagerResult) {
                    if (!gameManagerResult.getStatus().isSuccess()) {
                        App.getInstance().getCastConnectionManager().
                                disconnectFromReceiver(false);
                        showErrorDialog(gameManagerResult.getStatus().getStatusMessage());
                    }
                    updateFragments();
                }
            });
        }
        updateFragments();
    }

    private void showErrorDialog(final String errorMessage) {
        if (!isFinishing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Show a error dialog along with error messages.
                    AlertDialog alertDialog =
                            new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle(getString(R.string.game_connection_error_message));
                    alertDialog.setMessage(errorMessage);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,
                            getString(R.string.basic_ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            });
        }
    }

    private void updateFragments() {
        if (isChangingConfigurations() || isFinishing()) {
            return;
        }

        Fragment fragment;
        if (hasPlayerConnected()) {
            fragment = mGameFragment;
        } else {
            fragment = mCastConnectionFragment;
        }
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commitAllowingStateLoss();
    }

    private boolean hasPlayerConnected() {
        CastConnectionManager manager =
                App.getInstance().getCastConnectionManager();
        GameManagerClient gameManagerClient = manager.getGameManagerClient();
        if (manager.isConnectedToReceiver()) {
            GameManagerState state = gameManagerClient.getCurrentState();
            return state.getConnectedControllablePlayers().size() > 0;
        }
        return false;
    }
}
