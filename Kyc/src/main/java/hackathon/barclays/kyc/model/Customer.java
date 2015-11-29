package hackathon.barclays.kyc.model;

import org.springframework.data.annotation.Id;

import java.util.Set;

public class Customer {
    public Customer() {
    }

    @Id
    private Integer customerId;
    private String name;
    private int age;
    private String address;
	private String userName;
	private String password;

	private Set<String> documentName;
	private String aadharNumber;

    public void verify() {
        this.status = VerificationStatus.Verified;
    }

    private VerificationStatus status;

    public Customer(int customerId, String name, int age, String address, Set<String> documentName, String aadharNumber) {
        this(customerId, name, age, address);
        this.documentName = documentName;
        this.aadharNumber = aadharNumber;
    }

    public Customer(Integer customerId, String name, int age, String address){
        this.customerId = customerId;
        this.name = name;
        this.age = age;
        this.address = address;
        this.status = VerificationStatus.Not_Verified;
    }



	public Customer(int customerId, String name, int age, String address,
			Set<String> documentName, String aadharNumber,String userName,String password) {
		this(customerId, name, age, address, userName, password);
		this.documentName = documentName;
		this.aadharNumber = aadharNumber;
	}

	public Customer(Integer customerId, String name, int age, String address, String userName, String password) {
		this.customerId = customerId;
		this.name = name;
		this.age = age;
		this.address = address;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public String getAddress() {
		return address;
	}

	public Set<String> getDocumentName() {
		return documentName;
	}

	public String getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}

	public void setDocumentName(Set<String> documentName) {
		this.documentName = documentName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
