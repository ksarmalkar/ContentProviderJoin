package com.ksarmalkar.twotables;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import com.ksarmalkar.twotables.cursoradapter.DepartmentCursorAdapter;
import com.ksarmalkar.twotables.loadermanager.DepartmentLoaderManager;
import com.ksarmalkar.twotables.model.Department;

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

        findViewById(R.id.join_button).setVisibility(View.GONE);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.departments_button:
                finish();
                break;
        }
    }
}
