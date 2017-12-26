package com.mateus.tripadvisorapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.mateus.tripadvisorapi.dummy.DummyContent;

import java.util.List;

/**
 * An activity representing a list of Desenvolvedores. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link DesenvolvedorDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class DesenvolvedorListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desenvolvedor_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "support@tocomfomeapp.com", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (findViewById(R.id.desenvolvedor_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.desenvolvedor_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        //private final ItemListActivity mParentActivity;
        private final List<DummyContent.DummyItem> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(DesenvolvedorDetailFragment.ARG_ITEM_ID, item.id);
                    DesenvolvedorDetailFragment fragment = new DesenvolvedorDetailFragment();
                    fragment.setArguments(arguments);
                    /*mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.desenvolvedor_detail_container, fragment)
                            .commit();*/
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, DesenvolvedorDetailActivity.class);
                    intent.putExtra(DesenvolvedorDetailFragment.ARG_ITEM_ID, item.id);
                    if (item.details == "1") {
                        intent.putExtra("url", "https://avatars0.githubusercontent.com/u/12852396?s=460&v=4");
                    }
                    else if (item.id == "2") {
                        intent.putExtra("url", "https://avatars1.githubusercontent.com/u/20567432?s=400&u=92c5915995ba7ba3487ca8e9def1aec1b2a0ee56&v=4");
                    }
                    else if (item.id == "3") {
                        intent.putExtra("url", "https://avatars3.githubusercontent.com/u/7280844?s=460&v=4");
                    }
                    else if (item.id == "4") {
                        intent.putExtra("url", "https://avatars1.githubusercontent.com/u/6564928?s=460&v=4");
                    }

                    context.startActivity(intent);

                }
            }
        };

        SimpleItemRecyclerViewAdapter(DesenvolvedorListActivity parent,
                                      List<DummyContent.DummyItem> items,
                                      boolean twoPane) {
            mValues = items;
           // mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.desenvolvedor_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }
}
