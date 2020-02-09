package com.brunoshiroma.machineburner;

public class MemoryBurner implements Burner {

    private byte[][] dummyBytes;

    public static final int MEGA = 1024 * 1024;

    @Override
    public void burn() {
        final long initialFreeMemory = Runtime.getRuntime().maxMemory();

        final int megas = (int)(initialFreeMemory / MEGA);

        dummyBytes = new byte[megas][];

        for(int i = 0; i < megas; i++){
            if(Runtime.getRuntime().freeMemory() <= 128 * MEGA){
                break;
            }

            try{
                dummyBytes[i] = new byte[MEGA];
            }catch (OutOfMemoryError e){
                break;
            }
        }

        System.out.println(String.format("Free %d", Runtime.getRuntime().freeMemory()));
    }
}