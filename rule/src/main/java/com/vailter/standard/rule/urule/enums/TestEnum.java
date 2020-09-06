package com.vailter.standard.rule.urule.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TestEnum {
    aaa("张三"), bbb("李四");
    private final String label;
}
