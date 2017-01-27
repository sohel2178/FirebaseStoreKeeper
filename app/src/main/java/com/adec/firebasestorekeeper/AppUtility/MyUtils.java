package com.adec.firebasestorekeeper.AppUtility;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.hardware.input.InputManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sohel on 9/21/2016.
 */
public class MyUtils {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    public static boolean validatePhoneNumber(String phoneNo) {
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{11}")) return true;
            //validating phone number with -, . or spaces
        else if(phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;

        else if(phoneNo.matches("\\d{5}[-\\.\\s]\\d{6}")) return true;
            //validating phone number with extension length from 3 to 5
        else if(phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
            //validating phone number where area code is in braces ()
        else if(phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
            //return false if nothing matches the input
        else return false;

    }

    public static boolean isThisDateValid(String dateToValidate, String dateFromat){

        if(dateToValidate == null){
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
        sdf.setLenient(false);

        try {

            //if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);
            System.out.println(date);

        } catch (ParseException e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static void showDialogAndSetTime( Context context, final EditText editText) {
        final Calendar c = Calendar.getInstance();

        String date = editText.getText().toString().trim();


        if(!date.equals("")){
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            try{
                c.setTime(formatter.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                int actualMonth = monthOfYear+1;
                StringBuffer sb = new StringBuffer();
                String day="";
                String month="";
                if(String.valueOf(dayOfMonth).length()==1){
                    day="0"+dayOfMonth;
                }else{
                    day=String.valueOf(dayOfMonth);
                }
                if(String.valueOf(actualMonth).length()==1){
                    month="0"+(monthOfYear+1);
                }else{
                    month=String.valueOf(monthOfYear+1);
                }
                sb.append(day)
                        .append("-")
                        .append(month)
                        .append("-")
                        .append(year);

                editText.setText(sb.toString());
            }
        },mYear,mMonth,mDay);
        datePickerDialog.show();
        datePickerDialog.setTitle("Pick a Date");
    }


    public static void showDialogAndSetTime( Context context, final TextView textView) {

        final Calendar c = Calendar.getInstance();


        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                int actualMonth = monthOfYear+1;
                StringBuffer stringBuffer = new StringBuffer();
                String day="";
                String month="";
                if(String.valueOf(dayOfMonth).length()==1){
                    day="0"+dayOfMonth;
                }else{
                    day=String.valueOf(dayOfMonth);
                }
                if(String.valueOf(actualMonth).length()==1){
                    month="0"+(monthOfYear+1);
                }else{
                    month=String.valueOf(monthOfYear+1);
                }
                stringBuffer.append(day)
                        .append("-")
                        .append(month)
                        .append("-")
                        .append(year);

                textView.setText(stringBuffer.toString());
            }
        },mYear,mMonth,mDay);
        datePickerDialog.show();
        datePickerDialog.setTitle("Pick a Date");

    }

    public static void animate(Context context, RecyclerView.ViewHolder holder, boolean scrollingState){

        AnimatorSet animatorSet = new AnimatorSet();

        int width = getScreenWidth(context);

        if(holder.getAdapterPosition()%2==0){
            width=-width;
        }


        ObjectAnimator translateY = ObjectAnimator.ofFloat(holder.itemView,"translationY",scrollingState==true?-200:200,0);
        ObjectAnimator translateX = ObjectAnimator.ofFloat(holder.itemView,"translationX",width,0);
        translateY.setDuration(500);
        translateX.setDuration(1500);
        animatorSet.playTogether(translateX,translateY);

        animatorSet.start();


    }

    public static void animateScale(RecyclerView.ViewHolder holder, boolean scrollingState){

        AnimatorSet animatorSet = new AnimatorSet();

        //int width = getScreenWidth(context);
/*
        if(holder.getAdapterPosition()%2==0){
            width=-width;
        }*/




        ObjectAnimator translateY = ObjectAnimator.ofFloat(holder.itemView,"scaleY",2,1);
        ObjectAnimator translateX = ObjectAnimator.ofFloat(holder.itemView,"scaleX",2,1);
        translateY.setDuration(500);
        translateX.setDuration(1500);
        animatorSet.playTogether(translateX,translateY);

        animatorSet.start();


    }

    public static void testAnimation(RecyclerView.ViewHolder holder){

        AnimatorSet animatorSet = new AnimatorSet();
        int initial_position=holder.getAdapterPosition()*56;

        ObjectAnimator translateY = ObjectAnimator.ofFloat(holder.itemView,"translationY",-initial_position,0);
        //ObjectAnimator translateX = ObjectAnimator.ofFloat(holder.itemView,"translationX",width,0);
        translateY.setDuration(1500);
       // translateX.setDuration(1500);
        animatorSet.playTogether(translateY);

        animatorSet.start();


    }






    public static Date getDateFromString(String dateStr){
        Date date = null;

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        try {
            date = formatter.parse(dateStr);

        }catch (ParseException e){
            e.printStackTrace();
        }

        return date;
    }

    private static int getMonthFromDate(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return c.get(Calendar.MONTH)+1;

    }

    public static int getMonthFromString(String dateStr){
        Date date = getDateFromString(dateStr);

        return getMonthFromDate(date);
    }

    public static Date maxDate(List<Date> dateList){
        Collections.sort(dateList);

        Date date = dateList.get(dateList.size()-1);

        return date;
    }

    public static Date minDate(List<Date> dateList){
        Collections.sort(dateList);

        Date date = dateList.get(0);

        return date;
    }





    public static int monthDifference(Date startDate, Date endDate){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTime(startDate);
        cal2.setTime(endDate);



        int yearDiff = cal2.get(Calendar.YEAR)-cal1.get(Calendar.YEAR);




        int monthDiff = yearDiff*12+cal2.get(Calendar.MONTH)-cal1.get(Calendar.MONTH);

        return monthDiff;
    }

    public static int dayDiff(Date startDate,Date endDate){
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTime(startDate);
        cal2.setTime(endDate);

        long timediffinSecond = cal2.getTimeInMillis()-cal1.getTimeInMillis();
        int dayDiff = (int) (timediffinSecond/(1000*60*60*24));

        return dayDiff+1;
    }







    private static Date getEndDate(Date startDate){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        int dateInt =calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        int year = calendar.get(Calendar.YEAR);

        String date = dateInt+"-"+month+"-"+year;

        Date endDate = getDateFromString(date);

        return endDate;
    }





    public static String dateToString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
    }





    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static String getYear(Date date){
        String dateString = null;

        DateFormat formatter = new SimpleDateFormat("yyyy");

        dateString = formatter.format(date);

        return dateString;
    }

    public static Date incrementDate(int dayIncrement,Date currentDate){

        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE,dayIncrement);

        Date bal = c.getTime();

        return bal;

    }

