package project;

import javax.swing.*;
import java.awt.*;

public class AppTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 메인 프레임 생성
            JFrame frame = new JFrame("통합 애플리케이션");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1280, 800);
            frame.setLayout(new BorderLayout());

            // JTabbedPane 생성
            JTabbedPane tabbedPane = new JTabbedPane();

            // 캘린더 패널 추가
            JPanel calendarPanel = createCalendarTab();
            tabbedPane.addTab("캘린더", calendarPanel);

            // 시간표 패널 추가
            JPanel timeTablePanel = createTimeTableTab();
            tabbedPane.addTab("시간표", timeTablePanel);

            // 탭을 메인 프레임에 추가
            frame.add(tabbedPane, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }

    private static JPanel createCalendarTab() {
        // 캘린더 애플리케이션 생성
        CalendarApp calendarApp = new CalendarApp();
        return calendarApp.getMainPanel(); // 캘린더 패널 반환
    }

    private static JPanel createTimeTableTab() {
        // 교수 정보 생성
        Professor professor = new Professor("김진우", "", "02-940-5185");

        // 강의 정보 생성
        Course course = new Course("객체지향프로그래밍", professor, "참B101");
        course.addTime("화", "5"); // 5교시 (15:00)
        course.addTime("목", "6"); // 6교시 (16:30)

        // 시간표 GUI 생성
        TimeTableGUI timeTable = new TimeTableGUI();
        timeTable.addCourse(course);
        
        return timeTable.getMainPanel(); // 시간표 패널 반환
    }
}
