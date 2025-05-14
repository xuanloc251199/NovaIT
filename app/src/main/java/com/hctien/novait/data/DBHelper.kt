package com.hctien.novait.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.hctien.novait.Story
import com.hctien.novait.Category
import com.hctien.novait.User
import com.hctien.novait.model.Author
import com.hctien.novait.model.Chapter
import com.hctien.novait.model.Comment
import com.hctien.novait.model.CommentDetail

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "novait.db"
        const val DATABASE_VERSION = 1

        // Bảng người dùng
        const val TABLE_USERS = "users"
        const val COLUMN_ID = "_id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_ROLE = "role"
        const val COLUMN_DISPLAY_NAME = "display_name"

        // Bảng thể loại
        const val TABLE_CATEGORY = "category"
        const val COLUMN_CATEGORY_ID = "category_id"
        const val COLUMN_CATEGORY_NAME = "category_name"

        // Bảng truyện
        const val TABLE_STORIES = "stories"
        const val COLUMN_TITLE = "title"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CHAPTER_COUNT = "chapter_count"
        const val COLUMN_VIEW_COUNT = "view_count"
        const val COLUMN_RATING = "rating"
        const val COLUMN_CATEGORY_ID_FK = "category_id"  // Khóa ngoại đến bảng category
        const val COLUMN_STATUS = "status"
        const val COLUMN_COVER_IMAGE_URL = "cover_image_url"

        // Bảng chương
        const val TABLE_CHAPTERS = "chapters"
        const val COLUMN_CHAPTER_ID = "chapter_id"
        const val COLUMN_STORY_ID_FK = "story_id"  // Khóa ngoại đến bảng stories
        const val COLUMN_CHAPTER_TITLE = "chapter_title"
        const val COLUMN_CHAPTER_CONTENT = "chapter_content"

        //Bảng bình luận
        const val TABLE_COMMENTS = "comments"
        const val COLUMN_COMMENT_ID = "comment_id"
        const val COLUMN_COMMENT_STORY_ID = "story_id"
        const val COLUMN_COMMENT_USER_ID = "user_id"
        const val COLUMN_COMMENT_TEXT = "comment_text"
        const val COLUMN_COMMENT_TIMESTAMP = "timestamp"

        // Bảng tác giả
        const val TABLE_AUTHORS = "authors"
        const val COLUMN_AUTHOR_ID = "author_id"
        const val COLUMN_AUTHOR_NAME = "name"
        const val COLUMN_AUTHOR_BIOGRAPHY = "biography"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Tạo bảng người dùng
        val CREATE_USERS_TABLE = ("CREATE TABLE $TABLE_USERS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USERNAME TEXT, " +
                "$COLUMN_PASSWORD TEXT, " +
                "$COLUMN_ROLE TEXT, " +
                "$COLUMN_DISPLAY_NAME TEXT)")
        db?.execSQL(CREATE_USERS_TABLE)


        // Tạo bảng thể loại
        val CREATE_CATEGORY_TABLE = ("CREATE TABLE $TABLE_CATEGORY ("
                + "$COLUMN_CATEGORY_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_CATEGORY_NAME TEXT)")
        db?.execSQL(CREATE_CATEGORY_TABLE)

        // Tạo bảng truyện
        val CREATE_STORIES_TABLE = ("CREATE TABLE $TABLE_STORIES ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_TITLE TEXT, "
                + "$COLUMN_CONTENT TEXT, "
                + "$COLUMN_AUTHOR TEXT, "
                + "$COLUMN_CHAPTER_COUNT INTEGER, "
                + "$COLUMN_VIEW_COUNT INTEGER, "
                + "$COLUMN_RATING REAL, "
                + "$COLUMN_CATEGORY_ID_FK INTEGER, "
                + "$COLUMN_STATUS TEXT, "
                + "$COLUMN_COVER_IMAGE_URL TEXT, "
                + "FOREIGN KEY($COLUMN_CATEGORY_ID_FK) REFERENCES $TABLE_CATEGORY($COLUMN_CATEGORY_ID))")
        db?.execSQL(CREATE_STORIES_TABLE)

        // Tạo bảng comment
        val CREATE_COMMENTS_TABLE = ("CREATE TABLE $TABLE_COMMENTS (" +
                "$COLUMN_COMMENT_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_COMMENT_STORY_ID INTEGER, " +
                "$COLUMN_COMMENT_USER_ID INTEGER, " +
                "$COLUMN_COMMENT_TEXT TEXT, " +
                "$COLUMN_COMMENT_TIMESTAMP INTEGER, " +
                "FOREIGN KEY($COLUMN_COMMENT_STORY_ID) REFERENCES $TABLE_STORIES($COLUMN_ID), " +
                "FOREIGN KEY($COLUMN_COMMENT_USER_ID) REFERENCES $TABLE_USERS($COLUMN_ID))")
        db?.execSQL(CREATE_COMMENTS_TABLE)

        // Tạo bảng chương
        val CREATE_CHAPTERS_TABLE = ("CREATE TABLE $TABLE_CHAPTERS ("
                + "$COLUMN_CHAPTER_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_STORY_ID_FK INTEGER, "
                + "$COLUMN_CHAPTER_TITLE TEXT, "
                + "$COLUMN_CHAPTER_CONTENT TEXT, "
                + "FOREIGN KEY($COLUMN_STORY_ID_FK) REFERENCES $TABLE_STORIES($COLUMN_ID))")
        db?.execSQL(CREATE_CHAPTERS_TABLE)

        // Tạo bảng tác giả
        val CREATE_AUTHORS_TABLE = ("CREATE TABLE $TABLE_AUTHORS (" +
                "$COLUMN_AUTHOR_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_AUTHOR_NAME TEXT, " +
                "$COLUMN_AUTHOR_BIOGRAPHY TEXT)")
        db?.execSQL(CREATE_AUTHORS_TABLE)

        // Thêm dữ liệu mẫu vào bảng thể loại và truyện
        populateDatabase(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < DATABASE_VERSION) {
            // Nếu cơ sở dữ liệu cũ, xóa các bảng cũ và tạo lại
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORY")
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_STORIES")
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_CHAPTERS")
            db?.execSQL("DROP TABLE IF EXISTS $TABLE_AUTHORS")
            onCreate(db)
        }
    }

    // Thêm dữ liệu mẫu vào cơ sở dữ liệu
    private fun populateDatabase(db: SQLiteDatabase?) {

        // Thêm dữ liệu vào bảng người dùng với vai trò admin và client
        val users = listOf(
            ContentValues().apply {
                put(COLUMN_USERNAME, "admin")
                put(COLUMN_PASSWORD, "admin123")
                put(COLUMN_ROLE, "admin")
                put(COLUMN_DISPLAY_NAME, "Administrator")
            },
            ContentValues().apply {
                put(COLUMN_USERNAME, "client")
                put(COLUMN_PASSWORD, "client123")
                put(COLUMN_ROLE, "client")
                put(COLUMN_DISPLAY_NAME, "Client User")
            }
        )

        users.forEach {
            db?.insert(TABLE_USERS, null, it)
        }

        // Thêm dữ liệu vào bảng thể loại
        val categories = listOf("Fantasy", "Adventure", "Romance", "Horror")
        categories.forEach {
            val values = ContentValues()
            values.put(COLUMN_CATEGORY_NAME, it)
            db?.insert(TABLE_CATEGORY, null, values)
        }

        // Thêm dữ liệu vào bảng truyện
        val stories = listOf(
            Story(0, "The Great Adventure", "A thrilling adventure in the jungle.", "John Doe", 20, 500000, 4.5f, 1, "Mới đăng", "https://static2.truyenchuhay.vn/images/chi-ton-tu-la.jpg"),
            Story(0, "Love at First Sight", "A romantic story of two hearts meeting.", "Jane Smith", 50, 2000000, 4.7f, 2, "Mới cập nhật", "https://static2.truyenchuhay.vn/images/chi-ton-tu-la.jpg"),
            Story(0, "Haunted House", "A spine-chilling horror story.", "Alice Johnson", 10, 1000000, 4.2f, 3, "Truyện hoàn thành", "https://static2.truyenchuhay.vn/images/chi-ton-tu-la.jpg"),
            Story(0, "Mystery of the Lost Treasure", "An exciting treasure hunt in an ancient city.", "Bob Lee", 15, 1500000, 4.6f, 4, "Mới đăng", "https://static2.truyenchuhay.vn/images/chi-ton-tu-la.jpg")
        )
        stories.forEach {
            val values = ContentValues()
            values.put(COLUMN_TITLE, it.title)
            values.put(COLUMN_CONTENT, it.content)
            values.put(COLUMN_AUTHOR, it.author)
            values.put(COLUMN_CHAPTER_COUNT, it.chapterCount)
            values.put(COLUMN_VIEW_COUNT, it.viewCount)
            values.put(COLUMN_RATING, it.rating)
            values.put(COLUMN_CATEGORY_ID_FK, it.categoryId)
            values.put(COLUMN_STATUS, it.status)
            values.put(COLUMN_COVER_IMAGE_URL, it.coverImageUrl)
            db?.insert(TABLE_STORIES, null, values)
        }

        // Thêm dữ liệu vào bảng chương
        val chapters = listOf(
            Chapter(0, 1, "Chapter 1: The Adventure Begins", "In this chapter, our hero begins his journey."),
            Chapter(0, 1, "Chapter 2: The Love Story", "This chapter focuses on the blossoming romance between two characters.")
        )
        chapters.forEach {
            val values = ContentValues()
            values.put(COLUMN_STORY_ID_FK, it.storyId)
            values.put(COLUMN_CHAPTER_TITLE, it.title)
            values.put(COLUMN_CHAPTER_CONTENT, it.content)
            db?.insert(TABLE_CHAPTERS, null, values)
        }
    }

    //-------------------- Phương thức cho bảng người dùng -------------------- //

    // Thêm người dùng mới
    fun addUser(username: String, password: String, displayName: String): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USERNAME, username)  // Sử dụng username để đăng nhập
        values.put(COLUMN_PASSWORD, password)
        values.put(COLUMN_ROLE, "client")
        values.put(COLUMN_DISPLAY_NAME, displayName)
        val result = db.insert(TABLE_USERS, null, values)
        db.close()
        return result
    }

    // Kiểm tra thông tin đăng nhập
    @SuppressLint("Range")
    fun checkUser(username: String, password: String): User? {
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(username, password)
        )

        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
            val username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME))
            val password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD))
            val role = cursor.getString(cursor.getColumnIndex(COLUMN_ROLE))
            val displayName = cursor.getString(cursor.getColumnIndex(COLUMN_DISPLAY_NAME))

            cursor.close()
            db.close()

            return User(id, username, password, role, displayName)
        }

        cursor.close()
        db.close()
        return null
    }

    @SuppressLint("Range")
    fun getUserDisplayName(userId: Int): String {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COLUMN_DISPLAY_NAME FROM $TABLE_USERS WHERE $COLUMN_ID = ?",
            arrayOf(userId.toString())
        )

        var displayName = "No Name"
        if (cursor.moveToFirst()) {
            displayName = cursor.getString(cursor.getColumnIndex(COLUMN_DISPLAY_NAME))
        }
        cursor.close()
        db.close()
        return displayName
    }

    @SuppressLint("Range")
    fun getAllClients(): MutableList<User> {
        val db = readableDatabase
        val users = mutableListOf<User>()
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $COLUMN_ROLE = 'client'", null)
        if (cursor.moveToFirst()) {
            do {
                val user = User(
                    id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)),
                    password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)),
                    role = cursor.getString(cursor.getColumnIndex(COLUMN_ROLE)),
                    displayName = cursor.getString(cursor.getColumnIndex(COLUMN_DISPLAY_NAME))
                )
                users.add(user)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return users
    }

    fun updateDisplayName(userId: Int, newDisplayName: String): Int {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_DISPLAY_NAME, newDisplayName)
        val result = db.update(TABLE_USERS, values, "$COLUMN_ID = ?", arrayOf(userId.toString()))
        db.close()
        return result
    }

    fun updatePassword(username: String, newPassword: String): Int {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_PASSWORD, newPassword)
        val result = db.update(TABLE_USERS, values, "$COLUMN_USERNAME = ?", arrayOf(username))
        db.close()
        return result
    }

    fun deleteUser(userId: Int): Int {
        val db = writableDatabase
        val result = db.delete(TABLE_USERS, "$COLUMN_ID = ?", arrayOf(userId.toString()))
        db.close()
        return result
    }

    //-------------------- Phương thức cho bảng truyện -------------------- //

    // Lấy tất cả truyện
    @SuppressLint("Range")
    fun getAllStories(): List<Story> {
        val stories = mutableListOf<Story>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_STORIES", null)

        if (cursor.moveToFirst()) {
            do {
                val categoryId = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID_FK))

                // Lấy tên thể loại từ bảng category
                val categoryCursor = db.rawQuery(
                    "SELECT $COLUMN_CATEGORY_NAME FROM $TABLE_CATEGORY WHERE $COLUMN_CATEGORY_ID = ?",
                    arrayOf(categoryId.toString())
                )

                var categoryName = ""
                if (categoryCursor.moveToFirst()) {
                    categoryName = categoryCursor.getString(categoryCursor.getColumnIndex(COLUMN_CATEGORY_NAME))
                }
                categoryCursor.close()

                // Tạo đối tượng Story với các thông tin đầy đủ, bao gồm tên thể loại
                val story = Story(
                    id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                    content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT)),
                    author = cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR)),
                    chapterCount = cursor.getInt(cursor.getColumnIndex(COLUMN_CHAPTER_COUNT)),
                    viewCount = cursor.getInt(cursor.getColumnIndex(COLUMN_VIEW_COUNT)),
                    rating = cursor.getFloat(cursor.getColumnIndex(COLUMN_RATING)),
                    categoryId = categoryId,
                    status = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)),
                    coverImageUrl = cursor.getString(cursor.getColumnIndex(COLUMN_COVER_IMAGE_URL))
                )
                stories.add(story)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return stories
    }

    @SuppressLint("Range")
    fun getStoryById(storyId: Int): Story {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_STORIES WHERE $COLUMN_ID = ?", arrayOf(storyId.toString()))

        if (cursor.moveToFirst()) {
            val story = Story(
                id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT)),
                author = cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR)),
                chapterCount = cursor.getInt(cursor.getColumnIndex(COLUMN_CHAPTER_COUNT)),
                viewCount = cursor.getInt(cursor.getColumnIndex(COLUMN_VIEW_COUNT)),
                rating = cursor.getFloat(cursor.getColumnIndex(COLUMN_RATING)),
                categoryId = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID_FK)),
                status = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)),
                coverImageUrl = cursor.getString(cursor.getColumnIndex(COLUMN_COVER_IMAGE_URL))
            )
            cursor.close()
            db.close()
            return story
        }

        cursor.close()
        db.close()
        throw Exception("Story not found")
    }


    @SuppressLint("Range")
    fun searchStoriesByTitle(title: String): List<Story> {
        val stories = mutableListOf<Story>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM $TABLE_STORIES WHERE $COLUMN_TITLE LIKE ?",
            arrayOf("%$title%")
        )

        if (cursor.moveToFirst()) {
            do {
                val story = Story(
                    id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                    content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT)),
                    author = cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR)),
                    chapterCount = cursor.getInt(cursor.getColumnIndex(COLUMN_CHAPTER_COUNT)),
                    viewCount = cursor.getInt(cursor.getColumnIndex(COLUMN_VIEW_COUNT)),
                    rating = cursor.getFloat(cursor.getColumnIndex(COLUMN_RATING)),
                    categoryId = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID_FK)),
                    status = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)),
                    coverImageUrl = cursor.getString(cursor.getColumnIndex(COLUMN_COVER_IMAGE_URL))
                )
                stories.add(story)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return stories
    }

    @SuppressLint("Range")
    fun searchStoriesByAuthor(author: String): List<Story> {
        val stories = mutableListOf<Story>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM $TABLE_STORIES WHERE $COLUMN_AUTHOR LIKE ?",
            arrayOf("%$author%")
        )

        if (cursor.moveToFirst()) {
            do {
                val story = Story(
                    id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                    content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT)),
                    author = cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR)),
                    chapterCount = cursor.getInt(cursor.getColumnIndex(COLUMN_CHAPTER_COUNT)),
                    viewCount = cursor.getInt(cursor.getColumnIndex(COLUMN_VIEW_COUNT)),
                    rating = cursor.getFloat(cursor.getColumnIndex(COLUMN_RATING)),
                    categoryId = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID_FK)),
                    status = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)),
                    coverImageUrl = cursor.getString(cursor.getColumnIndex(COLUMN_COVER_IMAGE_URL))
                )
                stories.add(story)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return stories
    }

    @SuppressLint("Range")
    fun searchStoriesByCategory(categoryName: String): List<Story> {
        val stories = mutableListOf<Story>()
        val db = readableDatabase

        // Tìm categoryId dựa trên tên thể loại
        val categoryCursor: Cursor = db.rawQuery(
            "SELECT $COLUMN_CATEGORY_ID FROM $TABLE_CATEGORY WHERE $COLUMN_CATEGORY_NAME LIKE ?",
            arrayOf("%$categoryName%")
        )

        if (categoryCursor.moveToFirst()) {
            val categoryId =
                categoryCursor.getInt(categoryCursor.getColumnIndex(COLUMN_CATEGORY_ID))

            // Tìm truyện dựa trên categoryId
            val cursor: Cursor = db.rawQuery(
                "SELECT * FROM $TABLE_STORIES WHERE $COLUMN_CATEGORY_ID_FK = ?",
                arrayOf(categoryId.toString())
            )

            if (cursor.moveToFirst()) {
                do {
                    val story = Story(
                        id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                        title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                        content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT)),
                        author = cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR)),
                        chapterCount = cursor.getInt(cursor.getColumnIndex(COLUMN_CHAPTER_COUNT)),
                        viewCount = cursor.getInt(cursor.getColumnIndex(COLUMN_VIEW_COUNT)),
                        rating = cursor.getFloat(cursor.getColumnIndex(COLUMN_RATING)),
                        categoryId = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID_FK)),
                        status = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)),
                        coverImageUrl = cursor.getString(
                            cursor.getColumnIndex(
                                COLUMN_COVER_IMAGE_URL
                            )
                        )
                    )
                    stories.add(story)
                } while (cursor.moveToNext())
            }

            cursor.close()
        }

        categoryCursor.close()
        db.close()
        return stories
    }

    @SuppressLint("Range")
    fun getStoriesByCategoryId(categoryId: Int): List<Story> {
        val stories = mutableListOf<Story>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM $TABLE_STORIES WHERE $COLUMN_CATEGORY_ID_FK = ?",
            arrayOf(categoryId.toString())
        )

        if (cursor.moveToFirst()) {
            do {
                val story = Story(
                    id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                    content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT)),
                    author = cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR)),
                    chapterCount = cursor.getInt(cursor.getColumnIndex(COLUMN_CHAPTER_COUNT)),
                    viewCount = cursor.getInt(cursor.getColumnIndex(COLUMN_VIEW_COUNT)),
                    rating = cursor.getFloat(cursor.getColumnIndex(COLUMN_RATING)),
                    categoryId = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID_FK)),
                    status = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)),
                    coverImageUrl = cursor.getString(cursor.getColumnIndex(COLUMN_COVER_IMAGE_URL))
                )
                stories.add(story)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return stories
    }

    fun addStory(story: Story): Long {
        val db = writableDatabase
        val values = ContentValues()

        values.put(COLUMN_TITLE, story.title)
        values.put(COLUMN_CONTENT, story.content)
        values.put(COLUMN_AUTHOR, story.author)
        values.put(COLUMN_CHAPTER_COUNT, story.chapterCount)
        values.put(COLUMN_VIEW_COUNT, story.viewCount)
        values.put(COLUMN_RATING, story.rating)
        values.put(COLUMN_CATEGORY_ID_FK, story.categoryId)
        values.put(COLUMN_STATUS, story.status)
        values.put(COLUMN_COVER_IMAGE_URL, story.coverImageUrl)

        val result = db.insert(TABLE_STORIES, null, values)
        db.close()
        return result
    }

    fun updateStory(story: Story): Int {
        val db = writableDatabase
        val values = ContentValues()

        values.put(COLUMN_TITLE, story.title)
        values.put(COLUMN_CONTENT, story.content)
        values.put(COLUMN_AUTHOR, story.author)
        values.put(COLUMN_CHAPTER_COUNT, story.chapterCount)
        values.put(COLUMN_VIEW_COUNT, story.viewCount)
        values.put(COLUMN_RATING, story.rating)
        values.put(COLUMN_CATEGORY_ID_FK, story.categoryId)
        values.put(COLUMN_STATUS, story.status)
        values.put(COLUMN_COVER_IMAGE_URL, story.coverImageUrl)

        val result = db.update(
            TABLE_STORIES,
            values,
            "$COLUMN_ID = ?",
            arrayOf(story.id.toString())
        )
        db.close()
        return result
    }

    fun deleteStory(storyId: Int): Int {
        val db = writableDatabase
        db.delete(TABLE_CHAPTERS, "$COLUMN_STORY_ID_FK = ?", arrayOf(storyId.toString()))
        val result = db.delete(TABLE_STORIES, "$COLUMN_ID = ?", arrayOf(storyId.toString()))
        db.close()
        return result
    }

    @SuppressLint("Range")
    fun getTopViewedStories(limit: Int = 10): List<Story> {
        val stories = mutableListOf<Story>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_STORIES ORDER BY $COLUMN_VIEW_COUNT DESC LIMIT ?",
            arrayOf(limit.toString())
        )

        if (cursor.moveToFirst()) {
            do {
                val story = Story(
                    id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                    content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT)),
                    author = cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR)),
                    chapterCount = cursor.getInt(cursor.getColumnIndex(COLUMN_CHAPTER_COUNT)),
                    viewCount = cursor.getInt(cursor.getColumnIndex(COLUMN_VIEW_COUNT)),
                    rating = cursor.getFloat(cursor.getColumnIndex(COLUMN_RATING)),
                    categoryId = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID_FK)),
                    status = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)),
                    coverImageUrl = cursor.getString(cursor.getColumnIndex(COLUMN_COVER_IMAGE_URL))
                )
                stories.add(story)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return stories
    }

    fun increaseStoryViewCount(storyId: Int) {
        val db = writableDatabase
        db.execSQL(
            "UPDATE $TABLE_STORIES SET $COLUMN_VIEW_COUNT = $COLUMN_VIEW_COUNT + 1 WHERE $COLUMN_ID = ?",
            arrayOf(storyId.toString())
        )
        db.close()
    }

    //-------------------- Phương thức cho bảng Chương -----------------------//

    // Lấy số chương của một truyện theo ID
    @SuppressLint("Range")
    fun getChapterCountByStoryId(storyId: Int): Int {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT COUNT(*) FROM $TABLE_CHAPTERS WHERE $COLUMN_STORY_ID_FK = ?",
            arrayOf(storyId.toString())
        )

        var chapterCount = 0
        if (cursor.moveToFirst()) {
            chapterCount = cursor.getInt(0)
        }

        cursor.close()
        db.close()
        return chapterCount
    }


    // Lấy tất cả các chương của một truyện
    @SuppressLint("Range")
    fun getChaptersByStoryId(storyId: Int): List<Chapter> {
        val chapters = mutableListOf<Chapter>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM $TABLE_CHAPTERS WHERE $COLUMN_STORY_ID_FK = ?",
            arrayOf(storyId.toString())
        )

        if (cursor.moveToFirst()) {
            do {
                val chapter = Chapter(
                    id = cursor.getInt(cursor.getColumnIndex(COLUMN_CHAPTER_ID)),
                    storyId = cursor.getInt(cursor.getColumnIndex(COLUMN_STORY_ID_FK)),
                    title = cursor.getString(cursor.getColumnIndex(COLUMN_CHAPTER_TITLE)),
                    content = cursor.getString(cursor.getColumnIndex(COLUMN_CHAPTER_CONTENT))
                )
                chapters.add(chapter)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return chapters
    }

    // Thêm Chapter vào cơ sở dữ liệu
    fun addChapter(chapter: Chapter): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_STORY_ID_FK, chapter.storyId)
        values.put(COLUMN_CHAPTER_TITLE, chapter.title)
        values.put(COLUMN_CHAPTER_CONTENT, chapter.content)

        val result = db.insert(TABLE_CHAPTERS, null, values)
        db.close()
        return result
    }

    // Cập nhật thông tin Chapter
    fun updateChapter(chapter: Chapter): Int {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_CHAPTER_TITLE, chapter.title)
        values.put(COLUMN_CHAPTER_CONTENT, chapter.content)

        val result = db.update(
            TABLE_CHAPTERS,
            values,
            "$COLUMN_CHAPTER_ID = ?",
            arrayOf(chapter.id.toString())
        )
        db.close()
        return result
    }

    // Lấy thông tin Chapter theo ID
    @SuppressLint("Range")
    fun getChapterById(chapterId: Int): Chapter {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_CHAPTERS WHERE $COLUMN_CHAPTER_ID = ?",
            arrayOf(chapterId.toString())
        )

        if (cursor.moveToFirst()) {
            val chapter = Chapter(
                id = cursor.getInt(cursor.getColumnIndex(COLUMN_CHAPTER_ID)),
                storyId = cursor.getInt(cursor.getColumnIndex(COLUMN_STORY_ID_FK)),
                title = cursor.getString(cursor.getColumnIndex(COLUMN_CHAPTER_TITLE)),
                content = cursor.getString(cursor.getColumnIndex(COLUMN_CHAPTER_CONTENT))
            )
            cursor.close()
            db.close()
            return chapter
        }

        cursor.close()
        db.close()
        throw Exception("Chapter not found")
    }

    // Xóa Chapter
    fun deleteChapter(chapterId: Int): Int {
        val db = writableDatabase
        val result = db.delete(
            TABLE_CHAPTERS,
            "$COLUMN_CHAPTER_ID = ?",
            arrayOf(chapterId.toString())
        )
        db.close()
        return result
    }

    //-------------------- Phương thức cho bảng thể loại -------------------- //
    // Thêm thể loại
    fun addCategory(categoryName: String): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_CATEGORY_NAME, categoryName)

        val result = db.insert(TABLE_CATEGORY, null, values)
        db.close()
        return result
    }

    // Cập nhật thể loại
    fun updateCategory(categoryId: Int, categoryName: String): Int {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_CATEGORY_NAME, categoryName)

        val result = db.update(TABLE_CATEGORY, values, "$COLUMN_CATEGORY_ID = ?", arrayOf(categoryId.toString()))
        db.close()
        return result
    }

    // Xóa thể loại
    fun deleteCategory(categoryId: Int): Int {
        val db = writableDatabase
        val result = db.delete(TABLE_CATEGORY, "$COLUMN_CATEGORY_ID = ?", arrayOf(categoryId.toString()))
        db.close()
        return result
    }

    // Lấy tất cả thể loại
    @SuppressLint("Range")
    fun getCategories(): List<Category> {
        val categories = mutableListOf<Category>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_CATEGORY", null)

        if (cursor.moveToFirst()) {
            do {
                val categoryId = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID))
                val categoryName = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME))
                categories.add(Category(categoryId, categoryName))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return categories
    }

    // Lấy thể loại theo ID
    @SuppressLint("Range")
    fun getCategoryById(categoryId: Int): Category? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_CATEGORY WHERE $COLUMN_CATEGORY_ID = ?", arrayOf(categoryId.toString()))

        if (cursor.moveToFirst()) {
            val category = Category(
                categoryId = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID)),
                categoryName = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME))
            )
            cursor.close()
            db.close()
            return category
        }

        cursor.close()
        db.close()
        return null
    }

    //-------------------- Phương thức cho bảng tác giả -------------------- //

    // Thêm tác giả
    fun addAuthor(author: Author): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_AUTHOR_NAME, author.name)
        values.put(COLUMN_AUTHOR_BIOGRAPHY, author.biography)

        val result = db.insert(TABLE_AUTHORS, null, values)
        db.close()
        return result
    }

    // Cập nhật tác giả
    fun updateAuthor(author: Author): Int {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_AUTHOR_NAME, author.name)
        values.put(COLUMN_AUTHOR_BIOGRAPHY, author.biography)

        val result = db.update(TABLE_AUTHORS, values, "$COLUMN_AUTHOR_ID = ?", arrayOf(author.authorId.toString()))
        db.close()
        return result
    }

    // Xóa tác giả
    fun deleteAuthor(authorId: Int): Int {
        val db = writableDatabase
        val result = db.delete(TABLE_AUTHORS, "$COLUMN_AUTHOR_ID = ?", arrayOf(authorId.toString()))
        db.close()
        return result
    }

    // Lấy tất cả tác giả
    @SuppressLint("Range")
    fun getAllAuthors(): List<Author> {
        val authors = mutableListOf<Author>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_AUTHORS", null)

        if (cursor.moveToFirst()) {
            do {
                val author = Author(
                    authorId = cursor.getInt(cursor.getColumnIndex(COLUMN_AUTHOR_ID)),
                    name = cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR_NAME)),
                    biography = cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR_BIOGRAPHY))
                )
                authors.add(author)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return authors
    }

    // Lấy tác giả theo ID
    @SuppressLint("Range")
    fun getAuthorById(authorId: Int): Author {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_AUTHORS WHERE $COLUMN_AUTHOR_ID = ?", arrayOf(authorId.toString()))

        if (cursor.moveToFirst()) {
            val author = Author(
                authorId = cursor.getInt(cursor.getColumnIndex(COLUMN_AUTHOR_ID)),
                name = cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR_NAME)),
                biography = cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR_BIOGRAPHY))
            )
            cursor.close()
            db.close()
            return author
        }

        cursor.close()
        db.close()
        throw Exception("Author not found")
    }

    //-------------------- Phương thức cho bảng bình luận -------------------- //

    // Thêm comment mới
    fun addComment(storyId: Int, userId: Int, text: String): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COLUMN_COMMENT_STORY_ID, storyId)
        values.put(COLUMN_COMMENT_USER_ID, userId)
        values.put(COLUMN_COMMENT_TEXT, text)
        values.put(COLUMN_COMMENT_TIMESTAMP, System.currentTimeMillis())
        val result = db.insert(TABLE_COMMENTS, null, values)
        db.close()
        return result
    }

    // Lấy danh sách comment theo storyId (join để lấy tên user)
    @SuppressLint("Range")
    fun getCommentsByStoryId(storyId: Int): List<Comment> {
        val comments = mutableListOf<Comment>()
        val db = readableDatabase
        val query = """
        SELECT c.$COLUMN_COMMENT_ID, c.$COLUMN_COMMENT_STORY_ID, c.$COLUMN_COMMENT_USER_ID,
           c.$COLUMN_COMMENT_TEXT, c.$COLUMN_COMMENT_TIMESTAMP,
           u.$COLUMN_USERNAME, u.$COLUMN_DISPLAY_NAME
        FROM $TABLE_COMMENTS c
        LEFT JOIN $TABLE_USERS u ON c.$COLUMN_COMMENT_USER_ID = u.$COLUMN_ID
        WHERE c.$COLUMN_COMMENT_STORY_ID = ?
        ORDER BY c.$COLUMN_COMMENT_TIMESTAMP DESC
    """
        val cursor = db.rawQuery(query, arrayOf(storyId.toString()))
        if (cursor.moveToFirst()) {
            do {
                val usernameIdx = cursor.getColumnIndex(COLUMN_USERNAME)
                val displayNameIdx = cursor.getColumnIndex(COLUMN_DISPLAY_NAME)

                val comment = Comment(
                    id = cursor.getInt(cursor.getColumnIndex(COLUMN_COMMENT_ID)),
                    storyId = cursor.getInt(cursor.getColumnIndex(COLUMN_COMMENT_STORY_ID)),
                    userId = cursor.getInt(cursor.getColumnIndex(COLUMN_COMMENT_USER_ID)),
                    username = if (usernameIdx >= 0 && !cursor.isNull(usernameIdx)) cursor.getString(usernameIdx) else "Ẩn danh",
                    displayName = if (displayNameIdx >= 0 && !cursor.isNull(displayNameIdx)) cursor.getString(displayNameIdx) else "Ẩn danh",
                    text = cursor.getString(cursor.getColumnIndex(COLUMN_COMMENT_TEXT)),
                    timestamp = cursor.getLong(cursor.getColumnIndex(COLUMN_COMMENT_TIMESTAMP))
                )
                comments.add(comment)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        Log.d("COMMENT", "Số lượng bình luận lấy được: ${comments.size}")
        return comments
    }

    @SuppressLint("Range")
    fun getAllCommentsDetail(): List<CommentDetail> {
        val db = readableDatabase
        val comments = mutableListOf<CommentDetail>()
        val query = """
            SELECT c.comment_id, c.story_id, s.title, c.user_id, u.display_name, c.comment_text, c.timestamp
            FROM comments c
            LEFT JOIN users u ON c.user_id = u._id
            LEFT JOIN stories s ON c.story_id = s._id
            ORDER BY c.timestamp DESC
        """
        val cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                comments.add(
                    CommentDetail(
                        id = cursor.getInt(cursor.getColumnIndex("comment_id")),
                        storyId = cursor.getInt(cursor.getColumnIndex("story_id")),
                        storyTitle = cursor.getString(cursor.getColumnIndex("title")) ?: "Không rõ",
                        userId = cursor.getInt(cursor.getColumnIndex("user_id")),
                        displayName = cursor.getString(cursor.getColumnIndex("display_name")) ?: "Ẩn danh",
                        text = cursor.getString(cursor.getColumnIndex("comment_text")),
                        timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return comments
    }

    fun deleteComment(commentId: Int): Int {
        val db = writableDatabase
        val result = db.delete("comments", "comment_id = ?", arrayOf(commentId.toString()))
        db.close()
        return result
    }

    //-------------------- Phương thức cho thống kê -------------------- //

    // Tổng số truyện
    fun getStoryCount(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM stories", null)
        var count = 0
        if (cursor.moveToFirst()) count = cursor.getInt(0)
        cursor.close()
        db.close()
        return count
    }

    // Tổng số chương
    fun getChapterCount(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM chapters", null)
        var count = 0
        if (cursor.moveToFirst()) count = cursor.getInt(0)
        cursor.close()
        db.close()
        return count
    }

    // Tổng lượt xem truyện
    fun getTotalViewCount(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT SUM(view_count) FROM stories", null)
        var sum = 0
        if (cursor.moveToFirst()) sum = cursor.getInt(0)
        cursor.close()
        db.close()
        return sum
    }

    // Tổng số bình luận
    fun getCommentCount(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM comments", null)
        var count = 0
        if (cursor.moveToFirst()) count = cursor.getInt(0)
        cursor.close()
        db.close()
        return count
    }

    // Tổng số người dùng
    fun getUserCount(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM users", null)
        var count = 0
        if (cursor.moveToFirst()) count = cursor.getInt(0)
        cursor.close()
        db.close()
        return count
    }

}
