package project;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

public class CalendarApp {
    private JFrame frame;
    private JPanel calendarPanel;
    private JLabel monthYearLabel;
    private JButton prevButton, nextButton;
    private Map<LocalDate, String> notesMap;

    private YearMonth currentYearMonth;
    private final YearMonth startYearMonth;
    private final YearMonth endYearMonth;

    public CalendarApp() {
        notesMap = new HashMap<>();
        frame = new JFrame("Calendar with Notes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        LocalDate today = LocalDate.now();
        startYearMonth = YearMonth.of(today.getYear() - 2, 1);
        endYearMonth = YearMonth.of(today.getYear() + 2, 12);
        currentYearMonth = YearMonth.from(today);

        // Top panel for navigation
        JPanel topPanel = new JPanel(new BorderLayout());
        monthYearLabel = new JLabel("", SwingConstants.CENTER);
        monthYearLabel.setFont(new Font("Arial", Font.BOLD, 20));

        prevButton = new JButton("<");
        nextButton = new JButton(">");

        prevButton.addActionListener(e -> changeMonth(-1));
        nextButton.addActionListener(e -> changeMonth(1));

        topPanel.add(prevButton, BorderLayout.WEST);
        topPanel.add(monthYearLabel, BorderLayout.CENTER);
        topPanel.add(nextButton, BorderLayout.EAST);

        frame.add(topPanel, BorderLayout.NORTH);

        // Calendar panel
        calendarPanel = new JPanel(new GridLayout(0, 7));
        frame.add(calendarPanel, BorderLayout.CENTER);

        updateCalendar();
        frame.setVisible(true);
    }

    private void updateCalendar() {
        // Clear the previous calendar
        calendarPanel.removeAll();

        // Set the month and year label
        monthYearLabel.setText(currentYearMonth.getMonth() + " " + currentYearMonth.getYear());

        // Add day names
        String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String dayName : dayNames) {
            JLabel dayLabel = new JLabel(dayName, SwingConstants.CENTER);
            calendarPanel.add(dayLabel);
        }

        // Get the first and last day of the month
        LocalDate firstDayOfMonth = currentYearMonth.atDay(1);
        int firstDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue() % 7;
        int daysInMonth = currentYearMonth.lengthOfMonth();

        // Add empty buttons for days before the first day of the month
        for (int i = 0; i < firstDayOfWeek; i++) {
            calendarPanel.add(new JLabel(""));
        }

        // Add buttons for each day of the month
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentYearMonth.atDay(day);
            JButton dayButton = new JButton(String.valueOf(day));
            dayButton.setToolTipText(date.toString());
            dayButton.addActionListener(e -> openNoteWindow(date, dayButton));

            // Highlight button if a note exists
            if (notesMap.containsKey(date)) {
                dayButton.setBackground(Color.YELLOW);
            }

            calendarPanel.add(dayButton);
        }

        // Refresh the panel
        calendarPanel.revalidate();
        calendarPanel.repaint();

        // Enable/disable navigation buttons based on the range
        prevButton.setEnabled(!currentYearMonth.equals(startYearMonth));
        nextButton.setEnabled(!currentYearMonth.equals(endYearMonth));
    }

    private void changeMonth(int offset) {
        currentYearMonth = currentYearMonth.plusMonths(offset);
        updateCalendar();
    }

    private void openNoteWindow(LocalDate date, JButton dayButton) {
        JFrame noteFrame = new JFrame("Note for " + date);
        noteFrame.setSize(300, 200);
        noteFrame.setLayout(new BorderLayout());

        JTextArea noteArea = new JTextArea();
        noteArea.setText(notesMap.getOrDefault(date, ""));
        JButton saveButton = new JButton("Save Note");

        saveButton.addActionListener(e -> {
            String note = noteArea.getText();
            notesMap.put(date, note);
            if (!note.isEmpty()) {
                dayButton.setBackground(Color.YELLOW);
            } else {
                dayButton.setBackground(null);
            }
            noteFrame.dispose();
        });

        noteFrame.add(new JScrollPane(noteArea), BorderLayout.CENTER);
        noteFrame.add(saveButton, BorderLayout.SOUTH);

        noteFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CalendarApp::new);
    }
}
