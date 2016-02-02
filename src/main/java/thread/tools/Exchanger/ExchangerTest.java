package thread.tools.Exchanger;

import java.util.concurrent.Exchanger;  

/**
 * 随机交换，使用得非常少
 * 
 * 从输出的日志可以看到，thread 0 和 3 相互交换；thread 6 和 7 相互交换。
 * 
 * 我是线程：Thread_Thread-0我原先的数据为：0 , 交换后的数据为：3
 * 我是线程：Thread_Thread-3我原先的数据为：3 , 交换后的数据为：0
 * 我是线程：Thread_Thread-7我原先的数据为：7 , 交换后的数据为：6
 * 我是线程：Thread_Thread-6我原先的数据为：6 , 交换后的数据为：7
 * 
 * 
 * @author shang yang
 *
 * @version
 *
 * @createTime: 2016年2月2日 上午10:59:08
 *
 */
public class ExchangerTest {  
      
    public static void main(String []args) {  
    	
        final Exchanger <Integer>exchanger = new Exchanger<Integer>();
        
        for(int i = 0 ; i < 10 ; i++) {
        	
            final Integer num = i;
            
            new Thread() {
            	
                public void run() {
                	
                    System.out.println("我是线程：Thread_" + this.getName() + "我的数据是：" + num);
                    
                    try {
                    	
                        Integer exchangeNum = exchanger.exchange(num);
                        
                        Thread.sleep(1000); // 等待所有的线程都准备好了 exchange.
                        
                        System.out.println("我是线程：Thread_" + this.getName() + "我原先的数据为：" + num + " , 交换后的数据为：" + exchangeNum);
                        
                    } catch (InterruptedException e) {
                    	
                        e.printStackTrace();
                        
                    }  
                }  
                
            }.start();
            
        }  
    }  
}  