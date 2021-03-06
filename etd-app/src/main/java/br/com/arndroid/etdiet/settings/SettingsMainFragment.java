package br.com.arndroid.etdiet.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.action.ActivityActionCaller;
import br.com.arndroid.etdiet.dialog.PointDialog;
import br.com.arndroid.etdiet.dialog.TextDialog;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.parametershistory.ParametersHistoryManager;
import br.com.arndroid.etdiet.dialog.StringListDialog;
import br.com.arndroid.etdiet.utils.PreferencesUtils;
import br.com.arndroid.etdiet.utils.StringUtils;

public class SettingsMainFragment extends Fragment implements
        StringListDialog.OnStringSelectedListener,
        PointDialog.OnPointSetListener,
        TextDialog.OnTextSetListener{

    private TextView mTxtTrackingUnitName;
    private TextView mTxtDailyAllowanceActualValue;
    private TextView mTxtWeeklyAllowanceActualValue;
    private TextView mTxtExerciseUseMode;
    private TextView mTxtExerciseUseOrder;
    private TextView mTxtTrackingWeekday;

    public static final String OWNER_TAG = SettingsMainFragment.class.getSimpleName();

    private static final String DAILY_ALLOWANCE_SETTINGS_TAG = OWNER_TAG
            + ".DAILY_ALLOWANCE_SETTINGS_TAG";
    private static final String WEEKLY_ALLOWANCE_SETTINGS_TAG = OWNER_TAG
            + ".WEEKLY_ALLOWANCE_SETTINGS_TAG";
    private static final String EXERCISE_USE_MODE_SETTINGS_TAG = OWNER_TAG
            + ".EXERCISE_USE_MODE_SETTINGS_TAG";
    private static final String EXERCISE_USE_ORDER_SETTINGS_TAG = OWNER_TAG
            + ".EXERCISE_USE_ORDER_SETTINGS_TAG";
    private static final String TRACKING_WEEKDAY_SETTINGS_TAG = OWNER_TAG
            + ".TRACKING_WEEKDAY_SETTINGS_TAG";
    public static final String TRACKING_UNIT_NAME_SETTINGS_TAG = OWNER_TAG
            + ".TRACKING_UNIT_NAME_SETTINGS_TAG";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_main_fragment, container, false);

        bindScreen(rootView);
        setupScreen(rootView);
        refreshScreen();

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof ActivityActionCaller)) {
            throw new ClassCastException(activity.toString() + " must implement " +
                    ActivityActionCaller.class.getSimpleName());
        }

    }

    private void refreshScreen() {
        final Context applicationContext = getActivity().getApplicationContext();
        mTxtTrackingUnitName.setText(String.format(getString(R.string.marshaled_tracking_unit_name),
                PreferencesUtils.getTrackingUnitNameZero(applicationContext),
                PreferencesUtils.getTrackingUnitNameOne(applicationContext),
                PreferencesUtils.getTrackingUnitNameMany(applicationContext)));

        final ParametersHistoryManager manager = new ParametersHistoryManager(applicationContext);
        final String unitsActualValueFormat = getString(R.string.units_actual_value);
        final Date currentDate = new Date();
        mTxtDailyAllowanceActualValue.setText(String.format(unitsActualValueFormat,
                manager.getDailyAllowanceForDate(currentDate),
                PreferencesUtils.getTrackingUnitNameForQuantity(applicationContext, manager.getDailyAllowanceForDate(currentDate))));
        mTxtWeeklyAllowanceActualValue.setText(String.format(unitsActualValueFormat,
                manager.getWeeklyAllowanceForDate(currentDate),
                PreferencesUtils.getTrackingUnitNameForQuantity(applicationContext, manager.getWeeklyAllowanceForDate(currentDate))));
        mTxtExerciseUseMode.setText(exerciseUseModeDescription(
                manager.getExerciseUseModeForDate(currentDate)));
        mTxtExerciseUseOrder.setText(exerciseUseOrderDescription(
                manager.getExerciseUseOrderForDate(currentDate)));
        mTxtTrackingWeekday.setText(trackingWeekdayDescription(
                manager.getTrackingWeekdayForDate(currentDate)));
    }

    private String trackingWeekdayDescription(int trackingWeekday) {
        switch (trackingWeekday) {
            case Calendar.SUNDAY:
                return getString(R.string.sunday);
            case Calendar.MONDAY:
                return getString(R.string.monday);
            case Calendar.TUESDAY:
                return getString(R.string.tuesday);
            case Calendar.WEDNESDAY:
                return getString(R.string.wednesday);
            case Calendar.THURSDAY:
                return getString(R.string.thursday);
            case Calendar.FRIDAY:
                return getString(R.string.friday);
            case Calendar.SATURDAY:
                return getString(R.string.saturday);
            default:
                throw new IllegalArgumentException("Invalid trackingWeekday=" + trackingWeekday);
        }
    }

    private String exerciseUseOrderDescription(int exerciseUseOrder) {
        switch (exerciseUseOrder) {
            case Contract.ParametersHistory.EXERCISE_USE_ORDER_USE_EXERCISES_FIRST:
                return getString(R.string.exercise_use_order_use_exercises_first);
            case Contract.ParametersHistory.EXERCISE_USE_ORDER_USE_WEEKLY_ALLOWANCE_FIRST:
                return getString(R.string.exercise_use_order_use_weekly_allowance_first);
            default:
                throw new IllegalArgumentException("Invalid exerciseUseOrder=" + exerciseUseOrder);
        }
    }

    private String exerciseUseModeDescription(int exerciseUseMode) {
        switch (exerciseUseMode) {
            case Contract.ParametersHistory.EXERCISE_USE_MODE_DONT_USE:
                return getString(R.string.exercise_use_mode_dont_use);
            case Contract.ParametersHistory.EXERCISE_USE_MODE_USE_DONT_ACCUMULATE:
                return getString(R.string.exercise_use_mode_use_dont_accumulate);
            case Contract.ParametersHistory.EXERCISE_USE_MODE_USE_AND_ACCUMULATE:
                return getString(R.string.exercise_use_mode_use_and_accumulate);
            default:
                throw new IllegalArgumentException("Invalid exerciseUseMode=" + exerciseUseMode);
        }
    }

    private void bindScreen(View rootView) {
        mTxtTrackingUnitName = (TextView) rootView.findViewById(
                R.id.txtTrackingUnitNameActualValue);
        mTxtDailyAllowanceActualValue = (TextView) rootView.findViewById(
                R.id.txtDailyAllowanceActualValue);
        mTxtWeeklyAllowanceActualValue = (TextView) rootView.findViewById(
                R.id.txtWeeklyAllowanceActualValue);
        mTxtExerciseUseMode = (TextView) rootView.findViewById(
                R.id.txtExerciseUseModeActualValue);
        mTxtExerciseUseOrder = (TextView) rootView.findViewById(
                R.id.txtExerciseUseOrderActualValue);
        mTxtTrackingWeekday = (TextView) rootView.findViewById(
                R.id.txtTrackingWeekdayActualValue);
    }

    private void setupScreen(View rootView) {
        final RelativeLayout layTrackingUnitName = (RelativeLayout) rootView.findViewById(R.id.layTrackingUnitName);
        layTrackingUnitName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextDialog dialog = new TextDialog();
                dialog.setTitle(getString(R.string.tracking_unit_name));
                final Context applicationContext = getActivity().getApplicationContext();
                dialog.setInitialText(String.format(getString(R.string.unmarshaled_tracking_unit_name),
                        PreferencesUtils.getTrackingUnitNameZero(applicationContext),
                        PreferencesUtils.getTrackingUnitNameOne(applicationContext),
                        PreferencesUtils.getTrackingUnitNameMany(applicationContext)));
                dialog.show(getFragmentManager(), TRACKING_UNIT_NAME_SETTINGS_TAG);
            }
        });

        final RelativeLayout layDailyAllowance = (RelativeLayout) rootView.findViewById(R.id.layDailyAllowance);
        layDailyAllowance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PointDialog dialog = new PointDialog();
                dialog.setTitle(getString(R.string.daily_allowance));
                dialog.setMinIntegerValue(0);
                dialog.setMaxIntegerValue(99);
                final ParametersHistoryManager manager = new ParametersHistoryManager(getActivity()
                        .getApplicationContext());
                dialog.setInitialValue(manager.getDailyAllowanceForDate(new Date()));
                dialog.show(getFragmentManager(), DAILY_ALLOWANCE_SETTINGS_TAG);
            }
        });

        final RelativeLayout layWeeklyAllowance = (RelativeLayout) rootView.findViewById(R.id.layWeeklyAllowance);
        layWeeklyAllowance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PointDialog dialog = new PointDialog();
                dialog.setTitle(getString(R.string.weekly_allowance));
                dialog.setMinIntegerValue(0);
                dialog.setMaxIntegerValue(99);
                final ParametersHistoryManager manager = new ParametersHistoryManager(getActivity()
                        .getApplicationContext());
                dialog.setInitialValue(manager.getWeeklyAllowanceForDate(new Date()));
                dialog.show(getFragmentManager(), WEEKLY_ALLOWANCE_SETTINGS_TAG);
            }
        });

        final RelativeLayout layExerciseUseMode = (RelativeLayout) rootView.findViewById(R.id.layExerciseUseMode);
        layExerciseUseMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final StringListDialog dialog = new StringListDialog();
                dialog.setTitle(getString(R.string.exercise_use_mode));
                dialog.setStringListId(R.array.exercise_use_mode_list);
                final ParametersHistoryManager manager = new ParametersHistoryManager(getActivity()
                        .getApplicationContext());
                dialog.setInitialIndex(manager.getExerciseUseModeForDate(new Date()));
                dialog.show(getFragmentManager(), EXERCISE_USE_MODE_SETTINGS_TAG);
            }
        });

        final RelativeLayout layExerciseUseOrder = (RelativeLayout) rootView.findViewById(R.id.layExerciseUseOrder);
        layExerciseUseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final StringListDialog dialog = new StringListDialog();
                dialog.setTitle(getString(R.string.exercise_use_order));
                dialog.setStringListId(R.array.exercise_use_order_list);
                final ParametersHistoryManager manager = new ParametersHistoryManager(getActivity()
                        .getApplicationContext());
                dialog.setInitialIndex(manager.getExerciseUseOrderForDate(new Date()));
                dialog.show(getFragmentManager(), EXERCISE_USE_ORDER_SETTINGS_TAG);
            }
        });

        final RelativeLayout layTrackingWeekday = (RelativeLayout) rootView.findViewById(R.id.layTrackingWeekday);
        layTrackingWeekday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final StringListDialog dialog = new StringListDialog();
                dialog.setTitle(getString(R.string.tracking_weekday));
                dialog.setStringListId(R.array.weekdays_name_list);
                final ParametersHistoryManager manager = new ParametersHistoryManager(getActivity()
                        .getApplicationContext());
                dialog.setInitialIndex(manager.getTrackingWeekdayForDate(new Date()) - 1);
                dialog.show(getFragmentManager(), TRACKING_WEEKDAY_SETTINGS_TAG);
            }
        });

        final RelativeLayout layExerciseGoal = (RelativeLayout) rootView.findViewById(R.id.layExerciseGoal);
        layExerciseGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityActionCaller)getActivity()).onCallAction(R.id.settings_list_fragment,
                        SettingsListActivity.class, Contract.WeekdayParameters.EXERCISE_GOAL, null);
            }
        });

        final RelativeLayout layLiquidGoal = (RelativeLayout) rootView.findViewById(R.id.layLiquidGoal);
        layLiquidGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityActionCaller)getActivity()).onCallAction(R.id.settings_list_fragment,
                        SettingsListActivity.class, Contract.WeekdayParameters.LIQUID_GOAL, null);
            }
        });

        final RelativeLayout layOilGoal = (RelativeLayout) rootView.findViewById(R.id.layOilGoal);
        layOilGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityActionCaller)getActivity()).onCallAction(R.id.settings_list_fragment,
                        SettingsListActivity.class, Contract.WeekdayParameters.OIL_GOAL, null);
            }
        });

        final RelativeLayout laySupplementGoal = (RelativeLayout) rootView.findViewById(R.id.laySupplementGoal);
        laySupplementGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityActionCaller)getActivity()).onCallAction(R.id.settings_list_fragment,
                        SettingsListActivity.class, Contract.WeekdayParameters.SUPPLEMENT_GOAL, null);
            }
        });

        final RelativeLayout layBreakfastIdealValues = (RelativeLayout) rootView.findViewById(R.id.layBreakfastIdealValues);
        layBreakfastIdealValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityActionCaller)getActivity()).onCallAction(R.id.settings_list_fragment,
                        SettingsListActivity.class, Contract.WeekdayParameters.BREAKFAST_GOAL, null);
            }
        });

        final RelativeLayout layBrunchIdealValues = (RelativeLayout) rootView.findViewById(R.id.layBrunchIdealValues);
        layBrunchIdealValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityActionCaller)getActivity()).onCallAction(R.id.settings_list_fragment,
                        SettingsListActivity.class, Contract.WeekdayParameters.BRUNCH_GOAL, null);
            }
        });

        final RelativeLayout layLunchIdealValues = (RelativeLayout) rootView.findViewById(R.id.layLunchIdealValues);
        layLunchIdealValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityActionCaller)getActivity()).onCallAction(R.id.settings_list_fragment,
                        SettingsListActivity.class, Contract.WeekdayParameters.LUNCH_GOAL, null);
            }
        });

        final RelativeLayout laySnackIdealValues = (RelativeLayout) rootView.findViewById(R.id.laySnackIdealValues);
        laySnackIdealValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityActionCaller)getActivity()).onCallAction(R.id.settings_list_fragment,
                        SettingsListActivity.class, Contract.WeekdayParameters.SNACK_GOAL, null);
            }
        });

        final RelativeLayout layDinnerIdealValues = (RelativeLayout) rootView.findViewById(R.id.layDinnerIdealValues);
        layDinnerIdealValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityActionCaller)getActivity()).onCallAction(R.id.settings_list_fragment,
                        SettingsListActivity.class, Contract.WeekdayParameters.DINNER_GOAL, null);
            }
        });

        final RelativeLayout laySupperIdealValues = (RelativeLayout) rootView.findViewById(R.id.laySupperIdealValues);
        laySupperIdealValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityActionCaller)getActivity()).onCallAction(R.id.settings_list_fragment,
                        SettingsListActivity.class, Contract.WeekdayParameters.SUPPER_GOAL, null);
            }
        });
    }

    @Override
    public void onPointSet(String tag, float actualValue) {
        final ParametersHistoryManager manager = new ParametersHistoryManager(getActivity()
                .getApplicationContext());
        if (DAILY_ALLOWANCE_SETTINGS_TAG.equals(tag)) {
            manager.setDailyAllowance(actualValue);
        } else if (WEEKLY_ALLOWANCE_SETTINGS_TAG.equals(tag)) {
            manager.setWeeklyAllowance(actualValue);
        } else {
            throw new IllegalArgumentException("Invalid tag=" + tag);
        }
        refreshScreen();
    }

    @Override
    public void onStringSelected(String tag, int chosenIndex) {
        final ParametersHistoryManager manager = new ParametersHistoryManager(getActivity()
                .getApplicationContext());
        if (EXERCISE_USE_MODE_SETTINGS_TAG.equals(tag)) {
            manager.setExerciseUseMode(chosenIndex);
        } else if (EXERCISE_USE_ORDER_SETTINGS_TAG.equals(tag)) {
            manager.setExerciseUseOrder(chosenIndex);
        } else if (TRACKING_WEEKDAY_SETTINGS_TAG.equals(tag)) {
            manager.setTrackingWeekday(chosenIndex + 1);
        } else {
            throw new IllegalArgumentException("Invalid tag=" + tag);
        }
        refreshScreen();
    }

    @Override
    public void onTextSet(String tag, String actualText) {
        if (TRACKING_UNIT_NAME_SETTINGS_TAG.equals(tag)) {
            String[] trackingUnitNames = actualText.split(";");
            if (trackingUnitNames.length != 3
                    || StringUtils.isEmptyOrOnlySpaces(trackingUnitNames[0])
                    || StringUtils.isEmptyOrOnlySpaces(trackingUnitNames[1])
                    || StringUtils.isEmptyOrOnlySpaces(trackingUnitNames[2])) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getString(R.string.tracking_unit_name_edit_error));
                builder.setPositiveButton(android.R.string.ok, null);
                builder.create().show();
            } else {
                final Context applicationContext = getActivity().getApplicationContext();
                PreferencesUtils.setTrackingUnitNameZero(applicationContext, trackingUnitNames[0]);
                PreferencesUtils.setTrackingUnitNameOne(applicationContext, trackingUnitNames[1]);
                PreferencesUtils.setTrackingUnitNameMany(applicationContext, trackingUnitNames[2]);
            }
        } else {
            throw new IllegalArgumentException("Invalid tag=" + tag);
        }
        refreshScreen();
    }
}
