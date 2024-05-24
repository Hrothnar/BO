package com.tech.neo.bo.util;

import com.tech.neo.bo.domain.entity.Account;
import com.tech.neo.bo.domain.entity.User;
import com.tech.neo.bo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component()
public class Scheduler {
    private final UserService userService;
    private final ThreadPoolTaskScheduler taskScheduler;

    @Autowired()
    public Scheduler(UserService userService) {
        this.userService = userService;
        this.taskScheduler = new ThreadPoolTaskScheduler();
        this.taskScheduler.setPoolSize(1);
        this.taskScheduler.initialize();
    }

    @Scheduled(cron = "0 * * * * *")
    public void percentage() {
        taskScheduler.execute(() -> {
            List<User> users = userService.findAll();
            users.forEach((user) -> {
                Account account = user.getAccount();
                double initialDeposit = account.getInitialDeposit().doubleValue();
                double balance = account.getBalance().doubleValue();
                double increasedBalance = balance * 1.05;
                boolean wasNotLimitReached = increasedBalance <= (initialDeposit * 2.07);
                if (wasNotLimitReached) {
                    account.setBalance(BigDecimal.valueOf(increasedBalance));
                    userService.save(user);
                }
            });
        });
    }
}
