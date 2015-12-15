package proxy;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * Mock for the authentication...
 * 
 * @author shang yang
 *
 * @version
 *
 * @createTime: 2015年12月14日 下午4:16:34
 *
 */
public class AuthenticationHolder {
	
	static final String admin = "admin"; 
	
	static String currentUser;
	
	public static boolean isAdmin(){
		
		if( StringUtils.equals(admin, currentUser) ){
			
			return true;
		}
		
		return false;
		
	}
	
	public static void setUser(String username){
		
		currentUser = username;
	
	}
	
}
