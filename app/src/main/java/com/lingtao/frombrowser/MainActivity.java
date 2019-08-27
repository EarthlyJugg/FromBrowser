package com.lingtao.frombrowser;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    ViewPager viewpager;
    LinearLayout dot_layout;
    //记录当前页面的前一个页面的position
    private int prePosition = 0;
    private boolean isRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewpager = ((ViewPager) findViewById(R.id.bannerViewpager));
        dot_layout = ((LinearLayout) findViewById(R.id.dot_layout));
        setData();
    }

    private void setData() {
        try {
            JSONObject object = new JSONObject(getString());
            int code = object.getInt("code");
            String msg = object.getString("msg");
            if (code == 1) {
                JSONArray data = object.getJSONArray("data");
                int length = data.length();
                Gson gson = new Gson();
                List<HomeBannerEntry> list = new LinkedList<>();
                for (int i = 0; i < length; i++) {
                    JSONObject item = data.getJSONObject(i);
                    HomeBannerEntry entry = gson.fromJson(item.toString(), HomeBannerEntry.class);
                    list.add(entry);
                }
                setStandbyBanner(list);
            } else {
                Toast.makeText(this, "数据错误", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setStandbyBanner(final List<HomeBannerEntry> imgs) {

        NetAdapter adapter = new NetAdapter(this, imgs);

        viewpager.setAdapter(adapter);
        for (int i = 0; i < imgs.size(); i++) {
            HomeBannerEntry homeBannerEntry = imgs.get(i);
            //创建一个新的ImageView
            View view = new View(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
            lp.leftMargin = 10;
            lp.rightMargin = 10;
            //设置view的宽高左边距等参数
            view.setLayoutParams(lp);
            //默认情况下所有设置View的所有属性为false
            view.setSelected(false);
            view.setBackgroundResource(R.drawable.selector_home_banner_indicator);
            //将View添加到容器中
            dot_layout.addView(view);
        }
        //设置第一个View的enable为true，则该View  背景为红色
        dot_layout.getChildAt(0).setSelected(true);
        //设置预加载页数，最小值为1，默认为1
        viewpager.setOffscreenPageLimit(1);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //页面滑动过程中回调该方法
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //页面滑动结束时回调该方法
            @Override
            public void onPageSelected(int position) {
                position = position % imgs.size();
                dot_layout.getChildAt(prePosition).setSelected(false);
                dot_layout.getChildAt(position).setSelected(true);
                prePosition = position;
            }

            //页面状态发生改变时回调
            //state三种取值：
            //0 表示页面静止
            //1 表示手指在ViewPager上拖动
            //2 表示手指离开ViewPager，页面自由滑动
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpager.setCurrentItem(0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("google_lenve_fb", "run: " + Thread.currentThread().getName());
                while (isRunning) {
                    //睡眠3秒
                    SystemClock.sleep(3000);
                    //子线程更新UI
                    runOnUiThread(new Runnable() {
                        //该方法运行在主线程
                        @Override
                        public void run() {
                            Log.d("google_lenve_fb", "run: +++++++++++++++++" + Thread.currentThread().getName());
                            viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
                        }
                    });
                }
            }
        }).start();
//        mHandler.sendEmptyMessageDelayed(0, 3000);
    }

    private String getString() {
        return "{\"code\":1,\"msg\":\"\",\"data\":[{\"id\":3,\"img\":\"https://img1.3lian.com/img013/v2/62/d/24.jpg\",\"link_url\":\"\",\"url_type\":2,\"ext\":{\"item_id\":\"1\"},\"sort\":0,\"status\":1,\"is_deleted\":0,\"create_at\":\"2019-08-21 18:21:38\"},{\"id\":1,\"img\":\"http://pic9.nipic.com/20100824/2531170_082435310724_2.jpg\",\"link_url\":\"\",\"url_type\":0,\"ext\":{},\"sort\":0,\"status\":1,\"is_deleted\":0,\"create_at\":\"2019-08-21 18:21:38\"},{\"id\":4,\"img\":\"http://pic53.nipic.com/file/20141115/9448607_175255450000_2.jpg\",\"link_url\":\"\",\"url_type\":3,\"ext\":{\"item_id\":\"2\"},\"sort\":1,\"status\":1,\"is_deleted\":0,\"create_at\":\"2019-08-21 18:21:38\"},{\"id\":2,\"img\":\"https://ps.ssl.qhmsg.com/sdr/400__/t01181fbc28eb62f0cf.jpg\",\"link_url\":\"http:\\/\\/www.baidu.com\",\"url_type\":1,\"ext\":{},\"sort\":2,\"status\":1,\"is_deleted\":0,\"create_at\":\"2019-08-21 18:21:38\"}]}\n";
    }

}
