package ru;

import ru.jdbc.Auto;
import ru.jdbc.classAuto;
import ru.jdbc.connection;
import ru.model.AutoModel;
import ru.model.classAutoModel;
import java.util.Scanner;

public class main {


    static Scanner scan = new Scanner(System.in).useDelimiter("\\n");


    public static void main(String[] args) {
        connection conn = new connection();

        Auto auto = new Auto(conn.getConnection());
        classAuto classAuto = new classAuto(conn.getConnection());

        int i = 0;
        do {
            System.out.println("1 - просмотр таблицы Auto,\n" +
                    "2 - добавить в таблицу Auto,\n" +
                    "3 - удалить запись из Auto,\n" +
                    "4 - просмотр таблицы classAuto,\n" +
                    "5 - добавление в таблицу classAuto,\n" +
                    "6 - удаление из таблицы classAuto,\n" +
                    "0 - выход");
            i = scan.nextInt();
            switch (i) {
                case 1: {

                    auto.SELECT();
                    break;
                }
                case 2: {
                    System.out.println("Введите id class, марку, модель");
                    auto.INSERT(new AutoModel(scan.nextInt(), scan.next(), scan.next()));
                    break;
                }

                case 3: {
                    System.out.print("Введите id автомобиля: ");
                    auto.DELETE(scan.nextInt());
                    break;
                }
                case 4: {
                    classAuto.SELECT();
                    break;
                }
                case 5: {
                    System.out.println("Введите имя класса и примечание (если есть)");
                    classAuto.INSERT(new classAutoModel(scan.next(), scan.next()));
                    break;
                }
                case 6: {
                    System.out.println("Введите id класса автомобиля");
                    classAuto.DELETE(scan.nextInt());
                }
            }
        } while (i != 0);

    }
}
