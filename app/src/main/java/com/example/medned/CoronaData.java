package com.example.medned;

public class CoronaData {
    private  String mPositiveCase;
    private String mNegativeCase;
    private String mTimeInMillisecond;
    private String mDeathCase;



    public CoronaData(String PositiveCase, String NegativeCase , String timeInMilliseconds, String DeathCase){
        mPositiveCase= PositiveCase;
        mNegativeCase = NegativeCase;
        mTimeInMillisecond= timeInMilliseconds;
        mDeathCase = DeathCase;
    }


    public String getPositiveCaseName(){
        return mPositiveCase;
    }
    public String getNegativeCase(){ return mNegativeCase; }
    public String getTimeInMilliseconds(){
        return mTimeInMillisecond;
    }
    public String getDeathCase(){ return mDeathCase;}


}
