package hackathon.barclays.kyc.model;


import org.springframework.data.annotation.Id;

import java.util.Set;

public class Customer {
    @Id
    private int customerId;
    private String name;
    private int age;
    private String address;
    private Set<String> documentName;
    private String aadharNumber;

    public int getCustomerId() {
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
    public Customer(int customerId, String name, int age, String address){
        this.customerId = customerId;
        this.name = name;
        this.age = age;
        this.address = address;
    }
    public Customer(int customerId, String name, int age, String address, Set<String> documentName, String aadharNumber) {
        this(customerId,name,age,address);
        this.documentName = documentName;
        this.aadharNumber = aadharNumber;
    }
}
