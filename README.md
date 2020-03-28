# QR BarCode Scanner Kotlin

## Introduction:

This project is created in the intention to understand QR and Barcode Scanner and make it as
readily usable component to integrate it with any projects

----------------------------------------------------------------------------------------------------

## Installation:

Step 1: Add the maven repository to build.gradle

Inside allprojects -> repositories

```
    maven(){
                url "https://maven.google.com"
            }
```

Step 2: Apply the vision plugin to the top of the application level build.gradle file.

```
    implementation 'com.google.android.gms:play-services-vision:19.0.0'
```

----------------------------------------------------------------------------------------------------

## Configuration:

Step 1. Add the below in AndroidManifest file for getting the Camera Permission

```
<uses-permission android:name="android.permission.CAMERA" />
<uses-feature android:name="android.hardware.camera.autofocus" />

```

Step 2. Add the below metadata for QR / Barcode scanning

```
<meta-data
            android:name="com.google.android.gms.vision.DEPENDENCIES"
            android:value="barcode" />
```

----------------------------------------------------------------------------------------------------

## Coding Part - Initialization:


### Initialization

```
    private fun initializeValue() {
            surfaceView = findViewById(R.id.surfaceView)

            barcodeDetector = BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS).build()

            cameraSource = CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1024)
                .setAutoFocusEnabled(true)
                .build()
    }
```

### Surface Detection

```
    private fun surfaceCallback() {
            surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceCreated(holder: SurfaceHolder) {
                    try {
                        cameraSource.start(surfaceView.holder)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

                }
                override fun surfaceDestroyed(holder: SurfaceHolder) {
                }
            })
    }
```

### Code Scanning

```
    private fun codeScan() {
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
                try {
                    var barcodes: SparseArray<Barcode> = detections!!.detectedItems
                    if (barcodes.size() != 0) {
                        var intent: Intent = Intent(applicationContext, MainActivity::class.java)
                        intent.putExtra("barcodes", barcodes.valueAt(0))
                        setResult(Activity.RESULT_OK, intent)
                        finish()

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }
```

----------------------------------------------------------------------------------------------------

## Usage Part

### Get Camera permission from the user

```
    fun getCameraPermission() {
         if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
             ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.CAMERA),1)
         }else{
             Toast.makeText(applicationContext,"Camera Permission granted already..",Toast.LENGTH_SHORT).show()
         }
     }
```

### Trigger Action

```
    fun setButtonAction() {
        scanButton.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                var scanIntent: Intent = Intent(this@MainActivity, ScanBarCode::class.java)
                startActivityForResult(scanIntent, 0)
            }
        })
    }
```

### Handle Callback Results

```
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
```
