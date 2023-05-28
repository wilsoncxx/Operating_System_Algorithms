import java.util.*;

@SuppressWarnings("all")

public class SJF {
    public static void nonPreemptive() {
        Scanner input = new Scanner(System.in);
        boolean processNumberOutOfbound;
        int numberOfProcess;

        System.out.println("\n*** Non-Preemptive SJF Scheduling ***\n");
        do {
            System.out.print("Enter no of process: ");
            numberOfProcess = input.nextInt();
            processNumberOutOfbound = false;
            if (numberOfProcess < 3 || numberOfProcess > 10) {
                processNumberOutOfbound = true;
                System.out.println("Number of process can only be in range from 3 to 10\n");
            }
        } while (processNumberOutOfbound);

        Process process[] = new Process[numberOfProcess];
        int systemTime = 0;         // system time
        int completedProcess = 0;
        float avgWT = 0, avgTAT = 0;

        // Below are variables for generating ganttChart purpose
        int ganttChart[] = new int[numberOfProcess];
        int order = 0;      // use for arranging the order of proccesses in the gantt chart
        int ganttChartExecutionTime[] = new int[numberOfProcess + 1];
        int ganttChartCompletionTime[] = new int[numberOfProcess];
        boolean proccessArriveAt0 = false;

        for (int i = 0; i < numberOfProcess; i++) {
            System.out.print("\nEnter P" + i + " arrival time: ");
            int arrivalTime = input.nextInt();
            System.out.print("Enter P" + i + " burst time: ");
            int burstTime = input.nextInt();
            process[i] = new Process(i, burstTime, arrivalTime);
        }

        for (int i = 0; i < numberOfProcess; i++) {
            if (process[i].getArrivalTime() == 0) {
                proccessArriveAt0 = true;
                break;
            }
        }

        while (true) {
            // 'current' is a pointer to the process which are going to be executed
            int current = numberOfProcess, minBurstTime = 100;     // preset current = number of process

            // if completed process = no of process, should break from this loop
            if (completedProcess == numberOfProcess)
                break;
            for (int i = 0; i < numberOfProcess; i++) {
                /*
                 * If i'th process' arrival time <= system time and it is not 'completed'  and its burst time < minimum burst time
                 * Then set 'current' points to that particular process
                 */
                if ((process[i].getArrivalTime() <= systemTime) && (!process[i].isComplete()) && (process[i].getBurstTime() < minBurstTime)) {
                    minBurstTime = process[i].getBurstTime();
                    current = i;
                }
            }
            /*
             * If current still equals to number of process, means no process's arrival time < system time
             * so have to increase the system time
             */
            if (current == numberOfProcess)
                systemTime++;
            else {
                int completionTime = systemTime + process[current].getBurstTime();
                process[current].updateProcessTimes(completionTime);
                completedProcess++;
                ganttChart[order] = process[current].getProcessNum();
                ganttChartExecutionTime[order] = systemTime;
                ganttChartCompletionTime[order] = process[current].getCompletionTime();
                order++;
                systemTime += process[current].getBurstTime();
            }
        }

        System.out.println("\nTable:");
        System.out.println("\n+---------+--------------+------------+-----------------+-----------------+--------------+");
        System.out.println("| Process | Arrival Time | Burst Time | Completion Time | Turnaround Time | Waiting Time |");
        System.out.println("+---------+--------------+------------+-----------------+-----------------+--------------+");

        for (int i = 0; i < numberOfProcess; i++) {
            int processNum, at, bt, ct, tat, wt;
            processNum = process[i].getProcessNum();
            at = process[i].getArrivalTime();
            bt = process[i].getBurstTime();
            ct = process[i].getCompletionTime();
            tat = process[i].getTurnAroundTime();
            wt = process[i].getWaitingTime();
            avgWT += wt;
            avgTAT += tat;

            String row = String.format(
                "|   P%d    |      %2d      |     %2d     |       %2d        |       %2d        |      %2d      |", 
                processNum, at, bt, ct, tat, wt
            );
            System.out.println(row);
        }
        System.out.println("+---------+--------------+------------+-----------------+-----------------+--------------+");

        // discard the last element in ganttChartExecutionTime[]
        ganttChartExecutionTime[process.length] = ganttChartCompletionTime[process.length - 1];

        System.out.println("\nGantt Chart : ");
        drawLine(proccessArriveAt0, ganttChart, ganttChartExecutionTime, ganttChartCompletionTime);
        System.out.print("|");
        if (!proccessArriveAt0)
            System.out.print("    |");
        for(int i = 0; i < ganttChart.length; i++) {
            System.out.print(" P" + ganttChart[i] + " |");
            if (ganttChartExecutionTime[i + 1] != ganttChartCompletionTime[i])
                System.out.print("    |");
        }
        drawLine(proccessArriveAt0, ganttChart, ganttChartExecutionTime, ganttChartCompletionTime);
        System.out.print("0    ");
        if (!proccessArriveAt0) {
            if (ganttChartExecutionTime[0] / 10 > 0)
                System.out.print(ganttChartExecutionTime[0] + "   ");
            else
                System.out.print(ganttChartExecutionTime[0] + "    ");
        }

        for(int i = 0; i < ganttChart.length; i++) {
            if (ganttChartCompletionTime[i] / 10 > 0) {
                System.out.print(ganttChartCompletionTime[i] + "   ");

                if (ganttChartExecutionTime[i + 1] != ganttChartCompletionTime[i])
                    System.out.print(ganttChartExecutionTime[i + 1] + "   ");
            }
            else {
                System.out.print(ganttChartCompletionTime[i] + "    ");

                if (ganttChartExecutionTime[i + 1] != ganttChartCompletionTime[i]) {
                    if (ganttChartExecutionTime[i + 1] / 10 > 0)
                        System.out.print(ganttChartExecutionTime[i + 1] + "   ");
                    else
                        System.out.print(ganttChartExecutionTime[i + 1] + "    ");
                }
            }
        }
        System.out.printf("\n\n**Average Turnaround Time = %.2f\n", (float)(avgTAT/numberOfProcess) );
        System.out.printf("**Average Waiting Time = %.2f\n", (float)(avgWT/numberOfProcess) );
    }

    private static void drawLine(boolean proccessArriveAt0, int[] ganttChart,
                                 int[] ganttChartExecutionTime, int[] ganttChartCompletionTime)
    {
        System.out.println();
        if (!proccessArriveAt0)
            System.out.print("+----");
        for(int i = 0; i < ganttChart.length; i++) {
            System.out.print("+----");
            if (ganttChartExecutionTime[i + 1] != ganttChartCompletionTime[i])
                System.out.print("+----");
        }
        System.out.print("+");
        System.out.println();
    }
}
