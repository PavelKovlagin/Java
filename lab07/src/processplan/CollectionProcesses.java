package processplan;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CollectionProcesses implements Serializable {

    Scanner scan = new Scanner(System.in);

    ArrayList<Process> collectionProcesses = new ArrayList<Process>();

    public void createCollectionProcesses() {
        ArrayList<Process> process = new ArrayList<Process>();
        System.out.println("Введите количество процессов");
        int n = scan.nextInt();
        for (int i = 0; i < n; i++) {
            String name = "p" + String.valueOf(i);
            System.out.print("Введите время посвления процесса " + name + ": ");
            int time = scan.nextInt();
            System.out.print("Введите приоритет процесса " + name + ": ");
            int priority = scan.nextInt();
            System.out.print("Введите продолжительность процесса " + name + ": ");
            int processLong = scan.nextInt();
            process.add(new Process(name, time, priority, processLong));
        }
        this.collectionProcesses = process;
    }

    public void createFile() throws IOException {
        System.out.println("Введите имя файла: ");
        String fileName = scan.next();
        FileOutputStream fos = new FileOutputStream(fileName + ".bin");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this.collectionProcesses);
        oos.flush();
        oos.close();
    }

    public boolean loadFile() throws ClassNotFoundException {
        boolean fileFound = false;
        try {
            System.out.println("Введите имя файла (без расширения");
            String fileName = scan.next();
            FileInputStream fis = new FileInputStream(fileName + ".bin");
            ObjectInputStream oin = null;
            oin = new ObjectInputStream(fis);
            this.collectionProcesses = (ArrayList<Process>) oin.readObject();
            fileFound = true;
        } catch (IOException e) {
            System.out.println("Файл не найден");
            fileFound = false;
        }
        return  fileFound;
    }

    public void writeProcessesInfo() {
        System.out.println(
                libFormat.format("Процесс", 8)
                        + libFormat.format("Время появления", 17)
                        + libFormat.format("Приоритет", 10)
                        + libFormat.format("Продолжительность", 18)
        );

        //вывод  списка процессов
        for (
                Process proc : this.collectionProcesses) {
            System.out.println(proc.toString());
        }
    }

    public void processesPlan() {
        //планирование процессов
        int step = 0;
        do {
            Process priorityProcess = new Process("p", 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
            for (Process proc : this.collectionProcesses) {
                if ((proc.getProcessLong() > 0
                        && proc.getTimeAppeapance() <= step)
                        && proc.getPriority() < priorityProcess.getPriority()) {
                    priorityProcess = proc;
                    if (proc.getProcessLong() < priorityProcess.getProcessLong()) {
                        priorityProcess = proc;
                    }
                }
            }

            for (Process proc : this.collectionProcesses) {
                if (proc.getTimeAppeapance() <= step && proc.getProcessLong() > 0) {
                    if (proc != priorityProcess) {
                        proc.addStep("Г");
                    } else {
                        proc.addStep("И");
                        proc.executeProcess();
                    }
                } else {
                    proc.addStep("-");
                }
            }
            step++;
        } while (step <= 100);
    }

    public void writeProcessesPlan() {
        //вывод индексов щагов
        System.out.print("   ");
        for (
                int i = 0; i < this.collectionProcesses.get(1).

                getProcessSize();

                i++) {
            System.out.print(libFormat.format(i, 3));
        }
        System.out.println();

        //вывод состояний процессов
        for (
                Process proc : this.collectionProcesses) {
            System.out.print(proc.getName() + " ");
            proc.getStep();
            System.out.println();
        }

        //
        double avgWaitTime = 0;
        double avgExecuteTime = 0;
        for (
                Process proc : this.collectionProcesses) {
            avgWaitTime = avgWaitTime + proc.getWaitTime();
            avgExecuteTime = avgExecuteTime + proc.getExecuteTime();
        }
        System.out.println("Среднее время ожидания = " + avgWaitTime + "/" + this.collectionProcesses.size() + " = " + avgWaitTime / this.collectionProcesses.size());
        System.out.println("Среднее время выполнения = " + avgExecuteTime + "/" + this.collectionProcesses.size() + " = " + avgExecuteTime / this.collectionProcesses.size());
    }
}
