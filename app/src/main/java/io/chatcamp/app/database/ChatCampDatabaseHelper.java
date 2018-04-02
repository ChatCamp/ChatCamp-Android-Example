package io.chatcamp.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import io.chatcamp.app.ConversationMessage;
import io.chatcamp.sdk.BaseChannel;
import io.chatcamp.sdk.Message;

import static io.chatcamp.app.database.ChatCampDatabaseContract.DATABASE_NAME;
import static io.chatcamp.app.database.ChatCampDatabaseContract.DATABASE_VERSION;
import static io.chatcamp.app.database.ChatCampDatabaseContract.MessageEntry;

/**
 * Created by shubhamdhabhai on 15/03/18.
 */

public class ChatCampDatabaseHelper extends SQLiteOpenHelper {

    public ChatCampDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_MESSAGE_TABLE = "CREATE TABLE IF NOT EXISTS " + MessageEntry.TABLE_NAME + "("
                + MessageEntry._ID + " INTEGER PRIMARY KEY," + MessageEntry.COLUMN_NAME_CHANNEL_ID + " TEXT,"
                + MessageEntry.COLUMN_NAME_MESSAGE + " TEXT," + MessageEntry.COLUMN_NAME_CHANNEL_TYPE + " TEXT,"
                + MessageEntry.COLUMN_NAME_TIME_STAMP + " INTEGER" + ")";
        sqLiteDatabase.execSQL(CREATE_MESSAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MessageEntry.TABLE_NAME);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public void addMessages(List<ConversationMessage> conversationMessages, String channelId, BaseChannel.ChannelType channelType) {
        SQLiteDatabase sqliteDatabase = this.getWritableDatabase();
        sqliteDatabase.delete(MessageEntry.TABLE_NAME, MessageEntry.COLUMN_NAME_CHANNEL_ID + " =? AND "
                + MessageEntry.COLUMN_NAME_CHANNEL_TYPE + "=?", new String[]{channelId, channelType.name()});

        for (int i = 0; i < conversationMessages.size(); ++i) {
            ContentValues values = new ContentValues();
            values.put(MessageEntry.COLUMN_NAME_MESSAGE, conversationMessages.get(i).getMessage().serialize()); // serialized message
            values.put(MessageEntry.COLUMN_NAME_CHANNEL_ID, channelId); // Group ID
            values.put(MessageEntry.COLUMN_NAME_CHANNEL_TYPE, channelType.name()); // Group Type
            values.put(MessageEntry.COLUMN_NAME_TIME_STAMP, conversationMessages.get(i).getMessage().getInsertedAt()); // Time of message inserted
            sqliteDatabase.insert(MessageEntry.TABLE_NAME, null, values);
        }
    }

    public void addMessage(ConversationMessage conversationMessage, String channelId, BaseChannel.ChannelType channelType) {
        SQLiteDatabase sqliteDatabase = this.getWritableDatabase();
        sqliteDatabase.execSQL("DELETE FROM " + MessageEntry.TABLE_NAME + " WHERE "
                + MessageEntry.COLUMN_NAME_CHANNEL_ID + " = '" + channelId + "' AND "
                + MessageEntry.COLUMN_NAME_CHANNEL_TYPE + " = '" + channelType.name() +  "' AND "
                + MessageEntry._ID + " NOT IN (SELECT " + MessageEntry._ID + " FROM " + MessageEntry.TABLE_NAME + " WHERE "
                + MessageEntry.COLUMN_NAME_CHANNEL_ID + " = '" + channelId + "' AND "
                + MessageEntry.COLUMN_NAME_CHANNEL_TYPE + " = '" + channelType.name() +  "' AND "
                + MessageEntry._ID + " ORDER BY " + MessageEntry.COLUMN_NAME_TIME_STAMP +
                "  DESC LIMIT 19)");

        ContentValues values = new ContentValues();
        values.put(MessageEntry.COLUMN_NAME_MESSAGE, conversationMessage.getMessage().serialize()); // serialized message
        values.put(MessageEntry.COLUMN_NAME_CHANNEL_ID, channelId); // Group ID
        values.put(MessageEntry.COLUMN_NAME_CHANNEL_TYPE, channelType.name()); // Group ID
        values.put(MessageEntry.COLUMN_NAME_TIME_STAMP, conversationMessage.getMessage().getInsertedAt()); // Time of message inserted
        sqliteDatabase.insert(MessageEntry.TABLE_NAME, null, values);
    }

    public List<ConversationMessage> getMessages(String channelId, BaseChannel.ChannelType channelType) {
        SQLiteDatabase db = this.getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                MessageEntry.COLUMN_NAME_MESSAGE,
                MessageEntry.COLUMN_NAME_CHANNEL_ID
        };
        String selection = MessageEntry.COLUMN_NAME_CHANNEL_ID + " = ? AND " + MessageEntry.COLUMN_NAME_CHANNEL_TYPE + " = ?";
        String[] selectionArgs = {channelId, channelType.name()};
        String sortOrder =
                MessageEntry.COLUMN_NAME_TIME_STAMP + " DESC";

        Cursor cursor = db.query(
                MessageEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        List<ConversationMessage> conversationMessages = new ArrayList<>();
        while (cursor.moveToNext()) {
            String message = cursor.getString(
                    cursor.getColumnIndexOrThrow(MessageEntry.COLUMN_NAME_MESSAGE));
            ConversationMessage conversationMessage = new ConversationMessage(Message.createfromSerializedData(message));
            conversationMessages.add(conversationMessage);
        }
        cursor.close();
        return conversationMessages;
    }
}
