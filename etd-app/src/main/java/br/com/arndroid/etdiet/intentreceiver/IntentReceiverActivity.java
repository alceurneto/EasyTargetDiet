package br.com.arndroid.etdiet.intentreceiver;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.utils.NavigationUtils;

public class IntentReceiverActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.intent_receiver_activity);

        final ActionBar actionBar = getActionBar();
        actionBar.setTitle(getString(R.string.incoming_request));
        actionBar.setDisplayHomeAsUpEnabled(true);

        // TODO: implement
//        final WeightsListFragment fragment = (WeightsListFragment) getFragmentManager()
//                .findFragmentById(R.id.weights_list_fragment);
//        fragment.onReplyActionFromOtherFragment(null, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.intent_receiver, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();

        switch (itemId) {
            case R.id.addAll:
                // TODO: implement.
//                MenuUtils.callMenuInFragmentByMethod(getFragmentManager(),
//                        R.id.weights_list_fragment, itemId);
                return true;
            case android.R.id.home:
                NavigationUtils.navigateUpFromSameTaskPreservingScreenState(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}