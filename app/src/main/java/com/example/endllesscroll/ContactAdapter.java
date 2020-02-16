package com.example.endllesscroll;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private boolean isLoading;

    private int visibleThreshoId = 5;
    private int lastVisibleItem, totalItemCount;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private OnLoadMoreListener loadMoreListener;
    private Activity activity;
    private List<Contact> contacts;

//    Untuk menghandle event scroll pada RecyclerView kita buat didalam konstruktor adapter class.
//    Buat method linearLayoutManager pada RecyclerView. Logikanya, jika scroll sudah mencapai batas data
//    yang ditampilkan maka event progressBar dimunculkan.

    public ContactAdapter(RecyclerView recyclerView, List<Contact> contacts, Activity activity){
        this.contacts = contacts;
        this.activity = activity;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager)
                recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshoId)){
                    if (loadMoreListener != null){
                        loadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return contacts.get(position)== null? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    //Kita definisikan 2 buat konstata yaitu VIEW_TYPE_ITEM dan VIEW_TYPE_LOADING.
    // Kemudian instansiasi OnLoadMoreListenerdan kita buat singleton.
    public void setOnLoadMoreListener(OnLoadMoreListener listener){
        this.loadMoreListener = listener;
    }

    //Pada bagian RecyclerView.Holder, kita buat 2 buah logika jika viewType == VIEW_TYPE_ITEM maka membubungkan dengan layout list_contact,
    // sedangkan jika viewType == VIEW_TYPE_LOADING maka akan membubungkan dengan layout item_loading
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM){
            View view = LayoutInflater.from(activity).inflate(R.layout.list_contact, parent, false);
            return new UserViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity).inflate(R.layout.progress, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserViewHolder){
            Contact cont = contacts.get(position);
            UserViewHolder viewHolder = (UserViewHolder) holder;
            viewHolder.phone.setText(cont.getPhone());
            System.out.println(cont.getPhone());
            viewHolder.email.setText(cont.getEmail());
        }else if(holder instanceof LoadingViewHolder){
            LoadingViewHolder viewHolder = (LoadingViewHolder) holder;
            viewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return contacts == null ? 0 : contacts.size();
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder{
        public ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }
    public void setLoaded(){
        isLoading = false;
    }
    private class UserViewHolder extends RecyclerView.ViewHolder{
        public TextView phone, email;
        public UserViewHolder(@NonNull View view) {
            super(view);
            phone = view.findViewById(R.id.txt_phone);
            email = view.findViewById(R.id.txt_email);
        }
    }
}
