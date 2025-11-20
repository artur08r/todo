package org.example;

import org.example.api.Controller;
import org.example.bd.Repository;
import org.example.domain.Service;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args ) {
        Repository repo = new Repository();
        Service service = new Service(repo);
        Controller controller = new Controller(service, new Scanner(System.in));
        Scanner scanner = new Scanner(System.in);

        while (true){
            System.out.println("Введите команду");
            String currentMethod = scanner.nextLine();
            if (currentMethod.equals("exit")){
                System.out.println("Программа завершается");
                break;
            }
            controller.run(currentMethod);
        }
    }
}
