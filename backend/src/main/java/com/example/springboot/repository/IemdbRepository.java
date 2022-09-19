package com.example.springboot.repository;


import com.example.springboot.imedb.Actor;
import com.example.springboot.imedb.User;
import com.example.springboot.imedb.Comment;
import com.example.springboot.imedb.Movie;
import com.example.springboot.repository.Actor.ActorRepository;
import com.example.springboot.repository.User.UserRepository;
import com.example.springboot.repository.Comment.CommentRepository;
import com.example.springboot.repository.Movie.MovieRepository;
import com.example.springboot.repository.Vote.VoteRepository;

import java.sql.SQLException;


public class IemdbRepository {
    private static IemdbRepository instance;

    private IemdbRepository() {
//        createAllTables();
    }

    public static IemdbRepository getInstance() {
        if (instance == null)
            instance = new IemdbRepository();
        return instance;
    }


    public void insertActor(Actor actor) throws SQLException {
        ActorRepository actorMapper = new ActorRepository();
        actorMapper.insert(actor);
    }

    public void insertMovie(Movie movie) throws SQLException {
        MovieRepository movieRepository = new MovieRepository();
        movieRepository.insert(movie);
    }

    public void insertComment(Comment comment) throws SQLException {
        CommentRepository commentRepository = new CommentRepository(true);
        commentRepository.insert(comment);
    }

    public void insertUser(User user) throws SQLException {
        UserRepository userMapper = new UserRepository();
        userMapper.insert(user);
    }

    public void insertMovieActor(Movie movie) throws SQLException {
        MovieRepository movieMapper = new MovieRepository();
        movieMapper.insertMovieActor(movie);
    }

    public void insertMovieWriter(Movie movie) throws SQLException {
        MovieRepository movieMapper = new MovieRepository();
        movieMapper.insertMovieWriter(movie);
    }

    public void insertMovieGenre(Movie movie) throws SQLException {
        MovieRepository movieMapper = new MovieRepository();
        movieMapper.insertMovieGenre(movie);
    }

    public void insertUserComment(String userEmail, int commentId, int vote) throws SQLException {
        VoteRepository voteMapper = new VoteRepository();
        voteMapper.insertUserComment(userEmail, commentId, vote);
    }


}

