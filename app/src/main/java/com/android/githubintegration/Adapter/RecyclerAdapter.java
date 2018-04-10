package com.android.githubintegration.Adapter;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.githubintegration.R;
import com.android.githubintegration.model.User;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<User> users;

    public RecyclerAdapter(List<User> users){
        this.users=users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user=users.get(position);
        holder.nameView.setText(user.getLogin());

        Glide.with(holder.avatar.getContext())
                .load(user.getAvatarUrl())
                .into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.name)
        AppCompatTextView nameView;

        @BindView(R.id.avatar)
        AppCompatImageView avatar;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
