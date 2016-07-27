package proxy.dynamic.jdk;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 
 * Markable annotation means which method needs authentication check.
 * 
 * @author shang yang
 *
 * @version
 *
 * @createTime: 2015年12月14日 下午4:30:26
 *
 */
@Retention(RetentionPolicy.RUNTIME) // indicates this annotation can be reached during Runtime.
public @interface Authorized {

}
