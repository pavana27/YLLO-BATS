package org.temple.cis.ec;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    // represents M
    int maxFps;

    // represents S
    List<Node> primaryNodes = new ArrayList<>();

    // represents !S
    List<Node> secondaryNodes = new ArrayList<>();

    List<Node> allNodes = new ArrayList<>();

    // represents R
    float bitRate;

    int primaryCamCount;

    int secondaryCamCount;

    float guaranteedRate;

    float minBitRate;

    float secondaryRate;

    float tot;

    float newFps;

    public static void main(String[] args) {
        Main main = new Main();
        main.init();
        // calculate the transmission rate for primary cameras - R/|S| -Ss
        // assign the rates to primary cameras
        main.assignRatesPrimaryCam();

        // calculate the transmission rate for secondary cameras - R/|!S| --> Rm
        // assign the rates to secondary cameras
        main.assignRatesNonPrimaryCam();

        // calculate total - Tot = Ss + Rm
        main.getTotalTransmissionRate();

        // calculate remaining Mm = Mm - Tot
        main.calculateNewFPS();

        // share the remaining Mm among the cameras - [ r ]
        // display the [ r ]
        main.calculateSamplingRates();
    }

    private void init() {
        Scanner scanner = new Scanner(System.in);

        // git physical layer bit rate - R
        System.out.println("Enter the physical layer rate \n");
        bitRate = scanner.nextFloat();

        // git maximum transmission rate - M
        System.out.println("Enter maximum transmission per seconds \n");
        maxFps = scanner.nextInt();

        // this is S
        System.out.println("Enter the number of primary cameras");
        primaryCamCount = scanner.nextInt();

        System.out.println("Enter the primary camera weights");
        for (int i = 1; i <= primaryCamCount; i ++) {
            Node node = new Node();
            node.setId(i);
            node.setWeight(scanner.nextFloat());
            primaryNodes.add(node);
        }

        // this is !S
        System.out.println("Enter the number of secondary cameras");
        secondaryCamCount = scanner.nextInt();

        System.out.println("Enter the secondary camera weights");
        for (int j = primaryCamCount + 1; j <= (primaryCamCount + secondaryCamCount); j ++) {
            Node node = new Node();
            node.setId(j);
            node.setWeight(scanner.nextFloat());
            secondaryNodes.add(node);
        }

        System.out.println("Enter a minimum guaranteed transfer rate");
        guaranteedRate = scanner.nextFloat();

        System.out.println("Enter a minimum transfer rate for non primary cameras");
        minBitRate = scanner.nextFloat();
    }

    // calculate the transmission rate for primary cameras - R/|S| -Ss
    // assign the rates to primary cameras
    private void assignRatesPrimaryCam() {
        float trnzRate = guaranteedRate / primaryNodes.size();
        int i = 0;
        for(Node node : primaryNodes) {
            node.setTranRate(trnzRate);
            primaryNodes.set(i, node);
            i ++;
        }
    }

    private void assignRatesNonPrimaryCam() {
        int i = 0;
        secondaryRate = 0f;
        for(Node node : secondaryNodes) {
            node.setTranRate(minBitRate);
            secondaryNodes.set(i, node);
            secondaryRate += minBitRate;
            i ++;
        }
    }

    private void getTotalTransmissionRate() {
        tot = guaranteedRate + secondaryRate;
    }

    private void calculateNewFPS() {
        newFps = maxFps - tot;
    }

    private void calculateSamplingRates() {
        allNodes.addAll(primaryNodes);
        allNodes.addAll(secondaryNodes);

        float totalWeights = 0f;
        for (Node node: allNodes) {
            totalWeights += node.getWeight();
        }

        System.out.println("NodeId,Node_Weight,TranRate");
        for (Node node: allNodes) {
            float r = node.getTranRate();
            node.setTranRate(r + ((node.getWeight()/totalWeights) * newFps));
            System.out.println(node.getId() + "," + node.getWeight() + "," + node.getTranRate());
        }
    }
}
