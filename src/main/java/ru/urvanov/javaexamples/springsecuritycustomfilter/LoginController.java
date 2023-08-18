package ru.urvanov.javaexamples.springsecuritycustomfilter;

import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rest/v1/login")
public class LoginController {
    
    @RequestMapping(method = RequestMethod.POST)
    public LoginResult login(@RequestAttribute LoginArg loginArg) {
        LoginResult result = new LoginResult();
        result.setSuccess(true);
        return result;
    }

}
