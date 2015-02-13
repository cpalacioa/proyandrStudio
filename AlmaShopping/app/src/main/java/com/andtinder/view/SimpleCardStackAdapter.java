package com.andtinder.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.almashopping.android.R;
import com.andtinder.Model.CardModel;
import com.squareup.picasso.Picasso;

public final class SimpleCardStackAdapter extends CardStackAdapter {

    public SimpleCardStackAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getCardView(int position, final CardModel model, View convertView, ViewGroup parent) {


            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.std_card_inner, parent, false);
                assert convertView != null;
            }


            ImageView foto = ((ImageView) convertView.findViewById(R.id.image));

            ((TextView) convertView.findViewById(R.id.title)).setText(model.getTitle());
            ((TextView) convertView.findViewById(R.id.description)).setText(
                    Html.fromHtml(model.getDescription()), TextView.BufferType.SPANNABLE);
            ((TextView) convertView.findViewById(R.id.description)).setMovementMethod(new ScrollingMovementMethod());

            Picasso.with(convertView.getContext())
                    .load(model.getCardImageDrawable())
                    .placeholder(R.drawable.ic_action_business)
                    .into(foto);


            final TextView like_dislike_text = ((TextView) convertView.findViewById(R.id.like_dislike_text));
            final ImageView likeimg=((ImageView)convertView.findViewById(R.id.like));
            final ImageView nolikeimg=((ImageView)convertView.findViewById(R.id.dislike));
/*        if(model.isLike()) {
            like_dislike_text.setText("Me gusta 1");
            likeimg.setImageDrawable(SimpleCardStackAdapter.this.getContext().getResources().getDrawable(R.drawable.ic_action_thumb_up_blue));
            nolikeimg.setImageDrawable(SimpleCardStackAdapter.this.getContext().getResources().getDrawable(R.drawable.ic_action_thumb_down));

        }
        else {
            like_dislike_text.setText("No Me Gusta 1");
            likeimg.setImageDrawable(SimpleCardStackAdapter.this.getContext().getResources().getDrawable(R.drawable.ic_action_thumb_up));
            nolikeimg.setImageDrawable(SimpleCardStackAdapter.this.getContext().getResources().getDrawable(R.drawable.ic_action_thumb_down_red));

        }*/
            ((ImageView) convertView.findViewById(R.id.like)).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    model.setLike(true);
                    like_dislike_text.setText("Me gusta 2");
                    likeimg.setImageDrawable(v.getContext().getResources().getDrawable(R.drawable.ic_action_thumb_up_blue));
                    nolikeimg.setImageDrawable(v.getContext().getResources().getDrawable(R.drawable.ic_action_thumb_down));
                }
            });

            ((ImageView) convertView.findViewById(R.id.dislike)).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    model.setLike(false);
                    likeimg.setImageDrawable(SimpleCardStackAdapter.this.getContext().getResources().getDrawable(R.drawable.ic_action_thumb_up));
                    nolikeimg.setImageDrawable(SimpleCardStackAdapter.this.getContext().getResources().getDrawable(R.drawable.ic_action_thumb_down_red));
                    like_dislike_text.setText("No me Gusta 2");
                }
            });

            return convertView;

        }

    }




