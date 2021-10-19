import java.io.*;



/* 
	Users class contains class variables id,name,password,usertype.

	Users class has a constructor with Arguments name, String password, String usertype.
	  
	Users  class contains getters and setters for id,name,password,usertype.

*/

public class User implements Serializable{
	private Long userId;
	private String userName;
	private String password;
	private String rePassword;
	private String userType;
	private Integer userAge;
	private String userGender;
	private String userOccupation;

	public User(Long userId, String userName, String password, String rePassword, String userType, Integer userAge,
				String userGender, String userOccupation) {
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.rePassword = rePassword;
		this.userType = userType;
		this.userAge = userAge;
		this.userGender = userGender;
		this.userOccupation = userOccupation;
	}

	public Integer getUserAge() {
		return userAge;
	}

	public void setUserAge(Integer userAge) {
		this.userAge = userAge;
	}

	public String getUserGender() {
		return userGender;
	}

	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}

	public String getUserOccupation() {
		return userOccupation;
	}

	public void setUserOccupation(String userOccupation) {
		this.userOccupation = userOccupation;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getRePassword() {
		return rePassword;
	}

	public void setRePassword(String rePassword) {
		this.rePassword = rePassword;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
}
