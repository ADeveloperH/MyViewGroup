package project.hjking.cn.myviewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2015/12/6 0006.
 */
public class MyScrollowView extends ViewGroup {

    private final String TAG = "MyScrollowView";
    private  GestureDetector gestureDetector;

    private  MyScroller scroller;

    private  int currentIndex = 0;//当前的页面下标
    public MyScrollowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyScrollowView(Context context) {
        super(context);
        init(context);
    }


    private void init(Context context) {

        //这里我们主要为了实现触摸滑动时View跟着滑动的效果。切换页面的动作没有在这里实现，直接写在onTouchEvent中
        gestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener(){
                    @Override
                    /**
                     *当手指在屏幕上移动时调用这个方法
                     * @param e1 : down事件的MotionEvent
                     * @param e2 : move事件的MotionEvent
                     * @param distanceX : X轴方向移动的距离
                     * @param distanceY : Y轴方向移动的距离
                     *
                     */
                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                        /**
                        *  移动View，是移动了，表示每次移动某个距离。不是移动到某处
                         * @param distanceX:x轴移动的距离。为正时表示向左移动，为负时向右移动
                         * @param distanceY:y轴移动的距离
                         *
                         */
                        scrollBy((int)distanceX,0);
                        Log.i(TAG, "distanceX :: " + distanceX);

                        /**
                         * 将当前的view移动到某个位置，某个点上去
                         */
//                        scrollTo((int)distanceX,(int)distanceY);


                        return true;//这个返回值没有什么用,是在onTouchEvent方法中调用时的返回值。我们没有接收这个返回值使用
                    }
                });

        scroller = new MyScroller(context);

    }


    /**
     * 测量的大小，与真实大小的区别
     * 测量的大小 是view自身通过onMesaure方法，计算的自身想要的大小
     * 真实大小 是父view指定大小，系统的控件在指定子view大小的时候，会参考子veiw的测量大小来指定
     * 只有当view.layout方法执行以后，该view才有真实的大小。
     */

    @Override
    /**
     * 系统测量当前控件时，调用此方法,
     * 如果当前控件，只是一个普通的view，只要测量自身的大小就可以了
     * 但，如果当前控件是一个viewGroup的话，除了测量自身大小外，
     * 还必须测量ViewGropu子view 的大小。
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        System.out.println("widthMeasureSpec::"+widthMeasureSpec);

        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        System.out.println("mode:"+mode);
        System.out.println("size:"+size);


        for (int i = 0; i < getChildCount(); i++) {
            //测量子控件的大小
            getChildAt(i).measure(widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    /**
     *
     * 当父view为我们指定位置时也是调用该方法
     * 当前自定义的类继承自ViewGroup，因此要调用该方法为当前View的子View指定位置
     *
     */
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

