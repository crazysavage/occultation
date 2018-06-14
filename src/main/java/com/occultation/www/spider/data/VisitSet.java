package com.occultation.www.spider.data;

import java.util.HashSet;
import java.util.Set;

/**
 * TODO
 *
 * @author Liss
 * @version V1.0
 * @since 2017-09-01 14:14
 */
public class VisitSet implements Visit {

    Set<String> visSet;

    public VisitSet() {
        this.visSet = new HashSet<>();
    }

    @Override
    public boolean isVisited(String hash) {

        return !visSet.add(hash);
    }
}
