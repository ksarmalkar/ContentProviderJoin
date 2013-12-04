package com.ksarmalkar.jointables;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import com.ksarmalkar.jointables.cursoradapter.DepartmentCursorAdapter;
import com.ksarmalkar.jointables.loader.DepartmentLoaderManager;
import com.ksarmalkar.jointables.model.Department;

public class DepartmentActivity extends ListActivity {

    private SimpleCursorAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);

        String[] uiBindFrom = {  Department.COL_COMPANY_NAME, Department.COL_DEPARTMENT_NAME};
        int[] uiBindTo = {R.id.first_name_text, R.id.last_name_text};

        /*Empty adapter that is used to display the loaded data*/
        mAdapter = new DepartmentCursorAdapter(this,R.layout.person_layout, null, uiBindFrom, uiBindTo,0);
        setListAdapter(mAdapter);
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(1, null, new DepartmentLoaderManager(this, mAdapter));

        findViewById(R.id.group).setVisibility(View.GONE);
        final Button backButton = (Button) findViewById(R.id.departments_button);
        backButton.setText(R.string.back);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.departments_button:
                finish();
                break;
            case R.id.join_button:
                startActivity(new Intent(this, JoinActivity.class));
                break;
        }
    }
}
