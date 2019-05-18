package com.goodscalculator.Servers;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.goodscalculator.R;

import java.util.ArrayList;

public class ServersAct extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerViewServer;
    private ServersCollection serversCollection = new ServersCollection();
    private String host, serversLink;
    private ServerAdapter serverAdapter;
    private SharedPreferences sPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servers);

        recyclerViewServer = (RecyclerView) findViewById(R.id.recyclerViewServers);
        recyclerViewServer.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        host = getString(R.string.host);
        serversLink = getString(R.string.serversLink);
        serversCollection.addServers(host, serversLink);
        displayRecyclerViewServers();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void displayRecyclerViewServers() {
        serverAdapter = new ServersAct.ServerAdapter(serversCollection.getServersCollection());
        recyclerViewServer.setAdapter(serverAdapter);
    }

    private class ServerAdapter extends RecyclerView.Adapter<ServersAct.ServerHolder> {
        private ArrayList<Server> servers;

        public ServerAdapter(ArrayList<Server> serversCollection) {
            this.servers = serversCollection;
        }

        @NonNull
        @Override
        public ServersAct.ServerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.list_item_server, viewGroup, false);
            return new ServersAct.ServerHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ServersAct.ServerHolder serverHolder, int i) {
            Server server = servers.get(i);
            serverHolder.bindCrime(server);
        }

        @Override
        public int getItemCount() {
            return servers.size();
        }
    }

    private class ServerHolder extends RecyclerView.ViewHolder {

        private TextView mServerNameTextView;
        private TextView mServerIPaddressTextView;
        private ImageButton mBtnServerChange;

        public ServerHolder(@NonNull View itemView) {
            super(itemView);
            mServerNameTextView = (TextView) itemView.findViewById(R.id.txtRVServerName);
            mServerIPaddressTextView = (TextView) itemView.findViewById(R.id.txtRVServerIPaddress);
            mBtnServerChange = (ImageButton) itemView.findViewById(R.id.btnRVChangeServer);

        }

        public void bindCrime(final Server server) {

            mServerNameTextView.setText(server.getName());
            mServerIPaddressTextView.setText(server.getIp_address());
            mBtnServerChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sPref = getSharedPreferences("mySettings", MODE_PRIVATE);
                    SharedPreferences.Editor ed = sPref.edit();
                    String JSONserver = server.getJSONfromServer();
                    ed.putString("Server", JSONserver);
                    ed.putString("ProductsCollection", "[]");
                    ed.commit();
                    Log.i("ServerAct, Server saved", JSONserver);
                    ServersAct.super.finish();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {

    }
}
