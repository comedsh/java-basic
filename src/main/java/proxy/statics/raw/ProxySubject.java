package proxy.statics.raw;

import proxy.AuthenticationHolder;
import proxy.ISubject;

/**
 * 
 * @author shang yang
 *
 * @version
 *
 * @createTime: 2015年12月14日 下午2:40:24
 *
 */
public class ProxySubject implements ISubject {

	private ISubject source;

	public ProxySubject(ISubject source) {
		
		this.source = source;
		
	}
	
	public void dealTask(String taskName) throws Exception{
		
		// 性能监控，每个方法都需要手动加入。 
		
		long stime = System.currentTimeMillis();
		
		source.dealTask(taskName);
		
		long ftime = System.currentTimeMillis();
		
		System.out.println("Static Proxy 切入，执行任务耗时 " + (ftime - stime) + " ms ");
	
	}

	public void authorizedCall() throws Exception{
		
		// 手动切入权限验证，如果所有方法都需要切入权限验证，则每个方法都需要手动的切入。

		long stime = System.currentTimeMillis();
		
		if( AuthenticationHolder.isAdmin() ){

			source.authorizedCall();
			
		}else{
			
			System.out.println("you don't have the permission to call authorizedCall();");
			
		}
		
		long ftime = System.currentTimeMillis();
		
		System.out.println("Static Proxy 切入，执行任务耗时 " + (ftime - stime) + " ms");		
	}
}
