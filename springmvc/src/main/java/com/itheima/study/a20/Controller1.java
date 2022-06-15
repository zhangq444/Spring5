package com.itheima.study.a20;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.yaml.snakeyaml.Yaml;

@Controller
@Slf4j
public class Controller1 {

    @GetMapping("/test1")
    public ModelAndView test1() throws Exception {
        log.info("======test1()");
        return null;
    }

    @PostMapping("/test2")
    public ModelAndView test2(@RequestParam("name") String name) throws Exception {
        log.info("======test1({})", name);
        return null;
    }

    @PutMapping("/test3")
    public ModelAndView test3(@Token String token) throws Exception {
        log.info("======test3({})", token);
        return null;
    }

    @RequestMapping("/test4.yml")
    @Yml
    public User test4() throws Exception {
        log.info("======test4()");
        return new User("小明", 18);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    static class User {

        private String name;
        private Integer age;
    }


}
