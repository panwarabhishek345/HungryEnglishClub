package app.com.HungryEnglish.Activity.Admin;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.Adapter.ReportAdapter;
import app.com.HungryEnglish.Adapter.TeacherListAdapter;
import app.com.HungryEnglish.Model.Report.Datum;
import app.com.HungryEnglish.Model.Report.ReportModel;
import app.com.HungryEnglish.Model.Teacher.TeacherListMainResponse;
import app.com.HungryEnglish.Model.Teacher.TeacherListResponse;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Services.ApiHandler;
import app.com.HungryEnglish.Util.Utils;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Bhadresh Chavada on 27-07-2017.
 */

public class ReportActivity extends BaseActivity {

    RecyclerView reportList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);

        reportList = (RecyclerView) findViewById(R.id.report_list);
        getReportDetails();
//        GenerateReport();
    }

    private void GenerateReport() {

        final String fileName = "TodoList.xls";

        //Saving file in external storage
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/javatechig.todo");

        //create directory if not exist
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }

        //file path
        File file = new File(directory, fileName);

        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;

        try {
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("MyShoppingList", 0);

            try {
                sheet.addCell(new Label(0, 0, "Subject")); // column and row
                sheet.addCell(new Label(1, 0, "Description"));
//                if (cursor.moveToFirst()) {
//                    do {
//                        String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TODO_SUBJECT));
//                        String desc = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TODO_DESC));
//
//                        int i = cursor.getPosition() + 1;
//                        sheet.addCell(new Label(0, i, title));
//                        sheet.addCell(new Label(1, i, desc));
//                    } while (cursor.moveToNext());
//                }
//                //closing cursor
//                cursor.close();
            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void getReportDetails() {
        if (!Utils.checkNetwork(ReportActivity.this)) {

            Utils.showCustomDialog("Internet Connection !", getResources().getString(R.string.internet_connection_error), ReportActivity.this);

            return;
        } else {
            Utils.showDialog(ReportActivity.this);
            ApiHandler.getApiService().getReportList(new retrofit.Callback<ReportModel>() {

                @Override
                public void success(ReportModel reportModel, Response response) {
                    Utils.dismissDialog();
                    if (reportModel == null) {
                        Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (reportModel.getStatus() == null) {
                        Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (reportModel.getStatus().equals("false")) {

                        Toast.makeText(getApplicationContext(), "" + reportModel.getMsg(), Toast.LENGTH_SHORT).show();

                        return;
                    }
                    if (reportModel.getStatus().equals("true")) {

                        List<Datum> reportArraylist  = new ArrayList<Datum>();
                        reportArraylist = reportModel.getData();

                        ReportAdapter reportAdapter = new ReportAdapter(ReportActivity.this, reportArraylist);
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        reportList.setLayoutManager(mLayoutManager);
                        reportList.setItemAnimator(new DefaultItemAnimator());
                        reportList.setAdapter(reportAdapter);

                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    error.getMessage();
                    Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
