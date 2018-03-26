package com.example.mostafa.bakingapp.ui.fragments;


import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.mostafa.bakingapp.utils.Constants;
import com.example.mostafa.bakingapp.R;
import com.example.mostafa.bakingapp.model.Step;
import com.example.mostafa.bakingapp.ui.activities.StepActivity;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepFragment extends Fragment {


    @BindView(R.id.step_video)
    SimpleExoPlayerView mVideoPlayerView;
    @BindView(R.id.step_full_description)
    TextView mStepDescriptionTextView;
    @BindView(R.id.no_video)
    TextView mNoVideoTextView;

    private SimpleExoPlayer mVideoPlayer;
    private String mVideoUrl;

    private long playbackPosition;


    public StepFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, view);
        playbackPosition = C.POSITION_UNSET;
        if (getArguments() != null) {
            Step step = getArguments().getParcelable(Constants.STEP);
            mVideoUrl = step.getVideoURL();
            mStepDescriptionTextView.setText(step.getDescription());
            if (mVideoUrl == null || mVideoUrl.isEmpty()) {
                mVideoPlayerView.setVisibility(View.GONE);
            } else {
                if (getActivity() instanceof StepActivity && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                    applyFullScreenMode();
            }
        }

        return view;
    }

    private void applyFullScreenMode() {

        try {
            mStepDescriptionTextView.setVisibility(View.GONE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mVideoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            ((StepActivity) getActivity()).getSupportActionBar().hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            if (mVideoUrl != null && !mVideoUrl.isEmpty()) {
                initializePlayer(Uri.parse(mVideoUrl));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mVideoPlayer == null)) {
            if (mVideoUrl != null && !mVideoUrl.isEmpty()) {
                initializePlayer(Uri.parse(mVideoUrl));
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong(Constants.POSITION);
        }
    }

    private void initializePlayer(Uri uri) {
        if (mVideoPlayer == null) {
            mNoVideoTextView.setVisibility(View.GONE);
            mVideoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            mVideoPlayerView.setPlayer(mVideoPlayer);
            mVideoPlayer.setPlayWhenReady(true);
            mVideoPlayer.seekTo(playbackPosition);
            MediaSource mediaSource = buildMediaSource(uri);
            mVideoPlayer.prepare(mediaSource, true, false);
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("baking_app"))
                .createMediaSource(uri);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (mVideoPlayer != null) {
            playbackPosition = mVideoPlayer.getCurrentPosition();

            mVideoPlayer.stop();
            mVideoPlayer.release();
            mVideoPlayer = null;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(Constants.POSITION, playbackPosition);
    }
}
