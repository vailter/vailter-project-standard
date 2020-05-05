package com.vailter.standard.java8.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 班级
 *
 * @author Vailter67
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SClass {
    private String name;
    private List<Student> students;
}
