package com.exmaple.java_room.db;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {User.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // 트랜잭션 시작
            database.beginTransaction();
            // 백업
            database.execSQL("ALTER TABLE User RENAME TO User_backup");
            // 새로 테이블 생성
            database.execSQL("CREATE TABLE User (uid INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, lastName TEXT)");
            // 복원
            database.execSQL("INSERT INTO User(uid, lastName) SELECT uid, lastName FROM User_backup");
            // 백업본 삭제
            database.execSQL("DROP TABLE User_backup");
            // 트랜잭션 끝
            database.endTransaction();
        }
    };
}