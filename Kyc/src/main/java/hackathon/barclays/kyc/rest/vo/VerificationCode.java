package hackathon.barclays.kyc.rest.vo;

public class VerificationCode {
    private int customerId;

    public String getCode() {
        return code;
    }

    public int getCustomerId() {
        return customerId;
    }

    private String code;
}
