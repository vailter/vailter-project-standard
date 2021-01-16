package com.vailter.standard.learn.chain.demo5;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private String userName;
    private String password;
    private String roleName;
}
