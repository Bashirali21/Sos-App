package p32929.passcodelock.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import p32929.passcodelock.db.Contact;
import p32929.passcodelock.db.FamilyContact;
import p32929.passcodelock.repositary.ContactRepositary;

public class ViewModal extends AndroidViewModel {

    // creating a new variable for course repository.
    private ContactRepositary repository;

    // below line is to create a variable for live 
    // data where all the courses are present.
    private LiveData<List<Contact>> allCourses;
    private LiveData<List<FamilyContact>> familyContacts;

    // constructor for our view modal.
    public ViewModal(@NonNull Application application) {
        super(application);
        repository = new ContactRepositary(application);
        allCourses = repository.getAllCourses();
        familyContacts = repository.getFamilyContact();
    }
    public void insert(Contact model) {
        repository.insert(model);
    }
    public void insertFamily(FamilyContact model) {
        repository.insertFamily(model);
    }
    public LiveData<List<Contact>> getAllCourses() {
        return allCourses;
    }
    public LiveData<List<FamilyContact>> getFamilyContacts() {
        return familyContacts;
    }

}
