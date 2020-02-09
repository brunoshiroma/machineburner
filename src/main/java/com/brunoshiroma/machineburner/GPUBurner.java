package com.brunoshiroma.machineburner;

import com.aparapi.Kernel;
import com.aparapi.device.Device;
import com.aparapi.internal.kernel.KernelManager;

import java.security.SecureRandom;
import java.util.LinkedHashSet;
import java.util.concurrent.atomic.AtomicReference;

public class GPUBurner implements Burner {

    final int MAX_INT_VALUE = Short.MAX_VALUE;

    @Override
    public void burn() {

        Thread runner = new Thread(() -> {
            while (true) {
                final float[] A = new float[MAX_INT_VALUE];
                final float[] B = new float[MAX_INT_VALUE];
                final float[] RESULT = new float[MAX_INT_VALUE];

                final SecureRandom secureRandom = new SecureRandom();

                for (int i = 0; i < MAX_INT_VALUE; i++) {
                    A[i] = secureRandom.nextFloat();
                    B[i] = secureRandom.nextFloat();
                }

                final int MAXGPUDUMMY = 1024;

                Kernel kernel = new Kernel() {
                    @Override
                    public void run() {
                        int passId = getGlobalId();

                        byte[] dummyBytes = new byte[1048000];

                        for (int i = 0; i < 1_000_000_000; i++) {
                            RESULT[passId] = acos(A[passId]) + abs(B[passId]);

                            if (i < 1024) {
                                dummyBytes[i] = 20;
                            } else {
                                dummyBytes[0] = 4;
                            }

                        }

                    }
                };

                final AtomicReference<Device> device = new AtomicReference<>();

                KernelManager.instance().getDefaultPreferences().getPreferredDevices(null).forEach(d -> {
                    System.out.println(String.format("APARAPI Device %s", d.getShortDescription()));

                    if (device.get() == null) {
                        device.set(d);
                    } else if (device.get().getType() == Device.TYPE.GPU) {

                        if (device.get().getShortDescription().toLowerCase().contains("intel") && !d.getShortDescription().toLowerCase().contains("intel")) {
                            device.set(d);
                        }
                    }

                });

                LinkedHashSet<Device> deviceLinkedHashSet = new LinkedHashSet<>();
                deviceLinkedHashSet.add(device.get());

                KernelManager.instance().setPreferredDevices(kernel, deviceLinkedHashSet);

                System.out.println(String.format("GPU Kernel %s", kernel.toString()));
                System.out.println(String.format("GPU Kernel executing %s", kernel.isExecuting()));
                System.out.println(String.format("GPU Kernel executing OpenCL %s", kernel.isRunningCL()));

                kernel.setExecutionModeWithoutFallback(Kernel.EXECUTION_MODE.GPU);
                kernel.execute(MAX_INT_VALUE);

                System.out.println(String.format("GPU Kernel %s", kernel.toString()));
                System.out.println(String.format("GPU Kernel executing %s", kernel.isExecuting()));
                System.out.println(String.format("GPU Kernel executing OpenCL %s", kernel.isRunningCL()));


                try {
                    Thread.sleep(1000 * 60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        runner.setDaemon(true);
        runner.start();
    }


}