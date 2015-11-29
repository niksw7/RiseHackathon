package hackathon.barclays.kyc;

import hackathon.barclays.kyc.model.Customer;
import hackathon.barclays.kyc.repository.CustomerRepository;
import hackathon.barclays.kyc.rest.vo.AdharInformation;
import hackathon.barclays.kyc.rest.vo.CustomerInformation;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

@RestController
public class KycController {

    private static final org.slf4j.Logger logger = LoggerFactory
            .getLogger(KycController.class);


    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private @Value("#{new java.util.Random()}")Random random;


    @RequestMapping(value = "/greeting", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity greeting(@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {
        model.addAttribute("name", name);
        return new ResponseEntity(model, HttpStatus.OK);
    }


    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    ResponseEntity uploadFileHandler(@RequestParam("documentType") String name,
                                     @RequestParam("file") MultipartFile file, Model model) {

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
                model.addAttribute("message", "Successfully uploaded file" + name);
                return new ResponseEntity("{\"message\":\"Success\"}",HttpStatus.OK);
            } catch (Exception e) {
                model.addAttribute("message", "Your file uploading failed");
                return new ResponseEntity("{\"message\":\"failure\"}",HttpStatus.BAD_REQUEST);
            }
        } else {
            model.addAttribute("message", "Empty uploading of files is rejected");
            return new ResponseEntity("{\"message\":\"Failure\"}",HttpStatus.BAD_REQUEST);

        }
    }

    @RequestMapping(value = "/addCustomer", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addCustomer(@RequestBody CustomerInformation customerInformation, Model model) {

        int customerId = random.nextInt();
        customerRepository.save(new Customer(customerId, customerInformation.getName(), customerInformation.getAge(), customerInformation.getAddress()));
        model.addAttribute("name", customerId);
        return  new ResponseEntity("{\"message\":\"Success\"}",HttpStatus.OK);
    }

    @RequestMapping(value = "/updateAdharInformation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateAdharInformation(@RequestBody AdharInformation adharInformation, Model model) {
        try {
            Customer customer = customerRepository.findOne(adharInformation.getCustomerId());
            customer.setAadharNumber(adharInformation.getAdharNumber());
            customerRepository.save(customer);

            //Let's mock the KYC thing for the time being as we do not have the KYC biometric in place

            //Send the OTP to verify the Emudra

            model.addAttribute("message", "Authenticated by UDIAI");
            return new ResponseEntity("{\"message\":\"Sucess\"}", HttpStatus.OK);
        }catch (Exception e){
           return new ResponseEntity("{\"message\":\"ERROR\"}",HttpStatus.BAD_REQUEST);
        }

    }

}
