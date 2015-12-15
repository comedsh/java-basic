package proxy;

import proxy.dynamic.Authorized;

/**
 * 
 * 申明接口 - 通过接口实现静态代理
 * 
 * @author shang yang
 *
 * @version
 *
 * @createTime: 2015年12月14日 下午2:34:15
 *
 */
public interface Subject {
	
	public void dealTask(String taskName);
	
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
	public void authorizedCall();
	
}
