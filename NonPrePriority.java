import java.util.*;
@SuppressWarnings("all")

public class NonPrePriority {
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
       
        Scanner input = new Scanner(System.in);
        System.out.println("\n*** Non-Preemptive Priority Scheduling ***\n");
        do {
            System.out.print("Enter no of process: ");
            numOfProcess = input.nextInt();
            if (numOfProcess < 3 || numOfProcess > 10)
                System.out.println("Number of process can only be in range from 3 to 10\n");
            else 
                break;
        } while (true);

        // populate processes
        for (int i = 0; i < numOfProcess; i++) {
            int arrivalTime, burstTime, priority;
            System.out.print("\nEnter P" + i + " arrival time: ");
            arrivalTime = input.nextInt();
            System.out.print("Enter P" + i + " burst time: ");
            burstTime = input.nextInt();
            System.out.print("Enter P" + i + " priority: ");
            priority = input.nextInt();
            processes.add(new Process(i, burstTime, arrivalTime, priority));
        }

        Collections.sort(processes);
        cycleCompletionTimes.add(0);
        Process firstArrivedProcess = processes.get(0);
        if (firstArrivedProcess.getArrivalTime() != 0) 
            loopForNextArrivalProcess();
        else {
            readyQueue.addFirst(firstArrivedProcess);
            firstArrivedProcess.setInQueue(true);
        }
        while (!readyQueue.isEmpty()) {
            Process finishedProcess = readyQueue.pop();
            processSequence.add(finishedProcess.getProcessNum());
            timer += finishedProcess.getBurstTime();
            finishedProcess.updateProcessTimes(timer);
            cycleCompletionTimes.add(timer);
            checkForNewArrivals(timer);
            if (readyQueue.isEmpty() && hasPendingProcesses())
                loopForNextArrivalProcess();
        }

        printTable();
        printGanttChart();
        System.out.printf("\n\n**Average Turnaround Time = %.2f\n", calculateAVGTurnaroundTime());
        System.out.printf("**Average Waiting Time = %.2f\n", calculateAVGWaitingTime());
    }
    private static void printTable() {
        System.out.println("\nTable:");
        System.out.println("\n+---------+--------------+------------+----------+-----------------+-----------------+--------------+");
        System.out.println("| Process | Arrival Time | Burst Time | Priority | Completion Time | Turnaround Time | Waiting Time |");
        System.out.println("+---------+--------------+------------+----------+-----------------+-----------------+--------------+");
        
        for (int i = 0; i < processes.size(); i++) {
            int at, bt, pr, ct, tt, wt;
            for (int j = 0; j < processes.size(); j++)
                if (processes.get(j).getProcessNum() == i) {
                    at = processes.get(j).getArrivalTime();
                    bt = processes.get(j).getInitialBurstTime();
                    pr = processes.get(j).getPriority();
                    ct = processes.get(j).getCompletionTime();
                    tt = processes.get(j).getTurnAroundTime();
                    wt = processes.get(j).getWaitingTime();

                    String row = String.format(
                        "|   P%d    |      %2d      |     %2d     |    %2d    |       %2d        |       %2d        |      %2d      |", 
                        i, at, bt, pr, ct, tt, wt
                    );
                    System.out.println(row);
                }
        }
        System.out.println("+---------+--------------+------------+----------+-----------------+-----------------+--------------+");
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
    private static boolean hasPendingProcesses() {
        for (Process process : processes)
            if (!process.isComplete())
                return true;
        return false;
    }
    private static boolean hasNewArrival(int time) {
        for (int i = 0; i < processes.size(); i++) {
            Process temp = processes.get(i);
            if (temp.getArrivalTime() <= time && !temp.inQueue() && !temp.isComplete())
                return true;
        }
        return false;
    }
    private static void checkForNewArrivals(int currentTime) {
        for (int i = 0; i < processes.size(); i++) {
            Process temp = processes.get(i);
            if (temp.getArrivalTime() <= currentTime && !temp.inQueue() && !temp.isComplete()) {
                temp.setInQueue(true);
                readyQueue.add(temp);
            }
        }
        Collections.sort(readyQueue, (p1, p2) -> (p1.getPriority() - p2.getPriority()));
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
