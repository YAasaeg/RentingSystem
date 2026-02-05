package com.example.rentingsystem.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentingsystem.R;
import com.example.rentingsystem.data.RentingDao;
import com.example.rentingsystem.model.User;

import java.util.List;

public class ChatListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    private RentingDao rentingDao;
    private int currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        rentingDao = new RentingDao(getContext());
        SharedPreferences sp = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        currentUserId = sp.getInt("user_id", -1);
        ViewGroup containerLayout = (ViewGroup) view; // The root ScrollView
        containerLayout.removeAllViews(); // Remove hardcoded stuff
        
        recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        containerLayout.addView(recyclerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadChatList();
    }

    private void loadChatList() {
        List<User> users = rentingDao.getChatUsers(currentUserId);
        adapter = new ChatListAdapter(users, user -> {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra(ChatActivity.EXTRA_TARGET_USER_ID, user.getUserId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    private static class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {
        private List<User> list;
        private OnItemClickListener listener;

        interface OnItemClickListener {
            void onItemClick(User user);
        }

        public ChatListAdapter(List<User> list, OnItemClickListener listener) {
            this.list = list;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Reusing item_application.xml? No, too complex.
            // Let's create a simple item layout or reuse existing.
            // Or create one programmatically?
            // Let's create item_chat_user.xml
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_user, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            User user = list.get(position);
            holder.tvName.setText(user.getRealName());
            holder.tvPhone.setText(user.getPhone());
            holder.itemView.setOnClickListener(v -> listener.onItemClick(user));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvPhone;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tv_name);
                tvPhone = itemView.findViewById(R.id.tv_phone);
            }
        }
    }
}
