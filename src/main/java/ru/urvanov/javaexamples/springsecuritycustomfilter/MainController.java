package ru.urvanov.javaexamples.springsecuritycustomfilter;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rest/v1/main")
public class MainController {

    @RequestMapping(method = RequestMethod.POST)
    public MainResult main(@RequestBody MainArg arg) {
        MainResult result = new MainResult();
        result.setMessage("The word is " + arg.getWord());
        return result;
    }
}
