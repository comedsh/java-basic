package thread.basic.interrupt;

/**
 * 
 * 试图中断运行状态中的线程 失败。
 * 
 * @author shang yang
 *
 * @version
 *
 * @createTime: 2016年2月3日 下午2:11:24
 *
 */
public class Case2 {
    
    public static void main(String[] args) throws Exception  {
    	
    	Case2 test = new Case2();
    	
        MyThread thread = test.new MyThread();
       
        thread.start();
        
        Thread.sleep( 200 );
        
        thread.interrupt(); // 视图中断线程的执行。但是失败。-> 直接试图中断一个正在运行过程中的线程，是不现实的。
    } 
     
    class MyThread extends Thread{
    	
        @Override
        public void run() {
        	
            int i = 0;
            
            while(i<Integer.MAX_VALUE){
            	
                System.out.println(i+" while循环");
                
                i++;
                
            }
            
        }
    }
}
