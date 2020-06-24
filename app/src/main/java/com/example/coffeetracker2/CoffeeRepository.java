package com.example.coffeetracker2;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.coffeetracker2.database.Coffee;
import com.example.coffeetracker2.database.CoffeeDAO;
import com.example.coffeetracker2.database.CoffeeDatabase;
import com.example.coffeetracker2.utils.CoffeeViewModelUtils;

import java.util.List;

public class CoffeeRepository extends AndroidViewModel {

    private CoffeeViewModelUtils utils = new CoffeeViewModelUtils();
    private String TAG = this.getClass().getSimpleName();
    private CoffeeDAO coffeeDao;
    private CoffeeDatabase coffeeDatabase;
    private LiveData<List<Coffee>> allCoffees;
    private LiveData<Integer> coffeeNrToday;
    private LiveData<Integer> caffeineToday;
    private LiveData<Integer> coffeeNrThisWeek;
    private LiveData<Integer> caffeineThisWeek;

    public CoffeeRepository(Application application) {
        super(application);
        coffeeDatabase = CoffeeDatabase.getDatabase(application);
        coffeeDao = coffeeDatabase.coffeeDao();
        allCoffees = coffeeDao.getCoffeeList();
        coffeeNrToday = coffeeDao.getCoffeeNr(utils.getTodayBegin(), utils.getTodayEnd());
        caffeineToday = coffeeDao.getCaffeine(utils.getTodayBegin(), utils.getTodayEnd());
        coffeeNrThisWeek = coffeeDao.getCoffeeNr(utils.getWeekBegin(), utils.getWeekEnd());
        caffeineThisWeek = coffeeDao.getCaffeine(utils.getWeekBegin(), utils.getWeekEnd());
    }


    public void insert(Coffee coffee) {
        new InsertAsyncTask(coffeeDao).execute(coffee);
    }

    public void update(Coffee coffee) {
        new UpdateAsyncTask(coffeeDao).execute(coffee);
    }

    public void updateProductivity(Coffee coffee) {
        new UpdateProductivityAsync(coffeeDao).execute(coffee);
    }

    public void delete(Coffee coffee){
        new DeleteAsyncTask(coffeeDao).execute(coffee);
    }

    public LiveData<Integer> getCoffeeNrToday() {
        return coffeeNrToday;
    }

    public LiveData<Integer> getCaffeine() {
        return caffeineToday;
    }

    public LiveData<Integer> getCoffeeNrThisWeek() {
        return coffeeNrThisWeek;
    }

    public LiveData<Integer> getCaffeineThisWeek() {
        return caffeineThisWeek;
    }

    public LiveData<List<Coffee>> getAllCoffees() {
        return allCoffees;
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG, "ViewModel Destroyed");
    }

    // This class is used to serve for implementation of all of the CRUD operations
    private static class OperationsAsyncTask extends AsyncTask<Coffee, Void, Void> {

        CoffeeDAO mAsyncTaskDao;

        OperationsAsyncTask(CoffeeDAO dao) {
            this.mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Coffee... coffees) {
            return null;
        }
    }

    private static class InsertAsyncTask extends OperationsAsyncTask {

        InsertAsyncTask(CoffeeDAO dao) {
            super(dao);
        }

        @Override
        protected Void doInBackground(Coffee... coffees) {

            mAsyncTaskDao.insert(coffees[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends OperationsAsyncTask {

        UpdateAsyncTask(CoffeeDAO dao) {
            super(dao);
        }

        @Override
        protected Void doInBackground(Coffee... coffees) {
            mAsyncTaskDao.update(coffees[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends OperationsAsyncTask {

        DeleteAsyncTask(CoffeeDAO dao) {
            super(dao);
        }

        @Override
        protected Void doInBackground(Coffee... coffees) {
            mAsyncTaskDao.delete(coffees[0]);
            return null;
        }
    }

    private static class UpdateProductivityAsync extends OperationsAsyncTask {

        UpdateProductivityAsync(CoffeeDAO dao) {
            super(dao);
        }

        @Override
        protected Void doInBackground(Coffee... coffees) {
            mAsyncTaskDao.updateProductivity(coffees[0].getId(), coffees[0].getProductivityRating());
            return null;
        }
    }
}
