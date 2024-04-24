//yaman
package main;

import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;


public class Member extends User {
    private int goalWeight;
    private double height;
    private double weight;
    private double bf;
    private String goalWorkout;
    private boolean paymentStatus;
    private String routine;
    private int plan;
    private LocalDate timeLine;

    // Default constructor
    public Member() {
        super();
        setUserType(3);
    }

    // Constructor
    public Member(int id, String name, String password, int userType, int goalWeight, String goalWorkout) {
        super(id, name, password, 3);
        this.goalWeight = goalWeight;
        this.goalWorkout = goalWorkout;
    }

    // Getters and Setters for additional attributes
    public int getGoalWeight() {
        return goalWeight;
    }

    public void setGoalWeight(int goalWeight) {
        this.goalWeight = goalWeight;
    }

    public String getGoalWorkout() {
        return goalWorkout;
    }

    public void setGoalWorkout(String goalWorkout) {
        this.goalWorkout = goalWorkout;
    }
    public void initializeMember(Scanner scanner){
        // Member member = new Member();
        setUserType(3);
        setUserTypeString("Member");
        System.out.println("enter your first name");
        setFirstName(scanner.next());
        System.out.println("enter your last name");
        setLastName(scanner.next());
        System.out.println("enter your password");
        setPassword(scanner.next());
        //Goal_weight, goal_workout, height,weight, bf%, routineENUM, paymentstatus, plan
        System.out.println("enter your goal weight");
        goalWeight = scanner.nextInt();
        System.out.println("enter your timeline; format; \"2021-01-23\"");
        String date = scanner.next();
        timeLine = LocalDate.parse(date);
        System.out.println("enter your goal workout");
        goalWorkout = scanner.next();
        //clean the scanner
        scanner.nextLine();
        System.out.println("enter your height");
        height = scanner.nextDouble();
        System.out.println("enter your weight");
        weight = scanner.nextDouble();
        System.out.println("enter your body fat percentage");
        bf = scanner.nextInt();
        System.out.println("enter your routine: 'upper', 'lower', 'cardio', 'full body', 'rest'");
        routine = scanner.next();
        //calculate payment plan by goal workout
        paymentStatus = false;

        //payment
        //write member to database

    }
    public boolean authenticateMember(Scanner scanner, Connection connection) throws SQLException {
        System.out.println("enter your first name");
        setFirstName(scanner.next());
        System.out.println("enter your last name");
        setLastName(scanner.next());
        System.out.println("enter your password");
        setPassword(scanner.next());
        if (authenticate(connection, "Member")) {
            //populate member object
            populateMember(connection);
            System.out.println("user is a member for reallzy");
            return true;
        }else{
            return false;
        }
//        if(authenticate(connection)){
//            //populate member object
//            populateMember(connection);
//            return true;
//        }else{
//            return false;
//        }
        // check if member exists
    }

