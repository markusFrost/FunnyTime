package funnytime.ru.whitemonkteam.funny_time.funnytime.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Андрей on 21.03.2015.
 */
public class HelpUtils
{

    public static boolean isToday ( long time)
    {
        time = getTimeMillsDay(time);
         if ( time == getTimeMillsDay(System.currentTimeMillis()))
         {
             return true;
         }
        else
         {
             return false;
         }
    }



    public static long getYerstaday(long data)
    {
        String format;

        format = "dd MM yyyy"; //задаём формат когда обрубаем по месяцу

        //format = "dd MMMM yyyy HH:mm:ss"; // здесь пишешь нужный формат

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        //Date time = new Date(data);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -3);

		Date time = new Date(cal.getTimeInMillis());

        Date dataResult;
        try {
            dataResult = simpleDateFormat.parse(simpleDateFormat.format(time));

            String value = simpleDateFormat.format(dataResult.getTime());


            return dataResult.getTime();

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;


    }
    public static long getTimeMillsDay(long data)
    {
        String format;

        format = "dd MM yyyy"; //задаём формат когда обрубаем по месяцу

        //format = "dd MMMM yyyy HH:mm:ss"; // здесь пишешь нужный формат

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        Date time = new Date(data);

//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.MONTH, -5);
//
//		Date time = new Date(cal.getTimeInMillis());

        Date dataResult;
        try {
            dataResult = simpleDateFormat.parse(simpleDateFormat.format(time));

            String value = simpleDateFormat.format(dataResult.getTime());


            return dataResult.getTime();

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;


    }




    public static long getTimeMills(long data)
    {
        String format;

        format = "MMMM yyyy"; //задаём формат когда обрубаем по месяцу

        //format = "dd MMMM yyyy HH:mm:ss"; // здесь пишешь нужный формат

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        Date time = new Date(data);

//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.MONTH, -5);
//
//		Date time = new Date(cal.getTimeInMillis());

        Date dataResult;
        try {
            dataResult = simpleDateFormat.parse(simpleDateFormat.format(time));

            String value = simpleDateFormat.format(dataResult.getTime());


            return dataResult.getTime();

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;


    }

    public static String getTimeString(long data)
    {
        String format;
       // format = "dd MM yyyy";// адаем формат когда обрубаем по месяцу
        format = "MMMM yyyy"; //здесь пишешь нужный формат
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date time = new Date(data);
        Date dataresult;

        try {

            dataresult = simpleDateFormat.parse(simpleDateFormat.format(time));
            String value = simpleDateFormat.format(dataresult);
            if ( time.getTime() == 0)
            {
                return "Unknown";
            }
            else
            {
                return value;
            }
        }
        catch (ParseException e)
        {

            e.printStackTrace();
        }//
        return null;
    }

}
