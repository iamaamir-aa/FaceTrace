package com.android.studentattendancerecorder.DialogueBox;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.android.studentattendancerecorder.Model.ClassAndSubjectDetails;
import com.android.studentattendancerecorder.Model.StudentsDetail;
import com.android.studentattendancerecorder.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddStudent extends DialogFragment implements View.OnClickListener {
    private View v1;
    private String KEY,CLASS_ID,EMP_ID;
    private String studentNameString, enrollmentNumberString;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;

    public AddStudent(String KEY) {
        this.KEY = KEY;
    }

    @Override
    public void onDismiss(final DialogInterface dialog) {
        super.onDismiss(dialog);
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) parentFragment).onDismiss(dialog);
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        //Initialization
        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("USERS");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        v1 = getLayoutInflater().inflate(R.layout.addstudentdialogue, null);
        Button addStudent = v1.findViewById(R.id.buttonAddStudent);
        Button cancelAddStudent = v1.findViewById(R.id.buttonCancelAddStudent);


        Bundle args = getArguments();
        CLASS_ID=args.getString("classId");
        EMP_ID=args.getString("empId");
        System.out.println(CLASS_ID+EMP_ID);

        builder.setView(v1);

        //Listeners
        addStudent.setOnClickListener(this);
        cancelAddStudent.setOnClickListener(this);


        return builder.create();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAddStudent: {
                EditText studentName = v1.findViewById(R.id.newStudentName);
                EditText enrollmentNumber = v1.findViewById(R.id.newEnrollmentNumber);
                studentNameString = studentName.getText().toString();
                enrollmentNumberString = enrollmentNumber.getText().toString();

                if((studentNameString.equals("") || enrollmentNumberString.equals(""))){
                    Toast.makeText(getContext(),"Provide all fields",Toast.LENGTH_SHORT).show();
                }else{
                    DatabaseReference newRef = databaseRef.child(EMP_ID).child("CLASS").child(CLASS_ID).child("students").child(enrollmentNumberString);
                    StudentsDetail newStudent = new StudentsDetail(studentNameString, enrollmentNumberString, enrollmentNumberString, new ArrayList<>());
                    databaseRef.child(EMP_ID).child("CLASS").child(CLASS_ID).child("students").child(enrollmentNumberString).setValue(newStudent);
                    dismiss();
                }



                break;
            }

            case R.id.buttonCancelAddStudent: {
                dismiss();
                break;
            }
        }
    }
}