//    private void createAllTables() {
//        try {
//            StudentMapper studentMapper = new StudentMapper(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            CourseMapper courseMapper = new CourseMapper(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            OfferingMapper offeringMapper = new OfferingMapper(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            PrerequisiteMapper prerequisiteMapper = new PrerequisiteMapper(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            ExamTimeMapper examTimeMapper = new ExamTimeMapper(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            ClassTimeMapper classTimeMapper = new ClassTimeMapper(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            GradeMapper gradeMapper = new GradeMapper(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            SelectionMapper selectionMapper = new SelectionMapper(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    //    Student
//    public void insertStudent(Student student) throws SQLException {
//        StudentMapper studentMapper = new StudentMapper();
//        studentMapper.insert(student);
//    }
//
//    //    Course
//    public void insertCourse(Course course) throws SQLException {
//        CourseMapper courseMapper = new CourseMapper();
//        courseMapper.insert(course);
//    }
//
//    //
//    public void insertOffering(Offering offering) throws SQLException {
//        OfferingMapper offeringMapper = new OfferingMapper();
//        offeringMapper.insert(offering);
//    }
//
//    //    ExamTime
//    public void insertExamTime(ExamTime examTime) throws SQLException {
//        ExamTimeMapper examTimeMapper = new ExamTimeMapper();
//        examTimeMapper.insert(examTime);
//    }
//
//    //    ClassTime
//    public void insertClassTime(ClassTime classTime) throws SQLException {
//        ClassTimeMapper classTimeMapper = new ClassTimeMapper();
//        classTimeMapper.insert(classTime);
//    }
//
//    //    Prerequisite
//    public void insertPrerequisite(HashMap<String, ArrayList<String>> prerequisiteInfo) throws SQLException {
//        PrerequisiteMapper prerequisiteMapper = new PrerequisiteMapper();
//        prerequisiteMapper.insert(prerequisiteInfo);
//    }
//
//    //    Grade
//    public void insertGrade(Grade grade) throws SQLException {
//        GradeMapper gradeMapper = new GradeMapper();
//        gradeMapper.insert(grade);
//    }
//
//    //    Selection
//    public void insertSelection(Selection selection) throws SQLException {
//        SelectionMapper selectionMapper = new SelectionMapper();
//        selectionMapper.insert(selection);
//    }
//
//    public void removeSelection(String studentId, String courseCode,
//                                String classCode) throws SQLException {
//        List<String> args = new ArrayList<>();
//        args.add(studentId);
//        args.add(courseCode);
//        Selection selection = new SelectionMapper().find(new Pair(args));
//        if (selection.getStatus().equals("submitted")) {
//            new OfferingMapper().decreaseSignedUp(courseCode, classCode);
//        }
//        new SelectionMapper().delete(new Pair(args));
//    }
//
//    public Offering findOfferingById(String courseCode, String classCode) throws SQLException {
//        List<String> args = new ArrayList<>();
//        args.add(courseCode);
//        args.add(classCode);
////        System.out.println("Offering we wanna get : " + courseCode + '-' + classCode);
//        Offering offering = new OfferingMapper().find(new Pair(args));
////        System.out.println("offering found");
//        Course course =  findCourseByCode(courseCode);
////        System.out.println("course found");
//        ExamTime examTime = new ExamTimeMapper().find(new Pair(args));
////        System.out.println("exam time found");
//        ClassTime classTime = new ClassTimeMapper().find(new Pair(args));
////        System.out.println("class time found");
//        ArrayList<String> prerequisites = new PrerequisiteMapper().getPrerequisites(courseCode);
//        course.setPrerequisites(prerequisites);
//        offering.setCourse(course);
//        offering.setClassTime(classTime);
//        offering.setExamTime(examTime);
////        System.out.println("IN THE DAMN DATABASE");
////        offering.print();
//        return offering;
//    }
//
//    public static Course findCourseByCode(String courseCode) throws SQLException {
//        Course course = new CourseMapper().find(courseCode);
//        return course;
//    }
//
//    public static Student findStudentById(String studentId) throws SQLException {
//        return new StudentMapper().find(studentId);
//    }
//
//    public int getCurrentTerm(String studentId) {
//        try {
//            return new GradeMapper().getCurrentTerm(studentId) + 1;
//        }
//        catch (SQLException e) {
//            return 1;
//        }
//    }
//
//    public WeeklySchedule findStudentScheduleById(String studentId, String status) throws SQLException {
//        List<Offering> offerings = new ArrayList<Offering>();
//        List<Selection> selections = new SelectionMapper().findStudentSchedule(studentId, status);
//        for (Selection selection: selections) {
//            Offering offering = findOfferingById(selection.getCourseCode(), selection.getClassCode());
//            offerings.add(offering);
//        }
//        int term = getCurrentTerm(studentId);
//        return new WeeklySchedule(offerings, term);
//    }
//
//    public CourseSelection findCourseSelectionById(String studentId) throws SQLException {
//        WeeklySchedule submitted = findStudentScheduleById(studentId, "submitted");
//        WeeklySchedule selected = findStudentScheduleById(studentId, "selected");
//        WeeklySchedule waiting = findStudentScheduleById(studentId, "waiting");
//        return new CourseSelection(submitted, selected, waiting);
//    }
//
//    public Student getStudent(String studentId) {
//        try {
//            Student student = new StudentMapper().find(studentId);
//            return student;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    public ArrayList<Grade> getStudentGrades(String studentId) {
//        try {
//            ArrayList<Grade> grades = new GradeMapper().getStudentGrades(studentId);
//            return grades;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    public Course getCourseByCode(String code) {
//        try {
//            Course course = new CourseMapper().find(code);
//            return course;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    public void deleteSelectionsById(String studentId) throws SQLException {
//        new SelectionMapper().deleteSelections(studentId);
//    }
//
//    public List<String> getStudentIds() throws SQLException {
//        StudentMapper studentMapper = new StudentMapper();
//        List<String> ids = studentMapper.getIds();
//        return ids;
//    }
//
//    public void finalizeScheduleById(String studentId) throws SQLException {
//        WeeklySchedule selected = findStudentScheduleById(studentId, "selected");
//        for (Offering offering: selected.getOfferings()) {
//            new OfferingMapper().increaseSignedUp(offering.getCourseCode(), offering.getClassCode());
//        }
//        new SelectionMapper().finalizeSelection(studentId);
//    }
//
//
//    public void checkWaitingLists() {
//        List<Selection> selections;
//        try {
//            selections = new SelectionMapper().findWaitings();
//            for (Selection selection: selections) {
//                new OfferingMapper().increaseCapacity(selection.getCourseCode(), selection.getClassCode());
//            }
//            new SelectionMapper().updateWaitings();
//        }
//        catch (Exception e) {
//            System.out.println("check waiting list exception " + e.getMessage());
//        }
//    }
//
//    private void setOfferingData(Offering offering) throws SQLException{
//        ArrayList<String> args = new ArrayList<>();
//        args.add(offering.getCourseCode());
//        args.add(offering.getClassCode());
//        Course course = new CourseMapper().find(offering.getCourseCode());
//        offering.setCourse(course);
//        ClassTime classTime = new ClassTimeMapper().find(new Pair(args));
//        offering.setClassTime(classTime);
//        ExamTime examTime = new ExamTimeMapper().find(new Pair(args));
//        offering.setExamTime(examTime);
//        ArrayList<String> prerequisites = Bolbolestan.getInstance().getPrerequisites(offering.getCourseCode());
//        offering.setPrerequisites(prerequisites);
//    }
//
//    public ArrayList<Offering> searchOfferings(SearchData searchData) {
//        System.out.println("in searchOfferings");
//        ArrayList<Offering> result;
//        try {
//            result = new OfferingMapper().getSearchedOfferings(searchData.getKeyword(), searchData.getType());
//            for (Offering offering : result)
//                setOfferingData(offering);
//            return result;
//        } catch (Exception e) {
//            System.out.println("error in searchOfferings in bolbol repo " + e.getMessage());