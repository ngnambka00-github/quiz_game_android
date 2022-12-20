package com.example.quizme.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ListQuestion implements Parcelable {
    private List<Question> listQuestions;

    public ListQuestion() {
    }

    public ListQuestion(List<Question> listQuestions) {
        this.listQuestions = listQuestions;
    }

    public List<Question> getListQuestions() {
        return listQuestions;
    }

    public void setListQuestions(List<Question> listQuestions) {
        this.listQuestions = listQuestions;
    }

    protected ListQuestion(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ListQuestion> CREATOR = new Creator<ListQuestion>() {
        @Override
        public ListQuestion createFromParcel(Parcel in) {
            return new ListQuestion(in);
        }

        @Override
        public ListQuestion[] newArray(int size) {
            return new ListQuestion[size];
        }
    };
}
