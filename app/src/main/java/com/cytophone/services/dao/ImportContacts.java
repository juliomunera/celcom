package com.cytophone.services.dao;

import android.database.sqlite.SQLiteDatabase;
import com.cytophone.services.utilities.Utils;
import java.io.InputStreamReader;
import android.database.Cursor;
import java.nio.charset.Charset;
import android.content.Context;
import android.database.Cursor;
import com.opencsv.CSVReader;
import java.io.InputStream;

public class ImportContacts {
    public static void importCSV(Context context)
    {
        SQLiteDatabase db;
        try {
            String dbPath = context.getDatabasePath("cytophone-db.dat").
                    getAbsolutePath();

            InputStream is = context.getAssets().open("GrupoCarmelo.txt");
            InputStreamReader reader = new InputStreamReader(is, Charset.forName("UTF-8"));
            CSVReader csvReader = new CSVReader(reader,';');
            db = SQLiteDatabase.openDatabase(dbPath,
                    null,
                    SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.NO_LOCALIZED_COLLATORS);

            String sql = "DELETE FROM Party;";
            db.execSQL(sql);

            sql = "INSERT INTO Party (" +
                "  createdDate, countryCode, number, name, placeID, roleID, updatedDate " +
                " ) VALUES ( ";

            db.beginTransaction();
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                System.out.println(nextLine[0] + nextLine[1] + "etc...");
                StringBuilder sb = new StringBuilder(sql);
                String number = Utils.encodeBase64( nextLine[2] );

                sb.append("'2021-03-07 20:00',");
                sb.append("'" + nextLine[0] + "',");
                sb.append("'" + number + "',");
                sb.append("'" + nextLine[4] + "',");
                sb.append("'" + nextLine[3] + "',");
                sb.append("'2',");
                sb.append("NULL);");

                db.execSQL(sb.toString());
            }
            db.setTransactionSuccessful();
            db.endTransaction();

            Cursor resulSet = db.rawQuery("SELECT COUNT (*) FROM Party", null);
            resulSet.moveToFirst();

            int count= resulSet.getInt(0);
            resulSet.close();
            db.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
