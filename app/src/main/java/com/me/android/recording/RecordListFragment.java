package com.me.android.recording;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ASUS on 2016/11/26.
 */
public class RecordListFragment extends Fragment {
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private RecyclerView mRecordRecyclerView;
    private RecordAdapter mAdapter;
    private boolean mSubtitleVisible;
    private Callbacks mCallbacks;


    public interface Callbacks{
        void onRecordSelected(Record record);
    }
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mCallbacks = (Callbacks)activity;
    }
    @Override
    public void onDetach(){
        super.onDetach();
        mCallbacks = null;
    }



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_record_list,container,false);
        mRecordRecyclerView = (RecyclerView)view.findViewById(R.id.record_recycler_view);
        mRecordRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(savedInstanceState != null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        updateUI();
        return view;

    }
    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }
    @Override
    public void onSaveInstanceState(Bundle outState){
          super.onSaveInstanceState(outState);
           outState.putBoolean(SAVED_SUBTITLE_VISIBLE,mSubtitleVisible);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
        super.onCreateOptionsMenu(menu,menuInflater);
        menuInflater.inflate(R.menu.fragment_record_list,menu);
        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.menu_item_new_record:
                Record record = new Record();
                RecordLab.get(getActivity()).addRecord(record);
                /*Intent intent = RecordPagerActivity
                        .newIntent(getActivity(),record.getId());
                startActivity(intent);*/
                updateUI();
                mCallbacks.onRecordSelected(record);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void updateSubtitle(){
        RecordLab recordLab = RecordLab.get(getActivity());
        int recordCount = recordLab.getRecords().size();
        String subtitle = getString(R.string.subtitle_format,recordCount);
        if(!mSubtitleVisible){
            subtitle = null;
        }
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);

    }


    public void updateUI(){
        RecordLab recordLab = RecordLab.get(getActivity());
        List<Record> records = recordLab.getRecords();
        if (mAdapter == null){

        mAdapter = new RecordAdapter(records);
        mRecordRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setRecords(records);
            mAdapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

    private class RecordHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitleTextView;
        private TextView mPhoneTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;
        private Record mRecord;
        public RecordHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView)
                    itemView.findViewById(R.id.list_item_record_title_text_view);
            mPhoneTextView = (TextView)
                    itemView.findViewById(R.id.list_item_record_phone_text_view);
            mDateTextView = (TextView)
                    itemView.findViewById(R.id.list_item_record_date_text_view);
            mSolvedCheckBox = (CheckBox)
                    itemView.findViewById(R.id.list_item_record_solved_check_box);

        }
        public void bindRecord(Record record){
            mRecord = record;
            mTitleTextView.setText(mRecord.getTitle());
            mPhoneTextView.setText(mRecord.getPhone());
            mDateTextView.setText(mRecord.getDate().toString());
            mSolvedCheckBox.setChecked(mRecord.isSolved());
        }
        @Override
        public void onClick(View V){
           /*Intent intent = RecordPagerActivity.newIntent(getActivity(),mRecord.getId());
            startActivity(intent);*/
            mCallbacks.onRecordSelected(mRecord);
    }
    }
    private class RecordAdapter extends RecyclerView.Adapter<RecordHolder>{
        private List<Record> mRecords;
        public RecordAdapter(List<Record> records){
            mRecords = records;
        }

     @Override
        public RecordHolder onCreateViewHolder(ViewGroup parent,int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_record,
                    parent,false);
            return new RecordHolder(view);
        }

        @Override
        public void onBindViewHolder(RecordHolder holder,int position){
            Record record = mRecords.get(position);
            holder.bindRecord(record);
        }
        @Override
        public int getItemCount(){
            return mRecords.size();
        }
        public void setRecords(List<Record> recordList){
            mRecords = recordList;
        }

    }

}
