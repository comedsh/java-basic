package proxy;

/**
 * 
 * @author shang yang
 *
 * @version
 *
 * @createTime: 2015年12月14日 下午2:38:16
 *
 */
public class SubjectImpl implements Subject{
	
	public void dealTask(String taskName) {
		System.out.println("正在执行 dealTask 任务" + taskName);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void authorizedCall() {
		
		System.out.println("正在执行任务 authorizedCall");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
