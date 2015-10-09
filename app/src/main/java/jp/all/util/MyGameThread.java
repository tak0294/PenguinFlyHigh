package jp.all.util;

import javax.microedition.khronos.opengles.GL11;


public class MyGameThread extends Thread
{
	private boolean mIsActive;

	//------------------------------------------------
	//	メンバ.
	//------------------------------------------------
	public GLSprite bgSprite[];
	
	
	//------------------------------------------------
	//	コンストラクタ.
	//------------------------------------------------
	public MyGameThread()
	{
		
	}


	
	
	
	//------------------------------------------------------------------------------------------------
	//	更新処理.
	//------------------------------------------------------------------------------------------------
	public void update()
	{
	    for(int ii=0;ii<20;ii++)
	    {
	        bgSprite[ii].position.z += 0.5f;
	        if(bgSprite[ii].position.z >= 0)
	            bgSprite[ii].position.z = -(5*19);
	    }
	}
	
	
	
	
	
	
	public void init()
	{
		bgSprite = new GLSprite[20];
		for(int ii=0;ii<20;ii++)
		{
	        //海.
	        bgSprite[ii] = new GLSprite(0.0f, -2.0f, -5.0f*ii, 15.0f, 7.0f, 1.0f);
	        //bgSprite[ii].rotationX = 91.0f;
	        bgSprite[ii].vbo = Global.primitives[SpriteType.PLANE];
		}
		
		mIsActive = true;
	}

	
	public void resumeGameThread() {
		mIsActive = true;
	}

	public void pauseGameThread() {
		mIsActive = false;
	}
	
	@Override
	public void run() {
        /*
		long lastUpdateTime = System.currentTimeMillis();
		while (true) {
			// 休憩
			try {
				Thread.sleep(10); // 負荷が大きくなりすぎるのを防ぐため、少し停止させましょう。
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!mIsActive) {
				// アクティブでなければゲームを進めない
				lastUpdateTime = System.currentTimeMillis();// 復帰時に更新処理が複数回行われないようにする
				continue;
			}
			// 1秒間に60回更新する
			long nowTime = System.currentTimeMillis();
			long difference = nowTime - lastUpdateTime;
			while (difference >= 16) {
				difference -= 16;
				update();
			}
			lastUpdateTime = nowTime - difference;
		}
		*/
        long error = 0;
        int fps = 60;
        long idealSleep = (1000 << 16) / fps;
        long oldTime;
        long newTime = System.currentTimeMillis() << 16;
        while (true) {
            if (!mIsActive) {
                // アクティブでなければゲームを進めない
                newTime = System.currentTimeMillis() << 16;// 復帰時に更新処理が複数回行われないようにする
                continue;
            }
            oldTime = newTime;
            update(); // メイン処理
            newTime = System.currentTimeMillis() << 16;
            long sleepTime = idealSleep - (newTime - oldTime) - error; // 休止できる時間
            if (sleepTime < 0x20000) sleepTime = 0x20000; // 最低でも2msは休止
            oldTime = newTime;
            try {
                Thread.sleep(sleepTime >> 16); // 休止
            }catch(InterruptedException e) {

            }
            newTime = System.currentTimeMillis() << 16;
            error = newTime - oldTime - sleepTime; // 休止時間の誤差
        }

    }

}
