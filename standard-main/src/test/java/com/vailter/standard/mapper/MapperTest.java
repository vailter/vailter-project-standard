package com.vailter.standard.mapper;

import org.junit.jupiter.api.Test;

public class MapperTest {
    @Test
    void testMapper() {
        MapperProxy proxy = new MapperProxy();

        UserMapper mapper = proxy.newInstance(UserMapper.class);
        mapper.hashCode();
        //User user = mapper.getUserById(1001);
        //
        //System.out.println("ID:" + user.getId());
        //System.out.println("Name:" + user.getName());
        //System.out.println("Age:" + user.getAge());
        //
        //System.out.println(mapper.toString());
    }
}
