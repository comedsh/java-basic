package thread.tools.CyclicBarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;  
  
/**
 * 
 * CyclicBarrier: 关卡模式，当所有线程准备好了以后，做一件由 CyclicBarrier 定义的事情。1) 指定需要准备线程的数量，2) 定义要做的事情 Runnable.
 * 
 * 
 * @author shang yang
 *
 * @version
 *
 * @createTime: 2016年2月2日 上午11:01:36
 *
 */
public class CyclicBarrierTest {  
      
    private static final int THREAD_COUNT = 10;  
    
    // 定义 Barrier
    private final static CyclicBarrier CYCLIC_BARRIER = new CyclicBarrier(THREAD_COUNT  ,  
    		
        new Runnable() {
    	
            public void run() {
            	
            	/**
            	 * 当所有的线程准备好以后，共同做一件事情。这里模拟的便是导游开始发号司令。
            	 */
                System.out.println("======>我是导游，本次点名结束，准备走下一个环节!");
                
            }  
            
        }  
    
    );  
      
    public static void main(String []args) throws InterruptedException, BrokenBarrierException {  
    	
        for(int i = 0 ; i < 10 ; i++) {
        	
            new Thread(String.valueOf(i)) {
            	
                public void run() {
                	
                    try {  
                    	
                        System.out.println("我是线程：" + this.getName() + " 我们达到旅游地点！");
                        
                        CYCLIC_BARRIER.await(); // (阻塞)等待所有线程( THREAD_COUNT指定的数目 ) 到达此处。齐备后，做一件由 CyclicBarrier 定义的事情。
                        
                        System.out.println("我是线程：" + this.getName() + " 我开始骑车！");
                        
                        CYCLIC_BARRIER.await();
                        
                        System.out.println("我是线程：" + this.getName() + " 我们开始爬山！");
                        
                        CYCLIC_BARRIER.await();
                        
                        System.out.println("我是线程：" + this.getName() + " 我们回宾馆休息！");
                        
                        CYCLIC_BARRIER.await();
                        
                        System.out.println("我是线程：" + this.getName() + " 我们开始乘车回家！");
                        
                        CYCLIC_BARRIER.await();
                        
                        System.out.println("我是线程：" + this.getName() + " 我们到家了！");
                        
                    } catch (InterruptedException e) {  
                    	
                        e.printStackTrace();
                        
                    } catch (BrokenBarrierException e) {
                    	
                        e.printStackTrace();
                        
                    }  
                    
                }  
                
            }.start();
            
        }  
        
    }  
    
}  