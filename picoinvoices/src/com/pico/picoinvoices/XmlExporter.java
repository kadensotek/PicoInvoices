package com.pico.picoinvoices;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class XmlExporter
{
    private static final String DATASUBDIRECTORY = "/Android/data/com.pico.picoinvoices/";

    private final SQLiteDatabase db;
    private XmlBuilder xmlBuilder = null;

    public XmlExporter(final SQLiteDatabase db)
    {
        this.db = db;
    }

    public void export(final String dbName, final String exportFileNamePrefix) throws IOException
    {
        System.out.println("exporting database - " + dbName + " exportFileNamePrefix=" + exportFileNamePrefix);

        xmlBuilder = new XmlBuilder();
        xmlBuilder.start(dbName);

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
        
        String xmlString = xmlBuilder.end(dbName);
        writeToFile(xmlString, exportFileNamePrefix + ".xml");
        
        System.out.println("exporting database complete");
    }

    /* Pulls all rows and columns from given table */
    private void exportTable(final String tableName) throws IOException
    {
        xmlBuilder.openTable(tableName);
        String sql = "select * from " + tableName;
        Cursor c = db.rawQuery(sql, new String[0]);
        
        if (c.moveToFirst())
        {
            int cols = c.getColumnCount();
            
            do
            {
                xmlBuilder.openRow(tableName);
                
                for (int i = 0; i < cols; i++)
                {
                    xmlBuilder.addColumn(c.getColumnName(i), c.getString(i));
                }
                
                xmlBuilder.closeRow(tableName);
            } while (c.moveToNext());
        }
        
        c.close();
        xmlBuilder.closeTable(tableName);
    }

    private void writeToFile(final String xmlString, final String exportFileName) throws IOException
    {
        File dir = new File(Environment.getExternalStorageDirectory(), XmlExporter.DATASUBDIRECTORY);
        
        if (!dir.exists())
        {
            dir.mkdirs();
        }
        
        File file = new File(dir, exportFileName);
        file.createNewFile();

        ByteBuffer buff = ByteBuffer.wrap(xmlString.getBytes());
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

    private static class XmlBuilder
    {
        private static final String OPEN_XML_STANZA = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
        private static final String DB_OPEN = "<database name='";
        private static final String DB_CLOSE = "</database>";
        private static final String OPEN_TAG_START = "<";
        private static final String CLOSE_TAG_START = "</";
        private static final String TAG_END = ">";

        private final StringBuilder sb;

        public XmlBuilder() throws IOException
        {
            sb = new StringBuilder();
        }

        void start(final String dbName)
        {
            sb.append(XmlBuilder.OPEN_XML_STANZA + "\n");
            sb.append(XmlBuilder.OPEN_TAG_START + dbName + XmlBuilder.TAG_END + "\n");
        }

        String end(final String dbName) throws IOException
        {
            sb.append(XmlBuilder.CLOSE_TAG_START + dbName
                    + XmlBuilder.TAG_END + "\n");
            return sb.toString();
        }

        void openTable(final String tableName)
        {
            sb.append(XmlBuilder.OPEN_TAG_START + tableName 
                    + XmlBuilder.TAG_END + "\n");
        }

        void closeTable(final String tableName)
        {
            sb.append(XmlBuilder.CLOSE_TAG_START + tableName 
                    + XmlBuilder.TAG_END + "\n");
        }

        void openRow(final String tableName)
        {
            sb.append("    " + XmlBuilder.OPEN_TAG_START 
                    + tableName.substring(0, tableName.length()-1)
                    + XmlBuilder.TAG_END + "\n");
        }

        void closeRow(final String tableName)
        {           
            sb.append("    " + XmlBuilder.CLOSE_TAG_START 
                    + tableName.substring(0, tableName.length()-1)
                    + XmlBuilder.TAG_END + "\n");
        }

        void addColumn(final String name, String val) throws IOException
        {
            /* Escapes & and ? */
            val = val.replace("&","&amp;");
            val = val.replace("?","&#63;");
            
            sb.append("        " + XmlBuilder.OPEN_TAG_START + name + XmlBuilder.TAG_END
                    + val 
                    + XmlBuilder.CLOSE_TAG_START + name + XmlBuilder.TAG_END + "\n");
        }
    }
}
