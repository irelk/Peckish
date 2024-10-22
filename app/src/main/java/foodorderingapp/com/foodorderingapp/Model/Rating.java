package foodorderingapp.com.foodorderingapp.Model;

public class Rating {
    private String userPhone;
    private String foodId;
    private String rateValue;
    private String comment;
    private String userName;

    public Rating(){

    }
    public Rating(String userPhoneandfoodId, String foodId, String rateValue, String comment,String userName) {
        this.userPhone = userPhoneandfoodId;
        this.foodId = foodId;
        this.rateValue = rateValue;
        this.comment = comment;
        this.userName=userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhoneandfoodId) {
        this.userPhone = userPhoneandfoodId;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
