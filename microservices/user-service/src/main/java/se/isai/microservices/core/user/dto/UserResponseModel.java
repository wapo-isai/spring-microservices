package se.isai.microservices.core.user.dto;

public class UserResponseModel {
    private String userId;
    private String username;
//    private List<OrderResponseModel> orders;

    public UserResponseModel(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
