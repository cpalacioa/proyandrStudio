package com.andtinder.Model;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class CardModel {

    private String   title;
    private String   description;
    private String cardImageDrawable;
    private String cardLikeImageDrawable;
    private String cardDislikeImageDrawable;

    private OnCardDimissedListener mOnCardDimissedListener = null;

    private OnClickListener mOnClickListener = null;

    public interface OnCardDimissedListener {
        void onLike();
        void onDislike();
    }

    public interface OnClickListener {
        void OnClickListener();
    }

    public CardModel() {
        this(null, null, null);
    }

    public CardModel(String title, String description, String cardImage) {
        this.title = title;
        this.description = description;
        this.cardImageDrawable = cardImage;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCardImageDrawable() {
        return cardImageDrawable;
    }

    public void setCardImageDrawable(String cardImageDrawable) {
        this.cardImageDrawable = cardImageDrawable;
    }

    public String getCardLikeImageDrawable() {
        return cardLikeImageDrawable;
    }

    public void setCardLikeImageDrawable(String cardLikeImageDrawable) {
        this.cardLikeImageDrawable = cardLikeImageDrawable;
    }

    public String getCardDislikeImageDrawable() {
        return cardDislikeImageDrawable;
    }

    public void setCardDislikeImageDrawable(String cardDislikeImageDrawable) {
        this.cardDislikeImageDrawable = cardDislikeImageDrawable;
    }

    public void setOnCardDimissedListener( OnCardDimissedListener listener ) {
        this.mOnCardDimissedListener = listener;
    }

    public OnCardDimissedListener getOnCardDimissedListener() {
        return this.mOnCardDimissedListener;
    }


    public void setOnClickListener( OnClickListener listener ) {
        this.mOnClickListener = listener;
    }

    public OnClickListener getOnClickListener() {
        return this.mOnClickListener;
    }
    private boolean isLike = false;
    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean isLike) {
        this.isLike = isLike;
    }
}
