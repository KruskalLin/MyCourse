package mycourse.web.course;

import mycourse.dao.*;
import mycourse.entity.*;
import mycourse.exception.ResourceNotFoundException;
import mycourse.payloads.*;
import mycourse.security.CurrentUser;
import mycourse.security.UserPrincipal;
import mycourse.utils.SendMailUtils;
import mycourse.utils.ZipUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * All rights Reserved, Designed by Popping Lim
 *
 * @Author: Popping Lim
 * @Date: 2019/2/19
 * @Todo:
 */
@RestController
@RequestMapping("/course")
public class CourseController {

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

    @Autowired
    private SendMailUtils sendMailUtils;

    @Autowired
    private ZipUtils zipUtils;

    @PostMapping(value = "/create")
    public ResponseEntity<?> createCourse(@Valid @RequestBody CourseCreateRequest courseCreateRequest, @CurrentUser UserPrincipal userPrincipal) {
        Course course = new Course();
        course.setName(courseCreateRequest.getName());
        course.setUser(userRepository.getOne(userPrincipal.getId()));
        course.setStatus(Status.DRAFT);
        Forum forum = new Forum();
        forum.setTopics(new HashSet<>());
        course.setForum(forum);
        courseRepository.save(course);
        return new ResponseEntity(new ApiResponse(true, "创建成功"), HttpStatus.ACCEPTED);
    }


