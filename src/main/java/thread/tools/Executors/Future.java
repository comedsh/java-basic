package thread.tools.Executors;

import java.util.concurrent.Callable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Future {
	
    public static void main(String[] args) throws Exception{
        
    	ExecutorService executor = Executors.newCachedThreadPool();
        
        Task1 task = new Task1();
        
        java.util.concurrent.Future<Integer> future = executor.submit( task );
        
        // 初始化结束操作，意在不让新的进程创建了
        executor.shutdown();
         
        long start = System.currentTimeMillis();
        
        Integer r = future.get(); // 主线程一直阻塞并直到线程的结果返回。
        
        long end = System.currentTimeMillis();
        
        System.out.println("task result:"+r+"; totoally executed "+ ( end - start ) / 1000 +" seconds ");
        
    }
}

class Task1 implements Callable<Integer>{
	
    @Override
    public Integer call() throws Exception {
    	
        System.out.println("子线程在进行计算");
        
        Thread.sleep(3000);
        
        int sum = 0;
        
        for(int i=0;i<100;i++)
        	
            sum += i;
        
        return sum;
    }
}