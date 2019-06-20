package com.goodscalculator.Servers;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.goodscalculator.R;

import java.util.ArrayList;

public class ServersAct extends AppCompatActivity implements View.OnClickListener {

    private ServersCollection serversCollection = new ServersCollection();
    private RecyclerView recyclerViewServer;
    private ServerAdapter serverAdapter;

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
        if (serversCollection.addServers(getString(R.string.host),  getString(R.string.serversLink))) {
            Log.i("ServersAct", "Список серверов загруден");
        } else {
            Log.i("ServersAct", "Ошибка загрузки серверов");
            dialogServers();
        }
        displayRecyclerViewServers();
    }

    private void dialogServers() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ошибка загрузки серверов");

        builder.setPositiveButton("Повторить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (serversCollection.addServers(getString(R.string.host), getString(R.string.serversLink))) {
                    Log.i("ServersAct", "Список серверов загружен");
                } else {
                    Log.i("ServersAct", "Ошибка загрузки серверов");
                    dialogServers();
                }
            }
        });
        builder.setNegativeButton("Назад", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
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
                    saveServer(server);
                    ServersAct.super.finish();
                }
            });
        }
    }

    private void saveServer(Server server) {
        server.saveServerToFile(this);
    }

    @Override
    public void onClick(View v) {

    }
}
