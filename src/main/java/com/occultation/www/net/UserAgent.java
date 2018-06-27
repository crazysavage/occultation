package com.occultation.www.net;

import com.occultation.www.util.FileUtil;

import java.util.Random;

/**
 * @Type UserAgent
 * @Desc TODO
 * @author Savage
 * @created 2017年8月18日 下午5:35:32
 * @version 1.0.0
 */
public class UserAgent {

    private static final String[] USER_AGENT;

    static {
        USER_AGENT = FileUtil.readLine("classpath:userAgents").toArray(new String[0]);
    }


    public static String getAgent() {
        int len = USER_AGENT.length;

        return USER_AGENT[new Random().nextInt(len)];
    }

}
