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
public class SubjectImpl implements ISubject{
	
	public void dealTask(String taskName) throws Exception{
		
		System.out.println("正在执行 dealTask 任务" + taskName);
		
		Thread.sleep( 500 );
		
	}
	
	public void authorizedCall() throws Exception{
		
		System.out.println("正在执行任务 authorizedCall");
		
		Thread.sleep( 500 );

	}

}
