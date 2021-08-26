package com.example.todoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OnBoardActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout tab;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tab = findViewById(R.id.tab_dots);
        tab.setupWithViewPager(mViewPager, true);
        Log.d("ololo", " " + mViewPager.getCurrentItem());


    }


    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_on_board, container, false);
            int number = getArguments().getInt(ARG_SECTION_NUMBER);
            TextView textView = (TextView) rootView.findViewById(R.id.text_title);
            ImageView image = rootView.findViewById(R.id.image_view);
            Button btn = rootView.findViewById(R.id.button_finish);
            btn.setVisibility(View.INVISIBLE);

            switch (number) {
                case 0:
                    textView.setText("hello");
                    image.setImageResource(R.drawable.smile1);
                    rootView.setBackground(getResources().getDrawable(R.drawable.gradient_blue_red));
                    break;
                case 1:
                    textView.setText("how are you");
                    image.setImageResource(R.drawable.smile2);
                    rootView.setBackground(getResources().getDrawable(R.drawable.gradient_blue_green));
                    break;
                case 2:
                    textView.setText("what!");
                    image.setImageResource(R.drawable.smile3);
                    rootView.setBackground(getResources().getDrawable(R.drawable.gradient_green_red)  );
                    btn.setVisibility(View.VISIBLE);

                    break;
            }
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveState();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            });

            return rootView;
        }

        private void saveState() {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("settings", MODE_PRIVATE);
            sharedPreferences.edit().putBoolean("shown", true).apply();
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position);
        }


        @Override
        public int getCount() {
            return 3;
        }
    }


    public void onRightClick(View v) {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
        Log.d("ololo", "to right " + mViewPager.getCurrentItem());
    }

    public void onLeftClick(View v) {
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1, true);
        Log.d("ololo", "to left " + mViewPager.getCurrentItem());
    }


}
