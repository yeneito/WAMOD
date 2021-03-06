package com.wamod.WAclass;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.wamod.ColorsManager;
import com.wamod.Resources;
import com.wamod.Utils;

/**
 * Created by BrianValente on 3/28/16.
 */
public class StarredMessagesActivity extends AppCompatActivity {
    public static void _onCreate(AppCompatActivity a) {
        ViewGroup empty = (ViewGroup) a.findViewById(android.R.id.empty);
        if (empty != null) {
            empty.setBackgroundColor(ColorsManager.getColor(ColorsManager.UI_ACTIVITY_BACKGROUND));
            ViewGroup empty_view = (ViewGroup) empty.getChildAt(0);
            for (int i = 0; i < empty_view.getChildCount(); i++) {
                View v = empty_view.getChildAt(i);
                if (v instanceof TextView)
                    ((TextView) empty_view.getChildAt(i)).setTextColor(ColorsManager.getColor(ColorsManager.UI_ACTIVITY_TEXT_PRIMARY));
                else if (v instanceof ImageView)
                    ((ImageView) empty_view.getChildAt(i)).setImageDrawable(Utils.tintToColor(((ImageView) empty_view.getChildAt(i)).getDrawable(), Color.WHITE));
            }
        }

        ListView list = (ListView) a.findViewById(android.R.id.list);
        if (list != null) list.setBackgroundColor(ColorsManager.getColor(ColorsManager.UI_ACTIVITY_BACKGROUND));
    }


    /* Called on
     *    com.whatsapp.kj.getView(ILandroid/view/View;Landroid/view/ViewGroup;)Landroid/view/View;
     * Before
     *    return-object p2
     * Smali
     *    invoke-static {p2}, Lcom/wamod/WAclass/StarredMessagesActivity;->_getView(Landroid/view/View;)Landroid/view/View;
     */
    public static View _getView(View v) {
        TextView sender_name    = (TextView) v.findViewById(Resources.id.sender_name);
        TextView bullet         = (TextView) v.findViewById(Resources.id.bullet);
        TextView recipient_name = (TextView) v.findViewById(Resources.id.recipient_name);
        TextView message_date   = (TextView) v.findViewById(Resources.id.message_date);
        ImageView chevron       = (ImageView) v.findViewById(Resources.id.chevron);

        sender_name.setTextColor(ColorsManager.getColor(ColorsManager.UI_ACTIVITY_TEXT_PRIMARY));
        bullet.setTextColor(ColorsManager.getColor(ColorsManager.UI_ACTIVITY_TEXT_PRIMARY));
        recipient_name.setTextColor(ColorsManager.getColor(ColorsManager.UI_ACTIVITY_TEXT_PRIMARY));
        message_date.setTextColor(ColorsManager.getColor(ColorsManager.UI_ACTIVITY_TEXT_SECONDARY));
        chevron.setImageDrawable(Utils.tintToColor(chevron.getDrawable(), ColorsManager.getColor(ColorsManager.UI_ACTIVITY_TEXT_SECONDARY)));

        return v;
    }

    public static void callGetView() {
        View v = _getView(null);
    }
}
