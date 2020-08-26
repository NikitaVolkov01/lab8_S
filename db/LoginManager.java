package db;

import java.util.Scanner;

public class LoginManager {
    public static void start(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Введите логин или введите Р если желаете зарегистрироваться");
        String login = sc.nextLine();
        System.out.println("Введите пароль ");
        String password = sc.nextLine();
    }
}
