import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Scanner;

class Bank {
    private static final Scanner scanner = new Scanner(System.in);

    private static String SHA256(String Input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(Input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append(0);
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException _) {
        }
        return "";
    }

    public static void createAccount(Connection connection) throws SQLException {
        String firstName;
        String lastName;
        long accountNumber;
        String userID = "";
        long aadhaarNumber = 0;
        String panNumber = "";
        long mobileNumber = 0;
        String emailID = "";
        long pinCode = 0;
        String pinHash = "";
        String passwordHash = "";
        double yearlyIncome = 0;
        String address = "";
        String city = "";
        String state = "";
        String fatherName;
        Statement statement = connection.createStatement();
        ResultSet result;

        System.out.println("Tell Me Your First Name:");
        firstName = scanner.nextLine().trim();
        firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();
        System.out.println("Tell Me Your Last Name:");
        lastName = scanner.nextLine().trim();
        lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase();
        System.out.println("Tell Me Your Father Name:");
        fatherName = scanner.nextLine().trim();
        fatherName = fatherName.substring(0, 1).toUpperCase() + fatherName.substring(1).toLowerCase();
        boolean isAadhaarTrue = false;
        while (!isAadhaarTrue) {
            System.out.println("Tell Me Your Aadhaar Number:");
            aadhaarNumber = scanner.nextLong();
            scanner.nextLine();
            if ((aadhaarNumber + "").length() != 12 || aadhaarNumber == 0) {
                System.out.println("Wrong Aadhaar Given!\n Try Again!");
            } else {
                result = statement.executeQuery(String.format("SELECT AADHAAR_NUMBER FROM CUSTOMER WHERE AADHAAR_NUMBER = '%d'", aadhaarNumber));
                if (result.next()) {
                    if (result.getString("AADHAAR_NUMBER").equals(aadhaarNumber + "")) {
                        System.out.println("Account Exist With This Aadhaar Number!\nTry With Another Aadhaar Number!");
                    } else {
                        isAadhaarTrue = true;
                    }
                } else {
                    isAadhaarTrue = true;
                }

            }
        }
        boolean isPANTrue = false;
        while (!isPANTrue) {
            System.out.println("Tell Me Your PAN Number:");
            panNumber = scanner.nextLine().trim().toUpperCase();
            if (!panNumber.matches("[A-Z]{3}[PCHAFTGLJB][A-Z][0-9]{4}[A-Z]")) {
                System.out.println("Wrong PAN Given!\nTry Again!");
            } else {
                result = statement.executeQuery(String.format("SELECT PAN_NUMBER FROM CUSTOMER WHERE PAN_NUMBER = '%s'", panNumber));
                if (result.next()) {
                    if (result.getString("PAN_NUMBER").equals(panNumber)) {
                        System.out.println("Account Exist With This PAN Number!\nTry With Another PAN Number!");
                    } else {
                        isPANTrue = true;
                    }
                } else {
                    isPANTrue = true;
                }
            }
        }
        boolean isMobileTrue = false;
        while (!isMobileTrue) {
            System.out.println("Tell Me Your Mobile Number Without Country Code:");
            mobileNumber = scanner.nextLong();
            scanner.nextLine();
            if ((mobileNumber + "").length() != 10 || mobileNumber == 0) {
                System.out.println("Wrong Mobile Number Given!\n Try Again!");
            } else {
                result = statement.executeQuery(String.format("SELECT MOBILE_NUMBER FROM CUSTOMER WHERE MOBILE_NUMBER = '%d'", mobileNumber));
                if (result.next()) {
                    if (result.getString("MOBILE_NUMBER").equals(mobileNumber + "")) {
                        System.out.println("Account Exist With This Mobile Number!\nTry With Another Mobile Number!");
                    } else {
                        isMobileTrue = true;
                    }
                } else {
                    isMobileTrue = true;
                }
            }
        }
        boolean isEmailTrue = false;
        while (!isEmailTrue) {
            System.out.println("Tell Me Your Email ID:");
            emailID = scanner.nextLine().trim().toLowerCase();
            if (!emailID.contains("@") || !emailID.contains(".") || emailID.length() < 4) {
                System.out.println("Wrong Email ID Given!\nTry Again!");
            } else {
                isEmailTrue = true;
            }
        }
        boolean isUserIDTrue = false;
        while (!isUserIDTrue) {
            System.out.println("Tell me a User ID:(Minimum 10 Character)\nUser ID is Case Sensitive!");
            userID = scanner.nextLine().trim();
            if (userID.length() < 10 || userID.contains(" ")) {
                System.out.println("Wrong User ID Given!\nTry Again!");
            } else {
                result = statement.executeQuery(String.format("SELECT USER_ID FROM CUSTOMER WHERE USER_ID = '%s'", userID));
                if (result.next()) {
                    if (result.getString("USER_ID").equals(userID)) {
                        System.out.println("User ID Exist!\nUse Another User ID!");
                    } else {
                        isUserIDTrue = true;
                    }
                } else {
                    isUserIDTrue = true;
                }
            }
        }
        boolean isAddressTrue = false;
        while (!isAddressTrue) {
            System.out.println("Tell Me Your Address:");
            address = scanner.nextLine().trim();
            if (address.isEmpty()) {
                System.out.println("Wrong Address Given!");
            } else {
                isAddressTrue = true;
            }
        }
        isAddressTrue = false;
        while (!isAddressTrue) {
            System.out.println("Tell Me Your City:");
            city = scanner.nextLine().trim();
            if (city.isEmpty()) {
                System.out.println("Wrong City Given!");
            } else {
                isAddressTrue = true;
            }
        }
        isAddressTrue = false;
        while (!isAddressTrue) {
            System.out.println("Tell Me Your State:");
            state = scanner.nextLine().trim();
            if (state.isEmpty()) {
                System.out.println("Wrong State Given!");
            } else {
                isAddressTrue = true;
            }
        }
        isAddressTrue = false;
        while (!isAddressTrue) {
            System.out.println("Tell Me Your Postal Code:");
            pinCode = scanner.nextLong();
            scanner.nextLine();
            if ((pinCode + "").length() != 6) {
                System.out.println("Wrong Postal Code Given!");
            } else {
                isAddressTrue = true;
            }
        }
        boolean isIncomeTrue = false;
        while (!isIncomeTrue) {
            System.out.println("Tell Me Your Yearly Income:");
            yearlyIncome = scanner.nextDouble();
            scanner.nextLine();
            if (yearlyIncome < 0) {
                System.out.println("Wrong Income Given!\nTry Again!");
            } else {
                isIncomeTrue = true;
            }
        }
        boolean isPassTrue = false;
        while (!isPassTrue) {
            System.out.println("Create a Password for your Account:\nPassword is Case Sensitive!\nPassword Must Contains At least 1 Character [@, #, _, $, &]");
            passwordHash = scanner.nextLine();
            if (passwordHash.length() < 8 ||
                    !passwordHash.matches(".*[a-zA-Z].*") ||
                    !passwordHash.matches(".*[0-9].*") ||
                    !passwordHash.matches(".*[@#_$&].*")) {
                System.out.println("Password is weak!\nTry Again!");
            } else {
                passwordHash = SHA256(passwordHash);
                isPassTrue = true;
            }
        }
        isPassTrue = false;
        while (!isPassTrue) {
            System.out.println("Create A PIN For Your Account:\nPIN is 6 Digit Numeric Only!");
            pinHash = scanner.nextLine().trim();
            if (pinHash.length() != 6 || !pinHash.matches("[0-9]{6}")) {
                System.out.println("PIN is Wrong!\nTry Again!");
            } else {
                pinHash = SHA256(pinHash);
                isPassTrue = true;
            }
        }
        String insertCustomer = "INSERT INTO CUSTOMER (USER_ID, FIRST_NAME, LAST_NAME, FATHER_NAME, FATHER_LAST_NAME, AADHAAR_NUMBER, PAN_NUMBER, MOBILE_NUMBER, EMAIL_ID, ADDRESS, CITY, STATE, PINCODE, YEARLY_INCOME, CURRENT_BALANCE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";
        String insertLogin = "INSERT INTO LOGIN_INFO (USER_ID,ACCOUNT_NUMBER, PASSWORD_HASH, PIN_HASH) VALUES (?, ?, ?, ?)";

        try (PreparedStatement psCust = connection.prepareStatement(insertCustomer, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement psLog = connection.prepareStatement(insertLogin)) {

            psCust.setString(1, userID);
            psCust.setString(2, firstName);
            psCust.setString(3, lastName);
            psCust.setString(4, fatherName);
            psCust.setString(5, lastName);
            psCust.setString(6, aadhaarNumber + "");
            psCust.setString(7, panNumber);
            psCust.setString(8, mobileNumber + "");
            psCust.setString(9, emailID);
            psCust.setString(10, address);
            psCust.setString(11, city);
            psCust.setString(12, state);
            psCust.setString(13, pinCode + "");
            psCust.setDouble(14, yearlyIncome);

            psCust.executeUpdate();

            try (ResultSet generatedKeys = psCust.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    accountNumber = generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Creating User Failed, No ID Obtained.");
                }
            }

            psLog.setString(1, userID);
            psLog.setLong(2, accountNumber);
            psLog.setString(3, passwordHash);
            psLog.setString(4, pinHash);

            psLog.executeUpdate();

            connection.commit();

            System.out.println("Account Created Successfully!");
            System.out.println("Account Number: " + accountNumber + "\nUser ID: " + userID);
        } catch (SQLException e) {
            connection.rollback();
            System.out.println("Error Creating Account!");
        }
    }

