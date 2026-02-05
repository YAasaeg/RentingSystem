package com.example.rentingsystem.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentingsystem.R;
import com.example.rentingsystem.data.RentingDao;
import com.example.rentingsystem.model.Notice;

import java.util.List;

public class NoticeListFragment extends Fragment {

    private RecyclerView recyclerView;
    private NoticeAdapter adapter;
    private RentingDao rentingDao;
    private int currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notice_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        rentingDao = new RentingDao(getContext());
        SharedPreferences sp = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        currentUserId = sp.getInt("user_id", -1);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        loadNotices();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        loadNotices();
    }

    private void loadNotices() {
        List<Notice> list = rentingDao.getTenantNotices(currentUserId);
        adapter = new NoticeAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    private static class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {
        private List<Notice> list;

        public NoticeAdapter(List<Notice> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Notice notice = list.get(position);
            holder.tvTitle.setText(notice.getTitle());
            holder.tvPublisher.setText("发布者: " + notice.getPublisherName());
            holder.tvContent.setText(notice.getContent());
            holder.tvTime.setText(notice.getCreateTime());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvPublisher, tvContent, tvTime;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tv_title);
                tvPublisher = itemView.findViewById(R.id.tv_publisher);
                tvContent = itemView.findViewById(R.id.tv_content);
                tvTime = itemView.findViewById(R.id.tv_time);
            }
        }
    }
}
