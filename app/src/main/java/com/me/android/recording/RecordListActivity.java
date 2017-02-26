package com.me.android.recording;

import android.app.Fragment;
import android.content.Intent;

/**
 * Created by ASUS on 2016/11/26.
 */
public class RecordListActivity extends SingleFragmentActivity
    implements RecordListFragment.Callbacks,RecordFragment.Callbacks{


    @Override
    protected Fragment createFragment(){
        return new RecordListFragment();
    }
    @Override
     protected int getLayoutResId(){
        return R.layout.activity_twopane;
    }
    @Override
    public void onRecordSelected(Record record){
        if (findViewById(R.id.detail_fragment_container) == null){
            Intent intent = RecordPagerActivity.newIntent(this,record.getId());
            startActivity(intent);
        }else {
            /*android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            android.support.v4.app.Fragment oldDetail =  fragmentManager.findFragmentById(R.id.detail_fragment_container);
            RecordFragment newDetail = RecordFragment.newInstance(record.getId());

            if (oldDetail != null){
                fragmentTransaction.remove(oldDetail);
            }
            fragmentTransaction.add(R.id.detail_fragment_container,newDetail);
            fragmentTransaction.commit();*/
            RecordFragment newDetail = RecordFragment.newInstance(record.getId());

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container,newDetail)
                    .commit();
        }

    }
    public void onRecordUpdated(Record record){
        RecordListFragment listFragment = (RecordListFragment)
                getFragmentManager()
                .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }



}
