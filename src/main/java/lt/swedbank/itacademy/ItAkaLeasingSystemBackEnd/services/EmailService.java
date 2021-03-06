package lt.swedbank.itacademy.ItAkaLeasingSystemBackEnd.services;

import lt.swedbank.itacademy.ItAkaLeasingSystemBackEnd.beans.documents.Customer;
import lt.swedbank.itacademy.ItAkaLeasingSystemBackEnd.beans.front.EmailCredentials;
import lt.swedbank.itacademy.ItAkaLeasingSystemBackEnd.beans.front.LoginCredentials;
import lt.swedbank.itacademy.ItAkaLeasingSystemBackEnd.beans.tokens.PasswordResetToken;
import lt.swedbank.itacademy.ItAkaLeasingSystemBackEnd.utils.EndPoints;
import lt.swedbank.itacademy.ItAkaLeasingSystemBackEnd.utils.PasswordResetTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ResetTokenService resetTokenService;

    @Async
    void sendEmail(SimpleMailMessage message){
        mailSender.send(message);
    }

    public ResponseEntity<String> forgotPassword(EmailCredentials creds){

        //Customer customer = customerService.findCustomerByEmail(email);
        Customer customer = customerService.findCustomerByEmailAndID(creds.getEmail(), creds.getUserId());
        if(customer == null){
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }
        else{
            PasswordResetToken tokenCheck = resetTokenService.findByCustomerID(customer.getUserID());
            if(tokenCheck != null){
                if(!this.validTimePassed(tokenCheck, 2)){
                    return new ResponseEntity<>("Only one request every 2 minutes", HttpStatus.FORBIDDEN);
                }
                else{
                    resetTokenService.deleteByToken(tokenCheck.getToken());
                }
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.HOUR_OF_DAY, 24);

            PasswordResetToken token = new PasswordResetToken(customer.getUserID(), UUID.randomUUID().toString(), cal.getTime(), new Date());

            String recoveryUrl = EndPoints.PASS_RECOVERY_TOKEN_LINK + token.getToken();

            System.out.println(recoveryUrl);

            String recoveryText = "A password reset has been requested for the user ID: " + customer.getUserID() + "\n\n"
                    + "If you have requested this and want to reset your password, please visit this link: " + "\n\n"
                    + recoveryUrl;

            SimpleMailMessage recoveryMessage = new SimpleMailMessage();
            recoveryMessage.setFrom("leasingservicemail@gmail.com");
            recoveryMessage.setTo(creds.getEmail());
            recoveryMessage.setSubject("Reset your password");
            recoveryMessage.setText(recoveryText);

            sendEmail(recoveryMessage);
            resetTokenService.addToken(token);

            return new ResponseEntity<>("Password request successful", HttpStatus.OK);
        }
    }

    public ResponseEntity<String> resetPassword(String token) {
        PasswordResetToken resetToken = resetTokenService.findByToken(token);

        if(resetToken == null){
            return new ResponseEntity<>("Bad link", HttpStatus.NOT_FOUND);
        }
        else{
            String tokenStatus = PasswordResetTokenUtils.validate(resetToken);

            if(tokenStatus.equals("valid")){
                return new ResponseEntity<>("Valid", HttpStatus.OK);
            }
            else if(tokenStatus.equals("expired")){
                resetTokenService.deleteByToken(token);
                return new ResponseEntity<>("Expired link", HttpStatus.GONE);
            }
            return new ResponseEntity<>("Invalid token", HttpStatus.BAD_REQUEST);
        }
    }

    private boolean validTimePassed(PasswordResetToken passwordResetToken, int timeoutMinutes){
        Date lastSendDate = passwordResetToken.getSendTime();

        Calendar calendar = Calendar.getInstance();
        long testVal = (calendar.getTime().getTime() - lastSendDate.getTime())/1000/60;
        System.out.println(testVal);
        return testVal >= timeoutMinutes;
    }
}