//        ImageView imageView = (ImageView) getChildAt(0);
//        //为imageView这个View设置固定的位置
//        imageView.layout(0,0,getWidth(),getHeight());

        /**
         * 为所有的子view指定位置
         */
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).layout(0 + i* getWidth(),0,getWidth() + i* getWidth(),getHeight());
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    private int downInterX;
    private int downInterY;
    @Override
    /**
     * 当需要中断事件时，处理此方法
     * 返回false 意思为不中断，按正常处理.事件会一层一层的向下传递给子View
     * 返回 true 意思是中断，由本控件处理。
     */
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = false;
        // 目的：让listView即能上下滑动，也能水平滑动切换页面
        // 思路：当listView上下滑动时，返回 false 不中断，按默认处理
        // 当在listView中水平滑动时，返回true ，中断事件，由本控件处理
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downInterX = (int) ev.getX();
                downInterY = (int) ev.getY();

                /**
                 * 解决listView 水平滑动跳跃的BUG，就是把down事件，传递给需要的值
                 * 否则，MyScrollowView收到的第一个事件会是move事件，因为down事件会向下传递给listview由
                 * listview消费了。同时gestureDetector里面也没有收到down事件
                 *
                 */
                downX = (int) ev.getX();
                gestureDetector.onTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                if(Math.abs(ev.getX() - downInterX) > Math.abs(ev.getY() - downInterY)){
                    if(Math.abs(ev.getX() - downInterX) > 15){
                        //当有一定的唯一了才给他判定为是水平的滑动事件
                        isIntercept = true;
                    }else{
                        //否则就不中断事件的传递
                        gestureDetector.onTouchEvent(ev);
                    }
                }else{
                    isIntercept = false;
                }
                break;
            case  MotionEvent.ACTION_POINTER_UP:
                break;

        }
        return isIntercept;
    }

    private int downX;//按下时x轴的坐标
    @Override
    /**
     * 处理触摸事件
     *
     * 这里我们直接由自己处理，直接返回true
     */
    public boolean onTouchEvent(MotionEvent event) {

        /**
         * 这个方法里面主要处理触摸的滑动事件。
         *
         */
        gestureDetector.onTouchEvent(event);
        /**
         * 根据down和up之间的距离来判断确定要移动到哪个页面
         *
         * 如果downX - upX > getWith()/2：表示移动到下一页
         * 如果upX - downX > getWith()/2: 表示移动到上一页
         * 否则：下标不变
         */
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG,"MotionEvent.ACTION_DOWN");
                downX = (int) event.getX();
                break;
            case  MotionEvent.ACTION_MOVE:
                Log.i(TAG,"MotionEvent.ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG,"MotionEvent.ACTION_UP");
                if(downX - (int)event.getX() > getWidth()/2){
                    //移动到下一页
                    currentIndex ++;
                }else if((int)event.getX() - downX > getWidth()/2){
                    //移动到上一页
                    currentIndex --;
                }

                moveToDestination(currentIndex);
                break;
        }
        return true;
    }

    /**
     *
     * 移动View到指定的页面
     * @param currentIndex 移动到的页面下标 从0开始
     */
    public void moveToDestination(int currentIndex) {
        if(currentIndex < 0){
            currentIndex = 0;
        }
        if(currentIndex > getChildCount() - 1){
            currentIndex = getChildCount() - 1;
        }

        this.currentIndex = currentIndex;
        /**
         *
         * 当要改变页面是调用回调方法。触发页面改变的监听
         */
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageChanged(currentIndex);
        }

        //移动到指定的页面
//        scrollTo(currentIndex * getWidth(),0);

        //x轴方向移动的距离 = 终点坐标 - 当前的坐标
        int distanceX = currentIndex * getWidth() - getScrollX();
        //getScrollX()获取当前的x轴方向的坐标
        scroller.startScroller(getScrollX(),0 ,distanceX ,0,Math.abs(distanceX));

        //方法一：利用handler来循环移动。每次移动一点
//        handler.sendEmptyMessage(99);

        //方法二：利用invalidate();方法会自动调用computeScroll()方法的特性来实现循环移动
        invalidate();
    }



//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            //查询MyScroller当前的位置，刷新页面
//            if (scroller.computeScrollOffeset()) {
//                int currX = (int) scroller.getCurrX();
//                scrollTo(currX,0);
//                handler.sendEmptyMessage(99);
//
//            }
//        }
//    };

    @Override
    public void computeScroll() {
        //查询MyScroller当前的位置，刷新页面
        if (scroller.computeScrollOffeset()) {
            int currX = (int) scroller.getCurrX();
            scrollTo(currX,0);
            invalidate();
        }
    }



    private IOnPageChangeListener onPageChangeListener;
    /**
     *
     * 用来监听页面改变的接口
     */
    public interface IOnPageChangeListener{
        /**
         *
         * 当页面改变时回调该方法。
         * @param index ： 要移动到的页面下标
         */
        void onPageChanged(int index);
    }

    public IOnPageChangeListener getOnPageChangeListener() {
        return onPageChangeListener;
    }

    public void setOnPageChangeListener(IOnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }
}
