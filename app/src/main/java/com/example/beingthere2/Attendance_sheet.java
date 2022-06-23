package com.example.beingthere2;

class Attendance_sheet {
    String MAD,ST,KRAI,CC,DM;

    public Attendance_sheet(String MAD, String ST, String KRAI, String CC, String DM) {
        this.MAD=MAD;
        this.ST=ST;
        this.KRAI=KRAI;
        this.CC=CC;
        this.DM=DM;

    }

    public String getCn() {
        return MAD;
    }



    public String getDbms() {
        return ST;
    }



    public String getIsee() {
        return CC;
    }

    public String getSdl() {
        return DM;
    }



    public String getSepm() {
        return KRAI;
    }
}
