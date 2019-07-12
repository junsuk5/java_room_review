package com.exmaple.java_room;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.exmaple.java_room.db.AppDatabase;
import com.exmaple.java_room.db.User;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private AppDatabase mDb;

    public LiveData<List<User>> users = new MutableLiveData<>();
    private Thread insertThread;
    private InsertAsyncAdapter insertAsyncAdapter;

    public MainViewModel(@NonNull Application application) {
        super(application);

        mDb = Room.databaseBuilder(application,
                AppDatabase.class, "database-name")
                .addMigrations(AppDatabase.MIGRATION_1_2)
                .build();

        users = mDb.userDao().getAll();
    }

    void insert(User user) {
//        insertThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                isInserting = true;
//                mDb.userDao().insert(user);
//            }
//        });
//        insertThread.start();

        insertAsyncAdapter = new InsertAsyncAdapter(mDb);
        insertAsyncAdapter.execute(user);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

//        if (isInserting) {
//            insertThread.interrupt();
//        }

        insertAsyncAdapter.cancel(true);
    }

    static class InsertAsyncAdapter extends AsyncTask<User, Void, Void> {
        private AppDatabase mDb;

        public InsertAsyncAdapter(AppDatabase mDb) {
            this.mDb = mDb;
        }

        @Override
        protected Void doInBackground(User... users) {
            mDb.userDao().insert(users[0]);
            return null;
        }
    }

    void update(User user) {
        mDb.userDao().update(user);
    }

    void delete(User user) {
        mDb.userDao().delete(user);
    }
}
