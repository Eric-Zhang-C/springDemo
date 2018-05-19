package hello;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.ui.ModelMap;
import org.springframework.stereotype.Controller;

@Controller
public class WordladderController {
    @Autowired
    private WordladderService wordladderService;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/wordladder", method = RequestMethod.GET)
    public @ResponseBody ArrayList<String> wordLadder(String start, String end) {
        System.out.println("controller");
        return wordladderService.wordLadder(start, end);
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test() {
        System.out.println("test");
        return "test";
    }
}
