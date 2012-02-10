var scaleUp = true;

function onUpdate(pSecondsElapsed) {
    mSprite.setRotation(mSprite.getRotation() + 90 * pSecondsElapsed);

    var scale = mSprite.getScaleX();
    if(scaleUp) {
        if(scale < 2) {
            mSprite.setScale(scale + 1 * pSecondsElapsed);
        } else {
            scaleUp = false;        
        }
    } else {
        if(scale > 1) {
            mSprite.setScale(scale - 2 * pSecondsElapsed);
        } else {
            scaleUp = true;        
        }
    }
}

//function onClick() {
//    var widgets = Packages.android.widget;
//    var view = new widgets.TextView(mContext);
//    mContext.setContentView(view);
//    var text = 'Hello Android!\\nThis is JavaScript in action!';
//    view.append(text);
//    if(mSprite.isAnimationRunning()) {
//        mSprite.stopAnimation();
//    } else {
//        mSprite.animate(100);
//    }
//}
