package com.czh.note.util.toast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

/**
 * 用于解决 Toast 相关的 BadTokenException 引起的闪退问题
 * <p>
 * 问题出现的原因是调用了 Toast 的 show 方法之后，又在主线程执行了耗时操作，导致主线程阻塞
 * 而 Android 系统在检测到 show 方法在超时后仍未显示，会把 Toast 对应的 token 删掉
 * 因此在主线程的耗时操作执行完毕准备显示 Toast 时，系统无法找到对应的 token，就抛出了异常
 * <p>
 * 解决方案是把引起错误的代码使用 try-catch 吞掉异常，可以注册 BadToastListener 监听该异常的发生
 * <p>
 * 这个错误在 Android 8.0 之后已修复
 * 详见 NotificationManagerService、Toast 及 Toast 内部类 TN 的实现
 */
public class ToastCompat extends Toast {

    private static final List<BadToastListener> sBadToastListeners = new LinkedList<>();

    private boolean mIsHacked = false;

    @SuppressWarnings("WeakerAccess")
    public ToastCompat(Context context) {
        super(context);
    }

    public static void registerBadToastListener(BadToastListener listener) {
        sBadToastListeners.add(listener);
    }

    public static void unregisterBadToastListener(BadToastListener listener) {
        sBadToastListeners.remove(listener);
    }

    public static Toast makeText(Context context, @StringRes int resId, int duration)
            throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
    }

    @SuppressLint("ShowToast")
    public static Toast makeText(@NonNull Context context, @NonNull CharSequence text, int duration) {
        Toast toast = new ToastCompat(context);
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) {
            return Toast.makeText(context, text, duration);
        }
        try {
            Resources resources = context.getResources();
            View v = inflater.inflate(resources.getIdentifier("transient_notification",
                    "layout", "android"), null);
            TextView tv = v.findViewById(resources.getIdentifier("message", "id",
                    "android"));
            tv.setText(text);
            toast.setView(v);
            toast.setDuration(duration);
        } catch (Exception e) {
            toast = Toast.makeText(context, text, duration);
        }
        return toast;
    }

    public static Toast custom(@NonNull Context context, @NonNull View view, int gravity, int offsetX, int offsetY, int duration) {
        Toast toast = new ToastCompat(context);
        toast.setGravity(gravity, offsetX, offsetY);
        toast.setView(view);
        toast.setDuration(duration);
        return toast;
    }

    @Override
    public void show() {
        if (!mIsHacked && Build.VERSION.SDK_INT < 28) {
            tryToHack();
        }
        super.show();
    }

    private void tryToHack() {
        try {
            mIsHacked = true;
            Object mTN = getFieldValue(this, "mTN");
            if (mTN != null) {
                // a hack to some device which use the code between android 6.0 and android 7.1.1
                Object rawShowRunnable = getFieldValue(mTN, "mShow");
                if (rawShowRunnable instanceof Runnable) {
                    setFieldValue(mTN, "mShow", new InnerRunnable((Runnable) rawShowRunnable));
                }

                // hack to android 7.1.1,these cover 99% devices.
                Object rawHandler = getFieldValue(mTN, "mHandler");
                if (rawHandler instanceof Handler) {
                    setFieldValue(rawHandler, "mCallback", new InnerHandlerCallback((Handler) rawHandler));
                }
            }
        } catch (Throwable e) {
            /* ignore */
        }
    }

    private static Object getFieldValue(Object obj, final String fieldName) {
        Field field = getDeclaredField(obj, fieldName);
        return getFieldValue(obj, field);
    }

    private static Field getDeclaredField(final Object obj, final String fieldName) {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                /* ignore */
            }
        }
        return null;
    }

    private static Object getFieldValue(Object obj, Field field) {
        if (field != null) {
            try {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                return field.get(obj);
            } catch (Exception e) {
                /* ignore */
            }
        }
        return null;
    }

    private static void setFieldValue(Object object, String fieldName, Object newFieldValue) {
        Field field = getDeclaredField(object, fieldName);
        if (field != null) {
            try {
                int accessFlags = field.getModifiers();
                if (Modifier.isFinal(accessFlags)) {
                    Field modifiersField = Field.class.getDeclaredField("accessFlags");
                    modifiersField.setAccessible(true);
                    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                }
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                field.set(object, newFieldValue);
            } catch (Exception e) {
                /* ignore */
            }
        }
    }

    private static class InnerRunnable implements Runnable {

        private final Runnable mRunnable;

        private InnerRunnable(Runnable mRunnable) {
            this.mRunnable = mRunnable;
        }

        @Override
        public void run() {
            try {
                this.mRunnable.run();
            } catch (Throwable e) {
                for (BadToastListener listener : sBadToastListeners) {
                    listener.onBadToast(e);
                }
            }
        }
    }

    private static class InnerHandlerCallback implements Handler.Callback {

        private final Handler mHandler;

        private InnerHandlerCallback(Handler handler) {
            mHandler = handler;
        }

        @Override
        public boolean handleMessage(@NonNull Message msg) {
            try {
                mHandler.handleMessage(msg);
            } catch (Throwable e) {
                for (BadToastListener listener : sBadToastListeners) {
                    listener.onBadToast(e);
                }
            }
            return true;
        }
    }

    public interface BadToastListener {
        void onBadToast(Throwable e);
    }
}
