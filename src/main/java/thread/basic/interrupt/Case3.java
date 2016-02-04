package thread.basic.interrupt;

/**
 * 
 * 通过配合标志 isInterrupted() 作为 while 循环的判断条件，故而可以中断。
 * 
 * @author shang yang
 *
 * @version
 *
 * @createTime: 2016年2月3日 下午2:17:29
 *
 */
public class Case3 {
    
    public static void main(String[] args) throws Exception  {
        
    	Case3 test = new Case3();
        
        MyThread thread = test.new MyThread();
        
        thread.start();
        
        Thread.sleep(200);
        
        thread.interrupt(); // 试图中断正在执行的线程，这里，因为通过 isInterrupted() 作为 while 循环的判断条件，故而可以中断。
    } 
     
    class MyThread extends Thread{
    	
        @Override
        public void run() {
        	
            int i = 0;
            
            while(!isInterrupted() && i<Integer.MAX_VALUE){
            	
                System.out.println(i+" while循环");
                
                i++;
                
            }
            
        }
        
    }
}
