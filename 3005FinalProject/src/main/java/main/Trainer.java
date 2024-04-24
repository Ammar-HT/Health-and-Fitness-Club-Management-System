//yaman
package main;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalTime;

public class Trainer extends User {
    private String schedule;
    private List<Member> members; // A list of Member objects this trainer is associated with

    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate startDate;

    private LocalDate endDate;

    // Default constructor
    public Trainer() {
        super();
        setUserType(Trainer);
        this.members = new ArrayList<>(); // Initializes the list to avoid null pointer exceptions
    }

    // Constructor
    public Trainer(int id, String name, String password, int userType, String schedule, List<Member> members) {
        super(id, name, password, 2);
        this.schedule = schedule;
        this.members = members;
    }

    // Getters and Setters
    public void addSelftoDatabase(Connection conn){
        System.out.println("adding self to database");
        //write trainer to database
        String sql = "INSERT INTO TrainerAvailability (trainer_id, start_time, end_time, start_date, end_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters for the prepared statement
            pstmt.setInt(1, getId());
            pstmt.setObject(2, startTime);
            pstmt.setObject(3,endTime);
            pstmt.setObject(4, startDate);
            pstmt.setObject(5, endDate);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public void setStartTime(LocalTime startTime){ this.startTime = startTime; }

    public void setEndTime(LocalTime endTime){ this.endTime = endTime; }

    public void setStartDate(LocalDate startDate){ this.startDate = startDate; }

    public void setEndDate(LocalDate endDate){ this.endDate = endDate; }

    public LocalTime getStartTime() {
        return this.startTime;
    }

    public LocalTime getEndTime() {

        return this.endTime;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }


    public void viewMember(String name){
        for (int i = 0; i < members.size(); ++i){
            if (members.get(i).getName().equals(name)){
                members.get(i).printInformation();
            }
        }
        System.out.println("Member not found in trainer list!");
    }
public void viewMembers(){
        //get sessions from db where trainer_id = this.id
        //get members from sessions that have trainer_id = this.id
        //print members
        System.out.println("Members: \n ");
        String sql = "SELECT * FROM Sessions WHERE trainer_id = "+getId();
        HashSet<Integer> memberIds2 = new HashSet<>();
        try (Connection connection = DbUtil.connect();) {
            Statement statement = connection.createStatement();
            statement.executeQuery(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                memberIds2.add(resultSet.getInt("member_id"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        // use memberIds to get member names

        List<Integer> memberIds = new ArrayList<>(memberIds2);
        for (int i = 0; i < memberIds.size(); ++i){
            String sql2 = "SELECT * FROM users JOIN memberattributes ON users.id = memberattributes.id WHERE users.id = "+memberIds.get(i);
            try (Connection connection = DbUtil.connect();) {
                Statement statement = connection.createStatement();
                statement.executeQuery(sql2);
                ResultSet resultSet = statement.getResultSet();
                resultSet.next();
                System.out.println("Member ID: " + resultSet.getInt("id"));
                System.out.println("First Name: " + resultSet.getString("first_name"));
                System.out.println("Last Name: " + resultSet.getString("last_name"));
                System.out.println("Goal Weight: " + resultSet.getDouble("goal_weight"));
                System.out.println("Timeline: " + resultSet.getString("timeline"));
                System.out.println("Goal Workout: " + resultSet.getString("goal_workout"));
                System.out.println("Height: " + resultSet.getDouble("height"));
                System.out.println("Weight: " + resultSet.getDouble("weight"));
                System.out.println("Body Fat Percentage: " + resultSet.getDouble("bf_percentage"));
                System.out.println("Routine: " + resultSet.getString("routine"));
                System.out.println();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }


}
    public void initializeTrainer(Scanner scanner){

        setUserType(Trainer);
        setUserTypeString("Trainer");
        System.out.println("enter your first name");
        setFirstName(scanner.next());
        System.out.println("enter your last name");
        setLastName(scanner.next());
        System.out.println("enter your password");
        setPassword(scanner.next());
        System.out.println("enter your start time (format: hh:mm:ss):");
        String time = scanner.next();
        this.startTime = LocalTime.parse(time);
        System.out.println("enter your end time (format: hh:mm:ss): ");
        String time2 = scanner.next();
        this.endTime = LocalTime.parse(time2);
        System.out.println("enter your start date (format: yyyy-mm-dd): ");
        String Date = scanner.next();
        this.startDate = LocalDate.parse(Date);
        System.out.println("enter your end date (format: yyyy-mm-dd): ");
        String date2 = scanner.next();
        setEndDate(LocalDate.parse(date2));

    }

    public boolean authenticateTrainer(Scanner scanner, Connection connection) throws SQLException {
        System.out.println("enter your first name");
        setFirstName(scanner.next());
        System.out.println("enter your last name");
        setLastName(scanner.next());
        System.out.println("enter your password");
        setPassword(scanner.next());
        if(authenticate(connection,"Trainer")){
            //populate member object
            populateTrainer(connection);
            return true;
        }else{
            return false;
        }
        // check if member exists
    }

    public void populateTrainer(Connection connection) throws SQLException {
        //populate member object
        System.out.println("Populating trainer object\n");
        String sql = "SELECT * FROM TrainerAvailability WHERE trainer_id = "+getId();
        Statement statement = connection.createStatement();
        statement.executeQuery(sql);
        ResultSet resultSet = statement.getResultSet();
        resultSet.next();
        startTime = resultSet.getTime("start_time").toLocalTime();
        endTime = resultSet.getTime("end_time").toLocalTime();
        startDate = resultSet.getDate("start_date").toLocalDate();
        endDate = resultSet.getDate("end_date").toLocalDate();

    }

    public void showSessions(){
        System.out.println("sessions: \n ");
        String sql = "SELECT * FROM Sessions WHERE trainer_id = "+getId();
        try (Connection connection = DbUtil.connect();) {
            Statement statement = connection.createStatement();
            statement.executeQuery(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                System.out.println("Session ID: " + resultSet.getInt("session_id"));
                System.out.println("Trainer ID: " + resultSet.getInt("trainer_id"));
                System.out.println("Member ID: " + resultSet.getInt("member_id"));
                System.out.println("Start Time: " + resultSet.getTime("start_time"));
                System.out.println("Group Session?: " + resultSet.getBoolean("is_group"));
                System.out.println("End Time: " + resultSet.getTime("end_time"));
                System.out.println("Date: " + resultSet.getDate("date"));
                System.out.println("Room Number: " + resultSet.getInt("room_number"));
                //System.out.println(": " + resultSet.getDate("end_date"));
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    public void printInformation(){
        // init trainer object

        System.out.println("Trainer Information: \n ");
        System.out.println("Trainer ID: " + getId());
        System.out.println("First Name: " + getFirstName());
        System.out.println("Last Name: " + getLastName());
        System.out.println("Start Time: " + startTime.toString());
        System.out.println("End Time: " + endTime);
        System.out.println("Start Date: " + startDate.toString());
        System.out.println("End Date: " + endDate);
        System.out.println();

    }

    public void addSession(Scanner scanner){
        
        System.out.println("adding session to database");
        System.out.println("enter your start time (format: hh:mm:ss):");
        Time startTimeOfSession = Time.valueOf(scanner.next());
        System.out.println("enter your end time (format: hh:mm:ss): ");
        Time endTimeOfSession = Time.valueOf(scanner.next());
        System.out.println("enter your date (format: yyyy-mm-dd): ");
        Date dateOfSession = Date.valueOf(scanner.next());
        System.out.println("enter your room number: ");
        int roomNumber = scanner.nextInt();
        System.out.println("is this a group session? (true/false): ");
        boolean isGroup = scanner.nextBoolean();
        System.out.println("enter the equipment id: 1. Treadmill 2. Dumbbells 3. Barbell 4. Kettlebell");
        int equipmentId = scanner.nextInt();


        String sql = "INSERT INTO Sessions (trainer_id, start_time, end_time, date, room_number, is_group, equipment_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DbUtil.connect();) {
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, getId());
                pstmt.setTime(2, startTimeOfSession);
                pstmt.setTime(3, endTimeOfSession);
                pstmt.setDate(4, dateOfSession);
                pstmt.setInt(5, roomNumber);
                pstmt.setBoolean(6, isGroup);
                pstmt.setInt(7, equipmentId);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}


