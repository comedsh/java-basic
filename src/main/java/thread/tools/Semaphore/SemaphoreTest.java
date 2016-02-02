package thread.tools.Semaphore;

import java.util.Date;
import java.util.Random;

/**
 * 
 *  Semaphore - 信号量
 * 
 *  这里是简单模拟并发100个线程去访问一段程序，此时要控制最多同时运行的是10个，通过信号量 Semaphore 实现。
 *  
 *  是保证，同一时间，最多有 10 个线程在执行。
 *  
 *  1. 如果有一个线程的信号量 reliease，那么其他线程就可以开始执行。 
 *  
 *  线程执行完后，就有线程获得了，没释放是获取不到的，内部实现方面;
 * 
 */
import java.util.concurrent.Semaphore;

import common.DateTimeUtil;  
  
  public class SemaphoreTest {  
      
	  private final static Semaphore MAX_SEMA_PHORE = new Semaphore(10);  
   
	  
      public static void main(String []args) {  
      
    	  for(int i = 0 ; i < 100 ; i++) {  
          
    		  	final int num = i;  
                
    		  	final Random random = new Random();  
                
    		  	new Thread() {  
                
    		  		public void run() {  
                    
    		  			boolean acquired = false;  
                        
    		  			try {  
                        
    		  				MAX_SEMA_PHORE.acquire();  
                            
    		  				acquired = true;  
                            
    		  				System.out.println("我是线程：" + num + " 我获得了使用权！" + DateTimeUtil.format( new Date() ) );  
                            
    		  				long time = 1000 * Math.max(1, Math.abs(random.nextInt() % 10));  
                            
    		  				Thread.sleep(time);  
                            
    		  				System.out.println("我是线程：" + num + " 我执行完了！" + DateTimeUtil.format( new Date() ) );  
                         
    		  			}catch(Exception e) {  
                        
    		  				e.printStackTrace();  
                         
    		  			}finally {  
                        
    		  				if(acquired) {  
                            
    		  					MAX_SEMA_PHORE.release();  
                              
    		  				}  
                         
    		  			}  
                      
    		  		}  
                
    		  	}.start();  
           
    	  }  
      
      }  
  
  }  