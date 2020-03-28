package com.example.qrbarcodescanner

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.vision.barcode.Barcode

class MainActivity : AppCompatActivity() {


    //region Variable Initialization
    lateinit var scannedText:TextView
    lateinit var scanButton: Button
    //endregion

    //region Activity Life Cycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeView()
        getCameraPermission()
        setButtonAction()

    }
    //endregion

    //region Initialization
    fun initializeView() {
        scannedText = findViewById(R.id.scannedText)
        scanButton = findViewById(R.id.scan_button)
    }
    fun getCameraPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.CAMERA),1)
        }else{
            Toast.makeText(applicationContext,"Camera Permission granted already..",Toast.LENGTH_SHORT).show()
        }
    }
    //endregion

    //region Trigger Action & Call Back results
    fun setButtonAction() {
        scanButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                var scanIntent: Intent = Intent(this@MainActivity, ScanBarCode::class.java)
                startActivityForResult(scanIntent, 0)
            }
        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == Activity.RESULT_OK){
            if(data!=null){
                var barcode:Barcode = data.getParcelableExtra("barcodes")
                scannedText.post(object: Runnable {
                    override fun run() {
                        scannedText.setText(barcode.displayValue)
                    }

                })
            }
        }
    }
    //endregion
}
