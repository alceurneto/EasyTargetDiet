package br.com.arndroid.etdiet.provider.days;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.Calendar;
import java.util.Date;

import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.parametershistory.ParametersHistoryManager;
import br.com.arndroid.etdiet.provider.weekdayparameters.WeekdayParametersEntity;
import br.com.arndroid.etdiet.provider.weekdayparameters.WeekdayParametersManager;
import br.com.arndroid.etdiet.utils.DateUtils;

public class DaysManager {
    final private Context mContext;

    public DaysManager(Context context) {
        mContext = context;
    }

    public DaysEntity dayFromDateId(String dateId) {
        return dayFromDate(DateUtils.dateIdToDate(dateId));
    }

    public DaysEntity dayFromDate(Date date) {
        Cursor cursor = null;
        try {
            cursor = mContext.getContentResolver().query(Contract.Days.CONTENT_URI, null,
                    Contract.Days.DATE_ID_SELECTION, new String[] {DateUtils.dateToDateId(date)}, null);
            if(cursor.getCount() == 0) {
                return buildMemoryDayForDate(date);
            } else {
                cursor.moveToFirst();
                return DaysEntity.fromCursor(cursor);
            }
        } finally {
            if(cursor != null) cursor.close();
        }
    }

    private DaysEntity buildMemoryDayForDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        WeekdayParametersEntity parameters = new
                WeekdayParametersManager(mContext).weekdayParametersFromWeekday(calendar.get(Calendar.DAY_OF_WEEK));
        return new DaysEntity(null,
                DateUtils.dateToDateId(date),
                new ParametersHistoryManager(mContext).getDailyAllowanceForDate(date),
                parameters.getBreakfastStartTime(), parameters.getBreakfastEndTime(), parameters.getBreakfastGoal(),
                parameters.getBrunchStartTime(), parameters.getBrunchEndTime(), parameters.getBrunchGoal(),
                parameters.getLunchStartTime(), parameters.getLunchEndTime(), parameters.getLunchGoal(),
                parameters.getSnackStartTime(), parameters.getSnackEndTime(), parameters.getSnackGoal(),
                parameters.getDinnerStartTime(), parameters.getDinnerEndTime(), parameters.getDinnerGoal(),
                parameters.getSupperStartTime(), parameters.getSupperEndTime(), parameters.getSupperGoal(),
                parameters.getExerciseGoal(),
                0, parameters.getLiquidGoal(),
                0, parameters.getOilGoal(),
                0, parameters.getSupplementGoal(),
                null);

    }

    public void refresh(DaysEntity entity) {

        entity.validateOrThrow();

        final Long id = entity.getId();
        if(id == null) {
            final Uri resultUri = mContext.getContentResolver().insert(Contract.Days.CONTENT_URI,
                    entity.toContentValues());
            entity.setId(Long.parseLong(resultUri.getLastPathSegment()));
        } else {
            mContext.getContentResolver().update(ContentUris.withAppendedId(Contract.Days.CONTENT_URI,id),
                    entity.toContentValues(), null, null);
        }
    }
}
