package com.livewater.comment.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.livewater.comment.R;
import com.livewater.comment.activity.MainActivity;


import com.livewater.comment.ui.materialList.card.Card;
import com.livewater.comment.ui.materialList.card.CardProvider;
import com.livewater.comment.ui.materialList.card.OnActionClickListener;
import com.livewater.comment.ui.materialList.card.action.TextViewAction;
import com.livewater.comment.ui.materialList.card.action.WelcomeButtonAction;
import com.livewater.comment.ui.materialList.card.provider.ListCardProvider;
import com.livewater.comment.ui.materialList.listeners.OnDismissCallback;
import com.livewater.comment.ui.materialList.listeners.RecyclerItemClickListener;
import com.livewater.comment.ui.materialList.view.MaterialListView;
import com.squareup.picasso.RequestCreator;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * Created by tiansj on 15/9/4.
 */
public class MaterialListFragment extends Fragment {
    private MainActivity mContext;
    private MaterialListView mListView;
    private ImageView emptyView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_material_list, container, false);
        ButterKnife.bind(this, view);
        mListView = (MaterialListView) view.findViewById(R.id.material_listview);
        emptyView = (ImageView) view.findViewById(R.id.image_View);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = (MainActivity) getActivity();


        emptyView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mListView.setEmptyView(emptyView);
        Picasso.with(mContext)
                .load("https://www.skyverge.com/wp-content/uploads/2012/05/github-logo.png")
                .resize(100, 100)
                .centerInside()
                .into(emptyView);

        // Fill the array withProvider mock content
        fillArray();


        // Set the dismiss listener
        mListView.setOnDismissCallback(new OnDismissCallback() {
            @Override
            public void onDismiss(@NonNull Card card, int position) {
                // Show a toast
                Toast.makeText(mContext, "You have dismissed a " + card.getTag(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the ItemTouchListener
        mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Card card, int position) {
                Log.d("CARD_TYPE", "" + card.getTag());
            }

            @Override
            public void onItemLongClick(@NonNull Card card, int position) {
                Log.d("LONG_CLICK", "" + card.getTag());
            }
        });


    }

    private void fillArray() {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            cards.add(getRandomCard(i));
        }
        mListView.getAdapter().addAll(cards);
    }
    private Card getRandomCard(final int position) {
        String title = "Card number " + (position + 1);
        String description = "Lorem ipsum dolor sit amet";

        switch (position % 7) {
            case 0: {
                return new Card.Builder(mContext)
                        .setTag("SMALL_IMAGE_CARD")
                        .setDismissible()
                        .withProvider(new CardProvider())
                        .setLayout(R.layout.material_small_image_card)
                        .setTitle(title)
                        .setDescription(description)
                        .setDrawable(R.drawable.sample_android)
                        .setDrawableConfiguration(new CardProvider.OnImageConfigListener() {
                            @Override
                            public void onImageConfigure(@NonNull final RequestCreator requestCreator) {
                                requestCreator.rotate(position * 90.0f)
                                        .resize(150, 150)
                                        .centerCrop();
                            }
                        })
                        .endConfig()
                        .build();
            }
            case 1: {
                return new Card.Builder(mContext)
                        .setTag("BIG_IMAGE_CARD")
                        .withProvider(new CardProvider())
                        .setLayout(R.layout.material_big_image_card_layout)
                        .setTitle(title)
                        .setSubtitle(description)
                        .setSubtitleGravity(Gravity.END)
                        .setDrawable("https://assets-cdn.github.com/images/modules/logos_page/GitHub-Mark.png")
                        .setDrawableConfiguration(new CardProvider.OnImageConfigListener() {
                            @Override
                            public void onImageConfigure(@NonNull final RequestCreator requestCreator) {
                                requestCreator.rotate(position * 45.0f)
                                        .resize(200, 200)
                                        .centerCrop();
                            }
                        })
                        .endConfig()
                        .build();
            }
            case 2: {
                final CardProvider provider = new Card.Builder(mContext)
                        .setTag("BASIC_IMAGE_BUTTON_CARD")
                        .setDismissible()
                        .withProvider(new CardProvider<>())
                        .setLayout(R.layout.material_basic_image_buttons_card_layout)
                        .setTitle(title)
                        .setTitleGravity(Gravity.END)
                        .setDescription(description)
                        .setDescriptionGravity(Gravity.END)
                        .setDrawable(R.drawable.dog)
                        .setDrawableConfiguration(new CardProvider.OnImageConfigListener() {
                            @Override
                            public void onImageConfigure(@NonNull RequestCreator requestCreator) {
                                requestCreator.fit();
                            }
                        })
                        .addAction(R.id.left_text_button, new TextViewAction(mContext)
                                .setText("left")
                                .setTextResourceColor(R.color.black_button)
                                .setListener(new OnActionClickListener() {
                                    @Override
                                    public void onActionClicked(View view, Card card) {
                                        Toast.makeText(mContext, "You have pressed the left button", Toast.LENGTH_SHORT).show();
                                        card.getProvider().setTitle("CHANGED ON RUNTIME");
                                    }
                                }))
                        .addAction(R.id.right_text_button, new TextViewAction(mContext)
                                .setText("right")
                                .setTextResourceColor(R.color.orange_button)
                                .setListener(new OnActionClickListener() {
                                    @Override
                                    public void onActionClicked(View view, Card card) {
                                        Toast.makeText(mContext, "You have pressed the right button on card " + card.getProvider().getTitle(), Toast.LENGTH_SHORT).show();
                                        card.dismiss();
                                    }
                                }));

                if (position % 2 == 0) {
                    provider.setDividerVisible(true);
                }

                return provider.endConfig().build();
            }
            case 3: {
                final CardProvider provider = new Card.Builder(mContext)
                        .setTag("BASIC_BUTTONS_CARD")
                        .setDismissible()
                        .withProvider(new CardProvider())
                        .setLayout(R.layout.material_basic_buttons_card)
                        .setTitle(title)
                        .setDescription(description)
                        .addAction(R.id.left_text_button, new TextViewAction(mContext)
                                .setText("left")
                                .setTextResourceColor(R.color.black_button)
                                .setListener(new OnActionClickListener() {
                                    @Override
                                    public void onActionClicked(View view, Card card) {
                                        Toast.makeText(mContext, "You have pressed the left button", Toast.LENGTH_SHORT).show();
                                    }
                                }))
                        .addAction(R.id.right_text_button, new TextViewAction(mContext)
                                .setText("right")
                                .setTextResourceColor(R.color.accent_material_dark)
                                .setListener(new OnActionClickListener() {
                                    @Override
                                    public void onActionClicked(View view, Card card) {
                                        Toast.makeText(mContext, "You have pressed the right button", Toast.LENGTH_SHORT).show();
                                    }
                                }));

                if (position % 2 == 0) {
                    provider.setDividerVisible(true);
                }

                return provider.endConfig().build();
            }
            case 4: {
                final CardProvider provider = new Card.Builder(mContext)
                        .setTag("WELCOME_CARD")
                        .setDismissible()
                        .withProvider(new CardProvider())
                        .setLayout(R.layout.material_welcome_card_layout)
                        .setTitle("Welcome Card")
                        .setTitleColor(Color.WHITE)
                        .setDescription("I am the description")
                        .setDescriptionColor(Color.WHITE)
                        .setSubtitle("My subtitle!")
                        .setSubtitleColor(Color.WHITE)
                        .setBackgroundColor(Color.BLUE)
                        .addAction(R.id.ok_button, new WelcomeButtonAction(mContext)
                                .setText("Okay!")
                                .setTextColor(Color.WHITE)
                                .setListener(new OnActionClickListener() {
                                    @Override
                                    public void onActionClicked(View view, Card card) {
                                        Toast.makeText(mContext, "Welcome!", Toast.LENGTH_SHORT).show();
                                    }
                                }));

                if (position % 2 == 0) {
                    provider.setBackgroundResourceColor(android.R.color.background_dark);
                }

                return provider.endConfig().build();
            }
            case 5: {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1);
                adapter.add("Hello");
                adapter.add("World");
                adapter.add("!");

                return new Card.Builder(mContext)
                        .setTag("LIST_CARD")
                        .setDismissible()
                        .withProvider(new ListCardProvider())
                        .setLayout(R.layout.material_list_card_layout)
                        .setTitle("List Card")
                        .setDescription("Take a list")
                        .setAdapter(adapter)
                        .endConfig()
                        .build();
            }
            default: {
                final CardProvider provider = new Card.Builder(mContext)
                        .setTag("BIG_IMAGE_BUTTONS_CARD")
                        .setDismissible()
                        .withProvider(new CardProvider())
                        .setLayout(R.layout.material_image_with_buttons_card)
                        .setTitle(title)
                        .setDescription(description)
                        .setDrawable(R.drawable.photo)
                        .addAction(R.id.left_text_button, new TextViewAction(mContext)
                                .setText("add card")
                                .setTextResourceColor(R.color.black_button)
                                .setListener(new OnActionClickListener() {
                                    @Override
                                    public void onActionClicked(View view, Card card) {
                                        Log.d("ADDING", "CARD");

                                        mListView.getAdapter().add(generateNewCard());
                                        Toast.makeText(mContext, "Added new card", Toast.LENGTH_SHORT).show();
                                    }
                                }))
                        .addAction(R.id.right_text_button, new TextViewAction(mContext)
                                .setText("right button")
                                .setTextResourceColor(R.color.accent_material_dark)
                                .setListener(new OnActionClickListener() {
                                    @Override
                                    public void onActionClicked(View view, Card card) {
                                        Toast.makeText(mContext, "You have pressed the right button", Toast.LENGTH_SHORT).show();
                                    }
                                }));

                if (position % 2 == 0) {
                    provider.setDividerVisible(true);
                }

                return provider.endConfig().build();
            }
        }
    }
    private Card generateNewCard() {
        return new Card.Builder(mContext)
                .setTag("BASIC_IMAGE_BUTTONS_CARD")
                .withProvider(new CardProvider())
                .setLayout(R.layout.material_basic_image_buttons_card_layout)
                .setTitle("I'm new")
                .setDescription("I've been generated on runtime!")
                .setDrawable(R.drawable.dog)
                .endConfig()
                .build();
    }

    private void addMockCardAtStart() {
        mListView.getAdapter().addAtStart(new Card.Builder(mContext)
                .setTag("BASIC_IMAGE_BUTTONS_CARD")
                .setDismissible()
                .withProvider(new CardProvider())
                .setLayout(R.layout.material_basic_image_buttons_card_layout)
                .setTitle("Hi there")
                .setDescription("I've been added on top!")
                .addAction(R.id.left_text_button, new TextViewAction(mContext)
                        .setText("left")
                        .setTextResourceColor(R.color.black_button))
                .addAction(R.id.right_text_button, new TextViewAction(mContext)
                        .setText("right")
                        .setTextResourceColor(R.color.orange_button))
                .setDrawable(R.drawable.dog)
                .endConfig()
                .build());
    }

    @Override
    public void onResume() {
        super.onResume();
        Picasso.with(mContext).resumeTag(mContext);
    }

    @Override
    public void onPause() {
        super.onPause();
        Picasso.with(mContext).pauseTag(mContext);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Picasso.with(mContext).cancelTag(mContext);
    }

}