    public static String getDateAndMonth(Date date){
        String dateString = null;

        DateFormat formatter = new SimpleDateFormat("dd-MMM");

        dateString = formatter.format(date);

        return dateString;
    }

    public static int getScreenWidth(Context context){

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        //int height = metrics.heightPixels;

        return width;
    }

    public static int convertPixelsToDp(int px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int dp = (int) (px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public static int getScreenWidthInDp(Context context){
        int screenWidthinPx = getScreenWidth(context);

        return convertPixelsToDp(screenWidthinPx,context);
    }


    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth,
                                                     int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }


    public static void hideKey(View view){
        InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static boolean isProductIsInTheList(List<String> products,String product){
        boolean bool = false;

        for(String x: products){
            if(x.equals(product)){
                bool=true;
                break;
            }
        }

        return bool;
    }


    public static String getPath(Context context,Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }


    public static Bitmap resizeBitmap(String photoPath, int targetW, int targetH) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true; //Deprecated API 21

        return BitmapFactory.decodeFile(photoPath, bmOptions);
    }


    public static Bitmap uriToScaledBitmap(Context context,Uri uri, int targetWidth,int targetHeight){
        String path = getPath(context,uri);

        return resizeBitmap(path,targetWidth,targetHeight);
    }


    public static void underLineText(TextView textView){
        textView.setPaintFlags(textView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
    }


    public static String getDateText(long timeStamp){

        String returnText = null;

        long currentTime = System.currentTimeMillis();
        long timeDiff = currentTime-timeStamp;

        if(timeDiff<60*1000){
            returnText =(int)(timeDiff/1000)+" seconds ago";
        }else if(timeDiff<60*60*1000){
            returnText = (int)(timeDiff/(1000*60))+" minutes ago";
        }else if(timeDiff<24*60*60*1000){
            returnText = (int)(timeDiff/(1000*60*60))+" hours ago";
        }else{
            returnText = (int)(timeDiff/(1000*60*60*24))+" days ago";
        }

        return returnText;

    }


    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }










}
