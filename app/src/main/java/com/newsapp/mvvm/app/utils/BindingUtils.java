package com.newsapp.mvvm.app.utils;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class BindingUtils {

    private static final TimeZone timeZone = TimeZone.getTimeZone("UTC");

    public static String getElapsedTime(long utcTimeString) {
        long timeElapsedInSeconds = (System.currentTimeMillis() - utcTimeString) / 1000;

        if (timeElapsedInSeconds < 60) {
            return "less than 1m";
        } else if (timeElapsedInSeconds < 3600) {
            timeElapsedInSeconds = timeElapsedInSeconds / 60;
            return timeElapsedInSeconds + "m";
        } else if (timeElapsedInSeconds < 86400) {
            timeElapsedInSeconds = timeElapsedInSeconds / 3600;
            return timeElapsedInSeconds + "h";
        } else {
            timeElapsedInSeconds = timeElapsedInSeconds / 86400;
            return timeElapsedInSeconds + "d";
        }
    }

    public static String getSourceAndTime(String sourceName, Timestamp date) {
        return sourceName + " • " + getElapsedTime(date.getTime());
    }

    public static String getSourceName(String category, String country) {
        StringBuilder builder = new StringBuilder();
        builder.append(capitalise(category));

        if (!TextUtils.isEmpty(category) && !TextUtils.isEmpty(country)) {
            builder.append(" • ");
        }

        Locale locale = new Locale("en", country);
        builder.append(locale.getDisplayCountry());
        return builder.toString();
    }

    private static String capitalise(String s) {
        if (TextUtils.isEmpty(s))
            return s;

        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static String formatDateForDetails(Timestamp date) {
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy | hh:mm aaa", Locale.getDefault());
        return format.format(new Date(date.getTime()));
    }

    public static String truncateExtra(String content) {
        if (content == null)
            return "";
        return content.replaceAll("(\\[\\+\\d+ chars])", "");
    }

    /**
     * Utility method for Image url If image url is valid then it is parsed else
     * Article url provides url to website and icon finder utility is used to find icon
     *
     * @param imageView  Default view passed for displaying image
     * @param url        Url of the image
     * @param articleUrl URL to the article
     */
    @BindingAdapter({"bind:url", "bind:articleUrl"})
    public static void loadThumbnailImage(ImageView imageView, String url, String articleUrl) {
        Context context = imageView.getContext();
        if (url == null) {
            String iconUrl = "https://besticon-demo.herokuapp.com/icon?url=%s&size=80..120..200";
            url = String.format(iconUrl, Uri.parse(articleUrl).getAuthority());
        }
        Glide.with(imageView)
                .load(url)
                .apply(NewsGlideModule.roundedCornerImage(new RequestOptions(), imageView.getContext(), 4))
                //.placeholder(context.getResources().getDrawable(R.color.cardBackground))
                .into(imageView);
    }

    /**
     * Utility method for Image url If image url is valid then it is parsed else
     * Article url provides url to website and icon finder utility is used to find icon
     * This puts a radius 0 to image
     *
     * @param imageView  Default view passed for displaying image
     * @param url        Url of the image
     * @param articleUrl URL to the article
     */
    @BindingAdapter({"bind:urlToImage", "bind:articleUrl"})
    public static void loadImage(ImageView imageView, String url, String articleUrl) {
        Context context = imageView.getContext();
        if (url == null) {
            String iconUrl = "https://besticon-demo.herokuapp.com/icon?url=%s&size=80..120..200";
            url = String.format(iconUrl, Uri.parse(articleUrl).getAuthority());
        }
        Glide.with(imageView)
                .load(url)
                .apply(NewsGlideModule.roundedCornerImage(new RequestOptions(), imageView.getContext(), 0))
                //   .placeholder(context.getResources().getDrawable(R.color.cardBackground))
                .into(imageView);
    }

    @BindingAdapter("bind:sourceUrl")
    public static void loadSourceImage(ImageView imageView, String sourceUrl) {
        Context context = imageView.getContext();
        String iconUrl = "https://besticon-demo.herokuapp.com/icon?url=%s&size=80..120..200";
        sourceUrl = String.format(iconUrl, Uri.parse(sourceUrl).getAuthority());

        Glide.with(imageView)
                .load(sourceUrl)
                .apply(NewsGlideModule.roundedCornerImage(new RequestOptions(), context, 4))
                //   .placeholder(context.getResources().getDrawable(R.color.cardBackground))
                .into(imageView);
    }
}
