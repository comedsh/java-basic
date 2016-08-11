package proxy.statics.raw;

import proxy.Subject;
import proxy.SubjectImpl;

/**
 * 
 * @author shang yang
 *
 * @version
 *
 * @createTime: 2015年12月14日 下午2:42:45
 *
 */
public class StaticProxyFactory {

	public static Subject getInstance() {
		
		return new ProxySubject(new SubjectImpl());
	
	}
}
