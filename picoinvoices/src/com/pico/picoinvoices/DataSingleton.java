package com.pico.picoinvoices;

public class DataSingleton
{
        private static DataSingleton mInstance = null;
     
        private String mString;
     
        private DataSingleton(){
            mString = "Hello";
        }
     
        public static DataSingleton getInstance(){
            if(mInstance == null)
            {
                mInstance = new DataSingleton();
            }
            return mInstance;
        }
     
        public String getString(){
            return this.mString;
        }
     
        public void setString(String value){
            mString = value;
        }
}
