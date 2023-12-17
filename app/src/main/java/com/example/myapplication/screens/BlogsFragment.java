package com.example.myapplication.screens;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.Room;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

public class BlogsFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Room, RoomViewHolder> adapter;
    private String currentUserUid;

    public static BlogsFragment newInstance(String currentUserUid) {
        BlogsFragment fragment = new BlogsFragment();
        Bundle args = new Bundle();
        args.putString("currentUserUid", currentUserUid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentUserUid = getArguments().getString("currentUserUid");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blogs, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        databaseReference = FirebaseDatabase.getInstance().getReference("rooms");

        FirebaseRecyclerOptions<Room> options =
                new FirebaseRecyclerOptions.Builder<Room>()
                        .setQuery(databaseReference.orderByChild("userUid").equalTo(currentUserUid), Room.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Room, RoomViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RoomViewHolder holder, int position, @NonNull Room model) {
                holder.bind(model);
            }

            @NonNull
            @Override
            public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_room, parent, false);
                return new RoomViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView, descriptionTextView, priceTextView, locationTextView;
        private ImageView roomImageView;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            roomImageView = itemView.findViewById(R.id.roomImageView);
        }

        public void bind(Room room) {
            titleTextView.setText(room.getTitle());
            descriptionTextView.setText(room.getDescription());
            priceTextView.setText(room.getPrice() + "");
            locationTextView.setText(room.getLocation());

            if (room.getImageUrl() != null && !room.getImageUrl().isEmpty()) {
                Picasso.get().load(Uri.parse(room.getImageUrl())).into(roomImageView);
            }else {
                roomImageView.setImageResource(R.drawable.home_icon);
            }
        }
    }
}
