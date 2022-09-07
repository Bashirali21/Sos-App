package p32929.passcodelock.db;

import androidx.annotation.UiContext;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "FamilyContacts_table")
public class FamilyContact {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }



    @PrimaryKey(autoGenerate = true)
    int id;

//    public FamilyContact(String number, String name) {
//        this.number = number;
//        Name = name;
//    }

    @UiContext
    String number;
    String Name;
}
