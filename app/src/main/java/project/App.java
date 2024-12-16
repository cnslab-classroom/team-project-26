package project;

import javax.swing.SwingUtilities;

public class App {
        public static void main(String[] args) {
            
        SwingUtilities.invokeLater(CalendarApp::new);

         // 교수 정보 생성
         Professor professor = new Professor(
            "김진우",
            "",
            "02-940-5185");
        
        // 강의 정보 생성
        Course course = new Course(
            "객체지향프로그래밍",
            professor,
            "참B101");

        course.addTime("화", "5"); // 5교시 (15:00)
        course.addTime("목", "6"); // 6교시 (16:30)
        
        // 시간표 생성
        TimeTableGUI timeTable = new TimeTableGUI();
        timeTable.addCourse(course);

        }
    }

