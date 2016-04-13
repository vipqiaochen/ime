package com.imatrixmob.ffime.popup;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.imatrixmob.ffime.R;
import com.imatrixmob.ffime.activity.SettingsActivity;
import com.imatrixmob.ffime.core.OpenWnn;
import com.imatrixmob.ffime.core.TextCandidatesViewManager;
import com.imatrixmob.ffime.utils.ScreenUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * @author zhouwumin 功能描述：点击工具栏中的设置按钮（继承自PopupWindow）
 */
@SuppressLint("NewApi")
public class TitlePopup extends PopupWindow
{
    private OpenWnn mContext;
    
    public static AudioManager am;
    
    // 实例化一个矩形
    private Rect mRect = new Rect();
    
    // 坐标的位置（x、y）
    private final int[] mLocation = new int[2];
    
    // 屏幕的宽度和高度
    private int mScreenWidth, mScreenHeight;
    
    // 判断是否需要添加或更新列表子类项
    private boolean mIsDirty;
    
    // 位置不在中心
    private int popupGravity = Gravity.NO_GRAVITY;
    
    // 弹窗子类项选中时的监听
    private OnItemOnClickListener mItemOnClickListener;
    
    // 定义列表对象
    private static ImageView iv_auto, iv_key, iv_other;
    
    private static LinearLayout mLinear_auto, mLinear_key, mLinear_other;
    
    // 定义弹窗子类项列表
    private ArrayList<ActionItem> mActionItems = new ArrayList<ActionItem>();
    
    /** 媒体播放器 */
    private MediaPlayer mSound = null;
    
    /** 记录点击的item */
    private SharedPreferences preference;
    
    private SharedPreferences settings;
    
    /** State of the shift key */
    protected int mShiftOn = 0;
    
    /** 是否首字母是否大写 */
    private boolean mAutoCaps = false;
    
    /** 是否有按键音 */
    private boolean key_sound;
    
    /** 是否自动大写 */
    private boolean auto_caps;

	protected String UMENG_AUTO_CAPS = "AUTO_CAPS";
    
