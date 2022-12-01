package com.example.learning2;

import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;

public interface DialogCloseListener {
    boolean onMenuItemClick(MenuItem item);

    public void handleDialogClose(DialogInterface dialog);

    boolean onCreateOptionsMenu(Menu menu);
}
