package br.com.arndroid.etdiet.test.virtualweek;

import android.test.ProviderTestCase2;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.Provider;
import br.com.arndroid.etdiet.provider.parametershistory.ParametersHistoryManager;
import br.com.arndroid.etdiet.virtualweek.WeekPeriod;

public class WeekPeriodTest extends ProviderTestCase2<Provider>  {

    public WeekPeriodTest() {
        super(Provider.class, Contract.AUTHORITY);
    }

    @Override
    protected void setUp() throws Exception {
        // Important: calling the base class implementation of this method
        // where the "magic" of isolation is set up:
        super.setUp();
    }

    public void testInitialAndFinalDateForWholeWeekMustReturnsCorrectValues() {

        ParametersHistoryManager parametersHistoryManager = new ParametersHistoryManager(getMockContext());
        parametersHistoryManager.setTrackingWeekday(Calendar.SATURDAY);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, Calendar.SEPTEMBER, 21);
        Date expectedInitialDate = calendar.getTime();
        calendar.set(2013, Calendar.SEPTEMBER, 27);
        Date expectedFinalDate = calendar.getTime();

        // 25th, September 2013:
        calendar.set(2013, Calendar.SEPTEMBER, 25);
        WeekPeriod weekPeriod = new WeekPeriod(getMockContext(), calendar.getTime());
        assertEquals(expectedInitialDate, weekPeriod.getInitialDate());
        assertEquals(expectedFinalDate, weekPeriod.getFinalDate());
        assertEquals(calendar.getTime(), weekPeriod.getReferenceDate());

        // 26th, September 2013:
        calendar.set(2013, Calendar.SEPTEMBER, 26);
        weekPeriod = new WeekPeriod(getMockContext(), calendar.getTime());
        assertEquals(expectedInitialDate, weekPeriod.getInitialDate());
        assertEquals(expectedFinalDate, weekPeriod.getFinalDate());
        assertEquals(calendar.getTime(), weekPeriod.getReferenceDate());

        // 27th, September 2013:
        calendar.set(2013, Calendar.SEPTEMBER, 27);
        weekPeriod = new WeekPeriod(getMockContext(), calendar.getTime());
        assertEquals(expectedInitialDate, weekPeriod.getInitialDate());
        assertEquals(expectedFinalDate, weekPeriod.getFinalDate());
        assertEquals(calendar.getTime(), weekPeriod.getReferenceDate());

        calendar.set(2013, Calendar.SEPTEMBER, 28);
        expectedInitialDate = calendar.getTime();
        calendar.set(2013, Calendar.OCTOBER, 4);
        expectedFinalDate = calendar.getTime();

        // 28th, September 2013:
        calendar.set(2013, Calendar.SEPTEMBER, 28);
        weekPeriod = new WeekPeriod(getMockContext(), calendar.getTime());
        assertEquals(expectedInitialDate, weekPeriod.getInitialDate());
        assertEquals(expectedFinalDate, weekPeriod.getFinalDate());
        assertEquals(calendar.getTime(), weekPeriod.getReferenceDate());

        // 29th, September 2013:
        calendar.set(2013, Calendar.SEPTEMBER, 29);
        weekPeriod = new WeekPeriod(getMockContext(), calendar.getTime());
        assertEquals(expectedInitialDate, weekPeriod.getInitialDate());
        assertEquals(expectedFinalDate, weekPeriod.getFinalDate());
        assertEquals(calendar.getTime(), weekPeriod.getReferenceDate());

        // 30th, September 2013:
        calendar.set(2013, Calendar.SEPTEMBER, 30);
        weekPeriod = new WeekPeriod(getMockContext(), calendar.getTime());
        assertEquals(expectedInitialDate, weekPeriod.getInitialDate());
        assertEquals(expectedFinalDate, weekPeriod.getFinalDate());
        assertEquals(calendar.getTime(), weekPeriod.getReferenceDate());

        // 1st, October 2013:
        calendar.set(2013, Calendar.OCTOBER, 1);
        weekPeriod = new WeekPeriod(getMockContext(), calendar.getTime());
        assertEquals(expectedInitialDate, weekPeriod.getInitialDate());
        assertEquals(expectedFinalDate, weekPeriod.getFinalDate());
        assertEquals(calendar.getTime(), weekPeriod.getReferenceDate());
    }

    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "==>ETD/" + WeekPeriod.class.getSimpleName();
    @SuppressWarnings("UnusedDeclaration")
    private static final boolean isLogEnabled = true;
}
