import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.*;

import javax.lang.model.util.ElementScanner14;

public class OnlineVoting {
    public static void main(String[] args) {
        try {
            Sign_Login();
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public static void Sign_Login() {
        Scanner input = new Scanner(System.in);

        try {
            System.out.println("Enter 1 or 2: ");
            System.out.println("1. Register for Vote\n2. Login");
            int choice = input.nextInt();
    
            if (choice == 1) {
                signup();
            } else if (choice == 2) {
                login();
            } else {
                System.out.println("Invalid Choice");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            input.close();
        }
    }

    public static void signup() {
        Scanner input = new Scanner(System.in);

        try {
            System.out.println("Enter Aadhar Number: ");
            String username = input.nextLine();
    
            System.out.println("Enter password: ");
            String password = input.nextLine();
    
            String filePath = "usernames_passwords.txt";
    
            if (writeCredentials(filePath, username, password)) {
                System.out.println("Registration Successful!....");
                System.out.println();
                System.out.println("Now Login ---->");
                login();
            } else {
                System.out.println("Signup Failed ..");
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            input.close();
        }
    }

    public static void login() {
        Scanner input = new Scanner(System.in);

        try {
            System.out.println("\nEnter Aadhar Number: ");
            String loginUsername = input.nextLine();
    
            System.out.println("Enter password: ");
            String loginPassword = input.nextLine();
    
            home(loginUsername, loginPassword);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            input.close();
        }
    }

    public static void home(String filePath1, String filePath2) {
        Scanner input = new Scanner(System.in);
        String filePath = "usernames_passwords.txt";

        if (checkCredentials(filePath, filePath1, filePath2)) {
            if (filePath1.equals("admin") && filePath2.equals("admin")) {
                boolean a = true;
                while (a) {
                    System.out.println("\nWelcome Admin Mowa...");
                    System.out.println("1. Add candidate");
                    System.out.println("2. Delete candidate");
                    System.out.println("3. View candidates");
                    // System.out.println("4. Start election");
                    System.out.println("0. Exit");
                    System.out.print("\nEnter your choice: ");

                    int adminChoice = input.nextInt();
                    input.nextLine(); // clear input buffer
                    switch (adminChoice) {
                        case 1:
                            System.out.println("Enter Candidate name to add: ");
                            String candidateNameToAdd = input.nextLine();
                            if (addCandidate("candidates.txt", candidateNameToAdd)) {
                                System.out.println("One Candidate added successfully!..");

                            } else {
                                System.out.println("Failed to add candidate../");
                            }
                            break;
                        case 2:
                            System.out.println("Enter Candidate name to delete: ");
                            String candidateNameToDel = input.nextLine();
                            if (deleteCandidate("candidates.txt", candidateNameToDel)) {
                                System.out.println("Candidate deleted succesfully!..");
                            } else {
                                System.out.println("Failed to delete candidate..");
                            }
                            break;

                        case 3:
                            listCandidates();

                            break;

                        case 0:
                            System.out.println("Exiting the Online Voting System...");
                            // System.exit(0);
                            Sign_Login();
                            a = false;
                            break;

                        default:
                            System.out.println("Invalid Choice");

                    }
                }
            } else {
                System.out.println("---Welcome to Online Voting System---");
                System.out.println("1. Cast your Vote");
                System.out.println("2. See Result");
                System.out.println("0. Exit");

                int userChoice = 0;
                userChoice = input.nextInt();
                input.nextLine(); // Clear input buffer
                

                switch (userChoice) {
                    case 1:
                    try {
                        if (castVote("candidates.txt", "votes.txt")) {
                            home(filePath1, filePath2);
                        } else {
                            System.out.println("Wrong Candidate");
                            home(filePath1, filePath2);
                        }
                    } catch (Exception e) {
                        System.out.println("An error occurred: " + e.getMessage());
                        // home(filePath1, filePath2);
                    }
                    break;                    

                    case 2:
                        seeResult();
                        break;

                    case 0:
                        Sign_Login();
                        break;

                    default:
                        System.out.println("Invalid Choice");
                        break;
                }
            }
        } else {
            System.out.println("Incorrect Credentials");
            Sign_Login();
        }
        input.close();
    }

    public static void listCandidates() {
        System.out.println("\nList of candidates: ");

        File inputFile = new File("candidates.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public static boolean castVote(String filePathCandidate, String filePathVote) {
        listCandidates();
        Scanner input = new Scanner(System.in);
        System.out.println("\nEnter the candidate's name you want to vote for: ");
        String votedCandidate = input.nextLine();

        if (checkCandidate("candidates.txt", votedCandidate)) {
            File voteFile = new File(filePathVote);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(voteFile, true))) {
                writer.write(votedCandidate + "\n");
                System.out.println("Thank you for casting your vote!");
                input.close();
                return true;
            } catch (IOException e) {
                System.err.println("Error writing file: " + e.getMessage());
                return false;
            }
        }  else {
            System.out.println("Invalid candidate name, Please try again...");
            return false;
        }

        // input.close();
    }

    public static void seeResult() {
        File voteFile = new File("votes.txt");
        Map<String, Integer> voteCount = new HashMap<String, Integer>();
        try (BufferedReader reader = new BufferedReader(new FileReader(voteFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (voteCount.containsKey(line)) {
                    voteCount.put(line, voteCount.get(line) + 1);
                } else {
                    voteCount.put(line, 1);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        System.out.println("\nResults:");
        for (Map.Entry<String, Integer> entry : voteCount.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " votes");
        }
    }

    public static boolean addCandidate(String filePath, String candidateName) {
        try {
            FileWriter writer = new FileWriter(filePath, true);
            writer.write(candidateName + "\n");
            writer.close();
            return true;
        } catch (IOException e) {
            // System.err.println("Error adding candidate to file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteCandidate(String filePath, String candidateName) {
        File inputFile = new File(filePath);
        File tempFile = new File("temp.txt");
    
        boolean found = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().equals(candidateName)) {
                    found = true;
                    continue;
                }
                writer.write(line + System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            System.err.println("Error reading/writing file: " + e.getMessage());
            return false;
        }
        if (found) {
            inputFile.delete();
            tempFile.renameTo(inputFile);
            return true;
        } else {
            tempFile.delete();
            return false;
        }
    }

    public static boolean writeCredentials(String filePath, String username, String password) {
        try {
            FileWriter writer = new FileWriter(filePath, true);
            writer.write(username + ", " + password + "\n");
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkCredentials(String filePath, String loginUsername, String loginPassword) {
        File file = new File(filePath);
        try {
            Scanner read = new Scanner(file);

            while (read.hasNextLine()) {
                String line = read.nextLine();
                String[] splitLine = line.split(", ");
                String username = splitLine[0];
                String password = splitLine[1];

                if (username.equals(loginUsername) && password.equals(loginPassword)) {
                    read.close();
                    return true;
                }
            }
            read.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkCandidate(String filePath, String candidateName) {
        File file = new File(filePath);
        try {
            Scanner read = new Scanner(file);

            while (read.hasNextLine()) {
                String line = read.nextLine();
                // String[] splitLine = line.split(", ");
                // String username = splitLine[0];
                String username = line;
                // String password = splitLine[1];

                if (username.equals(candidateName)) {
                    read.close();
                    return true;
                }
            }
            read.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
