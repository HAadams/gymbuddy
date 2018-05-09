package gymbuddy.project.capstone.gymbuddy.UI.MessagePage;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import gymbuddy.project.capstone.gymbuddy.Database.CurrentUser;
import gymbuddy.project.capstone.gymbuddy.R;


public class ChatFragment extends Fragment {
    private static final String USER_ID = "user_id_param";

    private String user_id;

    DatabaseReference mRef;
    DatabaseReference mRef2;
    EditText message;
    RecyclerView messageList;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user_id Parameter 1.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String user_id) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(USER_ID, user_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user_id = getArguments().getString(USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        message = view.findViewById(R.id.messageEditText);
        String user_id = getArguments().getString(USER_ID);
        mRef = FirebaseDatabase.getInstance().getReference()
                .child("Messages")
                .child(CurrentUser.getInstance().getUserID())
                .child(user_id);
        mRef2 = FirebaseDatabase.getInstance().getReference()
                .child("Messages")
                .child(user_id)
                .child(CurrentUser.getInstance().getUserID());

        Button btn = view.findViewById(R.id.sendMessageButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTextPressed();
            }
        });
        messageList = view.findViewById(R.id.messageRC);
        messageList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        messageList.setLayoutManager(linearLayoutManager);

        return view;
    }

    private void sendTextPressed(){
        String msg = message.getText().toString().trim();
        if(msg.length() != 0) {
            DatabaseReference newPost = mRef.push();
            DatabaseReference newPost2 = mRef2.push();
            newPost.child("content").setValue(msg);
            newPost2.child("content").setValue(msg);
            message.setText("");
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Message, MessageViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(
                Message.class,
                R.layout.message_pop,
                MessageViewHolder.class,
                mRef

        ) {
            final int speedScroll = 100;
            final Handler handler = new Handler();
            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, Message model, int position) {
                viewHolder.setContent(model.getContent());
                messageList.scrollToPosition(getItemCount() - 1);
                viewHolder.setRandomBackgroundColor();
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try{
                            messageList.smoothScrollToPosition(getItemCount()-1);}
                        catch(Exception e){}
                        handler.postDelayed(this,speedScroll);
                    }
                };

                handler.postDelayed(runnable,speedScroll);
            }
        };

        messageList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder{
        View mView;
        int[] colors = {R.color.c0, R.color.c1, R.color.c2, R.color.c3, R.color.c4, R.color.c5,
                R.color.c6, R.color.c7, R.color.c8, R.color.c9, R.color.c10};
        public MessageViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setContent(String content){
            TextView msg_content = mView.findViewById(R.id.messageTextView);
            msg_content.setText(content);
        }

        public void setRandomBackgroundColor(){
            int r = (int)(Math.random() * (colors.length-1));
            mView.setClipToOutline(true);
            ((GradientDrawable) mView.getBackground()).setColor(mView.getResources().getColor(colors[r]));
        }
    }
}
