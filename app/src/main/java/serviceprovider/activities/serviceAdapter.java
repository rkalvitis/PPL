package serviceprovider.activities;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.ppl.R;
import java.util.ArrayList;
import java.util.List;
import serviceprovider.models.Service;
public class serviceAdapter extends RecyclerView.Adapter<serviceAdapter.UserViewHolder> {
    private List<Service> services = new ArrayList<>();
    private serviceprovider.activities.serviceAdapter.OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new serviceprovider.activities.serviceAdapter.UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull serviceAdapter.UserViewHolder holder, int position) {
        Service service = services.get(position);
        String userNumber = (position + 1) + ".";
        String userName = service.getNosaukums();
        String userEmail = service.getEpasts();

        holder.userNumberTextView.setText(userNumber);
        holder.userNameTextView.setText(userName);
        holder.userEmailTextView.setText(userEmail);
    }
    @Override
    public int getItemCount() {
        return services.size();
    }

    public void setUsers(List<Service> users) {
        this.services = services;
        notifyDataSetChanged();
    }
    public void setServices(List<Service> services) {
        this.services = services;
        notifyDataSetChanged();
    }
    public void setOnItemClickListener(serviceAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(Service service);
    }
    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userNumberTextView;
        TextView userNameTextView;
        TextView userEmailTextView;
        UserViewHolder(View itemView) {
            super(itemView);
            userNumberTextView = itemView.findViewById(R.id.userNumberTextView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);
            userEmailTextView = itemView.findViewById(R.id.userEmailTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                        onItemClickListener.onItemClick(services.get(position));
                    }
                }
            });
        }
    }
}
