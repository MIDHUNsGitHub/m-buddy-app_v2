package com.mycompany.mbuddyapp;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import java.nio.charset.StandardCharsets;

@CapacitorPlugin(name = "BluetoothLe")
public class BluetoothLePlugin extends Plugin {

    // Internal UUID configuration definitions to match your ESP32
    private static final String SERVICE_UUID = "4fafc201-1fb5-459e-8fcc-c5c9c331914b";
    private static final String CHARACTERISTIC_UUID = "beb5483e-36e1-4688-b7f5-ea07361b26a8";

    @PluginMethod
    public void send(PluginCall call) {
        String data = call.getString("data");
        String deviceId = call.getString("deviceId"); 

        if (data == null) {
            call.reject("Value required: Data string missing");
            return;
        }

        try {
            // Convert to clean UTF-8 raw bytes preserving symbols like °
            byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
            
            // Calling native write routine (Assumes your background manager architecture is running)
            // Note: Replace "bluetoothManager" if your app uses a different connection tracker variable name
            bluetoothManager.writeCharacteristic(deviceId, SERVICE_UUID, CHARACTERISTIC_UUID, bytes);

            JSObject ret = new JSObject();
            ret.put("success", true);
            call.resolve(ret);
        } catch (Exception e) {
            call.reject("Write execution failed: " + e.getMessage());
        }
    }
}
