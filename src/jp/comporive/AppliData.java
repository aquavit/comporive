package jp.comporive;

import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**�R���e�L�X�g�Ǘ�*/
public class AppliData {
	public static final short DEBUG_MODE = 0;	//1-�f�o�b�O�@0-�����[�X
	public static final short WRITE_OPTION = 0;
	
	//�J�����̉�ʃT�C�Y
	public static final int bWidth = 960;	//���쎞�̊�ɂ�����ʕ�
	public static final int bHeight = 540;	//���쎞�̊�ɂ�����ʏc
	
	//�v���O�����S�ʂŎg���d�v�f�[�^
	public static final int FRAME = 30;
	public static GL11 gl = null;
	public static Context context = null;
	public static int sWidth;		//�Q�[����ʕ�
	public static int sHeight;		//�Q�[����ʍ���
	public static int wWidth;	//�E�B���h�E�̕�
	public static int wHeight; 	//�E�B���h�E�̍���
	
	public static int sBaseX;	//�X�N���[�����W��Android��ʓ��̋N�_�ʒu
	public static int sBaseY;	//�X�N���[�����W��Android��ʓ��̋N�_�ʒu
	public static GLSurfaceView render = null;
	
	/**GL�L���[�C�x���g*/
	public static void queGlEvent(Runnable proc){
		if (null == render) return;
		render.queueEvent(proc);
	}
	
	/**���ݐݒ肵�Ă���t���[�����ɕϊ������t���[������Ԃ�
	 * @param FPS60���̃t���[����
	 * @return ���ݐݒ肵�Ă���FPS�p�ɕϊ������t���[����
	 * */
	public static int getRevisedFrame(int fps60Frame){
		//FPS��60�������Ƃ��́A���̂܂ܕԂ��B
		if (FRAME == 60) 
			return fps60Frame;
				
		//30�t���[���̏ꍇ�́A2�Ŋ����ĕԂ��B
		return fps60Frame/2;
	}
	
	/**�n���ꂽ�X�P�[���l�ƌ��݂̉�ʃT�C�YX�����������̂�Ԃ�
	 * */
	public static float getResizeX2Scale(float scaleX){
		return sWidth*scaleX;
	}
	
	/**�n���ꂽ�X�P�[���l�ƌ��݂̉�ʃT�C�YY�����������̂�Ԃ�
	 * */
	public static float getResizeY2Scale(float scaleY){
		return sHeight*scaleY;
	}
	
	/**�E�B���h�E���W�ɕϊ�����*/
	public static float getResizeWindowX(float scaleX){
		//�X�N���[����ʓ��̈ʒu���Z�o
		float x = sWidth*scaleX;
		
		//Android��ʂ̒��S�n���Z�o
		float cx = wWidth*0.5f;
		
		//��_�ʒu���Z�o
		float bx = cx - sWidth*0.5f;
		
		return bx+x;
	}
	
	/**�E�B���h�E���W�ɕϊ�����*/
	public static float getResizeWindowY(float scaleY){
		//�X�N���[����ʓ��̈ʒu���Z�o
		float y = sHeight*scaleY;
		
		//Android��ʂ̒��S�n���Z�o
		float cy = wHeight*0.5f;
		
		//��_�ʒu���Z�o
		float by = cy - sHeight*0.5f;
		
		return by+y;
	}
	
	/**�쐬���̃T�C�Y�����ƂɃf�[�^��␳����
	 * @return ���݂̃X�N���[�����WX*(�n�����f�[�^/���쎞�̉�ʃT�C�YX)
	 * */
	public static float getResizeWidthSize(float width){
		//�n���ꂽ���l�𐧍�T�C�Y�Ŋ���A�X�P�[���l�����߂�
		//��������݂̃X�N���[���l�ɂ�����
		return sWidth * (width/bWidth);
	}
	
	/**�쐬���̃T�C�Y�����ƂɃf�[�^��␳����
	 * @return ���݂̃X�N���[�����WY*(�n�����f�[�^/���쎞�̉�ʃT�C�YY)
	 * */
	public static float getResizeHeightSize(float height){
		return sHeight * (height/bHeight);
	}
	
	/**��*/
	public static float getWidthToScreenSize(int width){
		return sWidth * getBaseWidthRate(width);
	}
	
	/**����*/
	public static float getHeightToScreenSize(int width){
		return sHeight * getBaseWidthRate(width);
	}
	
	/**���쎞�̕��T�C�Y����ɂ���������Ԃ�*/
	public static float getBaseWidthRate(int width){
		return width/bWidth;
	}
	
	/**���쎞�̏c�T�C�Y����ɂ���������Ԃ�*/
	public static float getBaseHeightRate(int height){
		return height/bHeight;
	}
}


