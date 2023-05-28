import java.util.*;
@SuppressWarnings("all")

public class RR {
    private static int quantum;
    private static int timer;
    private static ArrayList<Process> processes;
    private static LinkedList<Process> readyQueue;
    private static ArrayList<Integer> cycleCompletionTimes;
    private static ArrayList<Integer> processSequence;

    public static void run() {
        int numOfProcess;
        timer = 0;
        processes = new ArrayList<>();
        readyQueue = new LinkedList<>();
        cycleCompletionTimes = new ArrayList<>(); // store the timer for each cycle
        processSequence = new ArrayList<>(); // store the overall sequence of process executed
        
        Scanner in = new Scanner(System.in);
        System.out.println("\n*** Round Robin Scheduling ***\n");
        do {
            System.out.print("Enter Time Quantum: ");
            quantum = in.nextInt();
            if (quantum < 1 || quantum > 10)
                System.out.println("Time quantum can only be in range from 1 to 10\n");
            else
                break;
        } while (true);
        
        do {
            System.out.print("Enter no of process: ");
            numOfProcess = in.nextInt();
            if (numOfProcess < 3 || numOfProcess > 10)
                System.out.println("Number of process can only be in range from 3 to 10\n");
            else 
                break;
        } while (true);

        // populate processes
        for (int i = 0; i < numOfProcess; i++) {
            int arrivalTime, burstTime;
            System.out.print("\nEnter P" + i + " arrival time: ");
            arrivalTime = in.nextInt();
            System.out.print("Enter P" + i + " burst time: ");
            burstTime = in.nextInt();
            processes.add(new Process(i, burstTime, arrivalTime));
        }

        // insert first arrived process
        Collections.sort(processes); // sort the processes by arrival time 
        cycleCompletionTimes.add(0); // as timer always starts from 0
        Process firstArrivedProcess = processes.get(0);
        if (firstArrivedProcess.getArrivalTime() != 0)
            loopForNextArrivalProcess();
        else {
            readyQueue.addFirst(firstArrivedProcess);
            firstArrivedProcess.setInQueue(true);
        }
        // start executing round robin
        while (!readyQueue.isEmpty()) {
            if (readyQueue.getFirst().getBurstTime() <= quantum) {
                Process finishedProcess = readyQueue.pop();
                processSequence.add(finishedProcess.getProcessNum());
                timer += finishedProcess.getBurstTime();
                while (finishedProcess.getBurstTime() > 0) 
                    finishedProcess.decrementBurstTime();
                finishedProcess.updateProcessTimes(timer); // set isComplete to true and calculate tt, wt
                checkForNewArrivals(timer);
            }
            else {
                Process unfinishProcess = readyQueue.pop();
                processSequence.add(unfinishProcess.getProcessNum());
                for (int i = 0; i < quantum; i++)
                    unfinishProcess.decrementBurstTime();
                timer += quantum;
                checkForNewArrivals(timer);
                readyQueue.addLast(unfinishProcess);
            }
            cycleCompletionTimes.add(timer);

            if (readyQueue.isEmpty() && hasPendingProcesses()) // no on going process between arrival times
                loopForNextArrivalProcess();
        }
        
        // construct table 
        printTable();

        // construct gantt chart
        printGanttChart();

        // Display Average TT & WT
        System.out.printf("\n\n**Average Turnaround Time = %.2f\n", calculateAVGTurnaroundTime());
        System.out.printf("**Average Waiting Time = %.2f\n", calculateAVGWaitingTime());
    } 

    private static void checkForNewArrivals(int currentTime) {
        for (int i = 0; i < processes.size(); i++) {
            Process temp = processes.get(i);
            if (temp.getArrivalTime() <= currentTime && !temp.inQueue() && !temp.isComplete()) {
                temp.setInQueue(true);
                readyQueue.addLast(temp);
            }
        }
    }
    private static boolean hasPendingProcesses() {
        for (Process process : processes)
            if (!process.isComplete())
                return true;
        return false;
    }
    private static void loopForNextArrivalProcess() {
        processSequence.add(null);
        int i = timer + 1;
        while (readyQueue.isEmpty()) {
            checkForNewArrivals(i);
            if (!readyQueue.isEmpty()) {
                timer = i;
                cycleCompletionTimes.add(timer);
            }
            else if (i == timer + quantum) {
                timer += quantum;
                processSequence.add(null);
                cycleCompletionTimes.add(timer);
            }
            i++;
        }
    }
    private static double calculateAVGTurnaroundTime() {
        double totalTT = 0;
        for (int i = 0; i < processes.size(); i++)
            totalTT += processes.get(i).getTurnAroundTime();
        return totalTT / processes.size();
    }
    private static double calculateAVGWaitingTime() {
        double totalWT = 0;
        for (int i = 0; i < processes.size(); i++)
            totalWT += processes.get(i).getWaitingTime();
        return totalWT / processes.size();
    }
    private static void printTable() {
        System.out.println("\nTable:");
        System.out.println("\n+---------+--------------+------------+-----------------+-----------------+--------------+");
        System.out.println("| Process | Arrival Time | Burst Time | Completion Time | Turnaround Time | Waiting Time |");
        System.out.println("+---------+--------------+------------+-----------------+-----------------+--------------+");
        
        for (int i = 0; i < processes.size(); i++) {
            int at, bt, ct, tt, wt;
            for (int j = 0; j < processes.size(); j++)
                if (processes.get(j).getProcessNum() == i) {
                    at = processes.get(j).getArrivalTime();
                    bt = processes.get(j).getInitialBurstTime();
                    ct = processes.get(j).getCompletionTime();
                    tt = processes.get(j).getTurnAroundTime();
                    wt = processes.get(j).getWaitingTime();

                    String row = String.format(
                        "|   P%d    |      %2d      |     %2d     |       %2d        |       %2d        |      %2d      |", 
                        i, at, bt, ct, tt, wt
                    );
                    System.out.println(row);
                }
        }
        System.out.println("+---------+--------------+------------+-----------------+-----------------+--------------+");
    }
    private static void printGanttChart() {
        System.out.println("\nGantt Chart: ");
        int numOfBoxes = processSequence.size();
        drawLine(numOfBoxes);
        System.out.print("|");
        for (int i = 0; i < numOfBoxes; i++) {
            if (processSequence.get(i) == null) 
                System.out.print("    |");
            else
                System.out.print(" P" + processSequence.get(i) + " |");
        }
        drawLine(numOfBoxes);
        for (int i = 0; i < cycleCompletionTimes.size(); i++) 
            if (cycleCompletionTimes.get(i) / 10 > 0)
                System.out.print(String.format("%d   ", cycleCompletionTimes.get(i)));
            else
                System.out.print(String.format("%d    ", cycleCompletionTimes.get(i)));
    }
    private static void drawLine(int numOfBoxes) {
        System.out.println();
        while (numOfBoxes > 0) {
            System.out.print("+----");
            numOfBoxes--;
        }
        System.out.print("+");
        System.out.println();
    }
}