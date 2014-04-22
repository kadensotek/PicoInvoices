
package com.pico.picoinvoices;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class CsvExporter
{
    private static final String DATASUBDIRECTORY = "/Android/data/com.pico.picoinvoices/";

    private final SQLiteDatabase db;
    private CsvBuilder csvBuilder = null;

    public CsvExporter(final SQLiteDatabase db)
    {
        this.db = db;
    }
    
    public void export(final String dbName, final String exportFileNamePrefix) throws IOException
    {
        System.out.println("CSV: exporting database - " + dbName + " exportFileNamePrefix=" + exportFileNamePrefix);

        csvBuilder = new CsvBuilder();

        /* get the tables */
        String sql = "select * from sqlite_master";
        Cursor c = db.rawQuery(sql, new String[0]);
        
        /* Moves through tables */
        if (c.moveToFirst())
        {
            do
            {
                String tableName = c.getString(c.getColumnIndex("name"));

                /* skip metadata, sequence, and uidx (unique indexes) */
                if (!tableName.equals("android_metadata")
                        && !tableName.equals("sqlite_sequence")
                        && !tableName.startsWith("uidx"))
                {
                    exportTable(tableName);
                }
            } while (c.moveToNext());
        }
        
        String csvString = csvBuilder.end();
        writeToFile(csvString, exportFileNamePrefix + ".csv");
        
        System.out.println("Exporting database complete");
    }
    
    /* Pulls all rows and columns from given table */
    private void exportTable(final String tableName) throws IOException
    {
        csvBuilder.openTable(tableName);
        csvBuilder.newLine();
        String sql = "select * from " + tableName;
        Cursor c = db.rawQuery(sql, new String[0]);
        
        if (c.moveToFirst())
        {
            int cols = c.getColumnCount();
            
            do
            {                
                for (int i = 0; i < cols; i++)
                {
                    csvBuilder.addColumn(c.getString(i));
                    
                    /* Doesn't add comma after last column value */
                    if(i != cols-1)
                    {
                        csvBuilder.comma();
                    }
                }
                
                csvBuilder.newLine();
            } while (c.moveToNext());
        }
        
        c.close();
        csvBuilder.newLine();
    }
    
    @SuppressWarnings("resource")
    private void writeToFile(final String csvString, final String exportFileName) throws IOException
    {
        File dir = new File(Environment.getExternalStorageDirectory(), CsvExporter.DATASUBDIRECTORY);
        
        if (!dir.exists())
        {
            dir.mkdirs();
        }
        
        File file = new File(dir, exportFileName);
        file.createNewFile();

        ByteBuffer buff = ByteBuffer.wrap(csvString.getBytes());
        FileChannel channel = new FileOutputStream(file).getChannel();
        
        try
        {
            channel.write(buff);
        }
        finally
        {
            if (channel != null)
            {
                channel.close();
            }
        }
    }
    
    static class CsvBuilder
    {
        private static final String NEW_LINE = "\n";
        private static final String COMMA = ",";

        private final StringBuilder sb;

        public CsvBuilder() throws IOException
        {
            sb = new StringBuilder();
        }

        String end() throws IOException
        {
            return sb.toString();
        }

        void openTable(final String tableName)
        {
            sb.append(tableName);
        }
        
        void addColumn(final String val) throws IOException
        {
            /* checks to see if quotations are needed around value due to embedded comma */
            if(!val.contains(","))
            {
                sb.append(val);
            }
            else
            {
                sb.append("\"" + val + "\"");    /* quotes are added */
            }
        }
        
        void newLine()
        {
            sb.append(CsvBuilder.NEW_LINE);
        }
        
        void comma()
        {
            sb.append(CsvBuilder.COMMA);
        }
    }
    
    
}
