package com.gusticahya.thesocialmedia.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "thesocialmedia"

        // Table name and columns
        private const val TABLE_NAME = "users"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_BIO = "bio"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"

        // Create table SQL statement
        private const val CREATE_TABLE = (
                "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "$COLUMN_NAME TEXT, $COLUMN_EMAIL TEXT, $COLUMN_BIO TEXT, $COLUMN_USERNAME TEXT, $COLUMN_PASSWORD TEXT)"
                )

        // Table name and columns for posts
        private const val POST_TABLE_NAME = "posts"
        private const val POST_COLUMN_ID = "_id"
        private const val POST_COLUMN_USER_ID = "user_id" // Foreign key referencing users table
        private const val POST_COLUMN_CONTENT = "content"

        // Create table SQL statement for posts
        private const val CREATE_POST_TABLE = (
                "CREATE TABLE $POST_TABLE_NAME ($POST_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "$POST_COLUMN_USER_ID INTEGER, " +
                        "$POST_COLUMN_CONTENT TEXT, " +
                        "FOREIGN KEY ($POST_COLUMN_USER_ID) REFERENCES $TABLE_NAME($COLUMN_ID))"
                )
    }

    object UserCache {
        private var userId: Int? = null

        fun getUserId(): Int? {
            return userId
        }

        fun setUserId(id: Int) {
            userId = id
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
        db.execSQL(CREATE_POST_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")

        // Create tables again
        onCreate(db)
    }

    fun addUser(username: String, password: String, callback: () -> Unit) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
        }
        val success = db.insert(TABLE_NAME, null, values) != -1L
        db.close()
        if (success) {
            // Call the callback function if user added successfully
            callback()
        }
    }

    fun updateProfile(userId: Int, name: String, email: String, bio: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_EMAIL, email)
            put(COLUMN_BIO, bio)
        }

        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(userId.toString())
        val rowsAffected = db.update(TABLE_NAME, contentValues, whereClause, whereArgs)
        db.close()
        return rowsAffected > 0
    }

    fun loginUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val query = "SELECT $COLUMN_ID FROM $TABLE_NAME WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))
        val userExists = cursor.moveToFirst()
        val columnIndex = cursor.getColumnIndex(COLUMN_ID)
        if (userExists) {
            var userId = 0
            if(columnIndex != -1) {
                userId = cursor.getInt(columnIndex)
            }
            UserCache.setUserId(userId)
        }
        cursor.close()
        db.close()
        return userExists
    }

    fun getUserInfo(userId: Int): User? {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
        var userInfo: User? = null
        if (cursor.moveToFirst()) {
            val nameIndex = cursor.getColumnIndex(COLUMN_NAME)
            val emailIndex = cursor.getColumnIndex(COLUMN_EMAIL)
            val bioIndex = cursor.getColumnIndex(COLUMN_BIO)
            val usernameIndex = cursor.getColumnIndex(COLUMN_USERNAME)

            val name = if (nameIndex != -1) cursor.getString(nameIndex) else null
            val email = if (nameIndex != -1) cursor.getString(emailIndex) else null
            val bio = if (nameIndex != -1) cursor.getString(bioIndex) else null
            val username = if (nameIndex != -1) cursor.getString(usernameIndex) else null

            userInfo = User(userId, name ?: "", email ?: "", bio ?: "", username ?: "")
        }
        cursor.close()
        db.close()
        return userInfo
    }

    fun addPost(userId: Int, content: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(POST_COLUMN_USER_ID, userId)
            put(POST_COLUMN_CONTENT, content)
        }
        val success = db.insert(POST_TABLE_NAME, null, values) != -1L
        db.close()
        return success
    }

    fun getAllPosts(): List<Post> {
        val db = readableDatabase
        val posts = mutableListOf<Post>()
        val query = "SELECT * FROM $POST_TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        val postIdIndex = cursor.getColumnIndex(POST_COLUMN_ID)
        val userIdIndex = cursor.getColumnIndex(POST_COLUMN_USER_ID)
        val contentIndex = cursor.getColumnIndex(POST_COLUMN_CONTENT)

        while (cursor.moveToNext()) {
            val postId = cursor.getIntOrNull(postIdIndex)
            val userId = cursor.getIntOrNull(userIdIndex)
            val content = cursor.getStringOrNull(contentIndex)
            val username = ""
            postId?.let {
                posts.add(Post(it, userId ?: -1, content ?: "", username))
            }
        }
        cursor.close()
        db.close()
        return posts
    }



    data class User(val id: Int, val name: String, val email: String, val bio: String, val username: String)
    data class Post(val id: Int, val userId: Int, val content: String, val username: String)
}
