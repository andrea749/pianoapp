package com.sample.pianoguide

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.sample.pianoguide.MainActivity.Companion.ENABLE_BT
import com.sample.pianoguide.ui.theme.PianoguideTheme

class MainActivity : ComponentActivity() {
//    TODO: lazy
    var blManager: BluetoothManager? = null
    var blAdapter: BluetoothAdapter? = null
    private var bluetoothLeScanner: BluetoothLeScanner? = null
    private var scanning = false
    private val handler = Handler()

    // Stops scanning after 10 seconds.
    private val SCAN_PERIOD: Long = 10000

//    private val leDeviceListAdapter = BaseAdapter()
    // Device scan callback.
//    private val leScanCallback: ScanCallback = object : ScanCallback() {
//        override fun onScanResult(callbackType: Int, result: ScanResult) {
//            super.onScanResult(callbackType, result)
//            leDeviceListAdapter.addDevice(result.device)
//            leDeviceListAdapter.notifyDataSetChanged()
//        }
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        blManager = ContextCompat.getSystemService(this, BluetoothManager::class.java)
        Log.d("zzz", "blManager = $blManager")
        blAdapter = blManager?.adapter
        bluetoothLeScanner = blAdapter?.bluetoothLeScanner
        var buttonText: String = "send json"
        val btEnable: () -> Unit = {
            blAdapter?.let {
                Log.d("zzz", "adapter not null")
                if (!it.isEnabled) {
                    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(
                        this,
                        enableBtIntent,
                        ENABLE_BT,
                        null
                    )
                } else {
                    setContent {
                        ShowBlueToothList()
                    }
                }

            }
        }


        setContent {
            PianoguideTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BluetoothButton(
                        btEnable,
                        buttonText,
                        modifier = Modifier
                            .wrapContentSize(),
                    )
                }
            }
        }
    }

//    private fun scanLeDevice() {
//        if (!scanning) { // Stops scanning after a pre-defined scan period.
//            handler.postDelayed({
//                scanning = false
//                bluetoothLeScanner.stopScan(leScanCallback)
//            }, SCAN_PERIOD)
//            scanning = true
//            bluetoothLeScanner.startScan(leScanCallback)
//        } else {
//            scanning = false
//            bluetoothLeScanner.stopScan(leScanCallback)
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ENABLE_BT &&
            resultCode == RESULT_OK &&
            data != null &&
            data.data != null) {
            // scan
//            setContent {
//                ShowBlueToothList()
//            }
        }
    }
    companion object {
        const val ENABLE_BT = 9
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun BluetoothButton(enableBtClick: () -> Unit, buttonText: String, modifier: Modifier = Modifier) {
    Button(
        onClick = enableBtClick)
    {
        Text(text = buttonText)
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonPreview() {
    PianoguideTheme {
        BluetoothButton({}, "send json")
    }
}