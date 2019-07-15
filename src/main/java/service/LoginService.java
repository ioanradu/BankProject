package service;

import model.Account;
import model.User;
import utils.ApplicationConst;
import utils.TxtFileReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginService {
    private String user;

    public User login(String userId, String password) {
        User user = null;
        TxtFileReader fileReader = new TxtFileReader(ApplicationConst.USERS_FILE_PATH);
        ArrayList<String> lines = fileReader.read();
        for (String line : lines) {
            String[] tokens = line.split(" ");
            if (tokens.length != 2) {
                continue;
            }
            String fileUserId = tokens[0];
            String filePassword = tokens[1];

            if (userId.equals(fileUserId) && password.equals(filePassword)) {
                Map<String, Account> accounts = new HashMap<String, Account>();
                user = new User(userId, password, accounts);
                break;
            }
        }
        return user;
    }
}
