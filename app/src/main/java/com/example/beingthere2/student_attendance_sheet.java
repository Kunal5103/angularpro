package com.example.beingthere2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class student_attendance_sheet extends AppCompatActivity {

    public static int count,P,A;
    float average= (float) 0.0;
    TextView t;
    String p1,p2,p3,p4,p5;
    String student_id;
    final ArrayList dates = new ArrayList<>();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference dbAttendance;
    ListView listView;
    DatabaseReference dbStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance_sheet);

        count = 0;
        P = 0;
        A = 0;

        t = findViewById(R.id.textView3);
        listView = findViewById(R.id.list);
        Bundle bundle = getIntent().getExtras();
        student_id = bundle.getString("sid");
        t.setText(student_id);

        dbStudent = ref.child("Student");
        dbStudent.child(student_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String no = dataSnapshot.child("rollno").getValue().toString();
                String classname = dataSnapshot.child("classname").getValue().toString();
                display(no, classname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void display(final String rollno,String classname){

        dates.clear();
        dates.add("Date          "+"MAD  "+"ST  "+"KRAI  "+"CC   "+ "DM   ");
        //Toast.makeText(this, dates.toString(), Toast.LENGTH_SHORT).show();
        dbAttendance = ref.child("attendance").child(classname);
        dbAttendance.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                        if(dsp.child(rollno).hasChild("MAD")){
                            p1=dsp.child(rollno).child("MAD").getValue().toString().substring(0,1);
                        }else
                            p1="--";
                        if(dsp.child(rollno).hasChild("ST")){
                            p2=dsp.child(rollno).child("ST").getValue().toString().substring(0,1);
                        }else
                            p2="--";
                        if(dsp.child(rollno).hasChild("KRAI")){
                            p3=dsp.child(rollno).child("KRAI").getValue().toString().substring(0,1);
                        }else
                            p3="--";
                        if(dsp.child(rollno).hasChild("CC")){
                            p4=dsp.child(rollno).child("CC").getValue().toString().substring(0,1);
                        }else
                            p4="--";
                        if(dsp.child(rollno).hasChild("DM")){
                            p5=dsp.child(rollno).child("DM").getValue().toString().substring(0,1);
                        }else
                            p5="--";


                        dates.add(dsp.getKey() + "    " + p1 + "     " + p2 + "     " + p3 + "     " + p4 + "      " + p5); //add result into array list

                        if (p1.equals("P") || p2.equals("P") || p3.equals("P") || p4.equals("P") || p5.equals("P") ) {

                            P++;
                            count++;
                        }
                        if (p1.equals("A") || p2.equals("A") || p3.equals("A") || p4.equals("A") || p5.equals("A") ) {
                            A++;
                            count++;
                        }
                        list1(dates, P, count, A);
                    }
                }catch (Exception e){
                    Toast.makeText(student_attendance_sheet.this,"Something went wrong...Please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void list1(ArrayList studentlist,int P,int count,int A){

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, studentlist);
        listView.setAdapter(adapter);
        try {

            average =(float)((P*100)/count);
            String avg=Float.toString(average);
            t.setText("Your Attendance is :"+avg+"%");
            if(average>=75)
                t.setTextColor(Color.GREEN);
            if(average<75){
                t.setTextColor(Color.RED);
            }

        }
        catch (Exception e){e.printStackTrace();}
    }
}
 