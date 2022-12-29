import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/*
@author Mustafa Bulut
@date 20.12.2022
 */

public class c20170808030 {
    public static List<Integer> burstTimes=new ArrayList<>();
    public static List<Integer> tATimes=new ArrayList<>();
    public static HashMap<Integer,ArrayList<ArrayList<Integer>>> processLine=new HashMap<>();



    public static void main(String[] args) throws FileNotFoundException {

        int processValue=0;
        int SetTime = 0;
        int counter = 0;

        File jobs = new File(args[0]);

        fileOperation(jobs);
        FirstComeFirstServed(processValue, SetTime, counter);

        for (int i= 0 ;i<tATimes.size();i++){
            System.out.println();
            System.out.println("Turnaround time of process '" + (i+1) + "' is " + tATimes.get(i));
            System.out.println("Waiting time of process '" + (i+1) + "' is " + (tATimes.get(i)-burstTimes.get(i)));
            System.out.println();   //Here I have printed the turnaround time and waiting time for each process.
        }

        HALT();




    }
    public static String LineSimplification(String myStr){ //To remove semicolons and parentheses in lines
        myStr=myStr.replace("(","`");
        myStr=myStr.replace(")","");
        myStr=myStr.replace(";","");

        return myStr;
    }
    public static int ProcessHandler(int timeValue) {
        for(int a=1;a<=processLine.size();a++){ //Here I am going through the ProcessLine hashmap one by one and the arraylist value of the 1st element with the key i value of the hashmap is 0. If the element is 0, the process is not finished. If the time value of the process is less than or equal to that process, that is Returns the i.ci process is executable.
            if(processLine.get(a).get(1).get(0)==0){
                if(processLine.get(a).get(0).get(0)<=timeValue){
                    return a;
                }
            }
        }
        return -1;
    }



    public static void fileOperation(File file) throws FileNotFoundException {
          //for command line
        Scanner myScanner = new Scanner(file);
        int counter = 0; // To calculate number of lines in jobs.txt
        while (myScanner.hasNextLine()) {
            String jobsLine = myScanner.nextLine(); //ı take each row as a job row then i wıll split these jobs rows for reading tupples.
            tATimes.add(0);
            burstTimes.add(0);
            counter++;
            ArrayList<ArrayList<Integer>> currentLine = new ArrayList<>();  //I took the data type of this arraylist as an arraylist in order to make the rows in tupple an arraylist one by one and add them to this arraylist
            currentLine.add(new ArrayList<>(){{ add(0);}});
            currentLine.add(new ArrayList<>(){{ add(0);}});  // assign to first and second value 0 for the return time and finishing process
            /*
            Spliting Line
             */

            String[] pid = jobsLine.split(":"); // :: or a stringaarray holding the parts and ı called that part as pid
            jobsLine=pid[1];
            jobsLine=LineSimplification(jobsLine); //To remove semicolons and parentheses in lines
            jobsLine=jobsLine.substring(1,jobsLine.length());
            String[] simpLine=jobsLine.split("`");  //Now that we have removed the apostrophes at the beginning, we can now split our row into individual tupples.

            for(int i=0; i<simpLine.length;i++){
                String myStr2=simpLine[i];
                String[] tuppleTemp = myStr2.split(",");
                currentLine.add(new ArrayList<>(){{
                    add(Integer.parseInt(tuppleTemp[0]));
                    add(Integer.parseInt(tuppleTemp[1]));
                    add(0);
                    //I made our tupple's values into an arraylist and added them to our currentLine arraylist one by one, and I added 0 to the first two elements above as a value. If you remember, then we assign the tupples as cpu-i/0 and 0.
                }});


            }
            processLine.put(counter,currentLine);



        }
    }



    public static void FirstComeFirstServed(int processValue,int SetTime, int counter){

        while (counter<processLine.size()){

            processValue = ProcessHandler(SetTime);

            if (processValue != -1){
                for (int i=2 ; i< processLine.get(processValue).size(); i++) {
                    if (processLine.get(processValue).get(i).get(2) == 0){ //if processhandler returns -1 value, We assigned 0 to the first two elements and every element after that is a cpu io process.

                        SetTime = SetTime + processLine.get(processValue).get(i).get(0); //If the tuple has not been processed yet and the element showing this value is 0, we add the CPU burst time with our previous value.
                        if (processLine.get(processValue).get(i).get(1) == -1){

                            int timeValue = SetTime;
                            processLine.get(processValue).get(0).set(0,timeValue);
                            processLine.get(processValue).get(i).set(2,1);
                            processLine.get(processValue).get(1).set(0,1);
                            tATimes.set(processValue-1 , processLine.get(processValue).get(0).get(0) );
                            burstTimes.set(processValue-1, burstTimes.get(processValue-1) + processLine.get(processValue).get(i).get(0));
                            counter++;
                            break;
                            /*

                        Here, if the process is finished, I change the end value of the process with the 3rd element of my hash.
                        then I make the time value the current value and the process ends. and the last process is now over, I am changing the 2nd value of my hash.
                             */
                        }

                        else {
                            int return_time = SetTime + processLine.get(processValue).get(i).get(1);
                            processLine.get(processValue).get(0).set(0,return_time);
                            processLine.get(processValue).get(i).set(2,1);
                            burstTimes.set(processValue-1, burstTimes.get(processValue-1) + processLine.get(processValue).get(i).get(0));
                            break;

                            /*

Here I collect the time value with cpu burst and io burst. and then I set this value with the current value. After I set the 3rd value of the tuple to 1, I finish the process.
                             */
                        }

                    }
                }


            }else {
                SetTime++;

                /*
                 also increase the time value in case the processes are not running.
                 */
            }
        }


    }

    public static void HALT(){ //Here I calculated and printed the average turnaround time and average waiting time.
        int turnAroundSum = 0;
        int waitingTimeSum = 0;

        for (int i = 0;i< tATimes.size();i++){
            turnAroundSum = turnAroundSum + tATimes.get(i);
            waitingTimeSum = waitingTimeSum + (tATimes.get(i)-burstTimes.get(i));
        }

        int AverageTurnAroundTime = turnAroundSum/tATimes.size();
        int AverageWaitingTime = waitingTimeSum/tATimes.size();


        System.out.println("Average Turnaround Time : " + AverageTurnAroundTime );
        System.out.println("Average Waiting Time : " + AverageWaitingTime);
    }

}