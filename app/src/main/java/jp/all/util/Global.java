package jp.all.util;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

//import all.jp.MainActivity;

import android.app.Activity;

public class Global {
	
	// MainActivity
	public static Activity mainActivity;
	public static Activity activity;
	
	public static boolean isES11;//ES 1.1であればtrue
	
	//GLコンテキストを保持する変数
	public static GL10 gl;
	
	//ランダムな値を生成する
	public static Random rand = new Random(System.currentTimeMillis());
	
	//デバックモードであるか
	public static boolean isDebuggable;
	
	//基本図形.
	public static MyVbo[] primitives;
}
