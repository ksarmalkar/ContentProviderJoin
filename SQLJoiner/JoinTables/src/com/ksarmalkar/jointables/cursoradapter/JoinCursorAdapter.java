package com.ksarmalkar.jointables.cursoradapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import com.ksarmalkar.jointables.R;

public class JoinCursorAdapter extends SimpleCursorAdapter {

    public JoinCursorAdapter(Context context, int layout, Cursor c,
                                   String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        final View viewButton = view.findViewById(R.id.delete_button);
        viewButton.setVisibility(View.GONE);
    }
}
