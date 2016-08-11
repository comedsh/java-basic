package proxy.dynamic.javassist;

import java.io.File;
import java.lang.reflect.Constructor;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;

/**
 * 生成一个与 @see StationProxy 一模一样的 class 字节码，并动态加载入 Class Loader
 * 
 * 不过通过此案例，我怀疑，Javassist 使用了自定义的 Class Loader，因为我在本地有硬编码一个 {@linkplain StationProxy}，在运行时期，该 StationProxy 先于 Javassist 动态生成的 StationProxy 先加载。
 * 
 * 测试过程中，发现不是 javassist 自定义的 ClassLoader，那为什么 StationProxy 可以被重复加载？
 * 
 * @author 商洋
 *
 */
public class GenerateStationProxyMain {

	public static final String packagePath = "proxy.dynamic.javassist";
	
    public static void main(String[] args) throws Exception {  
    	
        createProxy();
        
     }  
       
     /* 
      * 手动创建字节码 
      */  
     @SuppressWarnings({ "unchecked", "rawtypes" })
	private static void createProxy() throws Exception{
    	 
         ClassPool pool = ClassPool.getDefault();  
   
         CtClass cc = pool.makeClass( packagePath + ".StationProxy");  
           
         //设置接口  
         CtClass interface1 = pool.get( packagePath + ".TicketService");  
         cc.setInterfaces(new CtClass[]{interface1});  
           
         //设置Field  
         CtField field = CtField.make("private " + packagePath + ".Station station;", cc );  
           
         cc.addField(field);  
           
         CtClass stationClass = pool.get( packagePath + ".Station");  
         CtClass[] arrays = new CtClass[]{stationClass};  
         CtConstructor ctc = CtNewConstructor.make(arrays,null,CtNewConstructor.PASS_NONE,null,null, cc);  
         //设置构造函数内部信息  
         ctc.setBody("{this.station=$1;}");  
         cc.addConstructor(ctc);  
   
         //创建收取手续 takeHandlingFee方法  
         CtMethod takeHandlingFee = CtMethod.make("private void takeHandlingFee() {}", cc);  
         takeHandlingFee.setBody("System.out.println(\"收取手续费，打印发票。。。。。\");");  
         cc.addMethod(takeHandlingFee);  
           
         //创建showAlertInfo 方法  
         CtMethod showInfo = CtMethod.make("private void showAlertInfo(String info) {}", cc);  
         showInfo.setBody("System.out.println($1);");  
         cc.addMethod(showInfo);  
           
         //sellTicket  
         CtMethod sellTicket = CtMethod.make("public void sellTicket(){}", cc);  
         sellTicket.setBody("{this.showAlertInfo(\"××××您正在使用车票代售点进行购票，每张票将会收取5元手续费！××××\");"  
                 + "station.sellTicket();"  
                 + "this.takeHandlingFee();"  
                 + "this.showAlertInfo(\"××××欢迎您的光临，再见！××××\");}");  
         cc.addMethod(sellTicket);  
           
         //添加inquire方法  
         CtMethod inquire = CtMethod.make("public void inquire() {}", cc);  
         inquire.setBody("{this.showAlertInfo(\"××××欢迎光临本代售点，问询服务不会收取任何费用，本问询信息仅供参考，具体信息以车站真实数据为准！××××\");"  
         + "station.inquire();"  
         + "this.showAlertInfo(\"××××欢迎您的光临，再见！××××\");}"  
         );  
         cc.addMethod(inquire);  
           
         //添加widthraw方法  
         CtMethod withdraw = CtMethod.make("public void withdraw() {}", cc);  
         withdraw.setBody("{this.showAlertInfo(\"××××欢迎光临本代售点，退票除了扣除票额的20%外，本代理处额外加收2元手续费！××××\");"  
                 + "station.withdraw();"  
                 + "this.takeHandlingFee();}"  
                 );  
         cc.addMethod(withdraw);  
           
         // 获取动态生成的 class  
         Class c = cc.toClass();
         
         // java.lang.LinkageError: loader (instance of  sun/misc/Launcher$AppClassLoader): attempted  duplicate class definition for name: "proxy/dynamic/javassist/StationProxy"
         // cc.toClass();
         
         //获取构造器  	
         Constructor constructor= c.getConstructor(Station.class);
         
         //通过构造器实例化，并强制转换为 TicketService 接口。
         TicketService o = (TicketService)constructor.newInstance(new Station());  
         
         o.inquire();  
           
         cc.writeFile( new File(".").getCanonicalPath() + "/target/classes/" );  // 将会覆盖原有的 class 字节码
     }  	
	
	
}
