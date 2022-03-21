package io.wisoft.firstservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {

  @GetMapping("/hello")
  public String hello() {
    return "FirstService - Hello World!";
  }

}
