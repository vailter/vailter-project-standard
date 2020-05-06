package com.vailter.standard.java8;

import com.vailter.standard.java8.domain.SClass;
import com.vailter.standard.java8.domain.Student;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamDemo {
    public static void main(String[] args) {
//        testCollect();
//        testFilter();
//        testMap();
//        testFlatMap();
//         testCount();
//        testReduce();
        testAdvancedCollect();
    }

    private static void testCollect() {
        List<Student> studentList = Stream.of(
                new Student("卡卡罗特", 22, 175, null),
                new Student("孙悟空", 40, 180, null),
                new Student("孙行者", 50, 185, null),
                new Student("卡卡罗特", 50, 185, null))
                .collect(Collectors.toList());
        System.out.println(studentList);

        Set<Student> studentSet = Stream.of(
                new Student("卡卡罗特", 22, 175, null),
                new Student("孙悟空", 40, 180, null),
                new Student("孙行者", 50, 185, null),
                new Student("卡卡罗特", 50, 185, null))
                .collect(Collectors.toSet());
        System.out.println(studentSet);

        Map<String, Student> studentMap = Stream.of(
                new Student("卡卡罗特", 22, 175, null),
                new Student("孙悟空", 40, 180, null),
                new Student("孙行者", 50, 185, null),
                new Student("卡卡罗特", 50, 185, null))
                .distinct()
                .collect(Collectors.toMap(Student::getName, student -> student));
        System.out.println(studentMap);
    }

    private static void testFilter() {
        List<Student> studentList = Stream
                .of(
                        new Student("卡卡罗特", 22, 175, null),
                        new Student("孙悟空", 40, 180, null),
                        new Student("孙行者", 50, 185, null)
                )
                .filter(student -> student.getHeight() > 175)
                .collect(Collectors.toList());
        System.out.println(studentList);
    }

    private static void testMap() {
        List<Student> students = new ArrayList<>(3);
        students.add(new Student("卡卡罗特", 22, 175, null));
        students.add(new Student("孙悟空", 40, 180, null));
        students.add(new Student("孙行者", 50, 185, null));

        List<String> names = students.stream().map(Student::getName)
                .collect(Collectors.toList());
        System.out.println(names);
    }

    private static void testFlatMap() {
        List<Student> students = new ArrayList<>(3);
        students.add(new Student("卡卡罗特", 22, 175, null));
        students.add(new Student("孙悟空", 40, 180, null));
        students.add(new Student("孙行者", 50, 185, null));

        List<Student> studentList = Stream.of(students,
                Arrays.asList(new Student("据八戒", 22, 183, null),
                        new Student("沙和尚", 22, 175, null)))
                .flatMap(students1 -> students1.stream()).collect(Collectors.toList());
        System.out.println(studentList);

        List<String> names = students.stream()
                .map(Student::getName)
                .flatMap(s -> Stream.of(s.split("")))
                .collect(Collectors.toList());
        System.out.println(names);
    }

    private static void testCount() {
        // max min avg
        Integer maxAge = Stream.of(
                new Student("卡卡罗特", 22, 175, null),
                new Student("孙悟空", 40, 180, null),
                new Student("孙行者", 50, 185, null),
                new Student("卡卡罗特", 50, 185, null))
                .map(Student::getAge)
                .max(Integer::compareTo)
                .orElse(null);

        System.out.println(maxAge);

        long count = Stream.of(
                new Student("卡卡罗特", 22, 175, null),
                new Student("孙悟空", 40, 180, null),
                new Student("孙行者", 50, 185, null),
                new Student("卡卡罗特", 50, 185, null))
                .filter(student -> StringUtils.equals(student.getName(), "卡卡罗特"))
                .count();
        System.out.println(count);
    }

    private static void testReduce() {
//        Integer reduce = Stream.of(1, 2, 3, 4).reduce(0, (a, b) -> a + b);
        Integer reduce = Stream.of(1, 2, 3, 4).reduce(0, Integer::sum);
        System.out.println(reduce);
    }

    private static void testAdvancedCollect() {
        List<Student> students1 = new ArrayList<>(3);
        students1.add(new Student("卡卡罗特", 22, 175, null));
        students1.add(new Student("孙悟空", 40, 180, null));
        students1.add(new Student("孙行者", 50, 185, null));

        SClass sClass1 = new SClass("一班", students1);
        //复制students1，并移除一个学生
        List<Student> students2 = new ArrayList<>(students1);
        students2.remove(1);
        SClass sClass2 = new SClass("二班", students2);
        //将ostClass1、ostClass2转换为Stream
        Stream<SClass> classStream = Stream.of(sClass1, sClass2);
        //SClass biggestClass = classStream
        //        .collect(
        //                Collectors.maxBy(Comparator.comparing(sClass -> sClass.getStudents().size()))
        //        )
        //        .orElseGet(SClass::new);
        SClass biggestClass = classStream
                .max(Comparator.comparing(sClass -> sClass.getStudents().size()))
                .orElseGet(SClass::new);
        System.out.println("人数最多的班级是：" + biggestClass.getName());

        Double avgAge = students1.stream().collect(Collectors.averagingInt(Student::getAge));

        System.out.println("一班平均年龄是：" + avgAge);
    }
}
