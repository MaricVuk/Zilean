package com.example.zilean

import com.example.zilean.data.ProductMetadata
import com.example.zilean.data.getDatabaseBuilder
import com.example.zilean.data.getRoomDatabase
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import platform.AVFoundation.*
import platform.Foundation.NSSelectorFromString
import platform.UIKit.*
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_global_queue
import platform.darwin.dispatch_get_main_queue
import platform.darwin.DISPATCH_QUEUE_PRIORITY_DEFAULT

class IosScanner : Scanner {
    override fun scanBarcode(onProductFound: (ProductMetadata?, String) -> Unit) {
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        if (rootViewController != null) {
            val scannerVC = BarcodeScannerViewController(onProductFound)
            rootViewController.presentViewController(scannerVC, animated = true, completion = null)
        }
    }
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
class BarcodeScannerViewController(
    private val onProductFound: (ProductMetadata?, String) -> Unit,
) : UIViewController(nibName = null, bundle = null), AVCaptureMetadataOutputObjectsDelegateProtocol {

    private var captureSession: AVCaptureSession? = null
    private var previewLayer: AVCaptureVideoPreviewLayer? = null

    override fun viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = UIColor.blackColor
        
        setupCloseButton()
        checkPermissionAndSetup()
    }

    private fun setupCloseButton() {
        val closeButton = UIButton.buttonWithType(UIButtonTypeSystem)
        closeButton.setTitle("Zatvori", forState = UIControlStateNormal)
        closeButton.addTarget(this, action = NSSelectorFromString("dismissScanner"), forControlEvents = UIControlEventTouchUpInside)
        closeButton.setFrame(platform.CoreGraphics.CGRectMake(20.0, 40.0, 100.0, 40.0))
        closeButton.tintColor = UIColor.whiteColor
        view.addSubview(closeButton)
    }

    private fun checkPermissionAndSetup() {
        when (AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)) {
            AVAuthorizationStatusAuthorized -> {
                setupCaptureSession()
            }
            AVAuthorizationStatusNotDetermined -> {
                AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
                    dispatch_async(dispatch_get_main_queue()) {
                        if (granted) {
                            setupCaptureSession()
                        } else {
                            failed("Pristup kameri je odbijen.")
                        }
                    }
                }
            }
            else -> {
                failed("Pristup kameri je onemogucen u podesavanjima.")
            }
        }
    }

    private fun setupCaptureSession() {
        val videoCaptureDevice = AVCaptureDevice.defaultDeviceWithMediaType(AVMediaTypeVideo)
        if (videoCaptureDevice == null) {
            failed("Uredjaj ne podrzava kameru.")
            return
        }

        val session = AVCaptureSession()
        captureSession = session

        val videoInput = try {
            AVCaptureDeviceInput.deviceInputWithDevice(videoCaptureDevice, null)
        } catch (e: Exception) {
            null
        }

        if ((videoInput != null) && session.canAddInput(videoInput)) {
            session.addInput(videoInput)
        } else {
            failed("Neuspelo pokretanje kamere.")
            return
        }

        val metadataOutput = AVCaptureMetadataOutput()
        if (session.canAddOutput(metadataOutput)) {
            session.addOutput(metadataOutput)
            metadataOutput.setMetadataObjectsDelegate(this, queue = dispatch_get_main_queue())
            metadataOutput.metadataObjectTypes = listOf(
                AVMetadataObjectTypeQRCode, 
                AVMetadataObjectTypeEAN13Code, 
                AVMetadataObjectTypeEAN8Code, 
                AVMetadataObjectTypeCode128Code
            )
        } else {
            failed("Neuspelo procesiranje bar-kodova.")
            return
        }

        val layer = AVCaptureVideoPreviewLayer(session = session)
        layer.frame = view.layer.bounds
        layer.videoGravity = AVLayerVideoGravityResizeAspectFill
        view.layer.insertSublayer(layer, atIndex = 0u)
        previewLayer = layer

        startSession()
    }

    private fun startSession() {
        val session = captureSession ?: return
        if (!session.isRunning()) {
            dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT.toLong(), 0u)) {
                session.startRunning()
            }
        }
    }

    private fun stopSession() {
        val session = captureSession ?: return
        if (session.isRunning()) {
            dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT.toLong(), 0u)) {
                session.stopRunning()
            }
        }
    }

    @ObjCAction
    fun dismissScanner() {
        stopSession()
        dismissViewControllerAnimated(true, completion = null)
    }

    override fun captureOutput(
        output: AVCaptureOutput,
        didOutputMetadataObjects: List<*>,
        fromConnection: AVCaptureConnection
    ) {
        if (didOutputMetadataObjects.isNotEmpty()) {
            val metadataObject = didOutputMetadataObjects.first() as? AVMetadataMachineReadableCodeObject
            if (metadataObject != null) {
                val barcodeValue = metadataObject.stringValue
                if (barcodeValue != null) {
                    stopSession()
                    dismissViewControllerAnimated(true) {
                        CoroutineScope(Dispatchers.Main).launch {
                            val db = getRoomDatabase(getDatabaseBuilder())
                            val metadata = db.foodDao().getProductByBarcode(barcodeValue)
                            onProductFound(metadata, barcodeValue)
                        }
                    }
                }
            }
        }
    }

    private fun failed(message: String) {
        val ac = UIAlertController.alertControllerWithTitle(
            "Skeniranje nije podrzano",
            message,
            UIAlertControllerStyleAlert
        )
        ac.addAction(
            UIAlertAction.actionWithTitle("OK", UIAlertActionStyleDefault) {
                dismissScanner()
            }
        )
        presentViewController(ac, animated = true, completion = null)
        captureSession = null
    }

    override fun viewWillAppear(animated: Boolean) {
        super.viewWillAppear(animated)
        startSession()
    }

    override fun viewWillDisappear(animated: Boolean) {
        super.viewWillDisappear(animated)
        stopSession()
    }

    override fun viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        previewLayer?.frame = view.layer.bounds
    }
}

actual fun getScanner(): Scanner = IosScanner()
