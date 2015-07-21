package net.whend.soodal.whend.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

import net.whend.soodal.whend.R;

import java.util.Calendar;

/**
 * Created by wonkyung on 2015-07-22.
 * http://android--examples.blogspot.kr/2015/05/how-to-use-datepickerdialog-in-android.html
 */
public class F6_DatePickerFrgment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current date as the default date in the date picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        //Create a new DatePickerDialog instance and return it
        /*
            DatePickerDialog Public Constructors - Here we uses first one
            public DatePickerDialog (Context context, DatePickerDialog.OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth)
            public DatePickerDialog (Context context, int theme, DatePickerDialog.OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth)
         */
        return new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, this, year, month, day);
    }
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        EditText dateview = (EditText)getActivity().findViewById(R.id.date);
        dateview.setText(String.format("%d", monthOfYear + 1) + "월" + String.format("%d", dayOfMonth) + "일");
    }

}
