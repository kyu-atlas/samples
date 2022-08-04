package pers.sample.sb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/randomNumber")
public class RandomNumberController {
    private static final Logger _logger = LoggerFactory.getLogger(RandomNumberController.class);

    @Autowired
    private RandomNumberService _service;

    @GetMapping
    public @ResponseBody RandomNumber get() {
        _logger.info(this.toString());
        return _service.acquireRandomNumber();
    }

}
