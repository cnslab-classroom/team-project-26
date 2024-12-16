package project;

import javax.swing.*;
// import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

class Professor {
    private String name;        // 교수 이름
    private String office;      // 연구실
    private String phoneNumber; // 전화번호
    
    public Professor(String name, String office, String phoneNumber) {
        this.name = name;
        this.office = office;
        this.phoneNumber = phoneNumber;
    }
    
    // Getters & Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getOffice() { return office; }
    public void setOffice(String office) { this.office = office; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}

class Course {
    private String name;            // 과목명
    private Professor professor;     // 담당 교수
    private String classroom;        // 강의실
    private ArrayList<String[]> schedule; // 강의 시간표 (요일, 교시)
    private int credits;             // 학점
    private String memo;             // 메모
    
    public Course(String name, Professor professor, String classroom) {
        this.name = name;
        this.professor = professor;
        this.classroom = classroom;
        this.schedule = new ArrayList<>();
        this.memo = "";
    }
    
    public void addTime(String day, String period) {
        schedule.add(new String[]{day, period});
    }
    
    // Getters & Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Professor getProfessor() { return professor; }
    public void setProfessor(Professor professor) { this.professor = professor; }
    
    public String getClassroom() { return classroom; }
    public void setClassroom(String classroom) { this.classroom = classroom; }
    
    public ArrayList<String[]> getSchedule() { return schedule; }
    
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    
    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }
}

class TimeTableGUI extends JFrame {
    private JPanel mainPanel;
    private ArrayList<Course> courses;
    private final String[] PERIODS = {"08:00", "09:00", "10:30", "12:00", "13:30", "15:00", "16:30", "18:00"};
    private HashMap<JPanel, Course> cellToCourse;
    
    public TimeTableGUI() {
        this.setTitle("시간표");
        this.setSize(1280, 720);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        mainPanel = new JPanel(new BorderLayout());
        courses = new ArrayList<>();
        cellToCourse = new HashMap<>();
        
        // 시간표 그리드 생성
        JPanel timeTablePanel = createTimeTablePanel();
        mainPanel.add(timeTablePanel, BorderLayout.CENTER);
        
        this.add(mainPanel);
        this.setVisible(false);
    }
    
    private JPanel createTimeTablePanel() {
        JPanel panel = new JPanel(new GridLayout(9, 6));
        String[] days = {"교시", "월", "화", "수", "목", "금"};
        
        // 요일 헤더 추가
        for (String day : days) {
            JLabel label = new JLabel(day, SwingConstants.CENTER);
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panel.add(label);
        }
        
        // 교시별 그리드 생성
        for (int i = 0; i < PERIODS.length; i++) {
            JLabel timeLabel = new JLabel((i) + "교시\n" + PERIODS[i], SwingConstants.CENTER);
            timeLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panel.add(timeLabel);
            
            // 각 교시별 빈 셀 추가
            for (int j = 0; j < 5; j++) {
                JPanel cell = new JPanel();
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                panel.add(cell);
            }
        }
        
        return panel;
    }
    
    public void addCourse(Course course) {
        courses.add(course);
        updateTimeTable();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
    
    private void updateTimeTable() {
        // 기존 시간표 초기화
        cellToCourse.clear();
        Component[] components = mainPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                Component[] cells = panel.getComponents();
                for (Component cell : cells) {
                    if (cell instanceof JPanel) {
                        ((JPanel) cell).removeAll();
                    }
                }
            }
        }
        
        // 과목 정보 추가
        for (Course course : courses) {
            for (String[] schedule : course.getSchedule()) {
                int dayIndex = getDayIndex(schedule[0]);
                int periodIndex = Integer.parseInt(schedule[1]);
                
                if (dayIndex >= 0 && periodIndex >= 0 && periodIndex < 8) {
                    JPanel cell = new JPanel();
                    cell.setLayout(new BoxLayout(cell, BoxLayout.Y_AXIS));
                    
                    JLabel nameLabel = new JLabel(course.getName());
                    JLabel profLabel = new JLabel(course.getProfessor().getName());
                    JLabel phoneLabel = new JLabel(course.getProfessor().getPhoneNumber());
                    phoneLabel.setFont(phoneLabel.getFont().deriveFont(10f));
                    phoneLabel.setForeground(Color.GRAY);
                    JLabel roomLabel = new JLabel(course.getClassroom());
                    
                    cell.add(nameLabel);
                    cell.add(profLabel);
                    cell.add(phoneLabel);
                    cell.add(roomLabel);
                    cell.setBackground(new Color(230, 230, 250));
                    
                    // 클릭 이벤트 추가
                    cell.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            String currentMemo = course.getMemo();
                            String newMemo = JOptionPane.showInputDialog(TimeTableGUI.this, 
                                "메모를 입력하세요:", currentMemo);
                            if (newMemo != null) {
                                course.setMemo(newMemo);
                                if (!newMemo.isEmpty()) {
                                    cell.setToolTipText(newMemo);
                                }
                            }
                        }
                    });
                    
                    // 기존 메모가 있다면 툴팁으로 표시
                    if (!course.getMemo().isEmpty()) {
                        cell.setToolTipText(course.getMemo());
                    }
                    
                    cellToCourse.put(cell, course);
                    
                    // 해당 위치의 셀 업데이트
                    Component[] components2 = mainPanel.getComponents();
                    if (components2[0] instanceof JPanel) {
                        JPanel timeTablePanel = (JPanel) components2[0];
                        timeTablePanel.remove(6 + periodIndex * 6 + dayIndex);
                        timeTablePanel.add(cell, 6 + periodIndex * 6 + dayIndex);
                    }
                }
            }
        }
        
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    
    private int getDayIndex(String day) {
        switch (day) {
            case "월": return 1;
            case "화": return 2;
            case "수": return 3;
            case "목": return 4;
            case "금": return 5;
            default: return -1;
        }
    }
}

