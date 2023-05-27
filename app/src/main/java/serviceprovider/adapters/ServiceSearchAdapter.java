package serviceprovider.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppl.R;

import java.util.List;

import serviceprovider.activities.GetServiceProviderActivity;
import serviceprovider.models.Service;

public class ServiceSearchAdapter extends RecyclerView.Adapter<ServiceSearchAdapter.ServiceViewHolder> {
    private static List<Service> services;

    public void setServices(List<Service> services) {
        this.services = services;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = services.get(position);
        holder.bind(service);
    }

    @Override
    public int getItemCount() {
        return services != null ? services.size() : 0;
    }

    static class ServiceViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView ratingTextView;
        private TextView ratingCountTextView;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            ratingTextView = itemView.findViewById(R.id.ratingTextView);
            ratingCountTextView = itemView.findViewById(R.id.ratingCountTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Service clickedService = services.get(position);
                        Intent intent = new Intent(v.getContext(), GetServiceProviderActivity.class);
                        intent.putExtra("SERVISASNIEDZEJS_ID", clickedService.getServisasniedzejs_ID());
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }

        public void bind(Service service) {
            nameTextView.setText(service.getNosaukums());
            ratingTextView.setText(String.format("Rating: %s", service.getReitings()));
            ratingCountTextView.setText(String.format("Rating Count: %s", service.getReitinguSkaits()));
        }
    }

}
