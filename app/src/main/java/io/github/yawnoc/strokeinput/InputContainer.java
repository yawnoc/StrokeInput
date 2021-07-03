/*
  Copyright 2021 Conway
  Licensed under the GNU General Public License v3.0 (GPL-3.0-only).
  This is free software with NO WARRANTY etc. etc.,
  see LICENSE or <https://www.gnu.org/licenses/>.
*/
/*
  This is a re-implementation of the deprecated `KeyboardView` class,
  which is licensed under the Apache License 2.0,
  see <https://www.apache.org/licenses/LICENSE-2.0.html>.
*/

package io.github.yawnoc.strokeinput;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import java.util.List;

/*
  A container that holds:
  - TODO: Candidates bar
  - TODO: Keyboard
*/
public class InputContainer
  extends View
{
  // Container meta-properties
  private Keyboard inputKeyboard;
  private Keyboard.Key[] inputKeyArray;
  
  // Keyboard drawing
  Rect keyRectangle;
  Paint keyFillPaint;
  Paint keyBorderPaint;
  Paint keyTextPaint;
  
  public InputContainer(Context context, AttributeSet attributes) {
    
    super(context, attributes);
    
    keyRectangle = new Rect();
    
    keyFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    keyFillPaint.setStyle(Paint.Style.FILL);
    
    keyBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    keyBorderPaint.setStyle(Paint.Style.STROKE);
    
    keyTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    keyTextPaint.setTypeface(
      ResourcesCompat.getFont(context, R.font.stroke_input_keyboard)
    );
    keyTextPaint.setTextAlign(Paint.Align.CENTER);
  }
  
  public void setKeyboard(Keyboard keyboard) {
    inputKeyboard = keyboard;
    List<Keyboard.Key> keyList = inputKeyboard.getKeyList();
    inputKeyArray = keyList.toArray(new Keyboard.Key[0]);
    requestLayout();
  }
  
  @Override
  public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    
    int paddingHorizontal = getPaddingLeft() + getPaddingRight();
    int paddingVertical = getPaddingTop() + getPaddingBottom();
    
    int keyboardWidth;
    int keyboardHeight;
    if (inputKeyboard == null) {
      keyboardWidth = 0;
      keyboardHeight = 0;
    }
    else {
      keyboardWidth = inputKeyboard.getWidth();
      keyboardHeight = inputKeyboard.getHeight();
    }
    
    setMeasuredDimension(
      keyboardWidth + paddingHorizontal,
      keyboardHeight + paddingVertical
    );
  }
  
  @Override
  public void onDraw(Canvas canvas) {
    
    super.onDraw(canvas);
    
    if (inputKeyboard == null) {
      return;
    }
    
    for (final Keyboard.Key key : inputKeyArray) {
      
      keyRectangle.set(0, 0, key.width, key.height);
      
      keyFillPaint.setColor(key.keyFillColour);
      keyBorderPaint.setColor(key.keyBorderColour);
      keyBorderPaint.setStrokeWidth(key.keyBorderThickness);
      
      keyTextPaint.setColor(key.keyTextColour);
      keyTextPaint.setTextSize(key.keyTextSize);
      
      float key_text_x = (
        key.width / 2f
          + key.keyTextOffsetX
      );
      float key_text_y = (
        (key.height - keyTextPaint.ascent() - keyTextPaint.descent()) / 2f
          + key.keyTextOffsetY
      );
      
      canvas.translate(key.x, key.y);
      
      canvas.drawRect(keyRectangle, keyFillPaint);
      canvas.drawRect(keyRectangle, keyBorderPaint);
      canvas.drawText(
        key.displayText,
        key_text_x,
        key_text_y,
        keyTextPaint
      );
      
      canvas.translate(-key.x, -key.y);
    }
    
  }
  
}
