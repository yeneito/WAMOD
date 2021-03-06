package com.wamod.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.wamod.ColorsManager;
import com.wamod.Resources;
import com.wamod.Utils;
import com.whatsapp.HomeActivity;

import java.util.ArrayList;

/**
 * Created by BrianValente on 3/24/16.
 */
public class BottomNavigation extends LinearLayout {
    RelativeLayout callsBtn;
    RelativeLayout chatsBtn;
    RelativeLayout contactsBtn;
    HomeActivity.TabsPager pager;
    Context ctx;
    float originalLabelTextSize = 0;
    float originalIconTopMargin = 0;
    int inactiveColor;
    int activeColor;
    int backgroundColor;
    int activeItem;
    boolean firstAnim = true;
    float labelMultiplier = Utils.Nexus6PResToActualDevice(this.getContext(), 9, 9)[0];
    float iconMultiplier = Utils.Nexus6PResToActualDevice(this.getContext(), 5, 5)[0];

    public BottomNavigation(Context context) {
        super(context);
        this.ctx = context;
        init();
    }

    public BottomNavigation(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
        init();
    }

    private void init() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                View tabs = ((HomeActivity) ctx).findViewById(Resources.id.tabs);
                View pager_holder = ((HomeActivity) ctx).findViewById(Resources.id.pager_holder);
                ViewGroup content = (ViewGroup) ((HomeActivity) ctx).findViewById(Resources.id.content);
                pager = (HomeActivity.TabsPager) ((HomeActivity) ctx).findViewById(Resources.id.pager);

                callsBtn    = (RelativeLayout) findViewById(Resources.id.wamod_bottomnav_calls);
                chatsBtn    = (RelativeLayout) findViewById(Resources.id.wamod_bottomnav_chats);
                contactsBtn = (RelativeLayout) findViewById(Resources.id.wamod_bottomnav_contacts);

                loadColors();
                setInactive();

                tabs.setVisibility(View.GONE);
                pager_holder.setPadding(0,0,0,0);
                /*RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(content.getLayoutParams());
                params.setMargins(0,0,0,utils.Nexus6PResToActualDevice(getContext(),224,224)[0]);
                content.setLayoutParams(params);*/
                content.setPadding(0,0,0,getHeight());

