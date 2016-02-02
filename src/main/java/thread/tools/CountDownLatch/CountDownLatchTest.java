package thread.tools.CountDownLatch;

import java.util.concurrent.CountDownLatch;  

/**
 * 
 * CountDownLatch 门闩，线程先阻塞 (await)，直到 ( countDown 到 0 )，那么所有等待的线程就一窝蜂的全部开始执行。
 * 1. 初始化 门闩 的数量
 * 2. await()，阻塞线程
 * 3. countDown()，减门闩，直到门闩的数量为0，那么，#2中被阻塞的线程开始一窝蜂的执行。
 * 
 * 输出日志如下，
 * 
 *  ==========================>
	分组：分组1比赛开始：
	我是线程组：【分组1】,第：0 号线程,我已经准备就绪！
	我是线程组：【分组1】,第：1 号线程,我已经准备就绪！
	我是线程组：【分组1】,第：2 号线程,我已经准备就绪！
	我是线程组：【分组1】,第：3 号线程,我已经准备就绪！
	我是线程组：【分组1】,第：4 号线程,我已经准备就绪！
	各就各位，预备！
	我是线程组：【分组1】,第：0 号线程,我已执行完成！
	我是线程组：【分组1】,第：3 号线程,我已执行完成！
	我是线程组：【分组1】,第：4 号线程,我已执行完成！
	我是线程组：【分组1】,第：2 号线程,我已执行完成！
	我是线程组：【分组1】,第：1 号线程,我已执行完成！
	分组：分组1比赛结束！
	
	==========================>
	分组：分组2比赛开始：
	我是线程组：【分组2】,第：0 号线程,我已经准备就绪！
	我是线程组：【分组2】,第：1 号线程,我已经准备就绪！
	我是线程组：【分组2】,第：2 号线程,我已经准备就绪！
	我是线程组：【分组2】,第：3 号线程,我已经准备就绪！
	我是线程组：【分组2】,第：4 号线程,我已经准备就绪！
	各就各位，预备！
	我是线程组：【分组2】,第：0 号线程,我已执行完成！
	我是线程组：【分组2】,第：3 号线程,我已执行完成！
	我是线程组：【分组2】,第：4 号线程,我已执行完成！
	我是线程组：【分组2】,第：2 号线程,我已执行完成！
	我是线程组：【分组2】,第：1 号线程,我已执行完成！
	分组：分组2比赛结束！
 * 
 * 
 * @author shang yang
 *
 * @version
 *
 * @createTime: 2016年2月2日 上午11:18:49
 *
 */
public class CountDownLatchTest {  
      
    private final static int GROUP_SIZE = 5;  
      
    public static void main(String []args) throws Exception{  
    	
        processOneGroup("分组1");
        
        processOneGroup("分组2");
        
    }  
      
    private static void processOneGroup(final String groupName) throws Exception{
    	
        final CountDownLatch start_count_down = new CountDownLatch(1);
        
        final CountDownLatch end_count_down = new CountDownLatch( GROUP_SIZE );
        
        System.out.println("==========================>\n分组：" + groupName + "比赛开始：");
        
        for(int i = 0 ; i < GROUP_SIZE ; i++) {
        	
            new Thread( String.valueOf(i) ) {
            	
                public void run() {
                	
                    System.out.println("我是线程组：【" + groupName + "】,第：" + this.getName() + " 号线程,我已经准备就绪！");
                    
                    try {
                    	
                        start_count_down.await(); // 等待开始指令发出即：start_count_down.countDown();
                        
                    } catch (InterruptedException e) {
                    	
                        e.printStackTrace();
                        
                    }  
                    
                    System.out.println("我是线程组：【" + groupName + "】,第：" + this.getName() + " 号线程,我已执行完成！");
                    
                    end_count_down.countDown();
                    
                }  
                
            }.start();  
        }  
        
        Thread.sleep( 1000 ); // 必须等待所有线程处于 start_count_down.await 的状态，实际中，必须等待所有的线程初始化完毕，然后一起开始执行。
        
        System.out.println("各就各位，预备！");
        
        start_count_down.countDown(); // 开始赛跑.
        
        end_count_down.await(); // 阻塞主进程，等待多个赛跑者逐个结束.
        
        System.out.println("分组：" + groupName + "比赛结束！");
        
    }  
    
}  
