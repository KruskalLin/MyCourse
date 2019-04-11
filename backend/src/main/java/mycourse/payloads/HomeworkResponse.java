package mycourse.payloads;

import mycourse.entity.Homework;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2019/2/21
 * @Todo:
 */
public class HomeworkResponse {
    private Long id;

    private String desc;

    private String endDate;

    private Integer grade;

    public HomeworkResponse(Homework homework) {
        this.id = homework.getId();
        this.desc = homework.getDescription();
        this.endDate = homework.getEndDate().toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }
}
