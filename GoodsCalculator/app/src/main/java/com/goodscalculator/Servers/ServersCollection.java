package com.goodscalculator.Servers;

import android.util.Log;

import com.goodscalculator.JSONhelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ServersCollection extends ArrayList<Server> {

    private ArrayList<Server> serversCollection = new ArrayList<Server>();
    private final String TAG = this.getClass().getSimpleName();

    public ArrayList<Server> getServersCollection() {
        return serversCollection;
    }

    public boolean addServers(String host, String serversLink) {
        try {
            JSONhelper json = new JSONhelper();
            json.execute("http://192.168.43.36:777/servers/get_servers.php");
            String JSONstr = json.get();
            Log.i("JSONstr", JSONstr);
            if (JSONstr.equals("false\n")) {
                return false;
            } else {
                Gson gson = new Gson();
                serversCollection = gson.fromJson(JSONstr, ServersCollection.class);
                Log.i(TAG, "addServers, serverJSON: " + JSONstr);
                return true;
            }
        } catch (InterruptedException e) {
            Log.e(TAG, "addServers, InterruptedException: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (ExecutionException e) {
            Log.e(TAG, "addServers, ExecutionException: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
