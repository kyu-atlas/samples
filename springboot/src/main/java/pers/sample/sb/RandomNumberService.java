package pers.sample.sb;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class RandomNumberService {
    private static final Logger _logger = LoggerFactory.getLogger(RandomNumberService.class);

    private RandomDataGenerator _gen = new RandomDataGenerator();
    private RandomNumber _errData = new RandomNumber(-1, -1);
    private final String objectName = this.toString();

    @Autowired
    private RandomNumberRepository _repo;
    @Autowired
    private TransactionTemplate _trans;

    public RandomNumber acquireRandomNumber() {
        _logger.info(String.format("enter. thread: %s <-> object: %s", Thread.currentThread().getName(), objectName));
        _logger.info(this.toString());
        var data = _repo.findById(_gen.nextInt(1, 10000)).orElse(_errData);
        if (data.randomNumber != -1) {
            _trans.execute(status -> _repo.updateHitCountById(data.randomNumber));
        }
        _logger.info(String.format("leave. thread: %s <-> object: %s", Thread.currentThread().getName(), objectName));
        return data;
    }

}
