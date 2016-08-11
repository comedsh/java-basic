package scheduler;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class TimerSchedulerTest {
  
    // 2000 毫秒以后执行，且只一次
	@Test
    public void testScheduler1() throws InterruptedException {  
    	
        Timer timer = new Timer();
        
        timer.schedule(new TimerTask() {
        	
            public void run() {
            	
                System.out.println("-------设定要指定任务--------");
                
            }  
            
        }, 2000);// 设定指定的时间time,此处为2000毫秒
        
        // 如果在 main 启动函数中执行，不需要下面这段休眠的代码。Junit 在执行完方法以后，就会对方法 destroy，与之相关联的线程也会被杀死。
        // 所以这里写了这么一段循环以保证该方法存活。
        
        while( true ){
        	
        	TimeUnit.MILLISECONDS.sleep(500);
        	
        }
        
    }  
  
	
	/**
	 * 1000 ms 后开始执行，后续每隔 1000 ms 执行一次，是相对于上次完成后的的时间开始计算间隔时间。
	 *
	 * 并且，保证，下次执行一定是在上次执行完毕以后才开始执行。This is what I current need.
	 * 
	 */
	static int i = 0;
	static int n = 0;
    @Test
    public void testScheduler2() throws InterruptedException {  
    	
        Timer timer = new Timer();
        
        timer.schedule( new TimerTask() {
        	
            public void run() {
            		
        		System.out.println( i++ );
        		
        		try{
        			
        			while(true){
        				
        				if( n++ >= 5 ) break; // 第一个线程休眠 5 秒钟
        				
        				TimeUnit.MILLISECONDS.sleep(500);
        				
        			}
        			
        		}catch(Exception e){
        			
        		}
        	}
                
            
        }, 1000, 1000);  

        // 如果在 main 启动函数中执行，不需要下面这段休眠的代码。Junit 在执行完方法以后，就会对方法 destroy，与之相关联的线程也会被杀死。
        // 所以这里写了这么一段循环以保证该方法存活。
        
        while( true ){
        	
        	TimeUnit.MILLISECONDS.sleep(500);
        	
        }
        
    }  
  
    // 第三种方法：设定指定任务task在指定延迟delay后进行固定频率peroid的执行。  
    // scheduleAtFixedRate(TimerTask task, long delay, long period)  
    public static void timer3() throws InterruptedException {
    	
        Timer timer = new Timer();
        
        timer.scheduleAtFixedRate(new TimerTask() {
        	
            public void run() {
            	
                System.out.println("-------设定要指定任务--------");
                
            }  
            
        }, 1000, 2000);  
        
        // 如果在 main 启动函数中执行，不需要下面这段休眠的代码。Junit 在执行完方法以后，就会对方法 destroy，与之相关联的线程也会被杀死。
        // 所以这里写了这么一段循环以保证该方法存活。
        
        while( true ){
        	
        	TimeUnit.MILLISECONDS.sleep(500);
        	
        }        
    }  
     
    // 第四种方法：安排指定的任务task在指定的时间firstTime开始进行重复的固定速率period执行．  
    // Timer.scheduleAtFixedRate(TimerTask task,Date firstTime,long period)  
    public static void timer4() throws InterruptedException {  
    	
        Calendar calendar = Calendar.getInstance();  
        calendar.set(Calendar.HOUR_OF_DAY, 12); // 控制时  
        calendar.set(Calendar.MINUTE, 0);       // 控制分  
        calendar.set(Calendar.SECOND, 0);       // 控制秒  
  
        Date time = calendar.getTime();         // 得出执行任务的时间,此处为今天的12：00：00  
  
        Timer timer = new Timer();
        
        timer.scheduleAtFixedRate(new TimerTask() {
        	
            public void run() {
            	
                System.out.println("-------设定要指定任务--------");
                
            }  
            
        }, time, 1000 * 60 * 60 * 24);// 这里设定将延时每天固定执行  

        // 如果在 main 启动函数中执行，不需要下面这段休眠的代码。Junit 在执行完方法以后，就会对方法 destroy，与之相关联的线程也会被杀死。
        // 所以这里写了这么一段循环以保证该方法存活。
        
        while( true ){
        	
        	TimeUnit.MILLISECONDS.sleep(500);
        	
        }             
        
    }  
}  
