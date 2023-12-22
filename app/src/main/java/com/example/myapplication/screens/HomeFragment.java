package com.example.myapplication.screens;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.model.Room;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Room, RoomViewHolder> adapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        databaseReference = FirebaseDatabase.getInstance().getReference("rooms");

        FirebaseRecyclerOptions<Room> options =
                new FirebaseRecyclerOptions.Builder<Room>()
                        .setQuery(databaseReference.orderByKey(), Room.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Room, HomeFragment.RoomViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RoomViewHolder holder, int position, @NonNull Room model) {
                holder.bind(model);
            }

            @NonNull
            @Override
            public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_room, parent, false);
                RoomViewHolder viewHolder = new RoomViewHolder(view);
                return viewHolder;
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
        private ImageView roomImageView, favoriteImageView;
        private Room room;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            roomImageView = itemView.findViewById(R.id.roomImageView);

            itemView.findViewById(R.id.cardView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(itemView.getContext(), "Item Clicked", Toast.LENGTH_SHORT).show();
                    openRoomDetail(itemView.getContext());
                }
            });

            favoriteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleFavoriteStatus();
                }
            });
        }

        public void bind(Room room) {
            this.room = room;
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

        private void openRoomDetail(Context context) {
            Log.d("RoomViewHolder", "Open Room Detail");
            Intent intent = new Intent(itemView.getContext(), DetailRoomFragment.class);
            intent.putExtra("roomId", room.getRoomId());
            intent.putExtra("title", room.getTitle());
            intent.putExtra("description", room.getDescription());
            intent.putExtra("price", room.getPrice());
            intent.putExtra("location", room.getLocation());
            intent.putExtra("imageUrl", room.getImageUrl());
            itemView.getContext().startActivity(intent);
        }

        private void toggleFavoriteStatus() {

        }
    }
}