package org.starbunk.bunkbot.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class OnlineTester {


    @PostConstruct
    public void hello() {
        System.out.println("Begin");
    }
}
