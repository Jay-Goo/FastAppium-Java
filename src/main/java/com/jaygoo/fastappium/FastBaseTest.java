package com.jaygoo.fastappium;

import java.io.File;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * ================================================
 * 作    者：JayGoo
 * 版    本：
 * 创建日期：2017/11/2
 * 描    述:
 * ================================================
 *
 *

 —————————————————————————————————————— Appium常用属性文档 ———————————————————————————

 DesiredCapabilities属性：

 cap.setCapability("browserName", ""); // web 浏览器名称（'Safari' ,'Chrome'等）。如果对应用进行自动化测试，这个关键字的值应为空。
 cap.setCapability("platformName", "Android");//你要测试的手机操作系统
 cap.setCapability("platformVersion", "4.4");//手机操作系统版本
 cap.setCapability("automationName", "Appium");  //你想使用的自动化测试引擎：Appium (默认) 或 Selendroid
 cap.setCapability("deviceName", " Android Emulator"); //使用的手机类型或模拟器类型，真机时输入Android Emulator或者根据adb device获取设备序列号
 cap.setCapability("udid", udid); //连接的物理设备的唯一设备标识,Android可以不设置
 cap.setCapability("newCommandTimeout", "300");  //设置收到下一条命令的超时时间,超时appium会自动关闭session ,默认60秒
 cap.setCapability("unicodeKeyboard", "True");//支持中文输入，会自动安装Unicode 输入法。默认值为 false
 cap.setCapability("resetKeyboard", "True"); //在设定了 unicodeKeyboard 关键字的 Unicode 测试结束后，重置输入法到原有状态
 cap.setCapability("'app'", "D:\\AndroidAutomation\\AndroidAutoTest\\app\\zhongchou.apk");  //未安装应用时，设置app的路径,已安装app，直接从手机启动app，上面路径不设置
 cap.setCapability("appPackage", "com.smartstudy.smartmark");  //应用的包名
 cap.setCapability("appActivity", "com.smartstudy.smartmark.control.activity.common.SplashActivity");  //Activity包名路径
 cap.setCapability("appWaitActivity", "com.smartstudy.smartmark.control.activity.common.SplashActivity");  //你想要等待启动的Android Activity名称|比如`SplashActivity`|

 ————————————————————————————————————————————————————————————————————————————————————
 Driver 相关方法：

 driver.hideKeyboard();//隐藏键盘
 driver.backgroundApp(60);//60秒后把当前应用放到后台去
 driver.lockDevice(3); //锁定屏幕

 //在当前应用中打开一个 activity 或者启动一个新应用并打开一个 activity
 driver.StartActivity("com.iwobanas.screenrecorder.pro", "com.iwobanas.screenrecorder.RecorderActivity");
 driver.OpenNotifications();//打开下拉通知栏 只能在 Android 上使用
 driver.IsAppInstalled("com.example.android.apis-");//检查应用是否已经安装
 driver.InstallApp("path/to/my.apk");//安装应用到设备中去
 driver.RemoveApp("com.example.android.apis");//从设备中删除一个应用
 driver.ShakeDevice();//模拟设备摇晃
 driver.CloseApp();//关闭应用
 driver.LaunchApp();//根据服务关键字 (desired capabilities) 启动会话 (session) 。请注意这必须在设定 autoLaunch=false 关键字时才能生效。这不是用于启动指定的 app/activities
 driver.ResetApp();//应用重置
 driver.GetContexts();//列出所有的可用上下文
 driver.GetContext();//列出当前上下文
 driver.SetContext("name");//将上下文切换到默认上下文
 driver.GetAppStrings();//获取应用的字符串
 driver.KeyEvent(176);//给设备发送一个按键事件:keycode
 driver.GetCurrentActivity();//获取当前 activity。只能在 Android 上使用
 //driver.Pinch(25, 25);//捏屏幕 (双指往内移动来缩小屏幕)
 //driver.Zoom(100, 200);//放大屏幕 (双指往外移动来放大屏幕)
 driver.PullFile("Library/AddressBook/AddressBook.sqlitedb");//从设备中拉出文件
 driver.PushFile("/data/local/tmp/file.txt", "some data for the file");//推送文件到设备中去

 driver.FindElement(By.Name(""));
 driver.FindElementById("id");
 driver.FindElementByName("text");
 driver.FindElementByXPath("//*[@name='62']");
 */
public abstract class FastBaseTest {

    //Driver
    public static FastAndroidDriver driver;
    public WebDriverWait driverWait;
    public String appPackage;
    public String appActivity;
    public String appName;
    public String appDirName;

    public abstract String setAppName();

    public abstract String setAppPackage();

    public abstract String setAppActivity();

    public abstract String setDriverServerUrl();

    public String setAppDirName(){
        File classpathRoot = new File(System.getProperty("user.dir"));
        //app的目录
        return new File(classpathRoot, "apps/").getPath();
    }

    public boolean resetAppWhenInstall() {
        return false;
    }

    public boolean setSupportChinese() {
        return true;
    }

    public void setCapabilities(DesiredCapabilities capabilities, File appFile){

        //设置要调试的模拟器的名字
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Android Emulator");
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("newCommandTimeout", "300");
        //设置app的路径,如果设置那么每次都会都会安装app，如果不设置则直接启动app
        if (appFile != null) {
            capabilities.setCapability("app", new File(setAppDirName(),
                    setAppName()).getAbsolutePath());
        }

        //设置app的包名
        capabilities.setCapability("appPackage", appPackage);
        //设置app的启动activity
        capabilities.setCapability("appActivity", appActivity);

        //True时每次安装不会清空数据
        if (!resetAppWhenInstall()) {
            capabilities.setCapability("noReset", "True");
        }

        if (setSupportChinese()) {
            //支持中文输入
            capabilities.setCapability("unicodeKeyboard", "True");
            capabilities.setCapability("resetKeyboard", "True");
        }

    }


    /**
     * 配置启动driver
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        //app的目录
        appDirName = setAppDirName();
        System.out.print(appDirName);
        //app的名字，对应你apps目录下的文件
        appName = setAppName();
        File appFile = null;
        if (appDirName != null && appName != null) {
            File appDir = new File(setAppDirName());
            if (! appDir.exists()) {
                appDir.mkdir();
            }
            appFile = new File(appDir, setAppName());
        }
        appPackage = setAppPackage();
        appActivity = setAppActivity();
        //创建Capabilities
        DesiredCapabilities capabilities = new DesiredCapabilities();
        setCapabilities(capabilities,appFile);

        //启动driver
        driver = new FastAndroidDriver(new URL(setDriverServerUrl()), capabilities);
        driverWait = new WebDriverWait(driver, 10);
    }

    @After
    public void tearDown() throws Exception {
        //测试完毕，关闭driver，不关闭将会导致会话还存在，下次启动就会报错
        if (driver != null) {
            driver.quit();
        }
    }


    /**
     * 强制睡眠
     * @param second
     */
    public void sleep(float second) {
        try {
            Thread.sleep((int)(second * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
