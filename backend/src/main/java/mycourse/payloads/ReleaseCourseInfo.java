package mycourse.payloads;

import mycourse.entity.ReleasedCourse;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2019/3/2
 * @Todo:
 */
public class ReleaseCourseInfo {
    private Long id;

    private boolean showGrades;

    public ReleaseCourseInfo(ReleasedCourse releasedCourse) {
        this.id = releasedCourse.getCourse().getId();
        this.showGrades = releasedCourse.isShowGrades();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isShowGrades() {
        return showGrades;
    }

    public void setShowGrades(boolean showGrades) {
        this.showGrades = showGrades;
    }
}
