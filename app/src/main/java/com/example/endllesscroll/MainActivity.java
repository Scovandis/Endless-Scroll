package com.example.endllesscroll;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private List<Contact> contactList;
    private ContactAdapter adapter;
    private Random random;

    //Terakhir, kita buat koding untuk MainActivity-nya. dalam method onCreate() panggil setOnLoadMoreListener()
    // dan menampilkan data baru setelah memanggil event progressBar didalam onLoadMore(). pada kode MainActivity,
    // logikanya adalah kita buat dulu dummy data sebanyak 20 kemudian saat user menscroll sampai data ke-20 maka akan
    // muncul progressBar dengan waktu delay 5000ms. Lalu, saat user menscroll sampai data terakhir maka aplikasi akan
    // memberi pesan “Loading data completed”. Selebihnya, silahkan diset/diatur sesuai kebutuhan waktu delay dan
    // data yang akan ditampilkan pertama sebanyak berapa pada user.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contactList = new ArrayList<>();
        random = new Random();

        for (int a = 0; a<20; a++){
            Contact contact = new Contact();
            contact.setPhone("0240902");
            contact.setEmail("kurnia" + a + "@gmail.com");
            contactList.add(contact);
        }
        RecyclerView recyclerView =(RecyclerView) findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ContactAdapter(recyclerView, contactList, this);
        recyclerView.setAdapter(adapter);

        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (contactList.size() <= 20){
                    contactList.add(null);
                    adapter.notifyItemInserted(contactList.size() -1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            contactList.remove(contactList.size() -1);
                            adapter.notifyItemRemoved(contactList.size());


                            int index = contactList.size();
                            int end =  index+10;
                            for (int i = index; i<end; i++){
                                Contact contact = new Contact();
                                contact.setPhone("0888989");
                                contact.setEmail("kurnia" + i + "@gamil.com");
                                contactList.add(contact);
                            }
                            adapter.notifyDataSetChanged();
                            adapter.setLoaded();
                        }
                    }, 5000);
                }else{
                    Toast.makeText(MainActivity.this, "Loading data Complete", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
