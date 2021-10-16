package com.android.uccapp;

import android.Manifest;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.uccapp.model.ConfigUtility;
import com.android.uccapp.model.CourseForRegistration;
import com.android.uccapp.model.GradeBook;
import com.android.uccapp.model.RegisteredStudents;
import com.android.uccapp.model.Student;
import com.android.uccapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterFragment extends Fragment {
    private Toolbar mToolbar;
    private Button mRegisterButton;
    private InputStream mImageStream;
    Bitmap mProfileBitmap;
    private TextView mTotalCreditTextView;
    private static final String ARG_USER = "com.android.uccapp.userId";
    private User mUser;
    private String mUserLevel;
    private Student mStudent;
    private InputStream mInputStream;
    private RegisteredStudents mRegisteredStudents;
    private List<CourseForRegistration> mCourseForRegistration;
    private String mDepartmentName;
    private String mDepartmentNameWithoutSpace;
    private LinearLayout mCompulsoryCoursesLinearLayout;
    private LinearLayout mCompulsoryCourseCodeLinearLayout;
    private LinearLayout mCompulsoryCourseTitleLinearLayout;
    private LinearLayout mCompulsoryCreditHoursLinearLayout;
    private AssetManager mAssetManager;
    private ImageView mProfileImageView;
    public static RegisterFragment newInstance(User user){
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_USER, user);
        RegisterFragment registerFragment = new RegisterFragment();
        registerFragment.setArguments(bundle);
        return registerFragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser =  (User) getArguments().getSerializable(ARG_USER);
        mUserLevel = null;
        mRegisteredStudents = new RegisteredStudents();
        mCourseForRegistration = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        mToolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle("Register");
        mRegisterButton = (Button) view.findViewById(R.id.btnRegister);
        mProfileImageView = (ImageView) view.findViewById(R.id.ivStudentProfile);
        mCompulsoryCourseCodeLinearLayout = (LinearLayout) view.findViewById(R.id.comLLCourseCode);
        mCompulsoryCourseTitleLinearLayout = (LinearLayout) view.findViewById(R.id.comLLCourseTitle);
        mCompulsoryCreditHoursLinearLayout = (LinearLayout) view.findViewById(R.id.comLLCreditHours);
        mTotalCreditTextView = (TextView) view.findViewById(R.id.tvTotalCredit);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        handleRegisterButton(mRegisterButton);

        return view;
    }

    private void init(){
        final String[] depCode = mUser.getRegistrationNumber().split("_");
        DatabaseReference depRef = FirebaseDatabase.getInstance().getReference("departments");
        depRef.child(depCode[1]).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    final String departName = data.getValue().toString().replaceAll("\\s+", "");
                    mDepartmentName = data.getValue().toString();
                    mDepartmentNameWithoutSpace = departName;
                    String[] regCode = mUser.getRegistrationNumber().split("_");
                    DatabaseReference courseRegRef = FirebaseDatabase.getInstance().getReference("RegistrableCourses");
                    DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference("student");
                    studentRef.child(mUser.getRegistrationNumber()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Student student =  dataSnapshot.getValue(Student.class);
                            mStudent = student;
                            mUserLevel = student.getLevel().split("0")[0];
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    courseRegRef.child(departName).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            int total = 0;
                            for (DataSnapshot data:dataSnapshot.getChildren()){
                                CourseForRegistration registrableCourses = (CourseForRegistration) data.getValue(CourseForRegistration.class);
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                if (registrableCourses.getCourseCode().contains(depCode[1]+mUserLevel)){
                                    String[] creditHours = registrableCourses.getNumOfCreditHours().split(" ");
                                    mCourseForRegistration.add(registrableCourses);
                                    total += Integer.parseInt(creditHours[0]);
                                    TextView courseCodeTextView = new TextView(getContext());
                                    TextView courseTitleTextView = new TextView(getContext());
                                    TextView courseCreditHoursTextView = new TextView(getContext());
                                    courseCodeTextView.setTextSize(12);
                                    courseTitleTextView.setTextSize(12);
                                    courseCreditHoursTextView.setTextSize(12);
                                    Log.d("WORK", registrableCourses.getCourseCode());
                                    courseCodeTextView.setText(registrableCourses.getCourseCode());
                                    courseTitleTextView.setText(registrableCourses.getCourseTitle());
                                    courseCreditHoursTextView.setText(registrableCourses.getNumOfCreditHours());
                                    mTotalCreditTextView.setText(String.valueOf(total));

                                    courseCodeTextView.setLayoutParams(layoutParams);
                                    courseTitleTextView.setLayoutParams(layoutParams);
                                    courseCreditHoursTextView.setLayoutParams(layoutParams);
                                    mCompulsoryCourseCodeLinearLayout.addView(courseCodeTextView);
                                    mCompulsoryCourseTitleLinearLayout.addView(courseTitleTextView);
                                    mCompulsoryCreditHoursLinearLayout.addView(courseCreditHoursTextView);
                                } /*else {
                                    TextView noPreviewTextView = new TextView(getContext());
                                    noPreviewTextView.setLayoutParams(layoutParams);
                                    noPreviewTextView.setText("Sorry, no courses to preview!");
                                    noPreviewTextView.setTextSize(18);
                                    noPreviewTextView.setTextColor(Color.rgb(35, 63, 143));
                                    noPreviewTextView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                                    mCompulsoryCourseTitleLinearLayout.addView(noPreviewTextView);
                                    mRegisterButton.setEnabled(false);
                                    break;
                                }*/

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.d("CODE", depCode[1]);
    }

    private void handleRegisterButton(final Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printDocument(button);
                ConfigUtility.createFirebaseUtil("registeredStudents", getActivity());
                DatabaseReference registeredStudentsRef = ConfigUtility.mFirebaseReference;
                ConfigUtility.createFirebaseUtil("studentsGradeBook", getActivity());
                DatabaseReference studentsGradeBookRef = ConfigUtility.mFirebaseReference;
                final RegisteredStudents registeredStudent = new RegisteredStudents();
                final GradeBook gradeBook = new GradeBook();
                List<HashMap<String, String>> courseCodesAndGrades = new ArrayList<>();
                HashMap<String, String> courseCodeAndGrade = new HashMap<>();

                List<String> courseCodes = new ArrayList<>();
                for (int i = 0; i < mCompulsoryCreditHoursLinearLayout.getChildCount(); i++){
                    TextView codeTV = (TextView) mCompulsoryCourseCodeLinearLayout.getChildAt(i);
                    courseCodes.add(codeTV.getText().toString());
                    registeredStudent.setRegisteredCourseCode(courseCodes);
                    courseCodeAndGrade.put(codeTV.getText().toString(), "");
                    registeredStudentsRef.child(mDepartmentNameWithoutSpace).child(mUser.getRegistrationNumber()).setValue(registeredStudent);
                };
                courseCodesAndGrades.add(courseCodeAndGrade);
                gradeBook.setCourseCodesAndGrades(courseCodesAndGrades);
                gradeBook.setRegistrationNumber(mUser.getRegistrationNumber());
                studentsGradeBookRef.child(gradeBook.getRegistrationNumber()).setValue(gradeBook);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createRegistrationDocument(String path, List<CourseForRegistration> courseForRegistration) {
        if (new File(path).exists()){
            new File(path).delete();
        }
        try {
            final Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            document.setPageSize(PageSize.A4);
            document.addAuthor("Copyright UCC Portal 2021");
            document.addCreationDate();
            document.addCreator("Betty+Anny+Gustav");
            BaseColor primaryColor = new BaseColor(35, 63, 143, 253);
            BaseFont baseFont = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);
            float titleFontSize = 16.0f;
            float valueFontSize = 12.0f;
            float headingFontSize = 32.0f;

            Font headingFont = new Font(baseFont, headingFontSize, Font.BOLD, primaryColor);
            Font titleFont = new Font(baseFont, titleFontSize, Font.BOLD, primaryColor);
            Font valueFont = new Font(baseFont, valueFontSize, Font.NORMAL, BaseColor.BLACK);
            AssetManager assetManager = getActivity().getAssets();
            InputStream inputStream = null;
            try {
                 inputStream = assetManager.open("images/ucclogo.png");
            } catch (IOException e){
                e.printStackTrace();
            }
            Bitmap  bmp = BitmapFactory.decodeStream(inputStream);
            Bitmap resizedBmp = Bitmap.createScaledBitmap(bmp, 80, 80, false);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resizedBmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

            Image signature;
            signature = Image.getInstance(stream.toByteArray());


            addItemToDocument(document, "UNIVERSITY OF CAPE COAST", Element.ALIGN_CENTER, headingFont);
            addImageToDocument(document, signature, Element.ALIGN_CENTER);
            addItemToDocument(document, "REGISTRATION FORM", Element.ALIGN_CENTER, titleFont);
            addLineSeparator(document);
            addItemJustifiedToDocument(document, "NAME", mStudent.getLastName() + ", " + mStudent.getFirstName(), titleFont, valueFont);
            addItemJustifiedToDocument(document, "REG. No.", mStudent.getStudentsId().replace("_", "/"), titleFont, valueFont);
            addItemJustifiedToDocument(document, "LEVEL", mStudent.getLevel(), titleFont, valueFont);
            addItemJustifiedToDocument(document, "PROGRAM", mStudent.getProgramme(), titleFont, valueFont);
            addItemJustifiedToDocument(document, "MAJOR", mStudent.getMajor(), titleFont, valueFont);
            addLineSeparator(document);
            addItemToDocument(document, "COURSES REGISTERED FOR " + " ", Element.ALIGN_CENTER, titleFont);
             addItemLeftCenterRight(document, "CODE", "TITLE", "CREDIT", titleFont, titleFont, titleFont);
             addLineSeparator(document);
             addLineSpace(document);
             String[] creditHours = null;
             int total = 0;
             for (int i = 0; i < courseForRegistration.size(); i++){
                 addItemLeftCenterRight(document, courseForRegistration.get(i).getCourseCode(), courseForRegistration.get(i).getCourseTitle(), courseForRegistration.get(i).getNumOfCreditHours(), valueFont, valueFont, valueFont);
                 creditHours = courseForRegistration.get(i).getNumOfCreditHours().split(" ");
                total += Integer.parseInt(creditHours[0]);
             }
             addLineSeparator(document);
             addLineSpace(document);
             addItemJustifiedToDocument(document, "TOTAL CREDIT: ", total + " hours", titleFont, valueFont);
             addLineSeparator(document);
             addLineSpace(document);

             Picasso.with(getActivity())
                     .load(mStudent.getPhotoUrl())
                     .resize(80, 80)
                     .centerCrop()
                     .into(mProfileImageView);
           /*  Drawable d  = mProfileImageView.getDrawable();
             BitmapDrawable profBDrawable = (BitmapDrawable) d;
             Bitmap bitmap = profBDrawable.getBitmap();
            InputStream profileImageStream = new FileInputStream(mStudent.getPhotoUrl());
            mProfileBitmap = BitmapFactory.decodeStream(profileImageStream);
            Bitmap resizedProfileBmp = Bitmap.createScaledBitmap(bitmap, 40, 40, false);
            ByteArrayOutputStream profileStream = new ByteArrayOutputStream();
            resizedProfileBmp.compress(Bitmap.CompressFormat.JPEG, 100, profileStream);
            Image profileImageSignature;
            profileImageSignature = Image.getInstance(profileStream.toByteArray());
            addImageToDocument(document, profileImageSignature, Element.ALIGN_CENTER);*/
            document.close();
            printRegistrationDocument(mStudent.getStudentsId());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addImageToDocument(Document document, Image signature, int alignCenter) throws DocumentException {
//        signature.setAbsolutePosition(400f, 150f);
        signature.scalePercent(100f);
        signature.setAlignment(alignCenter);
        document.add(signature);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void printRegistrationDocument(String registrationNumber) {
        PrintManager printManager = (PrintManager)  getActivity().getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapter(getActivity(), PathGeneratorObject.getPathName(getActivity()) + mStudent.getStudentsId()+".pdf", registrationNumber);
        printManager.print(""+mStudent.getStudentsId(), printDocumentAdapter,  new PrintAttributes.Builder().build());
    }

    private void addItemLeftCenterRight(Document document, String textLeft, String textCenter, String textRight, Font titleFont, Font titleFont1, Font titleFont2) throws DocumentException {
        Chunk textLeftChunk = new Chunk(textLeft, titleFont);
        Chunk textCenterChunk = new Chunk(textCenter, titleFont1);
        Chunk textRightChunk = new Chunk(textRight, titleFont2);
        Paragraph paragraph = new Paragraph(textLeftChunk);
        paragraph.add(new Chunk(new VerticalPositionMark()));
        paragraph.add(textCenterChunk);
        paragraph.add(new Chunk(new VerticalPositionMark()));
        paragraph.add(textRightChunk);
        document.add(paragraph);
    }

    private void addItemJustifiedToDocument(Document document, String textToLeft, String textToRight, Font titleFont, Font valueFont) throws DocumentException {
        Chunk leftTextChunk = new Chunk(textToLeft, titleFont);
        Chunk rightTextChunk = new Chunk(textToRight, valueFont);
        Paragraph paragraph = new Paragraph(leftTextChunk);
        paragraph.add(new Chunk(new VerticalPositionMark()));
        paragraph.add(rightTextChunk);
        document.add(paragraph);
    }

    private void addLineSeparator(Document document) throws DocumentException {
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
        addLineSpace(document);
    }

    private void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));
    }

    private void addItemToDocument(Document document, String text, int aligment, Font font) throws DocumentException {
        Chunk chunk = new Chunk(text, font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(aligment);
        document.add(paragraph);
    }

    private void printDocument(final Button button){
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                createRegistrationDocument(PathGeneratorObject.getPathName(getActivity()) + mStudent.getStudentsId()+".pdf", mCourseForRegistration);
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
    }
}
