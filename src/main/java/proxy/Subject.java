package proxy;

import proxy.dynamic.jdk.Authorized;

/**
 * 
 * @author shang yang
 *
 * @version
 *
 * @createTime: 2015年12月14日 下午2:34:15
 *
 */
public interface Subject {
	
	public void dealTask(String taskName) throws Exception;
	
	/**
	 * Special case added for cglib case for method filter.
	 * 
	 * @author shang yang
	 *
	 * @version
	 *
	 * @createTime: 2015年12月14日 下午3:59:14
	 *
	 */
	@Authorized // annotation for Dynamic Proxy
	public void authorizedCall() throws Exception;
	
}
