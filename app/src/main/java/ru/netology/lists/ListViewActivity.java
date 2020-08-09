package ru.netology.lists;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewActivity extends AppCompatActivity {

    private final static String ATRIBUTE_TITLE = "title";
    private final static String ATRIBUTE_SUBTITLE = "subtitle";
    private final static String SHARED_PR_NAME = "pref";
    private final static String SHARED_PR_KEY = "pref_key";

    private List<Map<String, String>> simpleAdapterContent = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        ListView list = findViewById(R.id.list);
        //List<Map<String, String>> values = prepareContent();
        final BaseAdapter listContentAdapter = createAdapter(simpleAdapterContent);
        list.setAdapter(listContentAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                simpleAdapterContent.remove(i);
                listContentAdapter.notifyDataSetChanged();
            }
        });
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                simpleAdapterContent.clear();
                init();
                listContentAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    @NonNull
    private BaseAdapter createAdapter(List<Map<String, String>> values) {
        String[] from = {ATRIBUTE_TITLE, ATRIBUTE_SUBTITLE};
        int[] to = {R.id.tv_title,R.id.tv_subtitle};
        return new SimpleAdapter(this,values,R.layout.list_view,from,to);
    }

    @NonNull
    private List<Map<String, String>> prepareContent(String value) {
        String[] strings = value.split("\n\n");
        List<Map<String, String>> values = new ArrayList<>();
        for (String str : strings) {
            Map<String, String> map = new HashMap<>();
            map.put(ATRIBUTE_TITLE, str);
            map.put(ATRIBUTE_SUBTITLE, ""+str.length());
            values.add(map);
        }
        return values;
    }

    private void init(){
        SharedPreferences preferences = getSharedPreferences(SHARED_PR_NAME, MODE_PRIVATE);
        String savedStr = preferences.getString(SHARED_PR_KEY,"");
        if (savedStr.isEmpty()){
            String str = getString(R.string.large_text);
            simpleAdapterContent.addAll(prepareContent(str));
            preferences.edit().putString(SHARED_PR_KEY,str).apply();
        } else {
            simpleAdapterContent.addAll(prepareContent(savedStr));
        }
    }
}
