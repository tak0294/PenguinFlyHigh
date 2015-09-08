package jp.all.penguinflyhigh;

import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import jp.all.util.MyGLSurfaceView;
import jp.all.util.MyGameThread;
import jp.all.util.MyRenderer;


public class PenguinMain extends AppCompatActivity {

    MyGameThread mGameThread;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        super.onCreate(savedInstanceState);

        mGameThread = new MyGameThread();

        // フルスクリーン、タイトルバーの非表示
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 音量変更をハードウェアボタンで出来るようにする
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        MyRenderer renderer 			= new MyRenderer(this, mGameThread);
        MyGLSurfaceView glSurfaceView 	= new MyGLSurfaceView(this);
        glSurfaceView.setRenderer(renderer);
        setContentView(glSurfaceView);

        mGameThread.start();
    }

}
