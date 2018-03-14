package gymbuddy.project.capstone.gymbuddy.UI.MessagePage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import gymbuddy.project.capstone.gymbuddy.Database.User;
import gymbuddy.project.capstone.gymbuddy.R;


public class MatchedRecyclerViewAdapter extends RecyclerView.Adapter<MatchedRecyclerViewAdapter.ViewHolder> {

    private final List<User> mValues;
    private final OnListInteractionListener mListener;

    public MatchedRecyclerViewAdapter(List<User> items, OnListInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.matched_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.simpleDraweeView.setImageURI(mValues.get(position).getPhotoURL());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final SimpleDraweeView simpleDraweeView;
        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            simpleDraweeView = view.findViewById(R.id.simpleView2);
        }

    }
}
