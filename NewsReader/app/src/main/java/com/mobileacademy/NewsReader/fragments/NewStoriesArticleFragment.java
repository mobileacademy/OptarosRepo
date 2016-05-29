package com.mobileacademy.NewsReader.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobileacademy.NewsReader.R;
import com.mobileacademy.NewsReader.adapters.NewStoriesArticleFragmentRecyclerViewAdapter;
import com.mobileacademy.NewsReader.models.Article;
import com.mobileacademy.NewsReader.utils.HackerNewsAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class NewStoriesArticleFragment extends Fragment implements Callback {

    private static final String TAG = NewStoriesArticleFragment.class.getSimpleName();
    private static final int NO_OF_ARTICLES = 20;

    private OnListFragmentInteractionListener mListener;
    private NewStoriesArticleFragmentRecyclerViewAdapter mAdapter;
    private ArrayList<Article> mArticleList = new ArrayList<>();

    private ProgressDialog loadingDialog;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment from layouts (e.g. upon screen orientation changes).
     */
    public NewStoriesArticleFragment() {
    }

    @SuppressWarnings("unused")
    public static NewStoriesArticleFragment newInstance() {
        NewStoriesArticleFragment fragment = new NewStoriesArticleFragment();

        // set arguments if needed
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get arguments
        Bundle args = getArguments();

        loadingDialog = new ProgressDialog(getActivity());
        loadingDialog.setCancelable(false);
        loadingDialog.setMessage("Loading");

        mAdapter = new NewStoriesArticleFragmentRecyclerViewAdapter(mArticleList, mListener);

        showDialog();
        loadArticlesFromServer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if(mArticleList != null && mArticleList.size() > 0) {
            showDialog();
            loadArticlesFromServer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        hideDialog();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onNewStoryArticleSelected(Article item);
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Log.e(TAG, "failed to retrieve data", e);
        loadingDialog.dismiss();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String articleListJson = response.body().string();
        Log.d(TAG, "Received articles array " + articleListJson);

        new LoadArticlesAsync().execute(articleListJson);

    }

    private void loadArticlesFromServer() {
        loadingDialog.show();
        retrieveByUrl(HackerNewsAPI.NEW_STORIES_ENDPOINT);
    }

    private void retrieveByUrl(String url) {
        try {
            HackerNewsAPI.retrieveStories(url, this);
        } catch (IOException e) {
            Log.e(TAG, "ERROR retrieve by url", e);
        }
    }

    private Article getNewsItemFromJSON(JSONObject json) {
        Article item = new Article();

        String title = json.optString("title");
        String url = json.optString("url");
        String time = json.optString("time");
        int id = json.optInt("id");

        item.setId(id);
        item.setUrl(url);
        item.setName(title);
        item.setTime(time);

        return item;
    }

    private class LoadArticlesAsync extends AsyncTask<String, Void, ArrayList<Article>> {

        protected ArrayList<Article> doInBackground(String... ids) {
            ArrayList<Article> articles = new ArrayList<>();
            try {
                JSONArray jsonArticlesArray = new JSONArray(ids[0]);
                //take the first NO_OF_ARTICLES articles
                for (int i = 0; i < Math.min(NO_OF_ARTICLES,jsonArticlesArray.length()); i++) {
                    String articleURL = HackerNewsAPI.getArticleById(jsonArticlesArray.getString(i));
                    String articleString = HackerNewsAPI.retrieveStories(articleURL);
                    if(articleString == null) continue;
                    JSONObject articleJson = new JSONObject(articleString);
                    articles.add(getNewsItemFromJSON(articleJson));
                }

            }catch (IOException |JSONException e){
                Log.e(TAG, "doInBackground: ", e);
            }
            return articles;

        }

        protected void onPostExecute(ArrayList<Article> result) {
            loadingDialog.dismiss();
            mAdapter.updateData(result);
        }
    }

    private void showDialog() {
        loadingDialog.show();
    }

    private void hideDialog() {
        loadingDialog.dismiss();
    }
}