    public static void transactor(Connection connection) throws SQLException {
        boolean isAccountTrue = false;
        PreparedStatement statement;
        ResultSet accountDetails;
        long accountNumber = 0;
        while (!isAccountTrue) {
            System.out.println("Tell Me Your Account Number:");
            accountNumber = scanner.nextLong();
            scanner.nextLine();
            if ((accountNumber + "").length() < 12) {
                System.out.println("Wrong Account Number Given!\nTry Again!");
            } else {
                statement = connection.prepareStatement("SELECT * FROM customer WHERE ACCOUNT_NUMBER = (?)");
                statement.setLong(1, accountNumber);
                accountDetails = statement.executeQuery();
                if (accountDetails.next()) {
                    isAccountTrue = true;
                } else {
                    System.out.println("Account Number Does Not Exist!\nTry Again!");
                }
            }
        }
        System.out.println("Account Number: " + accountNumber);
        System.out.println("What Do You Want To Do: (Deposit = 1, Withdraw = 2, Transfer = 3)");
        String whatToDo = scanner.nextLine().trim();
        double amount = 0;
        boolean isAmountTrue;
        switch (whatToDo) {
            case "1" -> {
                isAmountTrue = false;
                while (!isAmountTrue) {
                    System.out.println("Tell me the amount to deposit:");
                    amount = scanner.nextDouble();
                    scanner.nextLine();
                    if (amount > 0) {
                        isAmountTrue = true;
                    } else {
                        System.out.println("Wrong Amount Given!\nTry Again!");
                    }
                }
                statement = connection.prepareStatement("INSERT INTO TRANSACTION(ACCOUNT_NUMBER,AMOUNT,TRANSACTION_TYPE,DESCRIPTION) VALUES (?,?,'CREDIT','BY CASH')");
                statement.setLong(1, accountNumber);
                statement.setDouble(2, amount);
                statement.executeUpdate();
                connection.commit();
                System.out.println("Deposit Successful in Account Number: " + accountNumber + " of Amount: ₹" + amount + "/-");
                System.out.println("Thank You for using our Bank!\nHave a Nice Day!");
            }
            case "2" -> {
                boolean loginMethodTrue = false;
                String loginMethod = "";
                while (!loginMethodTrue) {
                    System.out.println("How You Want To Login: (Password = 1, PIN = 2)");
                    loginMethod = scanner.nextLine().trim();
                    if (loginMethod.equals("1") || loginMethod.equals("2")) {
                        loginMethodTrue = true;
                    } else {
                        System.out.println("Wrong Input Given!\nTry Again!");
                    }
                }
                String passPin;
                boolean isPassTrue;
                ResultSet resultSet;
                if (loginMethod.equals("1")) {
                    isPassTrue = false;
                    while (!isPassTrue) {
                        System.out.println("Tell Me Your password: \nPassword Is Case Sensitive!");
                        passPin = scanner.nextLine().trim();
                        if (passPin.length() < 8) {
                            System.out.println("Wrong Password Given!\nTry Again!");
                        } else {
                            passPin = SHA256(passPin);
                            statement = connection.prepareStatement("SELECT PASSWORD_HASH FROM LOGIN_INFO WHERE ACCOUNT_NUMBER = (?)");
                            statement.setLong(1, accountNumber);
                            resultSet = statement.executeQuery();
                            resultSet.next();
                            if (resultSet.getString("PASSWORD_HASH").equals(passPin)) {
                                isPassTrue = true;
                            } else {
                                System.out.println("Wrong Password Given!\nTry Again!");
                                return;
                            }
                        }
                    }
                } else {
                    isPassTrue = false;
                    while (!isPassTrue) {
                        System.out.println("Tell me your PIN: \nPIN is 6 Digit Numeric!");
                        passPin = scanner.nextLine().trim();
                        if (passPin.length() != 6) {
                            System.out.println("Wrong PIN Given!\nTry Again!");
                        } else {
                            passPin = SHA256(passPin);
                            statement = connection.prepareStatement("SELECT PIN_HASH FROM LOGIN_INFO WHERE ACCOUNT_NUMBER = (?)");
                            statement.setLong(1, accountNumber);
                            resultSet = statement.executeQuery();
                            resultSet.next();
                            if (resultSet.getString("PIN_HASH").equals(passPin)) {
                                isPassTrue = true;
                            } else {
                                System.out.println("Wrong PIN Given!\nTry Again!");
                                return;
                            }
                        }
                    }
                }
                statement = connection.prepareStatement("SELECT * FROM CUSTOMER WHERE ACCOUNT_NUMBER = (?)");
                statement.setLong(1, accountNumber);
                resultSet = statement.executeQuery();
                resultSet.next();
                double currentBalance = resultSet.getDouble("CURRENT_BALANCE");
                System.out.println("Your Current Balance: ₹" + currentBalance + "/-");
                if (currentBalance <= 0) {
                    System.out.println("Insufficient Balance!");
                    return;
                }
                System.out.println("Tell Me The Amount To Withdraw:");
                amount = scanner.nextDouble();
                scanner.nextLine();
                if (amount > currentBalance || amount < 0) {
                    System.out.println("Insufficient Balance!");
                } else {
                    statement = connection.prepareStatement("INSERT INTO TRANSACTION(ACCOUNT_NUMBER,AMOUNT,TRANSACTION_TYPE,DESCRIPTION) VALUES (?,?,'DEBIT','BY CASH')");
                    statement.setLong(1, accountNumber);
                    statement.setDouble(2, amount);
                    statement.executeUpdate();
                    connection.commit();
                    System.out.println("Debit of Amount ₹" + amount + "/- is Successful!");
                    System.out.println("Thank You For Using Our Bank!\nHave a Nice Day!");
                }
            }
            case "3" -> {
                boolean loginMethodTrue = false;
                String loginMethod = "";
                while (!loginMethodTrue) {
                    System.out.println("How you want to login: (Password = 1, PIN = 2)");
                    loginMethod = scanner.nextLine().trim();
                    if (loginMethod.equals("1") || loginMethod.equals("2")) {
                        loginMethodTrue = true;
                    } else {
                        System.out.println("Wrong Input Given!\nTry Again!");
                    }
                }
                String passPin;
                boolean isPassTrue;
                ResultSet resultSet;
                if (loginMethod.equals("1")) {
                    isPassTrue = false;
                    while (!isPassTrue) {
                        System.out.println("Tell Me Your Password: \nPassword is Case Sensitive!");
                        passPin = scanner.nextLine().trim();
                        if (passPin.length() < 8) {
                            System.out.println("Wrong Password Given!\nTry Again!");
                        } else {
                            passPin = SHA256(passPin);
                            statement = connection.prepareStatement("SELECT PASSWORD_HASH FROM LOGIN_INFO WHERE ACCOUNT_NUMBER = (?)");
                            statement.setLong(1, accountNumber);
                            resultSet = statement.executeQuery();
                            resultSet.next();
                            if (resultSet.getString("PASSWORD_HASH").equals(passPin)) {
                                isPassTrue = true;
                            } else {
                                System.out.println("Wrong Password Given!\nTry Again!");
                                return;
                            }
                        }
                    }
                } else {
                    isPassTrue = false;
                    while (!isPassTrue) {
                        System.out.println("Tell me your PIN: \nPIN is 6 Digit Numeric!");
                        passPin = scanner.nextLine().trim();
                        if (passPin.length() != 6) {
                            System.out.println("Wrong PIN Given!\nTry Again!");
                        } else {
                            passPin = SHA256(passPin);
                            statement = connection.prepareStatement("SELECT PIN_HASH FROM LOGIN_INFO WHERE ACCOUNT_NUMBER = (?)");
                            statement.setLong(1, accountNumber);
                            resultSet = statement.executeQuery();
                            resultSet.next();
                            if (resultSet.getString("PIN_HASH").equals(passPin)) {
                                isPassTrue = true;
                            } else {
                                System.out.println("Wrong PIN Given!\nTry Again!");
                                return;
                            }
                        }
                    }
                }
                statement = connection.prepareStatement("SELECT * FROM CUSTOMER WHERE ACCOUNT_NUMBER = (?)");
                statement.setLong(1, accountNumber);
                resultSet = statement.executeQuery();
                resultSet.next();
                double currentBalance = resultSet.getDouble("CURRENT_BALANCE");
                System.out.println("Your Current Balance: ₹" + currentBalance + "/-");
                if (currentBalance <= 0) {
                    System.out.println("Insufficient Balance!");
                    return;
                }
                isAccountTrue = false;
                long receiverAccount = 0;
                while (!isAccountTrue) {
                    System.out.println("Tell Me The Account Number of Receiver: ");
                    receiverAccount = scanner.nextLong();
                    scanner.nextLine();
                    if ((receiverAccount + "").length() < 12) {
                        System.out.println("Wrong Account Given!\nTry Again!");
                    } else if (receiverAccount == accountNumber) {
                        System.out.println("You Cannot Transfer To Own Account!\nTry Again!");
                    } else {
                        statement = connection.prepareStatement("SELECT * FROM CUSTOMER WHERE ACCOUNT_NUMBER = (?)");
                        statement.setLong(1, receiverAccount);
                        resultSet = statement.executeQuery();
                        if (resultSet.next()) {
                            System.out.println("Do You Want To Transfer Money To " + resultSet.getString("FIRST_NAME") + " " + resultSet.getString("LAST_NAME") + ": (Yes = 1, No = 2)");
                            String confirmTransfer = scanner.nextLine().trim();
                            if (!confirmTransfer.equals("1")) {
                                System.out.println("Transaction Cancelled!");
                                return;
                            }
                            isAccountTrue = true;
                        } else {
                            System.out.println("Account Does Not Exist!\nTry Again!");
                        }
                    }
                }
                System.out.println("Tell Me The Amount To Transfer:");
                amount = scanner.nextDouble();
                scanner.nextLine();
                if (amount > currentBalance || amount < 0) {
                    System.out.println("Insufficient Balance!");
                } else {
                    statement = connection.prepareStatement("INSERT INTO TRANSACTION(ACCOUNT_NUMBER,AMOUNT,DESCRIPTION,TRANSACTION_TYPE) VALUES (?,?,?,'DEBIT')");
                    statement.setLong(1, accountNumber);
                    statement.setDouble(2, amount);
                    statement.setString(3, "TRANSFER TO " + receiverAccount);
                    statement.executeUpdate();
                    statement = connection.prepareStatement("INSERT INTO TRANSACTION(ACCOUNT_NUMBER,AMOUNT,DESCRIPTION,TRANSACTION_TYPE) VALUES (?,?,?,'CREDIT')");
                    statement.setLong(1, receiverAccount);
                    statement.setDouble(2, amount);
                    statement.setString(3, "RECEIVED FROM " + accountNumber);
                    statement.executeUpdate();
                    connection.commit();
                    System.out.println("Transfer of Amount ₹" + amount + "/- to Account Number: " + receiverAccount + " is Successful!");
                    System.out.println("Thank You For Using Our Bank!\nHave a Nice Day!");

                }
            }
            default -> System.out.println("Wrong Input Given!");
        }
    }

