package mycourse.web.course;

import mycourse.dao.*;
import mycourse.entity.*;
import mycourse.payloads.CourseRecordFilterRequest;
import mycourse.payloads.RecordResponse;
import mycourse.security.CurrentUser;
import mycourse.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2019/2/21
 * @Todo:
 */
@RestController
@RequestMapping("/data")
public class DataController {
    @Autowired
    private CourseDao courseRepository;

    @Autowired
    private UserDao userRepository;

    @Autowired
    private ReleasedCourseDao releasedCourseRepository;

    @Autowired
    private CourseChooseDao courseChooseRepository;

    @Autowired
    private CourseRecordDao courseRecordRepository;

    @Autowired
    private ForumTopicDao forumTopicRepository;

    @Autowired
    private HomeworkDao homeworkRepository;

    @Autowired
    private ForumPostDao forumPostRepository;


    @RequestMapping(value = "/student")
    public Integer getStudentUsers() {
        List<User> users = userRepository.findAll().stream().filter(t -> new ArrayList<Role>(t.getRoles()).contains(new Role(RoleName.ROLE_STUDENT))).collect(Collectors.toList());
        return users.size();
    }

    @RequestMapping(value = "/teacher")
    public Integer getTeacherUsers() {
        List<User> users = userRepository.findAll().stream().filter(t -> new ArrayList<Role>(t.getRoles()).contains(new Role(RoleName.ROLE_STUDENT))).collect(Collectors.toList());
        return users.size();
    }

    @PostMapping("/records")
    public List<RecordResponse> getTeacherRecordList(@Valid @RequestBody CourseRecordFilterRequest filterRequest, @CurrentUser UserPrincipal userPrincipal) throws ParseException {
        List<CourseRecord> list = courseRecordRepository.findAll();
        if(!filterRequest.getTitle().equals("")){
            if(filterRequest.getType().equals("TEACHER")){
                list = list.stream().filter(t -> t.getCourse().getUser().getName().contains(filterRequest.getTitle())).collect(Collectors.toList());
            }else if(filterRequest.getType().equals("NAME")){
                list = list.stream().filter(t -> t.getCourse().getName().contains(filterRequest.getTitle())).collect(Collectors.toList());
            }
        } else {
            list = list.stream().collect(Collectors.toList());
        }

        if (filterRequest.getTime() != null && filterRequest.getTime().size() == 2) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = simpleDateFormat.parse(filterRequest.getTime().get(0));
            Date endDate = simpleDateFormat.parse(filterRequest.getTime().get(1));
            Instant startInstant = startDate.toInstant();
            Instant endInstant = endDate.toInstant();
            list = list.stream().filter(t -> (t.getCreatedAt().isBefore(endInstant) && t.getCreatedAt().isAfter(startInstant))).collect(Collectors.toList());
        }

        return list.stream().map(RecordResponse::new).collect(Collectors.toList());
    }
}
