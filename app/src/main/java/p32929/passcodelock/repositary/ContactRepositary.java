package p32929.passcodelock.repositary;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import p32929.passcodelock.db.Contact;
import p32929.passcodelock.db.AppDataBase;
import p32929.passcodelock.db.Dao;
import p32929.passcodelock.db.FamilyContact;


public class ContactRepositary {

    // below line is the create a variable 
    // for dao and list for all courses.
    private Dao dao;
    private LiveData<List<Contact>> allCourses;
    private LiveData<List<FamilyContact>> familyContact;


    // creating a constructor for our variables
    // and passing the variables to it.
    public ContactRepositary(Application application) {
        AppDataBase database = AppDataBase.getInstance(application);
        dao =  database.Dao();
        allCourses = dao.getAllCourses();
        familyContact=dao.getAllFamilyContacts();
    }

    // creating a method to insert the data to our database.
    public void insert(Contact model) {
      dao.insert(model);
    }
    public void insertFamily(FamilyContact model) {
        dao.insertFamily(model);
    }
    public LiveData<List<Contact>> getAllCourses() {
        return allCourses;
    }

    public LiveData<List<FamilyContact>> getFamilyContact() {
        return familyContact;
    }

    // we are creating a async task method to insert new course.


    // we are creating a async task method to delete course.


    // we are creating a async task method to delete all courses.

}