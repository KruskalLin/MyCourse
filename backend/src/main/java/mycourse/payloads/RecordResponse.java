package mycourse.payloads;

import mycourse.entity.CourseRecord;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2019/2/22
 * @Todo:
 */
public class RecordResponse {

    private String time;

    private String operation;

    private String name;

    private String teacherName;

    public RecordResponse(CourseRecord courseRecord) {
        this.time = courseRecord.getCreatedAt().toString();
        this.operation = courseRecord.getOperation().name();
        this.name = courseRecord.getCourse().getName();
        this.teacherName = courseRecord.getCourse().getUser().getName();
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
