package proxy.statics;

import proxy.RealSubject;
import proxy.Subject;

/**
 * 
 * @author shang yang
 *
 * @version
 *
 * @createTime: 2015年12月14日 下午2:42:45
 *
 */
public class SubjectStaticFactory {

	public static Subject getInstance() {
		
		return new ProxySubject(new RealSubject());
	
	}
}
