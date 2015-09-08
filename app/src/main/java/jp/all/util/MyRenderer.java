package jp.all.util;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import jp.all.opengl.particle.ParticleSystem;
import android.app.Activity;
import android.content.res.Resources;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

public class MyRenderer implements Renderer
{
	private MyGameThread mGame;
	private Activity activity;
	private boolean isReadyVbo = false;
	
	private int frame = 0;

	private int particleTexture;
	private int rockTexture;
	private int numberTexture;
	
	private int mWidth;
	private int mHeight;
	private int mWidthOffset;
	private int mHeightOffset;
	
	private boolean isPush = false;
	
	private ParticleSystem particleSystem;
	
	public MyRenderer(Activity activity, MyGameThread gameThread)
	{
		this.mGame = gameThread;
		this.activity = activity;
		Global.activity = activity;
	}
	
	public MyRenderer() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public void draw3D(GL11 gl)
	{
		gl.glMatrixMode(GL11.GL_PROJECTION);
		gl.glLoadIdentity();
		float znear = 0.5f; //scene.zoom2;
		float zfar = 30.0f; //position[2];
	    gl.glFrustumf(-0.3f, 0.3f, -0.2f, 0.2f, znear, zfar);
		
	    // ライトとマテリアルの設定
	    float lightPos[]     = { 1.0f, 1.0f, 1.0f, 0.0f };
	    float lightColor[]   = { 1.0f, 1.0f, 1.0f, 1.0f };
	    float lightAmbient[] = { 0.0f, 0.0f, 0.0f, 1.0f };
	    float diffuse[]      = { 1.7f, 1.7f, 1.7f, 1.0f };
	    float ambient[]      = { 2.6f, 2.6f, 2.6f, 1.0f };
	    
	    
	    // カメラの設定(デフォルト)
		gl.glMatrixMode(GL11.GL_MODELVIEW);
		gl.glLoadIdentity();
	    
	    gl.glEnable(GL11.GL_LIGHTING);
	    gl.glEnable(GL11.GL_LIGHT0);
	    gl.glLightfv(GL11.GL_LIGHT0, GL11.GL_POSITION, lightPos, 0);
	    gl.glLightfv(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, lightColor, 0);
	    gl.glLightfv(GL11.GL_LIGHT0, GL11.GL_AMBIENT, lightAmbient, 0);
	    gl.glMaterialfv(GL11.GL_FRONT_AND_BACK, GL11.GL_DIFFUSE, diffuse, 0);
	    gl.glMaterialfv(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT, ambient, 0);
		
	    //深度テストを有効.
		gl.glEnable(GL11.GL_DEPTH_TEST);

	    //フォグ設定.
	    float fogColor[]= {1.0f, 1.0f, 1.0f, 1.0f}; //フォグの色
	    gl.glEnable(GL11.GL_FOG);
	    gl.glFogx(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
	    gl.glFogfv(GL11.GL_FOG_COLOR, fogColor, 0);
	    gl.glFogf(GL11.GL_FOG_DENSITY, 0.3f);
	    gl.glHint(GL11.GL_FOG_HINT, GL11.GL_DONT_CARE);
	    gl.glFogf(GL11.GL_FOG_START, 10.5f);
	    gl.glFogf(GL11.GL_FOG_END, 20.0f);
	    
	    for(int ii=0;ii<5;ii++)
	    {
	    	mGame.bgSprite[ii].draw(gl, rockTexture);
	    }

	    //Fog終了.
	    gl.glDisable(GL11.GL_FOG);

		//深度テスト終了
		gl.glDisable(GL11.GL_DEPTH_TEST);
	    gl.glDisable(GL11.GL_LIGHTING);
	    gl.glDisable(GL11.GL_LIGHT0);

	    //パーティクルを描画.
	    particleSystem.update();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		particleSystem.draw(gl, particleTexture);
		gl.glDisable(GL10.GL_BLEND); 
 	}
	
	public void draw2D(GL11 gl)
	{
		// 2D描画用に座標系を設定します
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(-1.5f, 1.5f, -1.0f, 1.0f, 0.5f, -0.5f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		GraphicUtil.drawNumbers(gl, 0.0f, 0.0f, 0.1f, 0.1f, numberTexture, 100, 8, 1.0f, 1.0f, 1.0f, 1.0f);
		//GraphicUtil.drawRectangle(gl, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f);
	}
	
	
	public void onDrawFrame(GL10 gl10)
	{
		GL11 gl = (GL11)gl10;
		
		gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glViewport(mWidthOffset, mHeightOffset, mWidth, mHeight);
		
		draw3D(gl);
		draw2D(gl);
		frame++;
	}
		
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		//----------------------------------------------
		//	VBOを作成する.
		//----------------------------------------------
		if(!isReadyVbo)
		{
			makeVbo((GL11)gl);
			mGame.init();
			isReadyVbo = true;
		}
		
		this.mWidth 		= width;
		this.mHeight 		= height;
		this.mWidthOffset   = 0;
		this.mHeightOffset  = 0;
		
		Global.gl = gl;// GLコンテキストを保持する

		// バージョンチェックを行う
		String vertion = gl.glGetString(GL10.GL_VERSION);
		Global.isES11 = false;
		if (vertion.contains("1.1"))
		{
			Global.isES11 = true;
		}
		
		gl.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL11.GL_NORMAL_ARRAY);
		
		//----------------------------------------------
		//	テクスチャの読み込み.
		//----------------------------------------------
		rockTexture 	= GraphicUtil.loadTexture(gl, "rock");
		particleTexture = GraphicUtil.loadTexture(gl, "particle");
		numberTexture	= GraphicUtil.loadTexture(gl, "number_texture");
		
		//----------------------------------------------
		//	パーティクルシステムの作成.
		//----------------------------------------------
	    particleSystem = new ParticleSystem(200, 15);
	}

	public void onSurfaceCreated(GL10 gl10, EGLConfig config)
	{
	}

	private void makeVbo(GL11 gl)
	{
		Global.primitives = new MyVbo[5];
		for(int ii=0;ii<5;ii++)
		{
			Global.primitives[ii] = new MyVbo();
		}
		
		GraphicUtil.makePlane(gl, Global.primitives[SpriteType.PLANE]);
		GraphicUtil.makeCube(gl, Global.primitives[SpriteType.CUBE]);
		GraphicUtil.makePyramid(gl, Global.primitives[SpriteType.PYRAMID]);
		GraphicUtil.makeSphere(gl, Global.primitives[SpriteType.SPHERE], 10);
		GraphicUtil.makeCylinder(gl, Global.primitives[SpriteType.CYLINDER]);
	}

	//画面がタッチされたときに呼ばれるメソッド
	public void touched(float x, float y, float glX, float glY)
	{
		isPush = false;
	}

	public void unTouched()
	{
		isPush = false;
	}

}
