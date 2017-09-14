package com.android.htc.greenroad.litepalbean;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/8/14.
 */

public class SupportDraftOrSubmit extends DataSupport implements Parcelable{

    private String lite_type;
    private int lite_ID;
    private String current_time;

    private SupportDetail mSupportDetail;
    private SupportScan mSupportScan;
    private SupportChecked mSupportChecked;
    private SupportMedia mSupportMedia;


    public SupportDraftOrSubmit() {
    }

    protected SupportDraftOrSubmit(Parcel in) {
        lite_type = in.readString();
        lite_ID = in.readInt();
        current_time = in.readString();
        mSupportDetail = in.readParcelable(SupportDetail.class.getClassLoader());
        mSupportScan = in.readParcelable(SupportScan.class.getClassLoader());
        mSupportChecked = in.readParcelable(SupportChecked.class.getClassLoader());
        mSupportMedia = in.readParcelable(SupportMedia.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lite_type);
        dest.writeInt(lite_ID);
        dest.writeString(current_time);
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

    @Override
    public String toString() {
        return "SupportDraftOrSubmit{" +
                "lite_type='" + lite_type + '\'' +
                ", lite_ID=" + lite_ID +
                ", current_time='" + current_time + '\'' +
                ", mSupportDetail=" + mSupportDetail +
                ", mSupportScan=" + mSupportScan +
                ", mSupportChecked=" + mSupportChecked +
                ", mSupportMedia=" + mSupportMedia +
                '}';
    }
}
