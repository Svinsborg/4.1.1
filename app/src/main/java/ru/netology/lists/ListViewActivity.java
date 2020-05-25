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

    private final static String TITLE = "key_title";
    private final static String NUM = "key_num";
    private static String PARAM = "text";
    private SharedPreferences memberSH;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        memberSH = getSharedPreferences("myDB", MODE_PRIVATE);
        SharedPreferences.Editor saveTxt = memberSH.edit();
        String text = getString(R.string.large_text);
        saveTxt.putString(PARAM, text);
        saveTxt.apply();

        final SwipeRefreshLayout swipeRefresh = findViewById(R.id.refresh);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ListView list = findViewById(R.id.list);

        final List<Map<String, String>> values = prepareContent();

        final BaseAdapter listContentAdapter = createAdapter(values);

        list.setAdapter(listContentAdapter);



        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                values.remove(i);
                listContentAdapter.notifyDataSetChanged();
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                List<Map<String, String>> values = prepareContent();

                BaseAdapter listContentAdapter = createAdapter(values);

                list.setAdapter(listContentAdapter);
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    @NonNull
    private BaseAdapter createAdapter(List<Map<String, String>> values) {
        return new SimpleAdapter(this, values, R.layout.lay_out, new String[]{TITLE, NUM}, new int[]{R.id.textTop, R.id.textBottom});
    }


    @NonNull
    private List<Map<String,String>> prepareContent() {
        List<Map<String, String>> result =new ArrayList<>();
        String[] titles =  memberSH.getString(PARAM, "").split("\n\n");
        for (String title:titles) {
            Map<String, String> map = new HashMap<>();
            map.put(TITLE, title);
            map.put(NUM,title.length()+ "");
            result.add(map);
        }
        return result;
    }
}