    /***/
    public TitlePopup(Context context)
    {
        // 设置布局的参数
        this(context, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        this.mContext = (OpenWnn)context;
        am = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
        
    }
    
    public TitlePopup(Context context, int width, int heigh)
    {
        this.mContext = (OpenWnn)context;
        // 设置可以获得焦点
        setFocusable(false);
        // 设置弹窗内可点击
        setTouchable(true);
        // 设置弹窗外可点击
        setOutsideTouchable(true);
        
        // 获得屏幕的宽度和高度
        mScreenWidth = ScreenUtil.getScreenWidth(mContext);
        mScreenHeight = ScreenUtil.getScreenHeight(mContext);
        
        // 设置弹窗的宽度和高度
        setWidth(width);
        setHeight(heigh);
        setBackgroundDrawable(new BitmapDrawable());
        
        am = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
        // 设置弹窗的布局界面
        setContentView(LayoutInflater.from(mContext).inflate(R.layout.title_popup, null));
        settings = getContentView().getContext().getSharedPreferences("key_sound", 0);
        preference = getContentView().getContext().getSharedPreferences("auto_caps", 0);
        key_sound = settings.getBoolean("key_sound", false);
        auto_caps = preference.getBoolean("auto_caps", true);
        initUI();
        onClickForSeting();
    }
    
    /**
     * 初始化弹窗列表
     */
    private void initUI()
    {
        mLinear_auto = (LinearLayout)getContentView().findViewById(R.id.Linear_auto);
        mLinear_key = (LinearLayout)getContentView().findViewById(R.id.Linear_key);
        mLinear_other = (LinearLayout)getContentView().findViewById(R.id.Linear_other);
        iv_auto = (ImageView)getContentView().findViewById(R.id.iv_autos);
        iv_key = (ImageView)getContentView().findViewById(R.id.iv_keys);
        /** 点击按键声音 */
        if (key_sound)
        {
            setImg(iv_auto, R.drawable.key_tone_disabled);
        }
        else
        {
            
            setImg(iv_auto, R.drawable.key_tone);
        }
        
        if (auto_caps)
        {
            setImg(iv_key, R.drawable.auto_c);
        }
        else
        {
            setImg(iv_key, R.drawable.auto_cdisabled);
        }
        iv_other = (ImageView)getContentView().findViewById(R.id.iv_others);
        
    }
    
    /** 添加点击事件 */
    public void onClickForSeting()
    {
        /** 开启按键音 */
        mLinear_auto.setOnClickListener(new OnClickListener()
        {
            private String UMENG_LINEAR_AUTO = "LINEAR_AUTO";

			@Override
            public void onClick(View v)
            {
            	
                /** 点击按键声音 */
                if (key_sound)
                {
                    Editor editor = settings.edit();
                    editor.putBoolean("key_sound", false);
                    editor.commit();
                    Toast.makeText(mContext, R.string.Setting_keyvoiceopen, Toast.LENGTH_SHORT).show();
                    dismiss();
                    TextCandidatesViewManager.otherSeting_isdismiss = false;
                }
                else
                {
                    Editor editor = settings.edit();
                    editor.putBoolean("key_sound", true);
                    editor.commit();
                    Toast.makeText(mContext, R.string.Setting_keyvoiceclose, Toast.LENGTH_SHORT).show();
                    dismiss();
                    TextCandidatesViewManager.otherSeting_isdismiss = false;
                }
                /**添加点击开启按键音的次数统计*/
                MobclickAgent.onEvent(mContext, UMENG_LINEAR_AUTO );
            }
        });
        mLinear_key.setOnClickListener(new OnClickListener()
        {
        	
            @Override
            public void onClick(View v)
            {	/**添加点击自动大小写更换次数统计*/
            	 MobclickAgent.onEvent(mContext, UMENG_AUTO_CAPS  ); 
                if (auto_caps)
                {
                    Editor editor = preference.edit();
                    editor.putBoolean("auto_caps", false);
                    editor.commit();
                    Toast.makeText(mContext, R.string.Setting_auto_capitalizationclose, Toast.LENGTH_SHORT).show();
                    dismiss();
                    mContext.sendBroadcast(new Intent("com.imatrixmob.ffime.core.AutoCapsBroadCastReceiver"));
                    TextCandidatesViewManager.otherSeting_isdismiss = false;
                }
                else
                {
                    Editor editor = preference.edit();
                    editor.putBoolean("auto_caps", true);
                    editor.commit();
                    Toast.makeText(mContext, R.string.Setting_auto_capitalizationopen, Toast.LENGTH_SHORT).show();
                    dismiss();
                    mContext.sendBroadcast(new Intent("com.imatrixmob.ffime.core.AutoCapsBroadCastReceiver1"));
                    TextCandidatesViewManager.otherSeting_isdismiss = false;
                }
                
            }
        });
        mLinear_other.setOnClickListener(new OnClickListener()
        {
            /** 更多设置点击事件 */
            private String UMENT_MORESETTINGS = "moresettings";
            
            @Override
            public void onClick(View v)
            {
                dismiss();
                Intent intent = new Intent(mContext, SettingsActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                /** 添加更多设置点击统计 */
                MobclickAgent.onEvent(getContentView().getContext(), UMENT_MORESETTINGS);
            }
        });
    }
    
    /** 更改UI图片 */
    private static void setImg(ImageView iv, int img)
    {
        iv.setImageResource(img);
    }
    
    /**
     * 显示弹窗列表界面
     */
    public void show(View view)
    {
        
        // 获得点击屏幕的位置坐标
        // view.getLocationOnScreen(mLocation);
        
        // 设置矩形的大小
        // mRect.set(mLocation[0], mLocation[1], mLocation[0] + view.getWidth(), mLocation[1] + view.getHeight());
        
        // 判断是否需要添加或更新列表子类项
        if (mIsDirty)
        {
            populateActions();
        }
        
        // 显示弹窗的位置
        showAsDropDown(view);
    }
    
    /**
     * 设置弹窗列表子项
     */
    private void populateActions()
    {
        mIsDirty = false;
    }
    
    /**
     * 添加子类项
     */
    public void addAction(ActionItem action)
    {
        if (action != null)
        {
            mActionItems.add(action);
            mIsDirty = true;
        }
    }
    
    /**
     * 清除子类项
     */
    public void cleanAction()
    {
        if (mActionItems.isEmpty())
        {
            mActionItems.clear();
            mIsDirty = true;
        }
    }
    
    /**
     * 根据位置得到子类项
     */
    public ActionItem getAction(int position)
    {
        if (position < 0 || position > mActionItems.size())
            return null;
        return mActionItems.get(position);
    }
    
    /**
     * @author yangyu 功能描述：弹窗子类项按钮监听事件
     */
    public static interface OnItemOnClickListener
    {
        public void onItemClick(ActionItem item, int position);
        
    }
    
}
