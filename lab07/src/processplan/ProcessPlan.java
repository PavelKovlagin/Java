package processplan;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ProcessPlan {

    static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int ind;
        boolean fileFound = false;

        CollectionProcesses CP = new CollectionProcesses();

        do {
            System.out.println("1 - создание файла, 2 - загрузка файла, 0 - выход");
            ind = scan.nextInt();
            switch (ind) {
                case 1:
                    CP.createCollectionProcesses();
                    CP.createFile();
                    fileFound = true;
                    break;
                case 2:
                    fileFound = CP.loadFile();
            }
            if (fileFound) {
                CP.writeProcessesInfo();
                CP.processesPlan();
                CP.writeProcessesPlan();
            }
        } while (ind != 0);
    }
}