    public static void printPassbook(Connection connection) throws SQLException {
        boolean isAccountTrue = false;
        long accountNumber = 0;
        ResultSet accountDetails = null;
        PreparedStatement preparedStatement;
        while (!isAccountTrue) {
            System.out.println("Tell Me Your Account Number:");
            accountNumber = scanner.nextLong();
            scanner.nextLine();
            if ((accountNumber + "").length() < 12) {
                System.out.println("Wrong Account Number!\nTry Again!");
            } else {
                preparedStatement = connection.prepareStatement("SELECT * FROM CUSTOMER WHERE ACCOUNT_NUMBER = (?)");
                preparedStatement.setLong(1, accountNumber);
                accountDetails = preparedStatement.executeQuery();
                if (accountDetails.next()) {
                    isAccountTrue = true;
                } else {
                    System.out.println("Account Does Not Exist!\nTry Again!");
                }
            }
        }
        preparedStatement = connection.prepareStatement("SELECT * FROM TRANSACTION WHERE ACCOUNT_NUMBER = (?)");
        preparedStatement.setLong(1, accountNumber);
        ResultSet transactions = preparedStatement.executeQuery();
        String accountInfo = String.format("""
                        Account Number: %d\s
                        User ID: %s\s
                        First Name: %s\s
                        Last Name: %s\s
                        Father Name: %s %s
                        Aadhaar Number: %s\s
                        PAN Number: %s\s
                        Mobile Number: %s\s
                        Email ID: %s\s
                        Address: %s\s
                        City: %s\s
                        State: %s\s
                        Postal Code: %s\s
                        Yearly Income: %f\s
                        Current Balance: %f\s
                        Account Creation Date: %s
                        """,
                accountDetails.getLong("ACCOUNT_NUMBER"), accountDetails.getString("USER_ID"), accountDetails.getString("FIRST_NAME"),
                accountDetails.getString("LAST_NAME"), accountDetails.getString("FATHER_NAME"), accountDetails.getString("FATHER_LAST_NAME"),
                accountDetails.getString("AADHAAR_NUMBER"), accountDetails.getString("PAN_NUMBER"), accountDetails.getString("MOBILE_NUMBER"),
                accountDetails.getString("EMAIL_ID"), accountDetails.getString("ADDRESS"), accountDetails.getString("CITY"),
                accountDetails.getString("STATE"), accountDetails.getString("PINCODE"), accountDetails.getDouble("YEARLY_INCOME"),
                accountDetails.getDouble("CURRENT_BALANCE"), accountDetails.getString("ACCOUNT_CREATION_DATE"));
        System.out.println(accountInfo);
        System.out.println("Transactions: ");
        while (transactions.next()) {
            System.out.printf("%d              %s              %s    %f        %s\n", transactions.getLong("TRANSACTION_ID"), transactions.getString("DATE_TIME"),
                    transactions.getString("TRANSACTION_TYPE"), transactions.getDouble("AMOUNT"), transactions.getString("DESCRIPTION"));
        }
    }
}

public class Main {
    void main() {
        try {
            String url = "jdbc:mysql://10.32.109.237:9203/bank";
            String user = "mridulsaha";
            String pass = "R@nd0m231";
            Connection con = DriverManager.getConnection(url, user, pass);
            con.setAutoCommit(false);
            System.out.println("Server Connected...");
            System.out.println("Hello!\nWelcome to Mridul's Bank");
            System.out.println(
                    "How I can help you today!\nFor Opening New Account! Type: 1\nFor Printing Account Details! Type: 2\nFor Credit, Withdraw or Transfer! Type: 3");
            String Input = new Scanner(System.in).next().trim().toLowerCase();
            if (Input.contains("1")) {
                System.out.println("To create account:");
                Bank.createAccount(con);
            } else if (Input.contains("2")) {
                System.out.println("To print account details:");
                Bank.printPassbook(con);
            } else if (Input.contains("3")) {
                System.out.println("To Transact:");
                Bank.transactor(con);
            } else {
                System.out.println("Wrong Input!\nTry Again!");
            }
            con.close();
        } catch (SQLException _) {
            System.out.println("Unable to Connect Server...");
        }
    }
}
