package net.whend.soodal.whend.model.top;

import net.whend.soodal.whend.model.base.Comment;
import net.whend.soodal.whend.model.base.HashTag;
import net.whend.soodal.whend.model.base.Schedule;
import net.whend.soodal.whend.model.base.User;

import java.util.Hashtable;

/**
 * Created by wonkyung on 15. 7. 9.
 */
public class Grid_Search_Schedule {

    private HashTag tag;


    // Constructor
    public Grid_Search_Schedule(){

    }
    public Grid_Search_Schedule(HashTag tag) {

        this.tag = tag;
    }

    public HashTag getTag() {
        return tag;
    }
}
