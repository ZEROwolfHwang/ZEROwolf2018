package com.android.htc.greenroad.litepalbean;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/8/14.
 */

public class SupportDraftOrSubmit extends DataSupport implements Parcelable{

    private String username;
    private String lite_type ; //GlobalManager.TYPE_DRAFT_LITE
    private int lite_ID;
    private String current_time;
    private String current_date;
    private int isPass;

    private SupportDetail mSupportDetail;
    private SupportScan mSupportScan;
    private SupportChecked mSupportChecked;
    private SupportMedia mSupportMedia;


    public SupportDraftOrSubmit() {
    }

    protected SupportDraftOrSubmit(Parcel in) {
        username = in.readString();
        lite_type = in.readString();
        lite_ID = in.readInt();
        current_time = in.readString();
        current_date = in.readString();
        isPass = in.readInt();
        mSupportDetail = in.readParcelable(SupportDetail.class.getClassLoader());
        mSupportScan = in.readParcelable(SupportScan.class.getClassLoader());
        mSupportChecked = in.readParcelable(SupportChecked.class.getClassLoader());
        mSupportMedia = in.readParcelable(SupportMedia.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(lite_type);
        dest.writeInt(lite_ID);
        dest.writeString(current_time);
        dest.writeString(current_date);
        dest.writeInt(isPass);
        dest.writeParcelable(mSupportDetail, flags);
        dest.writeParcelable(mSupportScan, flags);
        dest.writeParcelable(mSupportChecked, flags);
        dest.writeParcelable(mSupportMedia, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SupportDraftOrSubmit> CREATOR = new Creator<SupportDraftOrSubmit>() {
        @Override
        public SupportDraftOrSubmit createFromParcel(Parcel in) {
            return new SupportDraftOrSubmit(in);
        }

        @Override
        public SupportDraftOrSubmit[] newArray(int size) {
            return new SupportDraftOrSubmit[size];
        }
    };

    @Override
    public String toString() {
        return "SupportDraftOrSubmit{" +
                "username='" + username + '\'' +
                ", lite_type='" + lite_type + '\'' +
                ", lite_ID=" + lite_ID +
                ", current_time='" + current_time + '\'' +
                ", current_date='" + current_date + '\'' +
                ", isPass=" + isPass +
                ", mSupportDetail=" + mSupportDetail +
                ", mSupportScan=" + mSupportScan +
                ", mSupportChecked=" + mSupportChecked +
                ", mSupportMedia=" + mSupportMedia +
                '}';
    }

    public String getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date(String current_date) {
        this.current_date = current_date;
    }

    public int getIsPass() {
        return isPass;
    }

    public void setIsPass(int isPass) {
        this.isPass = isPass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLite_type() {
        return lite_type;
    }

    public void setLite_type(String lite_type) {
        this.lite_type = lite_type;
    }

    public int getLite_ID() {
        return lite_ID;
    }

    public void setLite_ID(int lite_ID) {
        this.lite_ID = lite_ID;
    }

    public String getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
    }
    public SupportScan getSupportScan() {
        return DataSupport.where("lite_ID = ?", String.valueOf(lite_ID)).findFirst(SupportScan.class);
    }

    public void setSupportScan(SupportScan supportScan) {
        mSupportScan = supportScan;
    }

    public SupportDetail getSupportDetail() {
        return DataSupport.where("lite_ID = ?", String.valueOf(lite_ID)).findFirst(SupportDetail.class);
    }

    public void setSupportDetail(SupportDetail supportDetail) {
        mSupportDetail = supportDetail;
    }

    public SupportChecked getSupportChecked() {
        return DataSupport.where("lite_ID = ?", String.valueOf(lite_ID)).findFirst(SupportChecked.class);
    }

    public SupportMedia getSupportMedia() {
        return DataSupport.where("lite_ID = ?", String.valueOf(lite_ID)).findFirst(SupportMedia.class);
    }

    public void setSupportMedia(SupportMedia supportMedia) {
        mSupportMedia = supportMedia;
    }

    public void setSupportChecked(SupportChecked supportChecked) {
        mSupportChecked = supportChecked;
    }


}
