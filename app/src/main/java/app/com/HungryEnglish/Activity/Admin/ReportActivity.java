package app.com.HungryEnglish.Activity.Admin;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

import app.com.HungryEnglish.Activity.BaseActivity;
import app.com.HungryEnglish.R;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * Created by Bhadresh Chavada on 27-07-2017.
 */

public class ReportActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        GenerateReport();
    }

    private void GenerateReport() {

        final String fileName = "TodoList.xls";

        //Saving file in external storage
        File sdCard = Environment.getExternalStorageDirectory();
        File directory = new File(sdCard.getAbsolutePath() + "/javatechig.todo");

        //create directory if not exist
        if(!directory.isDirectory()){
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
}
