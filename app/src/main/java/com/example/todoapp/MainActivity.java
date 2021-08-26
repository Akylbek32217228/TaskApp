package com.example.todoapp;

import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TaskAdapter adapter;
    private List<Task> list;
    private int d;
    int sortBy = 0;
    long timeZone;
    MenuItem i;

    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    ImageView image;
    public static final long dayInMilliseconds = 86400000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeZone = getTimeZone();
        /*SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        boolean shown = preferences.getBoolean("shown", false);
        if ( !shown) {
            startActivity(new Intent(this, OnBoardActivity.class));
            finish();
        }*/

        Date date = new Date(System.currentTimeMillis());
        Toolbar toolbar = findViewById(R.id.toolbar);
        image = findViewById(R.id.image_importance);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initList();
        //initUserList();
        loadTask();
        //loadUser();
    }

    public void initList() {
        list = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter(list);
        recyclerView.setAdapter(adapter);

        adapter.setClickListener(new TaskAdapter.ClickListener() {
            @Override
            public void onClick(int pos) {
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                intent.putExtra("task", list.get(pos));
                startActivity(intent);
            }

            @Override
            public void onLongClick(final int pos) {
                Log.d("ololo", "delete!");
                builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Delete");
                builder.setCancelable(false);
                d = pos;
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        App.getInstance().getDataBase().taskDao().delete(list.get(pos));
                    }
                });
                builder.setNegativeButton("Cancel", null);
                alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void loadTask() {
        App.getInstance().getDataBase().taskDao().getAll().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> tasks) {
                list.clear();
                list.addAll(tasks);
                adapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if ( id == R.id.action_sort_) {
            if ( sortBy % 2 == 0) {
                item.setIcon(getResources().getDrawable(R.drawable.ic_arrow_downward));
                App.getInstance().getDataBase().taskDao().getAll().observe(this, new Observer<List<Task>>() {
                    @Override
                    public void onChanged(@Nullable List<Task> tasks) {
                        list.clear();
                        list.addAll(tasks);
                        adapter.notifyDataSetChanged();
                    }
                });
                ++sortBy;
            } else if ( sortBy % 2 != 0) {
                item.setIcon(getResources().getDrawable(R.drawable.ic_arrow_upward));
                App.getInstance().getDataBase().taskDao().getAllByAscending().observe(this, new Observer<List<Task>>() {
                    @Override
                    public void onChanged(@Nullable List<Task> tasks) {
                        list.clear();
                        list.addAll(tasks);
                        adapter.notifyDataSetChanged();
                    }
                });
                ++sortBy;
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        i = item;
        int id = item.getItemId();

        if (id == R.id.nav_all) {
            App.getInstance().getDataBase().taskDao().getAll().observe(this, new Observer<List<Task>>() {
                @Override
                public void onChanged(@Nullable List<Task> tasks) {
                    list.clear();
                    list.addAll(tasks);
                    adapter.notifyDataSetChanged();
                }
            });
        } else if (id == R.id.nav_urgent) {
            App.getInstance().getDataBase().taskDao().getAllUrgent().observe(this, new Observer<List<Task>>() {
                @Override
                public void onChanged(@Nullable List<Task> tasks) {
                    list.clear();
                    list.addAll(tasks);
                    adapter.notifyDataSetChanged();
                }
            });
        } else if (id == R.id.nav_important) {
            App.getInstance().getDataBase().taskDao().getAllImportant().observe(this, new Observer<List<Task>>() {
                @Override
                public void onChanged(@Nullable List<Task> tasks) {
                    list.clear();
                    list.addAll(tasks);
                    adapter.notifyDataSetChanged();
                }
            });
        } else if (id == R.id.nav_common) {
            App.getInstance().getDataBase().taskDao().getAllCommon().observe(this, new Observer<List<Task>>() {
                @Override
                public void onChanged(@Nullable List<Task> tasks) {
                    list.clear();
                    list.addAll(tasks);
                    adapter.notifyDataSetChanged();
                }
            });
        } else if (id == R.id.old_tasks) {
            App.getInstance().getDataBase().taskDao().getAllOldTAsks(System.currentTimeMillis() + timeZone,
                    dayInMilliseconds, timeZone).observe(this, new Observer<List<Task>>() {
                @Override
                public void onChanged(@Nullable List<Task> tasks) {
                    list.clear();
                    list.addAll(tasks);
                    adapter.notifyDataSetChanged();
                }
            });
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private long getTimeZone() {
        long current = 0;
        String[] ids = TimeZone.getAvailableIDs();
        for (int i = 0; i < ids.length; ++i) {
            Log.d("ololo", ids[i]);
            if ( ids[i].equals(TimeZone.getDefault().getID())) {
                TimeZone tz;
                tz = TimeZone.getTimeZone(ids[i]);
                current = tz.getOffset(Calendar.ZONE_OFFSET);
                Log.d("ololo", "offset : " + current);
                break;
            }
        }
        return current;
    }
}
