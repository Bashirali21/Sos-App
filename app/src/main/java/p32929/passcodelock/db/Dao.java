package p32929.passcodelock.db;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// Adding annotation 
// to our Dao class
@androidx.room.Dao
public interface Dao {

    // below method is use to 
    // add data to database.
    @Insert
    void insert(Contact model);

    @Insert
    void insertFamily(FamilyContact model);
    // below method is use to update 
    // the data in our database.
    @Update
    void update(Contact model);

    @Update
     void updateFamily(FamilyContact model);

    // below line is use to delete a 
    // specific course in our database.
    @Delete
    void delete(Contact model);
    @Delete
    void deleteFamily(FamilyContact model);

    @Delete
    void FamilyContact(FamilyContact model);

    // on below line we are making query to
    // delete all courses from our database.


    // below line is to read all the courses from our database.
    // in this we are ordering our courses in ascending order
    // with our course name.
    @Query("SELECT * FROM contacts_table ORDER BY Name ASC")
    LiveData<List<Contact>> getAllCourses();

    @Query("SELECT * FROM FamilyContacts_table ORDER BY Name ASC")
    LiveData<List<FamilyContact>> getAllFamilyContacts();
}
