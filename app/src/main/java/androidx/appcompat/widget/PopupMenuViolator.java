package androidx.appcompat.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ListView;

public class PopupMenuViolator extends PopupMenu{
    public PopupMenuViolator(Context context, View view) {
        super(context, view);
    }

    @SuppressLint("RestrictedApi")
    public ListView getMenuListView(){
        return super.getMenuListView();
    }

}
