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
	private void update()
	{
	    for(int ii=0;ii<5;ii++)
	    {
	        bgSprite[ii].position.z += 0.05f;
	        if(bgSprite[ii].position.z >= (bgSprite[ii].height))
	            bgSprite[ii].position.z = -(bgSprite[ii].height*4);
	    }
	}
	
	
	
	
	
	
	public void init()
	{
		bgSprite = new GLSprite[5];
		for(int ii=0;ii<5;ii++)
		{
	        //海.
	        bgSprite[ii] = new GLSprite(0.0f, -2.0f, -15.0f*ii, 30.0f, 15.0f, 1.0f);
	        bgSprite[ii].rotationX = 91.0f;
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
			while (difference >= 17) {
				difference -= 17;
				update();
			}
			lastUpdateTime = nowTime - difference;
		}
	}

}
