package mycourse.web.course;

import mycourse.dao.CourseChooseDao;
import mycourse.dao.HomeworkDao;
import mycourse.dao.ReleasedCourseDao;
import mycourse.dao.UserDao;
import mycourse.entity.CourseChoose;
import mycourse.entity.Homework;
import mycourse.entity.ReleasedCourse;
import mycourse.entity.User;
import mycourse.exception.BadRequestException;
import mycourse.exception.InternalException;
import mycourse.exception.ResourceNotFoundException;
import mycourse.payloads.ApiResponse;
import mycourse.security.CurrentUser;
import mycourse.security.UserPrincipal;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Set;


@RestController
@RequestMapping("/upload")
public class FileUploadController {
    @Autowired
    private UserDao userRepository;

    @Autowired
    private HomeworkDao homeworkRepository;

    @Autowired
    private ReleasedCourseDao releasedCourseRepository;

    @Autowired
    private CourseChooseDao courseChooseRepository;

    private static String config = "http://193.112.82.110:8000/";

    @RequestMapping("/coursewares")
    public String uploadCoursewares(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id) throws FileNotFoundException {
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        if (!path.exists()) path = new File("");

        File upload = new File(path.getAbsolutePath(), "static/course/" + id + "/coursewares/");
        if ((!upload.exists() && !upload.mkdirs()))
            throw new InternalException("Create folder failed");
        String folderPath = upload.getAbsolutePath();
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (file.isEmpty())
            throw new BadRequestException("File is empty");
        if (filename.contains("..")) {
            // This is a security check
            throw new BadRequestException(
                    "Cannot store file with relative path outside current directory " + filename);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, Paths.get(folderPath).resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
            return "course/" + id + "/coursewares/" + filename;
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalException("Upload failed");
        }
    }

    @RequestMapping("/homeworkHandout")
    public String uploadHomeworkHandout(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id) throws FileNotFoundException {
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        if (!path.exists()) path = new File("");

        File upload = new File(path.getAbsolutePath(), "static/homeworkHandout/" + id + "/");
        if ((!upload.exists() && !upload.mkdirs()))
            throw new InternalException("Create folder failed");
        String folderPath = upload.getAbsolutePath();
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (file.isEmpty())
            throw new BadRequestException("File is empty");
        if (filename.contains("..")) {
            // This is a security check
            throw new BadRequestException(
                    "Cannot store file with relative path outside current directory " + filename);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, Paths.get(folderPath).resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
            return "homeworkHandout/" + id + "/" + filename;
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalException("Upload failed");
        }
    }

    @RequestMapping("/homework")
    public String uploadHomework(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id, @CurrentUser UserPrincipal userPrincipal) throws FileNotFoundException {
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        if (!path.exists()) path = new File("");

        File upload = new File(path.getAbsolutePath(), "static/homework/" + id + "/");
        if ((!upload.exists() && !upload.mkdirs()))
            throw new InternalException("Create folder failed");
        String folderPath = upload.getAbsolutePath();
        String filename = StringUtils.cleanPath(userPrincipal.getUsername().substring(0, userPrincipal.getUsername().indexOf('@')) + ".zip");
        if (file.isEmpty())
            throw new BadRequestException("File is empty");
        if (filename.contains("..")) {
            // This is a security check
            throw new BadRequestException(
                    "Cannot store file with relative path outside current directory " + filename);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, Paths.get(folderPath).resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
            return "homework/" + id + "/" + filename;
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalException("Upload failed");
        }
    }

    @RequestMapping("/removeHomework")
    public ResponseEntity<?> removeHomework(@RequestParam("id") Long id, @CurrentUser UserPrincipal userPrincipal) throws IOException {
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        if (!path.exists()) path = new File("");

        File upload = new File(path.getAbsolutePath(), "static/homework/" + id + "/");
        if ((!upload.exists() && !upload.mkdirs()))
            throw new InternalException("Create folder failed");
        String folderPath = upload.getAbsolutePath();
        String filename = StringUtils.cleanPath(userPrincipal.getUsername().substring(0, userPrincipal.getUsername().indexOf('@')) + ".zip");
        Files.delete(Paths.get(folderPath).resolve(filename));
        return new ResponseEntity(new ApiResponse(true, "删除成功"), HttpStatus.ACCEPTED);
    }


    @RequestMapping("/grades")
    public ResponseEntity<?> uploadGrades(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id) throws IOException, InvalidFormatException {
        ReleasedCourse releasedCourse = releasedCourseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        Set<User> set = releasedCourse.getUser();
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            int rows = sheet.getLastRowNum() + 1;
            for (int row = 0; row < rows; row++) {
                Row r = sheet.getRow(row);
                r.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                for (User user: set){
                    if(user.getNumber().equals(r.getCell(0).getStringCellValue())){
                        for(CourseChoose courseChoose: user.getCourseChooses()){
                            if (courseChoose.getReleasedCourse().equals(releasedCourse)){
                                courseChoose.setGrades((int) r.getCell(1).getNumericCellValue());
                            }
                        }
                    }
                }
            }
        }
        releasedCourseRepository.save(releasedCourse);
        return new ResponseEntity(new ApiResponse(true, "创建成功"), HttpStatus.ACCEPTED);
    }

    @RequestMapping("/homeworkGrades")
    public ResponseEntity<?> uploadHomeworkGrades(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id) throws IOException, InvalidFormatException {
        Homework homework = homeworkRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Homework not found"));
        ReleasedCourse releasedCourse = releasedCourseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Homework not found"));
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Set<User> set = releasedCourse.getUser();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            int rows = sheet.getLastRowNum() + 1;
            for (int row = 0; row < rows; row++) {
                Row r = sheet.getRow(row);
                r.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                for (User user: set){
                    if(user.getNumber().equals(r.getCell(0).getStringCellValue())){
                        for(CourseChoose courseChoose: user.getCourseChooses()){
                            if (courseChoose.getReleasedCourse().equals(releasedCourse)){
                                if(courseChoose.getHomeworkGrades()==null)
                                    courseChoose.setHomeworkGrades(new HashMap<>());
                                courseChoose.getHomeworkGrades().put(homework, (int) r.getCell(1).getNumericCellValue());
                            }
                        }
                    }
                }
            }
        }
        releasedCourseRepository.save(releasedCourse);
        return new ResponseEntity(new ApiResponse(true, "创建成功"), HttpStatus.ACCEPTED);
    }


}
