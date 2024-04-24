//yaman
package main;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
public class Admin extends User {

    // Default constructor
    public Admin() {
        super();
        setUserType(Admin);

    }

    // Constructor
    public Admin(int id, String name, String password, int userType) {
        super(id, name, password, 1);
    }

    @Override
    public void addSelftoDatabase(Connection connection) {
        // no need to add admin self to database
        return;
    }


    public boolean authenticateAdmin(Scanner scanner, Connection connection) throws SQLException {
        System.out.println("enter your first name");
        setFirstName(scanner.next());
        System.out.println("enter your last name");
        setLastName(scanner.next());
        System.out.println("enter your password");
        setPassword(scanner.next());
        if (authenticate(connection, "Admin")) {
            //populate member object
            populateAdmin(connection);
            return true;
        } else {
            return false;
        }
        // check if member exists
    }

    public void populateAdmin(Connection connection) throws SQLException {
        //no need to populate this class

    }
    private void showEquipment(){
        System.out.println("All Equipment: \n ");
        String sql = "SELECT * FROM Equipment";
        try (Connection connection = DbUtil.connect();) {
            Statement statement = connection.createStatement();
            statement.executeQuery(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                System.out.println("Equipment ID: " + resultSet.getInt("equipment_id"));
                System.out.println("Equipment Name: " + resultSet.getString("name"));
                System.out.println("Checkup Date: " + resultSet.getDate("check_update"));
                System.out.println("Equipment Availability: " + resultSet.getBoolean("is_available"));
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void maintenance(Scanner scanner){
        System.out.println("What would you like to do?");
        System.out.println("1. Update Availability \n2. Equipment Maintenance Updating\n3.Exit");
        int choice = scanner.nextInt();
        switch (choice){
            case 1:
                showEquipment();
                System.out.println("updating equipment availability");
                System.out.println("enter the equipment ID you would like to update");
                int equipmentID = scanner.nextInt();
                System.out.println("enter the new availability status (true/false)");
                boolean availability = scanner.nextBoolean();
                String sql = "UPDATE Equipment SET is_available = ? WHERE equipment_id = ?";
                try (Connection connection = DbUtil.connect();
                     PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setBoolean(1, availability);
                    pstmt.setInt(2, equipmentID);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 2:
                showEquipment();
                System.out.println("enter the equipment ID maintenance was performed on:");
                int equipmentID2 = scanner.nextInt();
                String sql2 = "UPDATE Equipment SET check_update = ?, is_available = true WHERE equipment_id = ?";
                try (Connection connection = DbUtil.connect();
                     PreparedStatement pstmt = connection.prepareStatement(sql2)) {
                    pstmt.setDate(1, Date.valueOf(LocalDate.now()));
                    pstmt.setInt(2, equipmentID2);
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 3:
                break;
        }


    }
    public void showSessions(){
        System.out.println("All Sessions: \n ");
        String sql = "SELECT * FROM Sessions";
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

                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void classScheduleUpdating(Scanner scanner, Connection conn) {
        //update session tables
        //update it in database
        showSessions();
        System.out.println("Enter the session ID you would like to update:");
        int sessionID = scanner.nextInt();
            System.out.println("What would you like to update?");
            System.out.println("\n1. start time\n2. end time\n3. date\n4. group\n5. room number\n6. edit equipment\n7. exit");
            int choice = scanner.nextInt();
            switch (choice){
                case 1:
                    System.out.println("Enter new start time");
                    Time startTime = Time.valueOf(scanner.next());
                    String sql3 = "UPDATE Sessions SET start_time = ? WHERE session_id = ?";
                    try (Connection connection = DbUtil.connect();
                         PreparedStatement pstmt = connection.prepareStatement(sql3)) {
                        pstmt.setTime(1, startTime);
                        pstmt.setInt(2, sessionID);
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    System.out.println("Enter new end time");
                    Time endTime = Time.valueOf(scanner.next());
                    String sql4 = "UPDATE Sessions SET end_time = ? WHERE session_id = ?";
                    try (Connection connection = DbUtil.connect();
                         PreparedStatement pstmt = connection.prepareStatement(sql4)) {
                        pstmt.setTime(1, endTime);
                        pstmt.setInt(2, sessionID);
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("Enter new date");
                    Date date = Date.valueOf(scanner.next());
                    String sql5 = "UPDATE Sessions SET date = ? WHERE session_id = ?";
                    try (Connection connection = DbUtil.connect();
                         PreparedStatement pstmt = connection.prepareStatement(sql5)) {
                        pstmt.setDate(1, date);
                        pstmt.setInt(2, sessionID);
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    System.out.println("Enter new group status");
                    boolean isGroup = scanner.nextBoolean();
                    String sql6 = "UPDATE Sessions SET is_group = ? WHERE session_id = ?";
                    try (Connection connection = DbUtil.connect();
                         PreparedStatement pstmt = connection.prepareStatement(sql6)) {
                        pstmt.setBoolean(1, isGroup);
                        pstmt.setInt(2, sessionID);
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 5:
                    System.out.println("Enter new room number");
                    int roomNumber = scanner.nextInt();
                    String sql7 = "UPDATE Sessions SET room_number = ? WHERE session_id = ?";
                    try (Connection connection = DbUtil.connect();
                         PreparedStatement pstmt = connection.prepareStatement(sql7)) {
                        pstmt.setInt(1, roomNumber);
                        pstmt.setInt(2, sessionID);
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 6:
                    System.out.println("Enter new equipment ID");
                    int equipmentID = scanner.nextInt();
                    String sql8 = "UPDATE Sessions SET equipment_id = ? WHERE session_id = ?";
                    try (Connection connection = DbUtil.connect();
                         PreparedStatement pstmt = connection.prepareStatement(sql8)) {
                        pstmt.setInt(1, equipmentID);
                        pstmt.setInt(2, sessionID);
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 7:
                    break;
            }
    }

    public void processPayment(Scanner scanner){
        // process payment
        System.out.println("processing payment for those who did not pay yet id's:");
        ArrayList<Integer> ids = new ArrayList<Integer>();
        // get payment status where it is false from memberatributes
        String sql = "SELECT * FROM MemberAttributes WHERE payment_status = false";
        try (Connection connection = DbUtil.connect();) {
            Statement statement = connection.createStatement();
            statement.executeQuery(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                System.out.println("Member ID: " + resultSet.getInt("id"));
                ids.add(resultSet.getInt("id"));
                System.out.println("their monthly payment is " + resultSet.getString("plan"));
                System.out.println("stealing their money.....");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        for (int i =0; i<ids.size(); i++){
            String sql2 = "UPDATE MemberAttributes SET payment_status = true WHERE id = "+ ids.get(i);
            try (Connection connection = DbUtil.connect();
                 PreparedStatement pstmt = connection.prepareStatement(sql2)) {
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public void initializeAdmin(Scanner scanner) {
        System.out.println("Enter your first name: ");
        setFirstName(scanner.next());
        System.out.println("Enter your last name: ");
        setLastName(scanner.next());
        System.out.println("Enter your password: ");
        setPassword(scanner.next());
        setUserType(1);
        setUserTypeString("Admin");
    }
}




