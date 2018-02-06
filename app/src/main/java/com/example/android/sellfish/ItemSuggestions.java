package com.example.android.sellfish;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by android on 2/5/18.
 */

@SuppressLint("ParcelCreator")
public class ItemSuggestions implements SearchSuggestion {

    public static final Parcelable.Creator<ItemSuggestions> CREATOR = new Parcelable.ClassLoaderCreator<ItemSuggestions>() {
        @Override
        public ItemSuggestions createFromParcel(Parcel source, ClassLoader loader) {
            return null;
        }

        @Override
        public ItemSuggestions createFromParcel(Parcel source) {
            return new ItemSuggestions(source);
        }

        @Override
        public ItemSuggestions[] newArray(int size) {
            return new ItemSuggestions[size];
        }
    };

    private String item;
    private boolean IsHistory = false;

    public ItemSuggestions(String item) {//constructor
        this.item = item;
    }


    public ItemSuggestions(Parcel source) {//constructor
        this.item = source.readString();
        this.IsHistory = source.readInt() != 0;
    }


    @Override
    public String getBody() {
        return item;
    }

    public boolean isHistory() {//setter
        return IsHistory;
    }

    public void setHistory(boolean history) {//getter
        IsHistory = history;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(item);
        dest.writeInt(IsHistory ? 1 : 0);
    }
}
