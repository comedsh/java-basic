package proxy.statics;

import proxy.Subject;

/**
 * 
 * @author shang yang
 *
 * @version
 *
 * @createTime: 2015年12月14日 下午2:40:24
 *
 */
public class ProxySubject implements Subject {

	private Subject delegate;

	public ProxySubject(Subject delegate) {
		this.delegate = delegate;
	}
	
	public void dealTask(String taskName) {
		
		long stime = System.currentTimeMillis();
		
		delegate.dealTask(taskName);
		
		long ftime = System.currentTimeMillis();
		
		System.out.println("Static Proxy 切入，执行任务耗时" + (ftime - stime) + "  ");
	
	}
}
