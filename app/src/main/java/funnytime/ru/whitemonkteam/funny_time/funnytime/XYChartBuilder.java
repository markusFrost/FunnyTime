package funnytime.ru.whitemonkteam.funny_time.funnytime;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;

import funnytime.ru.whitemonkteam.funny_time.funnytime.models.Book;
import funnytime.ru.whitemonkteam.funny_time.funnytime.models.Serial;
import funnytime.ru.whitemonkteam.funny_time.funnytime.objects.AppContext;
import funnytime.ru.whitemonkteam.funny_time.funnytime.utils.Constants;


public class XYChartBuilder extends ActionBarActivity
{

    /** The main dataset that includes all the series that go into a chart. */
    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
    /** The main renderer that includes all the renderers customizing a chart. */
    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
    /** The most recently added series. */
    private XYSeries mCurrentSeries;
    /** The most recently created renderer, customizing the current series. */
    private XYSeriesRenderer mCurrentRenderer;
    /** Button for creating a new series of data. */

    /** Button for adding entered data to the current series. */

    /** Edit text field for entering the X value of the data to be added. */

    /** Edit text field for entering the Y value of the data to be added. */

    /** The chart view that displays the data. */
    private GraphicalView mChartView;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save the current data, for instance when changing screen orientation
        outState.putSerializable("dataset", mDataset);
        outState.putSerializable("renderer", mRenderer);
        outState.putSerializable("current_series", mCurrentSeries);
        outState.putSerializable("current_renderer", mCurrentRenderer);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        // restore the current data, for instance when changing the screen
        // orientation
        mDataset = (XYMultipleSeriesDataset) savedState.getSerializable("dataset");
        mRenderer = (XYMultipleSeriesRenderer) savedState.getSerializable("renderer");
        mCurrentSeries = (XYSeries) savedState.getSerializable("current_series");
        mCurrentRenderer = (XYSeriesRenderer) savedState.getSerializable("current_renderer");
    }

    ArrayList<Integer> list ;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xychart_builder);
        String name =  "";
        Intent intent = getIntent();
        Serial s = null;
        Book b = null;
        try
        {
            b = (Book) intent.getSerializableExtra(Constants.EXTRA_ITEM);
        } catch (Exception e){}


        if ( b == null)
        {
            s = (Serial) intent.getSerializableExtra(Constants.EXTRA_ITEM);
            name = s.Name;
            list = AppContext.dbAdapter.getStatisticSerialForGraph(s);
        }
        else
        {
            name = b.Name;
            list = AppContext.dbAdapter.getStatisticBookForGraph(b);
        }
//        getActionBar().setDisplayHomeAsUpEnabled(true);
        //  getActionBar().setDisplayShowCustomEnabled(true);

        // здесь вытаскиваем даные


        //  getActionBar().setTitle(name);
        // Book book = AppContext.dbAdapter.getBooks("Тест").get(0);

     /*   list = new ArrayList<>();

        list.add(10);
        list.add(50);
        list.add(100);
        list.add(20);*/


        if ( list.size() == 0)
        {
            finish();
        }

        tv = (TextView) findViewById(R.id.tvChart);
        tv.setText(
                name
        );

        // the top part of the UI components for adding new data points

        // set some properties on the main renderer
        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.rgb(255, 255, 255));
        mRenderer.setAxisTitleTextSize(16);
        mRenderer.setChartTitleTextSize(20);
        mRenderer.setLabelsTextSize(15);
        mRenderer.setLegendTextSize(15);
        mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
        mRenderer.setZoomButtonsVisible(true);

        mRenderer.setPointSize(5);

        String seriesTitle = "Series " + (mDataset.getSeriesCount() + 1);
        seriesTitle = name;
        // create a new series of data
        XYSeries series = new XYSeries(seriesTitle);
        mDataset.addSeries(series);
        mCurrentSeries = series;
        // create a new renderer for the new series
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        mRenderer.addSeriesRenderer(renderer);
        // set some renderer properties
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setFillPoints(true);
        renderer.setDisplayChartValues(true);
        renderer.setDisplayChartValuesDistance(10);
        mCurrentRenderer = renderer;

        fillViews();
        mChartView.repaint();

        for ( int i = 1; i <= list.size(); i++)
        {
            mCurrentSeries.add((i ), list.get(i - 1));
        }


        // repaint the chart such as the newly added point to be visible
        mChartView.repaint();
    }

    private void fillViews()
    {
        if (mChartView == null)
        {
            LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
            mChartView = ChartFactory.getLineChartView(this, mDataset, mRenderer);
            // enable the chart click events
            mRenderer.setClickEnabled(true);
            mRenderer.setSelectableBuffer(10);
            mChartView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // handle the click event on the chart
                    SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
                    if (seriesSelection == null) {
                        //Toast.makeText(XYChartBuilder.this, "No chart element", Toast.LENGTH_SHORT).show();
                    } else {
                        // display information of the clicked point

                    }
                }
            });
            layout.addView(mChartView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.FILL_PARENT));

        } else {
            mChartView.repaint();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        int id = item.getItemId();
        if ( id == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        fillViews();
    }
}
