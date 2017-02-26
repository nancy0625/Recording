package com.me.android.recording;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.util.Date;
import java.util.UUID;

/**
 * Created by ASUS on 2016/11/26.
 */
public class RecordFragment extends Fragment {
    private static final String ARG_RECORD_ID = "record_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO=1;
    private Record mRecord;
    private File mPhotoFile;
    private EditText mTitleField;
    private EditText mPhoneField;
    private Button mFoundButton;
    private Button mPhotoButton;
    private ImageView mPhotoView;
    private Button mTellingButton;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Callbacks mCallbacks;


    public interface Callbacks{
        void onRecordUpdated(Record record);
    }



    public static RecordFragment newInstance(UUID recordId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_RECORD_ID,recordId);
        RecordFragment fragment = new RecordFragment();
        fragment.setArguments(args);
        return fragment;
    }//新加的一个方法
      @Override
      public void onAttach(Activity activity){
          super.onAttach(activity);
          mCallbacks = (Callbacks)activity;
      }


    private void updatePhotoView(){
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        }else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(),getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }





    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID recordId = (UUID)getArguments().getSerializable(ARG_RECORD_ID);
        mRecord = RecordLab.get(getActivity()).getRecord(recordId);
        mPhotoFile = RecordLab.get(getActivity()).getPhotoFile(mRecord);
    }

    @Override
    public void onPause(){
        super.onPause();
        RecordLab.get(getActivity()).updateRecord(mRecord);
    }
    //新加的
   @Override
   public void onDetach(){
       super.onDetach();
       mCallbacks = null;
   }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_record,container,false);
        mTitleField = (EditText)v.findViewById(R.id.record_title);
        mTitleField.setText(mRecord.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                  mRecord.setTitle(s.toString());
                updatedRecord();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mPhoneField = (EditText)v.findViewById(R.id.record_phone);
        mPhoneField .setText(mRecord.getPhone());
        mPhoneField .addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                   mRecord.setPhone(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
        mFoundButton = (Button)v.findViewById(R.id.record_found);
        mFoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.baidu.com"));
                startActivity(intent);
            }

        });
        mTellingButton = (Button)v.findViewById(R.id.record_tel);
        mTellingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:10086"));
                startActivity(intent);
            }
        });

        mDateButton = (Button)v.findViewById(R.id.record_date);
        mDateButton.setText(mRecord.getDate().toString());
        mDateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentManager fragmentManager = getFragmentManager();
                DatePickerFragment datePickerFragment = DatePickerFragment
                        .newInstance(mRecord.getDate());
                datePickerFragment.setTargetFragment(RecordFragment.this,
                        REQUEST_DATE);
                datePickerFragment.show(fragmentManager,DIALOG_DATE);
            }
        });


        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.record_solved);
        mSolvedCheckBox.setChecked(mRecord.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener () {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //set the Record's solved properly
                        mRecord.setSolved(isChecked);
                    updatedRecord();
                }

        });

        mPhotoButton = (Button)v.findViewById(R.id.record_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        PackageManager packageManager = getActivity().getPackageManager();
        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);
        
        if (canTakePhoto){
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        }

        mPhotoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivityForResult(captureImage,REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView)v.findViewById(R.id.picture);

        updatePhotoView();
        return v;
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent intent){
        if (resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_DATE){
            Date date = (Date)intent.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mRecord.setDate(date);
            updatedRecord();
            mDateButton.setText(mRecord.getDate().toString());
        }

        if (requestCode == REQUEST_PHOTO){
            updatedRecord();
            updatePhotoView();
        }


    }
    private void updatedRecord(){
        RecordLab.get(getActivity()).updateRecord(mRecord);
        mCallbacks.onRecordUpdated(mRecord);
    }
   
}