    public void populateMember(Connection connection) throws SQLException {
        //populate member object
        System.out.println("populating member object");
        String sql = "SELECT * FROM MemberAttributes WHERE id = "+getId();
        Statement statement = connection.createStatement();
        statement.executeQuery(sql);
        ResultSet resultSet = statement.getResultSet();
        resultSet.next();
        goalWeight = resultSet.getInt("goal_weight");
        timeLine = resultSet.getDate("timeline").toLocalDate();
        goalWorkout = resultSet.getString("goal_workout");
        height = resultSet.getDouble("height");
        weight = resultSet.getDouble("weight");
        bf = resultSet.getDouble("bf_percentage");
        routine = resultSet.getString("routine");
        paymentStatus = resultSet.getBoolean("payment_status");
        //paymentStatus = Boolean.parseBoolean(resultSet.getString("payment_status"));
        plan = Integer.parseInt(resultSet.getString("plan"));


    }
    public void addSelftoDatabase(Connection connection){
        System.out.println("adding self to database");
        //write member to database
        String sql = "INSERT INTO MemberAttributes (id, goal_weight, timeline, goal_workout, height, weight, bf_percentage, routine, payment_status, plan) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // Set parameters for the prepared statement
            pstmt.setInt(1, getId());
            pstmt.setDouble(2, goalWeight);
            pstmt.setObject(3, timeLine);
            pstmt.setString(4, goalWorkout);
            pstmt.setDouble(5, height);
            pstmt.setDouble(6, weight);
            pstmt.setDouble(7, bf);
            pstmt.setString(8, routine);
            pstmt.setBoolean(9, paymentStatus);

            pstmt.setString(10, Integer.toString(plan));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void setPlan(){
        if (goalWorkout.equals("weight loss")) {
            plan = 1;
        } else if (goalWorkout.equals("muscle gain")) {
            plan = 2;
        } else if (goalWorkout.equals("maintain")) {
            plan = 3;
        }
    }
    public void printInformation(){
        System.out.println("--------------------------------------------------");
        System.out.println("Information for Member ID: " + getId());
        System.out.println("--------------------------------------------------");
        System.out.println(String.format("Name: %-20s", getFirstName() + " " + getLastName()));
        System.out.println(String.format("Goal Weight: %-15s", goalWeight + " lbs"));
        System.out.println(String.format("Goal Workout: %-13s", goalWorkout));
        System.out.println(String.format("Height: %-18s", height + " inches"));
        System.out.println(String.format("Weight: %-17s", weight + " lbs"));
        System.out.println(String.format("Body Fat Percentage: %-5s", bf + "%"));
        System.out.println(String.format("Routine: %-17s", routine));
        System.out.println(String.format("Payment Status: %-10s", paymentStatus));
        System.out.println(String.format("Plan: %-20s", plan));
        System.out.println(String.format("Timeline: %-16s", timeLine));
        System.out.println("--------------------------------------------------");



    }
    public void updateInformation(Scanner scanner){
        System.out.println("What would you like to update?");
        System.out.println("1. Goal Weight\n2. Goal Workout\n3. Height\n4. Weight\n5. Body Fat Percentage\n6. Routine\n7. Add Achievement \n8. Timeline");
        int choice = scanner.nextInt();
        switch (choice){
            case 1:
                System.out.println("Enter new goal weight");
                goalWeight = scanner.nextInt();
                break;
            case 2:
                System.out.println("Enter new goal workout");
                goalWorkout = scanner.next();
                break;
            case 3:
                System.out.println("Enter new height");
                height = scanner.nextDouble();
                break;
            case 4:
                System.out.println("Enter new weight");
                weight = scanner.nextDouble();
                break;
            case 5:
                System.out.println("Enter new body fat percentage");
                bf = scanner.nextDouble();
                break;
            case 6:
                System.out.println("Enter new routine");
                routine = scanner.next();
                break;
            case 7:
                System.out.println("Enter new achievement:");
                scanner.nextLine();
                String achievement = scanner.nextLine();                //add achievement to database
                String sql = "INSERT INTO Achievements (id, achievements_user) VALUES (?, ?)";
                try (Connection connection = DbUtil.connect();
                     PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setInt(1, getId());
                    pstmt.setString(2, achievement);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 8:
                System.out.println("Enter new timeline");
                String date = scanner.next();
                timeLine = LocalDate.parse(date);
                break;
        }
        sendUpdates();
    }
    private void sendUpdates(){
        //send updates to database

        System.out.println("updating self to database");
        //write member to database
        String sql = "UPDATE MemberAttributes SET goal_weight = ?, timeline = ?, goal_workout = ?, height = ?, weight = ?, bf_percentage = ?, routine = ?, payment_status = ?, plan = ? WHERE id = "+ getId();
        try (Connection connection = DbUtil.connect();
                PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // Set parameters for the prepared statement
            pstmt.setDouble(1, goalWeight);
            pstmt.setObject(2, timeLine);
            pstmt.setString(3, goalWorkout);
            pstmt.setDouble(4, height);
            pstmt.setDouble(5, weight);
            pstmt.setDouble(6, bf);
            pstmt.setString(7, routine);
            pstmt.setBoolean(8, (paymentStatus));
            pstmt.setString(9, Integer.toString(plan));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public void showDashboard(){
        System.out.println("--------------------------------------------------");
        System.out.println("             Welcome to your dashboard            ");
        System.out.println("--------------------------------------------------");
        System.out.printf("Exercise Routines: %s%n", routine);
        System.out.println("Health Stats:");
        System.out.printf(" - Weight: %.2f lbs%n", weight);
        System.out.printf(" - Height: %.2f inches%n", height);
        System.out.printf(" - Body Fat Percentage: %.2f%%%n", bf);
        System.out.println("--------------------------------------------------");
        System.out.println("Fitness Achievements:");

        // display fitness achievements (join member attributes with achievements
        String sql = "select achievements_user from achievements where id = "+ getId();
        try (Connection connection = DbUtil.connect();
             Statement statement = connection.createStatement()) {
            statement.executeQuery(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()){
                System.out.println(resultSet.getString("achievements_user"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("--------------------------------------------------");

    }
    public void manageSchedule(Scanner scanner){
        System.out.println("What would you like to do?");
        System.out.println("1. View Schedule\n2. Update Schedule (register/unregister for class)\n3. Exit");
        int choice = scanner.nextInt();
        switch (choice){
            case 1:
                // to show personal registered sessions
                viewSchedule();
                break;
            case 2:
               // updateSchedule(scanner);
                updateSchedule(scanner);
                break;
            case 3:
                break;
        }
    }
    public void viewSchedule(){
        // view schedule
        // get from table - sessions user is registered to (member_id)  = id
        if (numSession() == 0){
            System.out.println("You are not registered for any sessions");
            return;
        }
        System.out.println("Sessions you are registered for: \n ");
        String sql = "SELECT * FROM Sessions WHERE member_id = "+getId();
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
    public void updateSchedule(Scanner scanner){
        /*
            how to do this:
            get from table sessions: start time, end time, trainer id, and check that member id is empty, and boolean
            split if bool then here
            group:
            or if false then here
            let them pick session id(check if has members or no)
            then update the session table with member id
                 */
        System.out.println("Would you like to register or unregister for a session? 1. Register 2. Unregister");
        int choice = scanner.nextInt();
        switch (choice){
            case 1:
                // register
                System.out.println("Are you registering for a group session or individual session? 1. Group 2. Individual");
                int group = scanner.nextInt();
                switch (group){
                    case 1:
                        // group sessions
                        // show available group sessions
                        showSessionGroups(true);
                        // let them pick session id
                        registerForGroupSession(scanner); //newnewnewnew
                        //registerForSession(scanner);
                        // update session table with member id
                        break;
                    case 2:
                        // individual
                        // show available sessions
                        showSessionGroups(false);
                        // let them pick session id
                        // register for group session:
                        registerForSession(scanner);
                        // update session table with member id
                        break;
                }
                break;
            case 2:
                // unregister
                System.out.println("Unregistering sessions");
                // show registered sessions
                if (numSession() == 0){
                    System.out.println("You are not registered for any sessions");

                }else {
                    viewSchedule();
                    // let them pick session id
                    // update session table with member id =0
                    unRegisterSessions(scanner);
                    break;
                }
        }

        // if register
            // group or individual
                // show available sessions (with id =0) and bool if group
        // if unregister
            // show registered sessions if any
            // update session table with member id =0
            // or else
        // say no registerd sessions and return

    }
    private int numSession(){
        // get number of sessions
        // get from table sessions user is registered to (member_id)  = id
        String sql = "SELECT COUNT(*) FROM Sessions WHERE member_id = "+getId();
        try (Connection connection = DbUtil.connect();
             Statement statement = connection.createStatement()) {
            statement.executeQuery(sql);
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
    public void showSessionGroups(boolean group){
        // show available sessions
        // get from table sessions: start time, end time, trainer id, and check that member id is empty, and boolean
        // split if bool then here
        // group:
        // or if false then here
        System.out.println("Showing sessions for group: \n " + group);
        String sql;
        if (group){
             sql = "SELECT * FROM Sessions WHERE member_id is null and is_group is not null";
        }else{
             sql = "SELECT * FROM Sessions WHERE member_id is null AND is_group is null";
        }

        try (Connection connection = DbUtil.connect();) {
            Statement statement = connection.createStatement();
            statement.executeQuery(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                System.out.println("Session ID: " + resultSet.getInt("session_id"));
                System.out.println("Trainer ID: " + resultSet.getInt("trainer_id"));
                //System.out.println("Member ID: " + resultSet.getInt("member_id"));
                System.out.println("Start Time: " + resultSet.getTime("start_time"));
                //System.out.println("Group Session?: " + resultSet.getBoolean("is_group"));
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
    public void registerForSession(Scanner scanner){
        System.out.println("Would you like to register for a session? 1. Yes 2. No");
        int choice = scanner.nextInt();
        if (choice == 2) {
            return;
        }
        System.out.println("Enter the # for session id you want to register for");
        int sessionID = scanner.nextInt();
        // update session table with member id
        String sql = "UPDATE Sessions SET member_id = ? WHERE session_id = "+ sessionID;
        try (Connection connection = DbUtil.connect();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void registerForGroupSession(Scanner scanner){
        System.out.println("Would you like to register for a group session? 1. Yes 2. No");
        int choice = scanner.nextInt();
        if (choice == 2) {
            return;
        }
        System.out.println("Enter the # for session id you want to register for");
        // get the session information
        // make new tuple with same information but with new member id
        int sessionID = scanner.nextInt();
        String sql = "SELECT * FROM Sessions where is_group is not null";
        try (Connection connection = DbUtil.connect();
             Statement statement = connection.createStatement()) {
            statement.executeQuery(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {

                int trainerid = resultSet.getInt("trainer_id");
                int memberid = resultSet.getInt("member_id");
                Time start_time = resultSet.getTime("start_time");
                Time end_time = resultSet.getTime("end_time");
                Date date = resultSet.getDate("date");
                int room_number = resultSet.getInt("room_number");
                boolean is_group = resultSet.getBoolean("is_group");
                // insert new tuple
                String insert = "INSERT into Sessions (member_id, trainer_id, start_time, end_time, date, room_number, is_group) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(insert)) {
                    pstmt.setInt(1, getId());
                    pstmt.setInt(2, trainerid);
                    pstmt.setTime(3, start_time);
                    pstmt.setTime(4, end_time);
                    pstmt.setDate(5, date);
                    pstmt.setInt(6, room_number);
                    pstmt.setBoolean(7, is_group);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }



    }
    private void unRegisterSessions(Scanner scanner){
        System.out.println("Would you like to unregister for a session? 1. Yes 2. No");
        int choice = scanner.nextInt();
        if (choice == 2) {
            return;
        }
        System.out.println("enter the # for session id you want to unregister from");
        int sessionID = scanner.nextInt();
        // update session table with member id to null
        String sql = "UPDATE Sessions SET member_id = null WHERE session_id = ?" ;
        try (Connection connection = DbUtil.connect();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, sessionID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public String toString(){
        return "Member: " + getName() + " with goal weight: " + goalWeight + " and goal workout: " + goalWorkout;
    }
}
