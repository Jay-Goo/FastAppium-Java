package com.jaygoo.fastappium;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;


/**
 * ================================================
 * 作    者：JayGoo
 * 版    本：
 * 创建日期：2017/11/3
 * 描    述: 封装一些driver方法
 * ================================================
 */
public class FastAndroidDriver extends AndroidDriver {

    final Duration duration = Duration.ofSeconds(1);
    public int screenHeight;
    public int screenWidth;

    public FastAndroidDriver(URL remoteAddress, Capabilities desiredCapabilities) {
        super(remoteAddress, desiredCapabilities);
        screenWidth = manage().window().getSize().width;
        screenHeight = manage().window().getSize().height;
    }


    /**
     * 根据findElementByXPath(using)封装滚动List方法，完美支持滚动到底部
     * @param using
     * @return
     */
    public WebElement scrollElementByXPath(String using){
        String sPage = "";//滑动前当前页面
        String ePage = "";//滑动后当前页面
        do {
            sPage = getPageSource();
            swipeToUp();
            try {
                WebElement element = findElementByXPath(using);
                //如果没找到会抛出异常，就不会执行下面的代码，如果找到了，那么跳出滚动循环即可
                return element;
            }catch (Exception e){
            }
            ePage = getPageSource();
        }while (!sPage.equals(ePage));//如果两者相同则认为滑动到底部

        return null;
    }

    /**
     * 上划
     */
    public void swipeToUp() {
        TouchAction action = new TouchAction(this)
                .press(screenWidth / 2,screenHeight * 4 / 5)
                .waitAction(duration)
                .moveTo(screenWidth / 2, screenHeight / 4)
                .release();
        action.perform();
    }

    /**
     * 下划
     */
    public void swipeToDown() {
        TouchAction action = new TouchAction(this)
                .press(screenWidth / 2,screenHeight / 4)
                .waitAction(duration)
                .moveTo(screenWidth / 2, screenHeight * 3 / 4)
                .release();
        action.perform();
    }

    /**
     * 左划
     */
    public void swipeToLeft() {
        TouchAction action = new TouchAction(this)
                .press(screenWidth - 10, screenHeight / 2)
                .waitAction(duration)
                .moveTo(screenWidth / 4, screenHeight / 2)
                .release();
        action.perform();
    }

    /**
     * 右划
     */
    public void swipeToRight() {
        TouchAction action = new TouchAction(this)
                .press(10, screenHeight / 2)
                .waitAction(duration)
                .moveTo(screenWidth * 3 / 4 + 10, screenHeight / 2)
                .release();
        action.perform();
    }

    /**
     * 截屏，默认存储到和项目目录下ScreenShots文件夹中
     */
    public void snapShot(){
        snapShot("screenshot_" + System.currentTimeMillis() + ".png");
    }

    /**
     * 截屏，默认存储到和项目目录下ScreenShots文件夹中
     * @param filename
     */
    public void snapShot(String filename) {

        //当前项目目录
        String currentPath = System.getProperty("user.dir") +"/ScreenShots";
        File currentFileFolder = new File(currentPath);
        if (!currentFileFolder.exists()){
            currentFileFolder.mkdir();
        }

        File scrFile = this.getScreenshotAs(OutputType.FILE);
        try {
            System.out.println("save snapshot path is:" + currentPath + "/"
                    + filename);
            FileUtils
                    .copyFile(scrFile, new File(currentPath , filename));
        } catch (IOException e) {
            System.out.println("Can't save screenshot");
            e.printStackTrace();
        } finally {
            System.out.println("screen shot finished, it's in " + currentPath
                    + " folder");
        }
    }

}
