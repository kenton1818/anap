����   3%	 �	 � 	 �!
 �"	 �#C  	 �$C�  	 �%� � ��  	 �&Bp  	 �'
 �(	 �)A   	 �*	 �+Ap  	 �,AP  	 �-	 �.	 �/@�  	 �0A   	 �12	 �34	 �5	 �6	 �7	 �8
 �9
 �:
 �;	<=
>?	<@
AB	<C	<D	<E
AF	<G
AH	<I	<J
AK	 �L	<M	 �N	<O	 �P	<Q	 �R	<S
AT	 �U	<V	 �W	<X
AY	<Z
 �[
 �\
A]
 �^
 �_`
 Ia	 �b	 Ic	 Id	 Ie	 If	 �g	 �hi
 Ra	 �j
kl
 Rm	 �n
 Ro	pq
 Rr
 Rs	tu
 Rv	 �w	 �x
 Ry	z{
 R|	 �}	 �~
 e�	 ���
 h�	 ���
 ka	 ��
��A  
��
��?�33
��C  
 k�
 h�
 R���
��
��@@  
��
 ��	 ��
 ��
��	 ��
��
���
 ��
��
��
 ��
>�
��	 ��?   �
>���
 �a ��
��	 ����   InnerClasses mWidth I mHeight diameter centerX F centerY allArcPaint Landroid/graphics/Paint; progressPaint 
vTextPaint 	hintPaint degreePaint curSpeedPaint bgRect Landroid/graphics/RectF; progressAnimator !Landroid/animation/ValueAnimator; mDrawFilter 'Landroid/graphics/PaintFlagsDrawFilter; sweepGradient  Landroid/graphics/SweepGradient; rotateMatrix Landroid/graphics/Matrix; 
startAngle 
sweepAngle currentAngle 	lastAngle colors [I 	maxValues 	curValues 
bgArcWidth progressWidth textSize hintSize curSpeedSize aniSpeed 
longdegree shortdegree DEGREE_PROGRESS_DISTANCE 	hintColor Ljava/lang/String; longDegreeColor shortDegreeColor 
bgArcColor titleString 
hintString isShowCurrentSpeed Z isNeedTitle 
isNeedUnit 
isNeedDial isNeedContent k <init> (Landroid/content/Context;)V Code LineNumberTable LocalVariableTable this 6Lmrkj/library/wheelview/circlebar/ColorArcProgressBar; context Landroid/content/Context; 7(Landroid/content/Context;Landroid/util/AttributeSet;)V attrs Landroid/util/AttributeSet; 8(Landroid/content/Context;Landroid/util/AttributeSet;I)V defStyleAttr 	initCofig a  Landroid/content/res/TypedArray; color1 color2 color3 	onMeasure (II)V widthMeasureSpec heightMeasureSpec width height initView ()V onDraw (Landroid/graphics/Canvas;)V i canvas Landroid/graphics/Canvas; StackMapTable setMaxValues (F)V setCurrentValues currentValues setBgArcWidth (I)V setProgressWidth setTextSize setHintSize setUnit (Ljava/lang/String;)V setDiameter setTitle title setIsNeedTitle (Z)V setIsNeedUnit setIsNeedDial setAnimation (FFI)V last current length dipToPx (F)I dip density� getScreenWidth ()I windowManager Landroid/view/WindowManager; displayMetrics Landroid/util/DisplayMetrics; 
access$002 :(Lmrkj/library/wheelview/circlebar/ColorArcProgressBar;F)F x0 x1 
access$102 
access$000 9(Lmrkj/library/wheelview/circlebar/ColorArcProgressBar;)F 
access$200 
SourceFile ColorArcProgressBar.java � � � � � � � � � � � � � � � � � �
 � � � � � � � � � � � � � � � � � � #4592F3 � � #111111 � � � � � � � � � � � � � ��� ����� ����� �� �� ���� ���� �� ��� � �� � � �� � � �� � � �� ��� � �� � � �� ���� � � � � �� �� � android/graphics/RectF � � � �� �� �� �� � � � � � android/graphics/Paint � ����� � � �������� ������ � � � � � ������ � � � � %android/graphics/PaintFlagsDrawFilter � � � � android/graphics/SweepGradient �� � � android/graphics/Matrix � ���������������� %.0f java/lang/Object��  � � �	 � �
 6mrkj/library/wheelview/circlebar/ColorArcProgressBar$1 � � � window android/view/WindowManager android/util/DisplayMetrics !" � 4mrkj/library/wheelview/circlebar/ColorArcProgressBar android/view/View# mrkj/library/R$styleable 	styleable ColorArcProgressBar android/content/Context obtainStyledAttributes ?(Landroid/util/AttributeSet;[I)Landroid/content/res/TypedArray;  ColorArcProgressBar_front_color1 android/content/res/TypedArray getColor (II)I  ColorArcProgressBar_front_color2  ColorArcProgressBar_front_color3 ColorArcProgressBar_total_engle 
getInteger ColorArcProgressBar_back_width getDimension (IF)F ColorArcProgressBar_front_width !ColorArcProgressBar_is_need_title 
getBoolean (IZ)Z #ColorArcProgressBar_is_need_content  ColorArcProgressBar_is_need_unit  ColorArcProgressBar_is_need_dial ColorArcProgressBar_string_unit 	getString (I)Ljava/lang/String;  ColorArcProgressBar_string_title !ColorArcProgressBar_current_value getFloat ColorArcProgressBar_max_value recycle setMeasuredDimension top left right bottom android/graphics/Color 
parseColor (Ljava/lang/String;)I setColor setAntiAlias android/graphics/Paint$Style Style STROKE Landroid/graphics/Paint$Style; setStyle !(Landroid/graphics/Paint$Style;)V setStrokeWidth android/graphics/Paint$Cap Cap ROUND Landroid/graphics/Paint$Cap; setStrokeCap (Landroid/graphics/Paint$Cap;)V android/graphics/Paint$Align Align CENTER Landroid/graphics/Paint$Align; setTextAlign !(Landroid/graphics/Paint$Align;)V 	(FF[I[F)V android/graphics/Canvas setDrawFilter  (Landroid/graphics/DrawFilter;)V rotate (FFF)V drawLine (FFFFLandroid/graphics/Paint;)V drawArc 6(Landroid/graphics/RectF;FFZLandroid/graphics/Paint;)V 	setRotate setLocalMatrix (Landroid/graphics/Matrix;)V 	setShader 4(Landroid/graphics/Shader;)Landroid/graphics/Shader; java/lang/Float valueOf (F)Ljava/lang/Float; java/lang/String format 9(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String; drawText /(Ljava/lang/String;FFLandroid/graphics/Paint;)V 
invalidate android/animation/ValueAnimator ofFloat %([F)Landroid/animation/ValueAnimator; setDuration $(J)Landroid/animation/ValueAnimator; 	setTarget (Ljava/lang/Object;)V 9(Lmrkj/library/wheelview/circlebar/ColorArcProgressBar;)V addUpdateListener$ AnimatorUpdateListener ;(Landroid/animation/ValueAnimator$AnimatorUpdateListener;)V start 
getContext ()Landroid/content/Context; getResources !()Landroid/content/res/Resources; android/content/res/Resources getDisplayMetrics ()Landroid/util/DisplayMetrics; getSystemService &(Ljava/lang/String;)Ljava/lang/Object; getDefaultDisplay ()Landroid/view/Display; android/view/Display 
getMetrics  (Landroid/util/DisplayMetrics;)V widthPixels mrkj/library/R 6android/animation/ValueAnimator$AnimatorUpdateListener ! � �   ,  � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �    � �     � �  �  T     �*+� *�� *� *� 	*� *�
Y
OY� OYOYO� *� *� **� �� **� �� **� �� **� �� **� �� *� **� �� **� �� **� � *� * � !* � "* � #*� $*� %�    �   b    P    .  /  0  2 : 3 @ 4 E 5 O 6 Z 7 e 8 p 9 { : � ; � < � = � ? � @ � A � B � F � Q � R �       � � �     � � �   � �  �  i     �*+,� &*�� *� *� 	*� *�
Y
OY� OYOYO� *� *� **� �� **� �� **� �� **� �� **� �� *� **� �� **� �� **� � *� * � !* � "* � #*� $*+,� '*� %�    �   f    U    .  /  0  2 ; 3 A 4 F 5 P 6 [ 7 f 8 q 9 | : � ; � < � = � ? � @ � A � B � F � V � W � X �        � � �     � � �    � � �   � �  �  s     �*+,� &*�� *� *� 	*� *�
Y
OY� OYOYO� *� *� **� �� **� �� **� �� **� �� **� �� *� **� �� **� �� **� � *� * � !* � "* � #*� $*+,� '*� %�    �   f    [    .  /  0  2 ; 3 A 4 F 5 P 6 [ 7 f 8 q 9 | : � ; � < � = � ? � @ � A � B � F � \ � ] � ^ �   *    � � �     � � �    � � �    � � �   � �  �  �     �+,� (� )N-� *
� +6-� ,� +6-� -� +6*�
YOYOYOYO� *-� .� /�� 	*-� 0*� �� 1� *-� 2*� �� 1� *-� 3� 4� 5*-� 6� 4� 7*-� 8� 4� 9*-� :� 4� ;*-� <� =� >*-� ?� =� @*-� A� B� *-� C� B� **� � D**� � E-� F�    �   R    f 	 g  h  i * j E l T m e n w o � p � q � r � s � t � u � v � w � x � y � { �   H    � � �     � � �    � � �  	 � � �   � � �   � � �  * � � �   � �  �   �     ?*� j*� b*� �b*� h�b�>*� j*� b*� �b*� h�b�6*� G�    �         � 7 � > � �   4    ? � �     ? � �    ? � �   $ � �  7  � �   � �  �  �    **� Hhl� *� IY� J� K*� K*� *� nb*� �b� L*� K*� *� nb*� �b� M*� K*� �*� *� nb*� �bb� N*� K*� �*� *� nb*� �bb� O**� j*� b*� �b*� h�bn� P**� j*� b*� �b*� h�bn� Q*� RY� S� T*� T*� !� U� V*� RY� S� W*� W� X*� W� Y� Z*� W*� � [*� W*� #� U� V*� W� \� ]*� RY� S� ^*� ^� X*� ^� Y� Z*� ^� \� ]*� ^*� � [*� ^
� V*� RY� S� _*� _*� � `*� _*� � U� V*� _� a� b*� RY� S� c*� c*� � `*� c*� � U� V*� c� a� b*� RY� S� d*� d*� � `*� d*� � U� V*� d� a� b*� eY� f� g*� hY*� P*� Q*� � i� j*� kY� l� m�    �   � &   �  �  � / � G � e � � � � � � � � � � � � � � � � � � � �% �- �7 �A �L �U �` �k �y �� �� �� �� �� �� �� �� �� �� � � � �       � �    � �  �  �    +*� g� n*� ;�"=(�� � +o*� P*� Q� p� �p� h*� T*� �� [*� T*� !� U� V+*� P*� Q*� l�f*� nf*� �f*� P*� Q*� l�f*� nf*� �f*� f*� T� q� ~*� T*r� �� [*� T*� "� U� V+*� P*� Q*� l�f*� nf*� �f*� *� fnf*� P*� Q*� l�f*� nf*� �f*� *� fnf*� f*� T� q+o*� P*� Q� p����+*� K*� *� 	*� W� s*� mt*� P*� Q� u*� j*� m� v*� ^*� j� wW+*� K*� *� *� ^� s*� 7� .+x� yY*� � zS� {*� P*� Q*� |nb*� _� }*� 9� !+*� >*� P*� Q*� j|nb*� c� }*� 5� !+*� @*� P*� Q*� j|nf*� d� }*� ~�    �   r    �  �  �  � # � 1 � 4 � : � G � U � � � � � � � �( �. �C �T �_ �k �� �� �� �� �� �� �� �  � �       � �    � �     � �  �    	� "� j� z� � �$$  � �  �   L     *#� **� 	#n� �    �       �  �  � �        � �      � �   � �  �   �     6#*� �� *� D#�� D*#� **� � **� #*� j*� � ��    �   "    	     #	 5
 �       6 � �     6 � �  �      � �  �   ?     *�� �    �   
     �        � �      � �   � �  �   ?     *�� �    �   
     �        � �      � �   � �  �   ?     *�� �    �   
   ! " �        � �      � �   � �  �   ?     *�� �    �   
   ) * �        � �      � �   � �  �   F     
*+� >*� ~�    �      1 2 	3 �       
 � �     
 � �   � �  �   C     **�� � �    �   
   : 
; �        � �      � �   � �  �   >     *+� @�    �   
   B C �        � �       �    �   >     *� 5�    �   
   J K �        � �      � �    �   >     *� 9�    �   
   R S �        � �      � �    �   >     *� ;�    �   
   Z [ �        � �      � �    �   �     A*�Y#QY$Q� �� �*� ��� �W*� �*� � z� �*� �� �Y*� �� �*� �� ��    �      c d e *f 9n @o �   *    A � �     A �    A �    A	 �  
  �   �     #*� �� �� �� �E#$j�#�� � �jb��    �   
   w x �        # � �     # �    �  �    �   �       �   n     $*� ��� �� �L� �Y� �M+� � ,� �,� ��    �      � � � � �        $ � �          �   ;     *#Z� �    �        �        �      �   �   ;     *#Z� �    �        �        �      �   �   /     *� �    �        �        �    �   /     *� �    �        �        �       �   2  �      <�� p R�@t R�@z R�@�	