    @RequestMapping(value = "/list")
    public ListData<CourseInfo> getCourseList(@Valid @RequestBody FilterRequest filter) {
        Specification<Course> specification = new CourseSpecification(filter);
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getLimit());
        Page<Course> courses = courseRepository.findAll(specification, pageable);
        return new ListData<>(courses.stream().map(CourseInfo::new).collect(Collectors.toList()), courses.getTotalPages());
    }

    @GetMapping(value = "/coursewaresList")
    public List<String> getCourseList(@RequestParam("id") Long id) throws FileNotFoundException {
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        File upload = new File(path.getAbsolutePath(), "static/course/" + id + "/coursewares/");
        if (upload.list() == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(upload.list());
    }

    @GetMapping(value = "/homeworkHandout")
    public List<String> getHomeworkHandout(@RequestParam("id") Long id) throws FileNotFoundException {
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        File upload = new File(path.getAbsolutePath(), "static/homeworkHandout/" + id + "/");
        if (upload.list() == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(upload.list());
    }

    @GetMapping(value = "/detail")
    public CourseInfo getCourse(@RequestParam("id") Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        CourseInfo courseInfo = new CourseInfo(course);
        return courseInfo;
    }

    @GetMapping(value = "/releaseDetail")
    public ReleaseCourseInfo getReleaseCourse(@RequestParam("id") Long id) {
        ReleasedCourse course = releasedCourseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        ReleaseCourseInfo courseInfo = new ReleaseCourseInfo(course);
        return courseInfo;
    }

    @GetMapping(value = "/grades")
    public ResponseEntity<?> changeGradesStatus(@RequestParam("id") Long id, @RequestParam("status") Boolean grade) {
        ReleasedCourse course = releasedCourseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        course.setShowGrades(grade);
        releasedCourseRepository.save(course);
        return new ResponseEntity(new ApiResponse(true, "创建成功"), HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/releasedCourse")
    public ReleasedCourseResponse getReleasedCourse(@RequestParam("id") Long id) {
        ReleasedCourse course = releasedCourseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        ReleasedCourseResponse releasedCourseResponse = new ReleasedCourseResponse(course);
        return releasedCourseResponse;
    }

    @GetMapping(value = "/post")
    public ResponseEntity<?> postTopic(@RequestParam("name") String name, @RequestParam("desc") String desc, @RequestParam("id") Long id, @CurrentUser UserPrincipal userPrincipal) throws FileNotFoundException {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        ForumTopic forumTopic = new ForumTopic();
        forumTopic.setName(name);
        forumTopic.setDescription(desc);
        forumTopic.setCreatedAt(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        User user = userRepository.getOne(userPrincipal.getId());
        forumTopic.setUser(user);
        course.getForum().getTopics().add(forumTopic);
        courseRepository.save(course);
        return new ResponseEntity(new ApiResponse(true, "创建成功"), HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/postList")
    public List<ForumTopicResponse> getTopicList(@RequestParam("id") Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        return course.getForum().getTopics().stream().map(ForumTopicResponse::new).collect(Collectors.toList());
    }

    @PostMapping(value = "/homework")
    public ResponseEntity<?> createHomework(@Valid @RequestBody HomeworkRequest homeworkRequest) throws ParseException {
        ReleasedCourse course = releasedCourseRepository.findById(homeworkRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        Homework homework = new Homework();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(homeworkRequest.getEndDate());
        homework.setEndDate(date.toInstant());
        homework.setDescription(homeworkRequest.getDesc());
        if(course.getHomework() == null){
            course.setHomework(new HashSet<>());
        }
        course.getHomework().add(homework);
        releasedCourseRepository.save(course);

        for (User user: course.getUser()){
           sendMailUtils.SEND_COURSE_EMAIL(user, "课程发布", "课程 " + course.getCourse().getName() + " 发布作业了");
        }

        return new ResponseEntity(new ApiResponse(true, "创建成功"), HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/homework")
    public HomeworkResponse getHomeworkDetail(@RequestParam("id") Long id) {
        Homework homework = homeworkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Homework not found"));
        return new HomeworkResponse(homework);
    }

    @GetMapping(value = "/sendMail")
    public ResponseEntity<?> sendMail(@RequestParam("number") String number, @RequestParam("content") String content) {
        String username = number + "@smail.nju.edu.cn";
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        sendMailUtils.SEND_COURSE_EMAIL(user, "注意邮件", content);
        return new ResponseEntity(new ApiResponse(true, "发送成功"), HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/homeworkList")
    public List<HomeworkResponse> getHomeworkList(@RequestParam("id") Long id, @CurrentUser UserPrincipal userPrincipal) {
        ReleasedCourse course = releasedCourseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        User user = userRepository.getOne(userPrincipal.getId());
        Set<Homework> set = course.getHomework();
        List<HomeworkResponse> list = set.stream().map(HomeworkResponse::new).collect(Collectors.toList());
        for(CourseChoose courseChoose: user.getCourseChooses()) {
            if(courseChoose.getReleasedCourse().equals(course)){
                for (Homework key : courseChoose.getHomeworkGrades().keySet()) {
                    for(HomeworkResponse homework: list) {
                        if(homework.getId().equals(key.getId())) {
                            homework.setGrade(courseChoose.getHomeworkGrades().get(key));
                        }
                    }
                }
            }
        }
        return list;
    }


    @GetMapping(value = "/homeworkGradesList")
    public List<GradesResponse> getHomeworkGradesList(@RequestParam("id") Long id, @RequestParam("courseId") Long courseId) {
        Homework homework = homeworkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Homework not found"));
        ReleasedCourse course = releasedCourseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        List<GradesResponse> list = new ArrayList<>();
        for (User user: course.getUser()) {
            for (CourseChoose courseChoose: user.getCourseChooses()){
                if(courseChoose.getReleasedCourse().equals(course)) {
                    GradesResponse gradesResponse = new GradesResponse();
                    gradesResponse.setNumber(user.getNumber());
                    gradesResponse.setGrades(courseChoose.getHomeworkGrades().get(homework));
                    list.add(gradesResponse);
                }
            }
        }
        return list;
    }

    @GetMapping(value = "/postPost")
    public ResponseEntity<?> postPost(@RequestParam("id") Long parentId, @RequestParam("content") String content, @CurrentUser UserPrincipal userPrincipal) {
        if(!forumTopicRepository.findById(parentId).isPresent()) {
            ForumPost forumPost = forumPostRepository.findById(parentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
            ForumPost newPost = new ForumPost();
            User user = userRepository.getOne(userPrincipal.getId());
            newPost.setUser(user);
            newPost.setCreatedAt(LocalDateTime.now().toInstant(ZoneOffset.UTC));
            newPost.setDescription(content);
            forumPostRepository.save(newPost);
            forumPost.getPosts().add(newPost);
            forumPostRepository.save(forumPost);
        } else {
            ForumTopic forumPost = forumTopicRepository.findById(parentId).get();
            ForumPost newPost = new ForumPost();
            User user = userRepository.getOne(userPrincipal.getId());
            newPost.setUser(user);
            newPost.setCreatedAt(LocalDateTime.now().toInstant(ZoneOffset.UTC));
            newPost.setDescription(content);
            forumPostRepository.save(newPost);
            forumPost.getPosts().add(newPost);
            forumTopicRepository.save(forumPost);
        }
        return new ResponseEntity(new ApiResponse(true, "创建成功"), HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/allPost")
    public List<ForumPostResponse> getPostList(@RequestParam("id") Long id) throws FileNotFoundException {
        ForumTopic forumTopic = forumTopicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        List<ForumPostResponse> forumPostResponses = new ArrayList<>();
        forumPostResponses.add(new ForumPostResponse(new ForumPost(forumTopic), null));
        getForumPost(forumPostResponses, forumTopic);
        forumPostResponses.sort(new Comparator<ForumPostResponse>() {
            @Override
            public int compare(ForumPostResponse o1, ForumPostResponse o2) {
                return o1.getCreatedAt().compareTo(o2.getCreatedAt());
            }
        });
        return forumPostResponses;
    }

    private void getForumPost(List<ForumPostResponse> forumPostResponses, ForumTopic father) {
        if (father.getPosts() == null) {
            return;
        }
        for (ForumPost forumPost: father.getPosts()) {
            forumPostResponses.add(new ForumPostResponse(forumPost, father.getUser().getName()));
            getForumPost(forumPostResponses, forumPost);
        }
    }


    @PostMapping(value = "/release")
    public ResponseEntity<?> release(@Valid @RequestBody ReleaseRequest releaseRequest, @CurrentUser UserPrincipal userPrincipal) {
        Course course = courseRepository.findById(releaseRequest.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        if (course.getStatus() == Status.DRAFT || course.getStatus() == Status.REJECTED) {
            return new ResponseEntity(new ApiResponse(false, "无法创建"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ReleasedCourse releasedCourse = new ReleasedCourse();
        releasedCourse.setCourse(course);
        releasedCourse.setLimitPeople(releaseRequest.getLimit());
        releasedCourse.setCourseStatus(CourseStatus.DRAFT);
        releasedCourse.setCourseTime(new HashSet<>(releaseRequest.getTime()));
        releasedCourse.setShowGrades(false);
        releasedCourseRepository.save(releasedCourse);
        return new ResponseEntity(new ApiResponse(true, "创建成功"), HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/courseRelease")
    public List<ReleasedCourseResponse> getCourseRelease(@CurrentUser UserPrincipal userPrincipal) {
        User user = userRepository.getOne(userPrincipal.getId());
        List<Course> courses = courseRepository.findByUser(user);
        ArrayList<ReleasedCourse> releasedCourses = new ArrayList<>();
        for (Course c: courses){
            List<ReleasedCourse> releasedCourseList = releasedCourseRepository.findByCourse(c);
            releasedCourses.addAll(releasedCourseList);
        }
        return releasedCourses.stream().map(ReleasedCourseResponse::new).collect(Collectors.toList());
    }

    @RequestMapping("/homeworkFile")
    public String getHomework(@RequestParam("id") Long id, @CurrentUser UserPrincipal userPrincipal) throws FileNotFoundException {
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        if (!path.exists()) {
            path = new File("");
        }
        File upload = new File(path.getAbsolutePath(), "static/homework/" + id + "/");
        if (!upload.exists()) {
            return null;
        }
        String folderPath = upload.getAbsolutePath();
        String filename = StringUtils.cleanPath(userPrincipal.getUsername().substring(0, userPrincipal.getUsername().indexOf('@')) + ".zip");
        if(Files.exists(Paths.get(folderPath).resolve(filename))){
            return "homework/" + id + "/" + filename;
        }
        return null;
    }

    @PostMapping("/chooseList")
    public ListData<ChooseResponse> getChooseList(@Valid @RequestBody FilterRequest filterRequest, @CurrentUser UserPrincipal userPrincipal){
        User user = userRepository.getOne(userPrincipal.getId());
        List<Long> ids = new ArrayList<>();
        for (CourseChoose courseChoose: user.getCourseChooses()) {
            ids.add(courseChoose.getReleasedCourse().getId());
        }
        List<CourseStatus> statuses = new ArrayList<>();
        statuses.add(CourseStatus.CHECKED);
        statuses.add(CourseStatus.RUNNING);
        Specification<ReleasedCourse> specification = new ReleasedCourseSpecification(filterRequest, statuses);
        Pageable pageable = PageRequest.of(filterRequest.getPage(), filterRequest.getLimit());
        Page<ReleasedCourse> courses = releasedCourseRepository.findAll(specification, pageable);
        ListData<ChooseResponse> list = new ListData<ChooseResponse>(courses.stream().map(ChooseResponse::new).collect(Collectors.toList()), courses.getTotalPages());
        for (ChooseResponse chooseResponse: list.getItems()) {
            if(ids.contains(chooseResponse.getId())){
                chooseResponse.setHasChoosed(true);
            }
        }
        return list;
    }

    @GetMapping("/quit")
    public ResponseEntity<?> quit(@RequestParam("id") Long id, @CurrentUser UserPrincipal userPrincipal){
        ReleasedCourse releasedCourse = releasedCourseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        User user = userRepository.getOne(userPrincipal.getId());
        if (releasedCourse.getCourseStatus().equals(CourseStatus.END)){
            return new ResponseEntity(new ApiResponse(false, "已结课"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        releasedCourse.getUser().remove(user);
        releasedCourseRepository.save(releasedCourse);
        for (CourseChoose choose: user.getCourseChooses()){
            if(choose.getReleasedCourse().equals(releasedCourse)) {
                user.getCourseChooses().remove(choose);
                break;
            }
        }
        userRepository.save(user);
        CourseRecord courseRecord = new CourseRecord();
        courseRecord.setCreatedAt(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        courseRecord.setOperation(RecordOperation.QUIT);
        courseRecord.setCourse(releasedCourse.getCourse());
        courseRecord.setUser(user);
        courseRecordRepository.save(courseRecord);
        user.getCourseRecords().add(courseRecord);
        userRepository.save(user);
        return new ResponseEntity(new ApiResponse(true, "退课"), HttpStatus.ACCEPTED);
    }

    @PostMapping("/recordList")
    public List<RecordResponse> getRecordList(@Valid @RequestBody CourseRecordFilterRequest filterRequest, @CurrentUser UserPrincipal userPrincipal) throws ParseException {
        User user = userRepository.getOne(userPrincipal.getId());
        List<CourseRecord> list = null;

        if(!filterRequest.getTitle().equals("")){
            if(filterRequest.getType().equals("TEACHER")){
                list = user.getCourseRecords().stream().filter(t -> t.getCourse().getUser().getName().contains(filterRequest.getTitle())).collect(Collectors.toList());
            }else if(filterRequest.getType().equals("NAME")){
                list = user.getCourseRecords().stream().filter(t -> t.getCourse().getName().contains(filterRequest.getTitle())).collect(Collectors.toList());
            }
        } else {
            list = user.getCourseRecords().stream().collect(Collectors.toList());
        }

        if (filterRequest.getTime() != null && filterRequest.getTime().size() == 2) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = simpleDateFormat.parse(filterRequest.getTime().get(0));
            Date endDate = simpleDateFormat.parse(filterRequest.getTime().get(1));
            Instant startInstant = startDate.toInstant();
            Instant endInstant = endDate.toInstant();
            list = list.stream().filter(t -> (t.getCreatedAt().isBefore(endInstant) && t.getCreatedAt().isAfter(startInstant))).collect(Collectors.toList());
        }

        if (list == null){
            list = new ArrayList<>();
        }

        return list.stream().map(RecordResponse::new).collect(Collectors.toList());
    }

    @PostMapping("/teacherRecordList")
    public List<RecordResponse> getTeacherRecordList(@Valid @RequestBody CourseRecordFilterRequest filterRequest, @CurrentUser UserPrincipal userPrincipal) throws ParseException {
        User user = userRepository.getOne(userPrincipal.getId());
        List<CourseRecord> list = courseRecordRepository.findByCourse_User(user);
        if(!filterRequest.getTitle().equals("")){
            list = user.getCourseRecords().stream().filter(t -> t.getCourse().getUser().getName().contains(filterRequest.getTitle())).collect(Collectors.toList());
        }

        if (filterRequest.getType() != null) {
            list = list.stream().filter(t -> t.getUser().getGrade().equals(filterRequest.getType())).collect(Collectors.toList());
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

    @GetMapping("/choose")
    public ResponseEntity<?> choose(@RequestParam("id") Long id, @CurrentUser UserPrincipal userPrincipal){
        User user = userRepository.getOne(userPrincipal.getId());
        if(user.getCourseChooses() == null){
            user.setCourseChooses(new HashSet<>());
        }
        ReleasedCourse course = releasedCourseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        if(course.getCourseStatus() == CourseStatus.RUNNING && course.getUser().size() == course.getLimitPeople()){
            return new ResponseEntity(new ApiResponse(false, "人已满"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(course.getUser() == null){
            course.setUser(new HashSet<>());
        }
        System.out.println(course.getId());
        course.getUser().add(user);
        releasedCourseRepository.save(course);
        CourseChoose courseChoose = new CourseChoose();
        courseChoose.setReleasedCourse(course);
        courseChooseRepository.save(courseChoose);
        user.getCourseChooses().add(courseChoose);
        if(user.getCourseRecords() == null){
            user.setCourseRecords(new HashSet<>());
        }
        CourseRecord courseRecord = new CourseRecord();
        courseRecord.setCreatedAt(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        courseRecord.setOperation(RecordOperation.SELECT);
        courseRecord.setCourse(course.getCourse());
        courseRecord.setUser(user);
        courseRecordRepository.save(courseRecord);
        user.getCourseRecords().add(courseRecord);
        userRepository.save(user);
        return new ResponseEntity(new ApiResponse(true, "选课成功"), HttpStatus.ACCEPTED);
    }

    @GetMapping("/getGrades")
    public Integer getGrades(@RequestParam("id") Long id, @CurrentUser UserPrincipal userPrincipal) {
        ReleasedCourse releasedCourse = releasedCourseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        User user = userRepository.getOne(userPrincipal.getId());
        for (CourseChoose courseChoose : user.getCourseChooses()){
            if(courseChoose.getReleasedCourse().equals(releasedCourse)){
                return courseChoose.getGrades();
            }
        }
        return 0;
    }

    @GetMapping("/changeGrades")
    public ResponseEntity<?> changeGrades(@RequestParam("number") String number, @RequestParam("id") Long id, @RequestParam("grades") Integer grades) {
        String username = number + "@smail.nju.edu.cn";
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        ReleasedCourse releasedCourse = releasedCourseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        for (CourseChoose courseChoose : user.getCourseChooses()) {
            if (courseChoose.getReleasedCourse().equals(releasedCourse)) {
                courseChoose.setGrades(grades);
                courseChooseRepository.save(courseChoose);
                break;
            }
        }
        return new ResponseEntity(new ApiResponse(true, "修改成功"), HttpStatus.ACCEPTED);
    }

    @GetMapping("/changeHomeworkGrades")
    public ResponseEntity<?> changeHomeworkGrades(@RequestParam("number") String number, @RequestParam("id") Long id, @RequestParam("homeworkId") Long homeworkId, @RequestParam("grades") Integer grades) {
        String username = number + "@smail.nju.edu.cn";
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        ReleasedCourse releasedCourse = releasedCourseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(() -> new ResourceNotFoundException("Homework not found"));
        for (CourseChoose courseChoose : user.getCourseChooses()) {
            if (courseChoose.getReleasedCourse().equals(releasedCourse)) {
                if(courseChoose.getHomeworkGrades().get(homework) != null){
                    courseChoose.getHomeworkGrades().put(homework, grades);
                }
            }
        }
        userRepository.save(user);
        return new ResponseEntity(new ApiResponse(true, "修改成功"), HttpStatus.ACCEPTED);
    }

    @GetMapping("/getGradesList")
    public List<GradesResponse> getGradesList(@RequestParam("id") Long id) {
        ReleasedCourse releasedCourse = releasedCourseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        List<GradesResponse> list = new ArrayList<>();
        for (User user: releasedCourse.getUser()) {
            for (CourseChoose courseChoose : user.getCourseChooses()) {
                if (courseChoose.getReleasedCourse().equals(releasedCourse)) {
                    GradesResponse gradesResponse = new GradesResponse();
                    gradesResponse.setNumber(user.getNumber());
                    gradesResponse.setGrades(courseChoose.getGrades());
                    list.add(gradesResponse);
                }
            }
        }
        return list;
    }

    @PostMapping("/courseList")
    public ListData<CourseListResponse> getCourseCheckList(@Valid @RequestBody FilterRequest filterRequest){
        Specification<Course> specification = new CourseSpecification(filterRequest);
        Pageable pageable = PageRequest.of(filterRequest.getPage(), filterRequest.getLimit());
        Page<Course> courses = courseRepository.findAll(specification, pageable);
        return new ListData<>(courses.stream().map(CourseListResponse::new).collect(Collectors.toList()), courses.getTotalPages());
    }

    @GetMapping("/checkCourse")
    public ResponseEntity<?> checkCourse(@RequestParam("id") Long id, @RequestParam("state") String courseState) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        Status state = Status.valueOf(courseState);
        course.setStatus(state);
        courseRepository.save(course);
        return new ResponseEntity(new ApiResponse(true, "修改成功"), HttpStatus.ACCEPTED);
    }

    @PostMapping("/releaseList")
    public ListData<CheckListResponse> getReleaseList(@Valid @RequestBody FilterRequest filterRequest){
        Specification<ReleasedCourse> specification = new ReleasedCourseSpecification(filterRequest);
        Pageable pageable = PageRequest.of(filterRequest.getPage(), filterRequest.getLimit());
        Page<ReleasedCourse> courses = releasedCourseRepository.findAll(specification, pageable);
        return new ListData<>(courses.stream().map(CheckListResponse::new).collect(Collectors.toList()), courses.getTotalPages());
    }

    @GetMapping("/check")
    public ResponseEntity<?> check(@RequestParam("id") Long id, @RequestParam("state") String courseState) {
        ReleasedCourse releasedCourse = releasedCourseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        CourseStatus state = CourseStatus.valueOf(courseState);
        releasedCourse.setCourseStatus(state);
        releasedCourseRepository.save(releasedCourse);
        return new ResponseEntity(new ApiResponse(true, "修改成功"), HttpStatus.ACCEPTED);
    }

    @GetMapping("/start")
    public ResponseEntity<?> start(@RequestParam("id") Long id) {
        ReleasedCourse releasedCourse = releasedCourseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        releasedCourse.setCourseStatus(CourseStatus.RUNNING);
        Set<User> users = new HashSet<>(releasedCourse.getUser());
        releasedCourse.setUser(this.reallocate(releasedCourse.getUser(), releasedCourse.getLimitPeople()));
        releasedCourse.setStartTime(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        releasedCourseRepository.save(releasedCourse);
        for (User user: users){
            if(!releasedCourse.getUser().contains(user)) {
                List<CourseChoose> courseChooses = new ArrayList<>();
                for (CourseChoose courseChoose: user.getCourseChooses()){
                    if(courseChoose.getReleasedCourse().equals(releasedCourse)){
                        courseChooses.add(courseChoose);
                    }
                }
                user.getCourseChooses().removeAll(courseChooses);
                userRepository.save(user);
            }
        }
        return new ResponseEntity(new ApiResponse(true, "开课成功"), HttpStatus.ACCEPTED);
    }

    @GetMapping("/end")
    public ResponseEntity<?> end(@RequestParam("id") Long id) {
        ReleasedCourse releasedCourse = releasedCourseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        releasedCourse.setCourseStatus(CourseStatus.END);
        releasedCourse.setEndTime(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        releasedCourseRepository.save(releasedCourse);
        return new ResponseEntity(new ApiResponse(true, "结课成功"), HttpStatus.ACCEPTED);
    }

    @GetMapping("/myCourse")
    public List<MyCourseResponse> getMyCourse(@CurrentUser UserPrincipal userPrincipal) {
        User user = userRepository.getOne(userPrincipal.getId());
        return user.getCourseChooses().stream().map(MyCourseResponse::new).collect(Collectors.toList());
    }


    @GetMapping("/downloadHomework")
    public String downloadHomework(@RequestParam("id") Long id) throws FileNotFoundException {
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        if (!path.exists()) {
            path = new File("");
        }
        File upload = new File(path.getAbsolutePath(), "static/homework/" + id + "/");
        if (!upload.exists()) {
            return null;
        }
        File zip = new File(path.getAbsolutePath(), "static/homeworkDownload/");
        if (!zip.exists()) {
            zip.mkdir();
        }
        zipUtils.folderToZip(upload.getAbsolutePath(), zip.getAbsolutePath(), id + "");
        return "homeworkDownload/" + id + ".zip";
    }

    private Set<User> reallocate(Set<User> users, int limit){
        if (users.size() < limit) {
            return users;
        }
        ArrayList<User> list = new ArrayList<>(users);
        Set<User> set = new HashSet<>();
        for (int i = 0; i < limit; i++) {
            set.add(list.get(i));
        }
        return set;
    }


}