                pager.addOnPageChangeListener(new HomeActivity.TabsPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        switch (position) {
                            case 0:
                                setActive(callsBtn, 0);
                                break;
                            case 1:
                                setActive(chatsBtn, 1);
                                break;
                            case 2:
                                setActive(contactsBtn, 2);
                                break;
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

                setActive(chatsBtn, 1);

                /*View label = callsBtn.getChildAt(1);
                if (label != null && label instanceof TextView) originalLabelTextSize = ((TextView) label).getTextSize();*/
                View icon = callsBtn.getChildAt(0);
                if (icon != null && icon instanceof ImageView) originalIconTopMargin = icon.getPaddingTop();

                callsBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setActive(callsBtn, 0);
                        pager.setCurrentItem(0);
                    }
                });

                chatsBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setActive(chatsBtn, 1);
                        pager.setCurrentItem(1);
                    }
                });

                contactsBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setActive(contactsBtn, 2);
                        pager.setCurrentItem(2);
                    }
                });

                Log.i("WAMOD", "Text size: " + originalLabelTextSize + "Icon margin: " + originalIconTopMargin);


                HomeActivity a = (HomeActivity) ctx;
                ViewGroup tabs2 = (ViewGroup) a.findViewById(Resources.getID("tabs"));
                ViewGroup tabsChild = (ViewGroup) tabs2.getChildAt(0);
                ViewGroup tab = (ViewGroup) tabsChild.getChildAt(1);

                final TextView badge = (TextView) tab.findViewById(Resources.getID("badge"));
                badge.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        ViewGroup wamod_badge_big = (ViewGroup) ((AppCompatActivity)ctx).findViewById(Resources.getID("wamod_badge_big"));
                        if (badge.getVisibility() == GONE) wamod_badge_big.setVisibility(GONE);
                        else wamod_badge_big.setVisibility(VISIBLE);
                    }
                });

                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void setActive(RelativeLayout button, int n) {
        if (activeItem == n) return;
        activeItem = n;

        setInactive();

        final View label = button.getChildAt(1);
        final View iconContainer = button.findViewById(Resources.getID("icon_container"));
        final View icon = button.findViewById(Resources.getID("icon"));

        if (label != null && label instanceof TextView) {
            TextView lb = ((TextView) label);
            lb.setTextColor(activeColor);
        }
        if (icon != null && icon instanceof ImageView) {
            ImageView icn = ((ImageView) icon);
            icn.setColorFilter(activeColor);
        }

        ImageView wamod_badge_small = (ImageView) button.findViewById(Resources.getID("wamod_badge_small"));

        if (wamod_badge_small != null) {
            Drawable badge_small = button.getResources().getDrawable(Resources.getDrawable("wamod_badge_small"));
            badge_small.setColorFilter(activeColor, PorterDuff.Mode.MULTIPLY);
            wamod_badge_small.setImageDrawable(badge_small);
        }

        Animation anim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 0 && originalLabelTextSize == 0)
                    originalLabelTextSize = ((TextView) label).getTextSize();
                if (interpolatedTime == 0)
                    ((TextView) label).setTextSize(TypedValue.COMPLEX_UNIT_PX, originalLabelTextSize);

                float newTextSize = (labelMultiplier * interpolatedTime) + originalLabelTextSize;
                ((TextView) label).setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);


                if (interpolatedTime == 0 && originalIconTopMargin == 0)
                    originalIconTopMargin = icon.getPaddingTop();
                int newPadding = (int) (originalIconTopMargin - (iconMultiplier * interpolatedTime));
                if (iconContainer == null) icon.setPadding(0, newPadding, 0, 0);
                else iconContainer.setPadding(0, newPadding, 0, 0);
            }
        };
        anim.setDuration(100);
        if (firstAnim) {
            anim.setDuration(0);
            firstAnim = false;
        }
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        button.startAnimation(anim);
    }

    private void setInactive() {
        ArrayList<RelativeLayout> buttons = new ArrayList<>();
        buttons.add(callsBtn);
        buttons.add(chatsBtn);
        buttons.add(contactsBtn);

        for (int i = 0; i<buttons.size(); i++) {
            if (!(i == activeItem)) {
                RelativeLayout btn = buttons.get(i);

                btn.setAnimation(null);

                final View label = btn.getChildAt(1);
                final View iconContainer = btn.findViewById(Resources.getID("icon_container"));
                final View icon = btn.findViewById(Resources.getID("icon"));

                if (label != null && label instanceof TextView) {
                    TextView lb = ((TextView) label);
                    lb.setTextColor(inactiveColor);
                }
                if (icon != null && icon instanceof ImageView) {
                    ImageView icn = ((ImageView) icon);
                    icn.setColorFilter(inactiveColor);
                }

                ImageView wamod_badge_small = (ImageView) btn.findViewById(Resources.getID("wamod_badge_small"));

                if (wamod_badge_small != null) {
                    Drawable badge_small = btn.getResources().getDrawable(Resources.getDrawable("wamod_badge_small"));
                    badge_small.setColorFilter(inactiveColor, PorterDuff.Mode.MULTIPLY);
                    wamod_badge_small.setImageDrawable(badge_small);
                }

                if (((TextView) label).getTextSize() != originalLabelTextSize) {
                    Animation anim = new Animation() {
                        @Override
                        protected void applyTransformation(float interpolatedTime, Transformation t) {
                            float interpolatorTime = (-interpolatedTime) + 1;
                            float newTextSize = (labelMultiplier * interpolatorTime) + originalLabelTextSize;
                            ((TextView) label).setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);



                            int newPadding = (int) (originalIconTopMargin - (iconMultiplier * interpolatorTime));
                            if (iconContainer == null) icon.setPadding(0, newPadding, 0, 0);
                            else iconContainer.setPadding(0, newPadding, 0, 0);
                        }
                    };
                    anim.setDuration(100);
                    anim.setInterpolator(new AccelerateDecelerateInterpolator());
                    btn.startAnimation(anim);
                }
            }
        }
    }

    private void loadColors() {
        try {
            if (Utils.prefs.getBoolean("home_bottomnavigationbar_autocolor", true)) {

                backgroundColor = ColorsManager.getColor(ColorsManager.UI_ACTIVITY_BACKGROUND);
                inactiveColor = ColorsManager.getColor(ColorsManager.UI_ACTIVITY_TEXT_SECONDARY);

                activeColor = ColorsManager.getColor(ColorsManager.UI_ACTIVITY_TOOLBAR);
            } else {
                backgroundColor = ColorsManager.getColor(ColorsManager.UI_HOME_BOTTOMBAR_BACKGROUND);
                activeColor = ColorsManager.getColor(ColorsManager.UI_HOME_BOTTOMBAR_ACTIVEITEM);
                inactiveColor = ColorsManager.getColor(ColorsManager.UI_HOME_BOTTOMBAR_INACTIVEITEM);
            }
            setBackgroundColor(backgroundColor);

            ViewGroup wamod_badge_big = (ViewGroup) ((AppCompatActivity)ctx).findViewById(Resources.getID("wamod_badge_big"));
            if (wamod_badge_big != null) {
                Drawable badge_big = ctx.getResources().getDrawable(Resources.getDrawable("wamod_badge_big"));
                badge_big.setColorFilter(backgroundColor, PorterDuff.Mode.MULTIPLY);
                wamod_badge_big.setBackground(badge_big);
            }
        } catch (Exception e) {
            Utils.manageException(e);
        }
    }
}
