package br.com.arndroid.etdiet.journal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.compat.Compat;
import br.com.arndroid.etdiet.foodsusage.FoodsUsageAct;
import br.com.arndroid.etdiet.foodsusage.FoodsUsageListFrag;
import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.days.DaysEntity;
import br.com.arndroid.etdiet.provider.days.DaysManager;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageEntity;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageManager;
import br.com.arndroid.etdiet.provider.parametershistory.ParametersHistoryManager;
import br.com.arndroid.etdiet.util.DateUtil;
import br.com.arndroid.etdiet.virtualweek.DaySummary;
import br.com.arndroid.etdiet.virtualweek.JournalApi;
import br.com.arndroid.etdiet.virtualweek.JournalService;
import br.com.arndroid.etdiet.virtualweek.TestContentObserver;
import br.com.arndroid.etdiet.virtualweek.ViewObserver;

public class JournalAct extends ActionBarActivity implements ViewObserver {

    private Spinner mSpnMeal;
    private DatePicker mDteDate;
    private JournalApi mJournalApi;
    private boolean mBound = false;
    private TestContentObserver mObserver = new TestContentObserver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.journal_act);

        setFieldsReferenceFromForm();
        setupFields();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.journal, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Binding to JournalService
        Intent intent = new Intent(this, JournalService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from JournalService
        if (mBound) {
            mJournalApi.unregisterViewObserver(this);
            unbindService(mConnection);
            mBound = false;
        }
    }

    private void setFieldsReferenceFromForm() {
        mSpnMeal = (Spinner) findViewById(R.id.spnMeal);
        mDteDate = (DatePicker) findViewById(R.id.dteDate);
    }

    private void setupFields() {
        SpinnerAdapter adapter = ArrayAdapter.createFromResource(this, R.array.meals_name_list,
                android.R.layout.simple_spinner_dropdown_item);
        mSpnMeal.setAdapter(adapter);
        Compat.getInstance().setCalendarViewShownToDatePicker(false, mDteDate);
    }

    public void cmdTestObserver(View view) {
        mObserver.mCalled = false;
        final String dateId = DateUtil.dateToDateId(new Date());
        getContentResolver().registerContentObserver(Uri.withAppendedPath(
                Contract.FoodsUsage.DATE_ID_CONTENT_URI, dateId), true, mObserver);

        final FoodsUsageManager foodsManager = new FoodsUsageManager(this);
        FoodsUsageEntity foodsEntity = null;

        foodsEntity = new FoodsUsageEntity(null, dateId,
                Meals.BREAKFAST, 1, "food for test", 7.7f);
        foodsManager.refresh(foodsEntity);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (mObserver.mCalled) {
                    Toast.makeText(JournalAct.this, "myObserver was called!!!!!", Toast.LENGTH_LONG).show();
                }
            }
        }, 100);

        getContentResolver().unregisterContentObserver(mObserver);
        //testInsertFood();
        //testUpdateFood();
        //testInsertDate();
        //testUpdateDate();
        //testParameter();
    }

    private void testParameter() {
        final ParametersHistoryManager parametersHistoryManager = new ParametersHistoryManager(this);
        parametersHistoryManager.setTrackingWeekday(Calendar.SATURDAY);
    }

    private void testUpdateDate() {
        final DaysManager daysManager = new DaysManager(this);
        DaysEntity daysEntity = daysManager.dayFromDate(new Date());

        daysManager.refresh(daysEntity);
    }

    private void testInsertDate() {
        final DaysManager daysManager = new DaysManager(this);
        DaysEntity daysEntity = null;

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        daysEntity = daysManager.dayFromDate(cal.getTime());

        daysManager.refresh(daysEntity);
    }

    private void testInsertFood() {
        final FoodsUsageManager foodsManager = new FoodsUsageManager(this);
        FoodsUsageEntity foodsEntity = null;

        foodsEntity = new FoodsUsageEntity(null, DateUtil.dateToDateId(new Date()),
                Meals.BREAKFAST, 1, "food for toast test", 7.7f);

        foodsManager.refresh(foodsEntity);
    }

    private void testUpdateFood() {
        final FoodsUsageManager foodsManager = new FoodsUsageManager(this);
        FoodsUsageEntity foodsEntity = foodsManager.foodUsageFromId(126L);
        foodsEntity.setDescription("updated!");

        foodsManager.refresh(foodsEntity);
    }

    public void cmdTestBreakfast(View view) {

        // TODO: for tests purposes only. Move and refactor.
        String dateId = DateUtil.datePickerToDateId(mDteDate);
        int meal = mSpnMeal.getSelectedItemPosition();

        FoodsUsageListFrag foodsUsageListFrag = (FoodsUsageListFrag)
                getSupportFragmentManager().findFragmentById(R.id.foods_usage_list_frag);

        if (foodsUsageListFrag != null) {
            foodsUsageListFrag.refresh(dateId, meal);
        } else {
            Intent intent = new Intent(this, FoodsUsageAct.class);
            intent.putExtra(FoodsUsageAct.DATE_ID_PARAMETER, dateId);
            intent.putExtra(FoodsUsageAct.MEAL_PARAMETER, meal);
            startActivity(intent);
        }

    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {

            JournalService.LocalBinder binder = (JournalService.LocalBinder) service;
            mJournalApi = binder.serviceApi();
            mJournalApi.registerViewObserver(JournalAct.this);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            mBound = false;
        }
    };

    @Override
    public void onDayChanged(DaySummary summary) {
        Toast.makeText(this, "onDayChanged called!!!!!!!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFoodsUsageChanged(DaySummary summary) {
        //Toast.makeText(this, "onFoodsUsageChanged called!!!!!!!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onParametersChanged() {
        Toast.makeText(this, "onParametersChanged called!!!!!!!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSummaryRequested(DaySummary daySummary) {
        Toast.makeText(this, "onSummaryRequested called!!!!!!!", Toast.LENGTH_LONG).show();
    }
}
