package foodorderingapp.com.foodorderingapp.Model;

public class User {
private String Name;
private String Email;
private String Password;
private String Phone;
private String isStaff;
public User(){

}


    public User(String name, String email, String password, String phone) {
        Name = name;
        Email = email;
        Password = password;
        Phone = phone;
        isStaff="false";
    }

    public String getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(String isStaff) {
        this.isStaff = isStaff;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }


}
