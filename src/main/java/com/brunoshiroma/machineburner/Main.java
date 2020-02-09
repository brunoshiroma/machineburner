package com.brunoshiroma.machineburner;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static final String CPU_PARAM = "cpu";
    public static final String GPU_PARAM = "gpu";
    public static final String MEMORY_PARAM = "mem";

    public static void main(String args[]) throws IOException, ParseException {

        final Options options = new Options();

        options.addOption(CPU_PARAM, "CPU burner");
        options.addOption(MEMORY_PARAM, "Memory allocation burner");
        options.addOption(GPU_PARAM, "GPU burner");

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args);

        if (!commandLine.hasOption(CPU_PARAM) &&
                !commandLine.hasOption(GPU_PARAM) &&
                !commandLine.hasOption(MEMORY_PARAM)) {

            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("java  -XmxXXXG -XmsXXXG -jar machineburner-XXX.jar", options);
            System.exit(0);
        }

        final List<Burner> burners = new ArrayList<>();

        if(commandLine.hasOption(CPU_PARAM)){
            final Burner cpuBurner = new CPUBurner();
            burners.add(cpuBurner);
        }
        if(commandLine.hasOption(GPU_PARAM)){
            final Burner gpuBurner = new GPUBurner();
            burners.add(gpuBurner);
        }
        if(commandLine.hasOption(MEMORY_PARAM)){
            final Burner memoryBurner = new MemoryBurner();
            burners.add(memoryBurner);
        }

        burners.forEach(b -> b.burn());

        final Scanner scanner = new Scanner(System.in);

        System.out.println("Press enter to exit");

        scanner.nextLine();
    }

}