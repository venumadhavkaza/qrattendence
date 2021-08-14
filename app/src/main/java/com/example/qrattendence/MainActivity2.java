package com.example.qrattendence;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DatabaseReference;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity2 extends AppCompatActivity {
    private DatabaseReference mDatabase;
    SurfaceView surfaceView;
    TextView txtBarcodeValue;

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnAction;
    boolean isEmail = false;
    int backpress = 0;
    ArrayList<String> attendee = new ArrayList<String>();
    ArrayList<String> errorcodes= new ArrayList<String>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4267B2")));
        setContentView(R.layout.activity_main2);
        initViews();

    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);

        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.btnAction);
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for(int i=0;i<attendee.size();i++){
                //  Toast.makeText(MainActivity2.this, attendee.get(i), Toast.LENGTH_SHORT).show();
                //}
                if (attendee.size() > 0) {
                    createExcel();
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        });
    }
    public void createExcel(){
        File myDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/vitap");
        //File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/abcabcabc");
        myDirectory.mkdir();
        Intent i3 = getIntent();
        // String  filename = "hitesting";
        String filename = i3.getStringExtra("slot");
        Toast.makeText(this, String.valueOf(attendee.size()), Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, filename, Toast.LENGTH_SHORT).show();
        File filepath =new File ( Environment.getExternalStorageDirectory()+"/vitap/"+ filename+".xls") ;
        Toast.makeText(this, filepath.getAbsolutePath(), Toast.LENGTH_SHORT).show();

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        String cellnamee[] = {"REGno","MAIL","SLOT","TIMESTAMP"};

        HSSFRow bodyrow = sheet.createRow(0);
        HSSFCell bodycell;
        for(int i=0;i<4;i++){
            bodycell = bodyrow.createCell(i);
            bodycell.setCellValue(cellnamee[i]);
        }
        Toast.makeText(this, attendee.get(0), Toast.LENGTH_SHORT).show();
        for(int i=0;i< attendee.size();i++){
            String reg = attendee.get(i).substring(0,9);
            String ema = attendee.get(i).substring(9, attendee.get(i).indexOf("#")-1);
            String slo = attendee.get(i).substring(attendee.get(i).indexOf("#")+1, attendee.get(i).indexOf("$")-1);
            String ts = attendee.get(i).substring(attendee.get(i).indexOf("$")+1, attendee.get(i).length()-1);
            String temp[] ={reg,ema,slo,ts};
            bodyrow = sheet.createRow(i+1);
            for(int j=0;j<4;j++){
                bodycell = bodyrow.createCell(j);
                bodycell.setCellValue(temp[j]);
            }

        }
        try {
            if (!filepath.exists()) {
            filepath.createNewFile();

            }
            FileOutputStream fileOutputStream = new FileOutputStream(filepath);
            workbook.write(fileOutputStream);
            fileOutputStream.close();

        }catch(Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();

        }
        Intent excelIntent = new Intent(MainActivity2.this,MainexcelshowActivity6.class);
        excelIntent.putExtra("excelpath",filepath.toString());
        startActivity(excelIntent);


    }
    private void initialiseDetectorsAndSources() {

        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(MainActivity2.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(MainActivity2.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    txtBarcodeValue.post(new Runnable() {
                        @Override
                        public void run() {
                            String s = barcodes.valueAt(0).displayValue;


                            if(!attendee.contains(s)&&lbmatches(s)){
                                attendee.add(s);
                                txtBarcodeValue.setText(s);
                            }if(!attendee.contains(s)&&thmatches(s)){
                                attendee.add(s);
                                txtBarcodeValue.setText(s);
                            }
                            if(!thmatches(s)&&!lbmatches(s)&&!errorcodes.contains(s)){
                                errorcodes.add(s);
                                alerter("Invalid QR CODE");
                            }
                            

                        }
                    });
                }

            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }
    public void onBackPressed(){


        if (backpress>1) {


            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        }else{
            backpress = (backpress + 1);
            Toast.makeText(getApplicationContext(), " Attendence will not be saved if you exit now \n press again to exit ", Toast.LENGTH_LONG).show();
        }
    }

    public boolean thmatches(String s){
        Pattern p = Pattern.compile("^[0-9]{2}[a-z]{3}[0-9]{4}:[a-z]*\\.[0-9]{2}[a-z]{3}[0-9]{4}@vitap.ac.in#[A-Z]{3}[0-9]{4}:[A-Z]{1}\\+[A-Z]{2}\\$[A-Z]{1}[a-z]{2} [A-Z]{1}[a-z]{2} [0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2} GMT\\+[0-9]{2}:[0-9]{2} [0-9]{4}");
        // Pattern p = Pattern.compile("^[0-9]{2}[a-z]{3}[0-9]{4}:[a-z]*\\.[0-9]{2}[a-z]{3}[0-9]{4}@vitap.ac.in#[A-Z]{3}[0-9]{4}:L[0-9]{2}\\+L[0-9]{2}\\$[A-Z]{1}[a-z]{2} [A-Z]{1}[a-z]{2} [0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2} GMT\\+[0-9]{2}:[0-9]{2} [0-9]{4}");
        Matcher m = p.matcher(s);
        return m.matches();


    }

    public boolean lbmatches(String s){

        Pattern p = Pattern.compile("^[0-9]{2}[a-z]{3}[0-9]{4}:[a-z]*\\.[0-9]{2}[a-z]{3}[0-9]{4}@vitap.ac.in#[A-Z]{3}[0-9]{4}:L[0-9]{2}\\+L[0-9]{2}\\$[A-Z]{1}[a-z]{2} [A-Z]{1}[a-z]{2} [0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2} GMT\\+[0-9]{2}:[0-9]{2} [0-9]{4}");
        Matcher m = p.matcher(s);
        return m.matches();


    }

    public void alerter(String msg){
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(MainActivity2.this);

        // Set the message show for the Alert time
        builder.setMessage(msg);

        // Set Alert Title
        builder.setTitle("Alert !");

        // Set Cancelable false
        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder
                .setPositiveButton(
                        "OK",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // When the user click yes button
                                // then app will close
                                
                                dialog.cancel();

                            }
                        });
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();

    }

}



