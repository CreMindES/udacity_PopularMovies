package com.example.android.popularmovies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.android.popularmovies.databinding.ActivityReviewBinding;

import org.antlr.v4.Tool;

import java.net.MalformedURLException;
import java.net.URI;

public class ReviewActivity extends AppCompatActivity {

    private Review review;
    private MyMovie movie;
    private Intent intent;
    private ActivityReviewBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        intent = getIntent();
        review = intent.getParcelableExtra("Review");
        movie  = intent.getParcelableExtra("Movie");

        setTitle(movie.getTitle() + " " + getString( R.string.movie_review_title_suffix));

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_review);

        mBinding.activityReviewAuthor.setText(review.getAuthor());
        mBinding.activityReviewContent.setText(review.getContent());
        mBinding.activityReviewLink.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent( Intent.ACTION_VIEW, Uri.parse( review.getUrl().toString() ) );
                    startActivity( i );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
