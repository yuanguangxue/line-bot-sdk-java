package com.hp.enumerate;


import org.junit.Test;



public class TagEnumTest {

    @Test
    public void toJsonString() {
        System.out.println(TagEnum.toJsonString());
    }

    @Test
    public void toMap(){
        System.out.println(TagEnum.toMap());
    }
}
