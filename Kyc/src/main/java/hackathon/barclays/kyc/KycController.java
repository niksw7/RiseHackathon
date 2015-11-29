package hackathon.barclays.kyc;

import com.nexmo.messaging.sdk.NexmoSmsClient;
import com.nexmo.messaging.sdk.SmsSubmissionResult;
import com.nexmo.messaging.sdk.messages.TextMessage;
import hackathon.barclays.kyc.model.Customer;
import hackathon.barclays.kyc.repository.CustomerRepository;
import hackathon.barclays.kyc.rest.vo.AdharInformation;
import hackathon.barclays.kyc.rest.vo.CustomerInformation;
import hackathon.barclays.kyc.rest.vo.VerificationCode;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class KycController {

    private static final org.slf4j.Logger logger = LoggerFactory
            .getLogger(KycController.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private @Value("#{new java.util.Random()}")Random random;


    private HashMap<Integer,String> verificationMap = new HashMap<>();


    @RequestMapping(value = "/greeting", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity greeting() {

        Map<String,String> map = new HashMap<>();
        map.put("Heroes","Coders");
        map.put("Hackers","Breakers");
        Customer customer = new Customer(1,"nikesh",12,"someaddress");
        customerRepository.save(customer);
        return new ResponseEntity(map, HttpStatus.OK);
    }


    @RequestMapping(value = "/addCustomer", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addCustomer(@RequestBody CustomerInformation customerInformation) {
        HashMap<String,String> map = new HashMap<>();
        int customerId = random.nextInt();
        customerRepository.save(new Customer(customerId, customerInformation.getName(), customerInformation.getAge(), customerInformation.getAddress(),customerInformation.getUserName(),
                customerInformation.getPassword()));
        map.put("message","Success");
        map.put("customerId", ""+customerId+"");
        return new ResponseEntity(map, HttpStatus.OK);
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity uploadFileHandle(@RequestParam("documentType") String name,
                                     @RequestParam("file") MultipartFile file) {
        Map<String,String> map = new HashMap<>();
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();

                // Creating the directory to store file
                String rootPath = System.getProperty("catalina.home");
                File dir = new File(rootPath + File.separator + "tmpFiles");
                if (!dir.exists())
                    dir.mkdirs();

                File serverFile = new File(dir.getAbsolutePath()
                        + File.separator + name);
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

                logger.info("Server File Location="
                        + serverFile.getAbsolutePath());
                map.put("message", "Successfully uploaded file");
                return new ResponseEntity(map,HttpStatus.OK);
            } catch (Exception e) {
                map.put("message", "Your file uploading failed");
                return new ResponseEntity(map,HttpStatus.BAD_REQUEST);
            }
        } else {
            map.put("message", "Empty uploading of files is rejected");
            return new ResponseEntity(map,HttpStatus.BAD_REQUEST);

        }
    }

    @RequestMapping(value = "/updateAdharInformation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateAdharInformation(@RequestBody AdharInformation adharInformation) {
        try {
            HashMap<String,String> map = new HashMap<>();
            Customer customer = customerRepository.findOne(adharInformation.getCustomerId());
            customer.setAadharNumber(adharInformation.getAdharNumber());
            //Perform the validation for OTP

            NexmoSmsClient nexmoSmsClient = new NexmoSmsClient("b47acb3d","87b4d088");
            String sender = "918149660151";
            String to = "8421186193";
            String messageBody = "OTP for KYC details";
            SmsSubmissionResult[] smsSubmissionResults = nexmoSmsClient.submitMessage(new TextMessage(sender, to, messageBody));
            if(smsSubmissionResults[0].getStatus() == SmsSubmissionResult.STATUS_OK){
                int code = 1;
                map.put("message", " OTP Authentication sent by e-mudra + "+ code);
                System.out.println("code="+code);
                verificationMap.put(customer.getCustomerId(),""+code+"");
                customerRepository.save(customer);
            }else{
                map.put("message", " OTP Server failed due to some reason");
                customerRepository.save(customer);
                return new ResponseEntity(map, HttpStatus.OK);
            }

            //Let's mock the KYC thing for the time being as we do not have the KYC biometric in place

            //Send the OTP to verify the Emudra


            return new ResponseEntity(map, HttpStatus.OK);
        }catch (Exception e){
           return new ResponseEntity("{\"message\":\"ERROR\"}",HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/verifyCustomer", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateAdharInformation(@RequestBody VerificationCode verificationCode) {
        HashMap<String,String> map = new HashMap<>();

        if(verificationCode.getCode().equals(verificationMap.get(verificationCode.getCustomerId()))){
            map.put("message:","success");
            return new ResponseEntity(map,HttpStatus.OK);
        }else {
            map.put("message:","failure");
            return new ResponseEntity(map,HttpStatus.BAD_REQUEST);
        }

    }


}
