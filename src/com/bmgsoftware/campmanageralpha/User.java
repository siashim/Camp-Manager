package com.bmgsoftware.campmanageralpha;

import java.util.ArrayList;
import java.util.List;

public class User {
    String mWeb,mUserName, mPassword, mName, mLastName, mEmail,
     mProductCode, mUserid, mAltitude, mLongitude, mGroupNumber, mMessage; 
    String mConversation[];
    String mConversationUser[];
    int mActiveConversation;
    boolean isLogged;
    List<String> user = new ArrayList<String>();
    public User() //Default constructor
    {
    	mWeb = "";
        mUserName = "bmgalarm";
        mPassword = "";
        mName = "";
        mLastName = "";
        mEmail = "";
        mProductCode = "";
        mUserid = "";
        mAltitude = "";
        mLongitude = "";
        mGroupNumber = "";
        mMessage = "";
        isLogged = false;
        mConversation = new String[3]; // stores the whole conversation
        mConversationUser = new String[3]; // stores the username of the receiver
        mActiveConversation = 0;
    }
    public User(String response) {
      String wordCount = response;
      int count = wordCount.length() - wordCount.replace("#"," ").length();
      for(int i=0; i<12; i++) {
          String[] hash = wordCount.split("#");
          user.add(hash[i]);
      }
      mWeb = user.get(0);
      mUserName = user.get(1);
      mPassword = user.get(2);
      mName = user.get(3);
      mLastName = user.get(4);
      mEmail = user.get(5);
      mProductCode = user.get(6);
      mUserid = user.get(7);
      mAltitude = user.get(8);
      mLongitude = user.get(9);
      mGroupNumber = user.get(10);
      mMessage = user.get(11);
      isLogged = false;
      mConversation = new String[3]; // stores the whole conversation
      mConversationUser = new String[3]; // stores the username of the receiver
      mActiveConversation = 0;
    }
    
 
    public int getmActiveConversation()
    {
    	return mActiveConversation;
    }
    public void setmActiveConversation(int i)
    {
    	mActiveConversation = i;
    }
    public String getmConversation(int i)
    {
    	return mConversation[i];
    }
    public void setmConversation(int i, String con)
    {
    	mConversation[i] = con;
    }
    
    public String getmConversationUser(int i)
    {
    	return mConversationUser[i];
    }
    public void setmConversationUser(int i, String user)
    {
    	mConversationUser[i] = user;
    }
    
    public boolean getisLogged()
    {
    	return isLogged;
    }
    public void setisLogged(boolean b)
    {
    	isLogged = b;
    }
    public String getmUserName() {
        return mUserName;
    }
    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }
    public String getmPassword() {
        return mPassword;
    }
    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }
    public String getmName() {
        return mName;
    }
    public void setmName(String mName) {
        this.mName = mName;
    }
    public String getmLastName() {
        return mLastName;
    }
    public void setmLastName(String mLastName) {
        this.mLastName = mLastName;
    }
    public String getmEmail() {
        return mEmail;
    }
    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }
    public String getmProductCode() {
        return mProductCode;
    }
    public void setmProductCode(String mProductCode) {
        this.mProductCode = mProductCode;
    }
    public String getmUserid() {
        return mUserid;
    }
    public void setmUserid(String mUserid) {
        this.mUserid = mUserid;
    }
    public String getmAltitude() {
        return mAltitude;
    }
    public void setmAltitude(String mAltitude) {
        this.mAltitude = mAltitude;
    }
    public String getmLongitude() {
        return mLongitude;
    }
    public void setmLongitude(String mLongitude) {
        this.mLongitude = mLongitude;
    }
    public String getmGroupNumber() {
        return mGroupNumber;
    }
    public void setmGroupNumber(String mGroupNumber) {
        this.mGroupNumber = mGroupNumber;
    }
    public String getmMessage() {
        return mMessage;
    }
    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }
    
    public String toString()
    {
    	
    	return mUserName+mPassword+ mName+mLastName+ mEmail+
    	        mProductCode+mUserid+mAltitude+ mLongitude+mGroupNumber+ mMessage;
    }
}