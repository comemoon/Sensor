package com.example.sensor.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sensor.R;
import com.example.sensor.chart.Data0;
import com.example.sensor.chart.LineChartMarkView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.io.InputStream;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

public class ChartFragment extends Fragment {
    private LineChart lineChart;
    private XAxis xAxis; //X轴
    private YAxis leftYAxis; //左侧Y轴
    private YAxis rightYAxis; //右侧Y轴
    private Legend legend; //图例
    private LimitLine limitLine;  //限制线
    private static final String TAG = "Main2Activity";
    private ArrayList<Data0> dataArrayList = new ArrayList<Data0>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initChart(lineChart);
            showLineChart(dataArrayList, "Temperature", getResources().getColor(R.color.blue));
            addLine(dataArrayList, "Humidness", getResources().getColor(R.color.orange));
            @SuppressLint("ResourceType") Drawable drawable = getResources().getDrawable(R.xml.fade_blue);
            setChartFillDrawable(drawable);
            // setMarkerView();


        }
    };

    private void initChart(LineChart lineChart) {
        lineChart.setDrawGridBackground(false);//是否显示网格线
        lineChart.setBackgroundColor(Color.WHITE);//修改背景
        lineChart.setDrawBorders(false);//是否显示边界 去掉边框
        lineChart.setDragEnabled(true);//是否可以拖动
        lineChart.setTouchEnabled(true);//是否有触摸事件
        lineChart.animateX(1000);//设置XY轴动画效果
        lineChart.animateY(1500);
        Description description = new Description();
//        description.setText("需要展示的内容");
        description.setEnabled(false);
        lineChart.setDescription(description);
        /*XY轴设置*/
        xAxis = lineChart.getXAxis();
        leftYAxis = lineChart.getAxisLeft();
        rightYAxis = lineChart.getAxisRight();
        xAxis.setDrawGridLines(false);//去掉XY轴自己的网格线
        rightYAxis.setDrawGridLines(false);
//        rightYAxis.setEnabled(false);//去掉右Y轴
        leftYAxis.setDrawGridLines(true);
        //设置X Y轴网格线为虚线（实体线长度、间隔距离、偏移量：通常使用 0）
        leftYAxis.enableGridDashedLine(10f, 10f, 0f);
        //X轴位置显示在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setAxisMaximum(0f);
//        xAxis.setAxisMaximum(1f);
        //保证Y轴从0开始
        leftYAxis.setAxisMinimum(0f);
        rightYAxis.setAxisMinimum(0f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String time = dataArrayList.get((int) value).getTime();
                return time;
            }
        });
        rightYAxis.setValueFormatter(new IndexAxisValueFormatter() {

            public String getFormattedValue(float value) {
                return ((int) value) + "%";
            }
        });
        leftYAxis.setValueFormatter(new IndexAxisValueFormatter() {

            public String getFormattedValue(float value) {
                return ((int) value) + "℃";
            }
        });
        /***折线图例 标签 设置***/
        legend = lineChart.getLegend();
        //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
        legend.setEnabled(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(12f);
        //显示位置 左下方
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);

    }

    public void setChartFillDrawable(Drawable drawable) {
        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
            LineDataSet lineDataSet = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            //避免在 initLineDataSet()方法中 设置了 lineDataSet.setDrawFilled(false); 而无法实现效果
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillDrawable(drawable);
            lineChart.invalidate();
        }
    }


    private void initLineDataSet(LineDataSet lineDataSet, int color, LineDataSet.Mode mode) {
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(3f);
        //设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(10f);
        //不显示点
        lineDataSet.setDrawCircles(false);
        //不显示值
        lineDataSet.setDrawValues(false);
        //设置折线图填充
        lineDataSet.setDrawFilled(false);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);


        if (mode == null) {//设置曲线展示为圆滑曲线（如果不设置则默认折线）
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        } else {
            lineDataSet.setMode(mode);
        }
    }

    public void showLineChart(ArrayList<Data0> dataList, String name, int color) {
        List<Entry> entries = new ArrayList<>();
        int ii = dataList.size() > 10 ? 0 : (dataList.size() - 10);
        for (int i = ii; i < dataList.size(); i++) {
            Data0 data0 = dataList.get(i);
            /**
             * 在此可查看 Entry构造方法，可发现 可传入数值 Entry(float x, float y)
             * 也可传入Drawable， Entry(float x, float y, Drawable icon) 可在XY轴交点 设置Drawable图像展示
             */
            float num = Float.valueOf(data0.getTemperature().toString());
//            Log.d(TAG, "showLineChart: "+num);
            entries.add(new Entry(i, num));
        }
        // 每一个LineDataSet代表一条线
        LineDataSet lineDataSet = new LineDataSet(entries, name);
        initLineDataSet(lineDataSet, color, LineDataSet.Mode.LINEAR);
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
    }

    private void addLine(List<Data0> dataList, String name, int color) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            Data0 data0 = dataList.get(i);
            float num = Float.valueOf(data0.getHumidness().toString());
            Entry entry = new Entry(i, num*100);
            entries.add(entry);
        }
        LineDataSet lineDataSet = new LineDataSet(entries, name);
        initLineDataSet(lineDataSet, color, LineDataSet.Mode.LINEAR);
        lineChart.setDrawGridBackground(false);
        lineChart.getLineData().addDataSet(lineDataSet);
    }

    public void readExcel(ChartFragment context) {
        try {
            InputStream is = context.getClass().getResourceAsStream("/assets/data.xls");
            Workbook book = Workbook.getWorkbook(is);
            book.getNumberOfSheets();
            Sheet sheet = book.getSheet(0);
            int Rows = sheet.getRows();
            for (int i = 0; i < Rows; ++i) {
                Data0 data0 = new Data0();
                String tem = sheet.getCell(0, i).getContents();
                String hum = sheet.getCell(1, i).getContents();
                data0.setTemperature(tem);
                data0.setHumidness(hum);
                dataArrayList.add(data0);
                Log.e("yy", "第" + i + "行数据=" + "," + tem + "," + hum);
            }
            book.close();
        } catch (Exception e) {
            Log.e("yy", "e" + e);
        }
    }


    public void setMarkerView() {
        LineChartMarkView mv = new LineChartMarkView(this.getContext(), xAxis.getValueFormatter());
        mv.setChartView(lineChart);
        lineChart.setMarker(mv);
        lineChart.invalidate();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        lineChart = view.findViewById(R.id.lineChart);

        readExcel(this);
        initChart(lineChart);
        showLineChart(dataArrayList, "Temperature", getResources().getColor(R.color.blue));
        addLine(dataArrayList,"Humidness",getResources().getColor(R.color.orange));
        @SuppressLint("ResourceType") Drawable drawable=getResources().getDrawable(R.xml.fade_blue);
        setChartFillDrawable(drawable);
        setMarkerView();

//        public void setMarkerView(View view) {
//            LineChartMarkView mv = new LineChartMarkView(view.gerContext(), xAxis.getValueFormatter());
//            mv.setChartView(lineChart);
//            lineChart.setMarker(mv);
//            lineChart.invalidate();
//        }


//        readExcel(ChartFragment.this);
//        new Thread() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void run() {
//                while (true) {
//                    Message msg = new Message();
//                    handler.sendMessage(msg);
//                    try {
//                        sleep(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    LocalTime localTime = LocalTime.now();
//                    String time = localTime.toString();
//                    String num1 = Math.random() * 40 + "";
//                    String num2 = Math.random() * 30 + "";
//                    Data0 data0 = new Data0();
//                    data0.setHumidness(num2);
//                    data0.setTime(time);
//                    data0.setTemperature(num1);
//                    dataArrayList.add(data0);
//                }
//            }
//        }.start();
        return view;
    }
}
