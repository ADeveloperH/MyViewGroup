package project.hjking.cn.myviewgroup;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by Administrator on 2015/12/7 0007.
 */
public class MyScroller {

    private int startX;
    private int startY;
    private int distanceY;
    private int distanceX;
    private int total_time;//移动过程的总的时间
    private long start_time;//开始移动的时间

    private  boolean isFinish = false;//判断当前的运动是否完成，默认是没有完成
    private long currX;//移动过程中当前的x轴距离
    private long currY;//移动过程中当前的y轴距离

    public MyScroller(Context context){

    }

    /**
     * @param startX：开始移动的x轴距离
     * @param startY：开始移动的y轴的距离
     * @param distanceX：x轴方向移动的距离
     * @param distanceY：y轴方向移动的距离
     */
    public void startScroller(int startX,int startY,int distanceX,int distanceY){
        startScroller(startX,startY,distanceX,distanceY,2000);
    }

    /**
     *
     * @param startX:开始移动的x轴距离
     * @param startY:开始移动的y轴距离
     * @param distanceX：x轴方向移动的距离
     * @param distanceY：y轴方向移动的距离
     * @param total_time:移动的总时间
     */
    public void startScroller(int startX, int startY, int distanceX, int distanceY, int total_time) {
        this.startX = startX;
        this.startY = startY;
        this.distanceX = distanceX;
        this.distanceY = distanceY;
        this.total_time = total_time;

        this.start_time = SystemClock.uptimeMillis();//获取当前的时间

        isFinish = false;
    }


    /**
     *计算当前的位移量，并返回结果
     * @return true表示还在运动 false表示动画已经结束
     */
    public boolean computeScrollOffeset(){
        if (isFinish) {
            return false;
        }

        long passTime = SystemClock.uptimeMillis() - start_time;//已经消耗的时间
        if(passTime < total_time){
            //还在运动
            // s = v*t; 位移 = 速度* 时间
            long disX = distanceX * passTime / total_time;
            this.currX = startX + disX;//当前x轴位置

            long disY = distanceY * passTime / total_time;
            this.currY = startY + disY;//当前y轴位置

        }else{
            Log.i("aaaaaa", "完成动画");
            //时间到了，运动应该结束了。当前的坐标为终点坐标
            this.currX = startX + distanceX;
            this.currY = startY + distanceY;
            isFinish = true;
        }
        return  true;
    }

    public long getCurrX() {
        return currX;
    }

    public long getCurrY() {
        return currY;
    }
}
