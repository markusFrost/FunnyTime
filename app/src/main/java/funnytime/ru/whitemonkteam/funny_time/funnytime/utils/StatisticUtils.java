package funnytime.ru.whitemonkteam.funny_time.funnytime.utils;

import funnytime.ru.whitemonkteam.funny_time.funnytime.models.Book;
import funnytime.ru.whitemonkteam.funny_time.funnytime.models.Film;
import funnytime.ru.whitemonkteam.funny_time.funnytime.models.Serial;
import funnytime.ru.whitemonkteam.funny_time.funnytime.models.StatisticBook;
import funnytime.ru.whitemonkteam.funny_time.funnytime.models.StatisticSerial;
import funnytime.ru.whitemonkteam.funny_time.funnytime.objects.AppContext;

/**
 * Created by Андрей on 22.03.2015.
 */
public class StatisticUtils
{
    public static void Save(Object o)
    {
        if ( o instanceof Film)
        {

        }
        else if ( o instanceof Serial)
        {
            Serial s = (Serial) o;
            if ( s.Action == Constants.TYPE_ALREADY_SEE)
            {
                saveSerial(s);
            }
        }
        else if ( o instanceof Book)
        {
            Book book = (Book) o;
            if (book.Action == Constants.TYPE_ALREADY_SEE)
            {
                saveBook(book);
            }
        }
    }

    private static  void saveSerial ( Serial s)
    {
        if (AppContext.dbAdapter.isStatisticSerialExists(s))
        {
            long time = HelpUtils.getTimeMillsDay(s.DateChange);
//1416603600000
            StatisticSerial item = AppContext.dbAdapter.getStatisticSerialByDateTime(s, time);

            if ( item == null)
            {
                item = AppContext.dbAdapter.getStatisticSerialWithPrevious(s);
                item.DateTime = time;

                //???
                if ( item.Season == s.Season)
                {
                    item.SerStart = item.SerStart + item.SerCount;
                    item.SerCount =  s.Series - item.Series;
                    item.Series = s.Series;

                }
                else
                {
                    item.SerCount = s.Series;
                    item.Series = s.Series;
                    item.Season = s.Season;
                }

                AppContext.dbAdapter.Add(item);
            }
            else
            {
                if ( item.Season == s.Season)
                {

                    item.SerCount +=  s.Series - item.Series;
                    item.Series = s.Series;
                }
                else
                {
                    item.SerCount += s.Series;
                    item.Series = s.Series;
                    item.Season = s.Season;
                }
                AppContext.dbAdapter.Update(item);
            }
        }
        else
        {
            StatisticSerial item = new StatisticSerial();
            item.Season = s.Season;

                item.Series = s.Series;
                item.SerCount = 1;

            item.SerStart = s.Series;

            item.DateTime = HelpUtils.getTimeMillsDay(s.DateChange);
            item.Type = Constants.TYPE_SERIAL;
            item.ItemId = s.Id;

         long id =    AppContext.dbAdapter.Add(item);

            id = id + 0;
        }

    }

    private static void saveBook ( Book book)
    {
        if (AppContext.dbAdapter.isStatisticBookExists(book))
        {
            long time = HelpUtils.getTimeMillsDay(book.DateChange);

            StatisticBook item = AppContext.dbAdapter.getStatisticBookByDateTime(book, time);

            if ( item == null) // записей в этот день ещё не было
            {
                item = AppContext.dbAdapter.getStatisticBookWithPrevious(book);

                item.PageStart = item.PageStart + item.PageCount;
                item.PageCount = book.Page - item.PageStart ;
                item.DateTime = HelpUtils.getTimeMillsDay(book.DateChange);

                AppContext.dbAdapter.Add(item);
            }
            else
            {
                item.PageCount = book.Page - item.PageStart;
                AppContext.dbAdapter.Update(item);
            }

        }
        else
        {
            StatisticBook item = new StatisticBook();
            item.DateTime = HelpUtils.getTimeMillsDay(book.DateChange);
            item.PageStart = book.Page;
            item.PageCount = 0;
            item.Type = Constants.TYPE_BOOK;
            item.ItemId = book.Id;
            AppContext.dbAdapter.Add(item);

        }
    }
}
