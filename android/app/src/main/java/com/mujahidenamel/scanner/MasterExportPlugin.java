package com.mujahidenamel.scanner;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import androidx.activity.result.ActivityResult;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@CapacitorPlugin(name = "MasterExport")
public class MasterExportPlugin extends Plugin {
    @PluginMethod
    public void save(PluginCall call) {
        String data = call.getString("data");
        if (data == null) {
            call.reject("No master data was supplied.");
            return;
        }

        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        intent.putExtra(Intent.EXTRA_TITLE, "mujahid-master-data.json");
        getActivity().runOnUiThread(() -> {
            try {
                startActivityForResult(call, intent, "documentCreated");
            } catch (Exception error) {
                call.reject("The Android file picker could not be opened.", error);
            }
        });
    }

    @ActivityCallback
    private void documentCreated(PluginCall call, ActivityResult result) {
        if (call == null) {
            return;
        }

        Intent resultIntent = result.getData();
        Uri destination = resultIntent == null ? null : resultIntent.getData();
        if (result.getResultCode() != Activity.RESULT_OK || destination == null) {
            JSObject response = new JSObject();
            response.put("cancelled", true);
            call.resolve(response);
            return;
        }

        String data = call.getString("data");
        if (data == null) {
            call.reject("Master data is no longer available. Please export again.");
            return;
        }

        try (OutputStream output = getContext().getContentResolver().openOutputStream(destination, "wt")) {
            if (output == null) {
                call.reject("The selected document could not be opened.");
                return;
            }
            output.write(data.getBytes(StandardCharsets.UTF_8));
            JSObject response = new JSObject();
            response.put("cancelled", false);
            call.resolve(response);
        } catch (Exception error) {
            call.reject("Master data could not be exported.", error);
        }
    }
}
