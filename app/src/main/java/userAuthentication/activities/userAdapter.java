package userAuthentication.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppl.R;

import java.util.ArrayList;
import java.util.List;

import userAuthentication.models.User;

public class userAdapter extends RecyclerView.Adapter<userAdapter.UserViewHolder> {
    private List<User> users = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        String userNumber = (position + 1) + ".";
        String userName = user.getVards();
        String userSurname = user.getUzvards();
        String userEmail = user.getEpasts();

        holder.userNumberTextView.setText(userNumber);
        holder.userNameTextView.setText(userName);
        holder.userSurnameTextView.setText(userSurname);
        holder.userEmailTextView.setText(userEmail);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userNumberTextView;
        TextView userNameTextView;
        TextView userSurnameTextView;
        TextView userEmailTextView;

        UserViewHolder(View itemView) {
            super(itemView);
            userNumberTextView = itemView.findViewById(R.id.userNumberTextView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            userSurnameTextView = itemView.findViewById(R.id.userSurnameTextView);
            userEmailTextView = itemView.findViewById(R.id.userEmailTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                        onItemClickListener.onItemClick(users.get(position));
                    }
                }
            });
        }
    }


}
