package cn.wx.utils;

import java.util.ArrayList;
import java.util.List;

import org.weixin4j.Weixin;
import org.weixin4j.component.MenuComponent;
import org.weixin4j.model.menu.ClickButton;
import org.weixin4j.model.menu.Menu;
import org.weixin4j.model.menu.ScancodeWaitMsgButton;
import org.weixin4j.model.menu.SingleButton;
import org.weixin4j.model.menu.ViewButton;

public class MenuUtil {
	
	//初始化weixin对象
	static Weixin weixin = new Weixin();
	//获取菜单组件
    static MenuComponent menuComponet = weixin.menu();
    
    public static void main(String[] args) {
    	menuCreate();
	}
	
	public static boolean menuCreate(){
		try{
			Menu menu = new Menu();
			List<SingleButton> buttons = new ArrayList<SingleButton>();
			ViewButton button1 = new ViewButton("公司内部", "http://www.nowlv.com/NN/html/login.html");
			ScancodeWaitMsgButton button2 = new ScancodeWaitMsgButton("用户绑定", "WXbangdin");
			ClickButton button3 = new ClickButton("测试", "test");
			
			ViewButton button3_1 = new ViewButton("搜索", "http://www.soso.com/");
			button3.getSubButton().add(button3_1);
			ClickButton button3_2 = new ClickButton("测试1", "test1");
			button3.getSubButton().add(button3_2);
			
			buttons.add(button1);
			buttons.add(button2);
			buttons.add(button3);
			
			menu.setButton(buttons);

            //调用微信自定义菜单组件，创建自定义菜单
            menuComponet.create(menu);
	        
            return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
}