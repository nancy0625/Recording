package com.me.android.recording;

import java.util.Date;
import java.util.UUID;

/**
 * Created by ASUS on 2016/11/26.
 */
public class Record {
    private UUID mId;
    private String mTitle;
    private String mPhone;
    private Date mDate;
    private boolean mSolved;




    public Record(){
        //创建唯一标识符
        this(UUID.randomUUID());

    }
    public Record(UUID uuid){
        mId = uuid;
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }


    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getPhone() {
        return mPhone;
    }
     ;
    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getPhotoFilename(){
        return "IMG_" + getId().toString() + ".jpg";
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }
}
