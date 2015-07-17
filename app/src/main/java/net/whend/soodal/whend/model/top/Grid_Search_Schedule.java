package net.whend.soodal.whend.model.top;

import net.whend.soodal.whend.model.base.HashTag;

/**
 * Created by wonkyung on 15. 7. 9.
 */
public class Grid_Search_Schedule {

    private HashTag tag;


    // Constructor
    public Grid_Search_Schedule(){
        //this.tag.setTitle("테스트지롱");
        //this.tag.setPhoto("이것도테스트지롱");

    }
    public Grid_Search_Schedule(HashTag tag) {

        this.tag = tag;
    }

    public HashTag getTag() {
        return tag;
    }
}
