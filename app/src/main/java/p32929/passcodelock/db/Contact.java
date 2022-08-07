package p32929.passcodelock.db;

import androidx.annotation.UiContext;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Contacts_table")
public class Contact {
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
    @UiContext
    String number;
    String Name;
}
