package thread.tools.Executors;

import java.util.concurrent.Callable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FutureTask {
	
    public static void main(String[] args) throws Exception{
    	
        //第一种方式
        ExecutorService executor = Executors.newCachedThreadPool();
        
        Task2 task = new Task2();
        
        java.util.concurrent.FutureTask<Integer> futureTask = new java.util.concurrent.FutureTask<Integer>(task);
        
        executor.submit(futureTask);
        
        executor.shutdown();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
         
        long start = System.currentTimeMillis();
        
        Integer r = futureTask.get(); // 方法会一直阻塞直到线程的结果返回。
        
        long end = System.currentTimeMillis();
        
        System.out.println("task result:"+r+"; totoally executed "+ ( end - start ) / 1000 +" seconds ");
        
    }
}

class Task2 implements Callable<Integer>{
    
	@Override
    public Integer call() throws Exception {
        
        Thread.sleep(3000);
        
        int sum = 0;
        
        for(int i=0;i<100;i++)
        	
            sum += i;
        
        return sum;
    
	}
}
