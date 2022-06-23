package com.example.beingthere2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class admin_attendanceSheet extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ListView listView;
    String classes;
    EditText date;
    ArrayList Userlist = new ArrayList<>();
    ArrayList Studentlist = new ArrayList<>();

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference dbAttendance;
    DatabaseReference dbStudent;
    String required_date;
    String p1,p2,p3,p4,p5,rollno1;
    ImageView download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_attendance_sheet);

        listView = findViewById(R.id.list);
        date = findViewById(R.id.date);
        download=findViewById(R.id.download);
        download.setVisibility(View.INVISIBLE);

        Spinner spinner =findViewById(R.id.spinner5);
        spinner.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("FY-MCA");
        categories.add("SY-MCA");
        categories.add("TY-MCA");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        classes = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void viewlist1(View view){
        AlphaAnimation buttonClick = new AlphaAnimation(2F, 0.9F);
        view.startAnimation(buttonClick);

        download.setVisibility(View.VISIBLE);

        //Toast.makeText(this, classes, Toast.LENGTH_SHORT).show();
        Userlist.clear();
        dbStudent = ref.child("Student");
        dbStudent.orderByChild("classname").equalTo(classes).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Userlist.add(dsp.child("rollno").getValue().toString());
                }
                display_list(Userlist);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
            }

        });
    }

    public void display_list(final ArrayList userlist){

        Studentlist.clear();
        required_date = date.getText().toString();
        final TextView subjectlist=findViewById(R.id.subjectlist);
        dbAttendance = ref.child("attendance").child(classes);
        dbAttendance.child(required_date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                subjectlist.setText("Roll-no  "+"MAD  "+"ST  "+"KRAI  "+"CC   "+ "DM   ");
                for (Object rollno : userlist) {
                    rollno1=dataSnapshot.child(rollno.toString()).getKey();
                    if(dataSnapshot.child(rollno.toString()).hasChild("MAD")){
                        p1=dataSnapshot.child(rollno.toString()).child("MAD").getValue().toString().substring(0,1);
                    }else
                        p1="--";
                    if(dataSnapshot.child(rollno.toString()).hasChild("ST")){
                        p2=dataSnapshot.child(rollno.toString()).child("ST").getValue().toString().substring(0,1);
                    }else
                        p2="--";
                    if(dataSnapshot.child(rollno.toString()).hasChild("KRAI")){
                        p3=dataSnapshot.child(rollno.toString()).child("KRAI").getValue().toString().substring(0,1);
                    }else
                        p3="--";
                    if(dataSnapshot.child(rollno.toString()).hasChild("CC")){
                        p4=dataSnapshot.child(rollno.toString()).child("CC").getValue().toString().substring(0,1);
                    }else
                        p4="--";
                    if(dataSnapshot.child(rollno.toString()).hasChild("DM")){
                        p5=dataSnapshot.child(rollno.toString()).child("DM").getValue().toString().substring(0,1);
                    }else
                        p5="--";

                    Studentlist.add( rollno1+"    "+p1+"     "+p2+"     "+p3+"     "+p4+"      "+p5); //add result into array list
                }
                list2(Studentlist);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
            }

        });
    }

    public void list2(ArrayList studentlist){
        Collections.sort(studentlist);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, studentlist);
        listView.setAdapter(adapter);
    }

    public boolean Download(View view){
        AlphaAnimation buttonClick = new AlphaAnimation(2F, 0.9F);
        view.startAnimation(buttonClick);

        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.w("FileUtils", "Storage not available or read only");
            return false;
        }
        boolean success = false;


        Workbook wb = new HSSFWorkbook();

        Cell c=null;

        Sheet sheet1 = null;
        sheet1 = wb.createSheet("myOrder");
        Row row = sheet1.createRow(0);

        c = row.createCell(0);
        c.setCellValue("Roll-No");
        //c.setCellStyle(cs);

        c = row.createCell(1);
        c.setCellValue("MAD");
        //c.setCellStyle(cs);

        c = row.createCell(2);
        c.setCellValue("ST");

        c = row.createCell(3);
        c.setCellValue("KRAI");
        //c.setCellStyle(cs);

        c = row.createCell(4);
        c.setCellValue("CC");

        c = row.createCell(5);
        c.setCellValue("DM");

        if(listView!=null)
        {
            int size = Userlist.size();
            for(int i=1;i<=size;i++)
            {
                /* Create row to save employee info.*/
                Row row1 = sheet1.createRow(i);

                row1.createCell(0).setCellValue(rollno1);
                row1.createCell(1).setCellValue(p1);
                row1.createCell(2).setCellValue(p2);
                row1.createCell(3).setCellValue(p3);
                row1.createCell(4).setCellValue(p4);
                row1.createCell(5).setCellValue(p5);

            }
        }

        sheet1.setColumnWidth(0, (15 * 300));
        sheet1.setColumnWidth(1, (15 * 300));
        sheet1.setColumnWidth(2, (15 * 300));
        sheet1.setColumnWidth(3, (15 * 300));
        sheet1.setColumnWidth(4, (15 * 300));
        sheet1.setColumnWidth(5, (15 * 300));


        File file = new File(admin_attendanceSheet.this.getExternalFilesDir(null), "myexcel.xls");
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            Log.w("FileUtils", "Writing file" + file);
            success = true;
            if(success == true)
                Toast.makeText(this, "File created success", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }

        return success;
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
}
