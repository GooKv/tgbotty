package team.guest.tgbotty.controller;

import com.google.common.collect.ImmutableMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @RequestMapping("hi")
    @ResponseBody
    public Object hi(String name) {
        return ImmutableMap.builder()
                .put("message", "Hello, " + name + "!")
                .build();
    }
}
