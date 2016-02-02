package thread.tools.CountDownLatch;

/**
 * 
 * Thread[].join -> 进程中调用，阻塞当前进程，直到线程组中的线程完全执行完毕以后，进程才能继续执行。
 * 
 * 
 * @author shang yang
 *
 * @version
 *
 * @createTime: 2016年2月2日 下午2:11:02
 *
 */

public class CountDownLatchThreadJoinTest {  
    
    private final static int GROUP_SIZE = 5;  
  
    public static void main(String []args) throws InterruptedException {
    	
        Thread[] threadGroup1 = new Thread[5];
        
        Thread[] threadGroup2 = new Thread[5];
        
        // Step 1: 初始化两个线程组
        for(int i = 0 ; i < GROUP_SIZE ; i++) {
        	
            final int num = i;
            
            threadGroup1[i] = new Thread() {
            	
                public void run() {
                	
                    int j = 0;
                    
                    while(j++ < 10) {
                    	
                        System.out.println("我是1号组线程：" + num + " 这个是我第：" + j + " 次运行！");
                        
                    }  
                    
                }  
                
            };  
            
            threadGroup2[i] = new Thread() {
            	
                public void run() {
                	
                    int j = 0;
                    
                    while(j++ < 10) {
                    	
                        System.out.println("我是2号组线程：" + num + " 这个是我第：" + j + " 次运行！");
                        
                    }  
                    
                }  
                
            };  
            
        }  
        
        // 让第一个线程组开始执行
        for(int i=0; i< GROUP_SIZE; i++){
        	
        	threadGroup1[i].start();
        	
        }
        
        // 主进程需要等待线程组中的所有进程执行完成以后，才能继续执行
        for(int i = 0 ; i < GROUP_SIZE ; i++) {
        	
            threadGroup1[i].join(); // 阻塞，知道所有线程执行完成后，才能继续执行。
            
        }  
        
        System.out.println("-==================>线程组1执行完了，该轮到俺了！");  
        
        for(int i = 0 ; i < GROUP_SIZE ; i++) {
        	
            threadGroup2[i].start();
            
        }  
        
        for(int i = 0 ; i < GROUP_SIZE ; i++) {
        	
            threadGroup2[i].join();  
        
        }  
        
        System.out.println("全部结束啦！哈哈，回家喝稀饭！");  
        
    }  
}  