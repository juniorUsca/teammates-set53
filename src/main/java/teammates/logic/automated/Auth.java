package teammates.logic.automated;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class Auth extends Authenticator {
    
    public String username=null;
    public String password=null;
    
    public Auth(String user,String pass){
        this.username=user;
        this.password=pass;
    }
    
    @Override
    protected PasswordAuthentication getPasswordAuthentication(){
        return new PasswordAuthentication(username,password);
    }
}