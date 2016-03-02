# 专用键盘

## 中国车牌号码专用键盘

支持以下类型的车牌号输入：

- 民用车牌号
- 警用车牌号
- 武警车牌号

### 使用方式

```java
VehiclePlateKeyboard keyboard = new VehiclePlateKeyboard(MainActivity.this, new OnKeyActionListener() {
    @Override
    public void onFinish(String input) {
        // 输入车牌完成/点击“完成”后回调
    }

    @Override
    public void onProcess(String input) {
        // 输入过程中回调，返回已输入的车牌号
    }
});
keyboard.setDefaultPlateNumber("WJ粤12345");
keyboard.show(Activity.this.getWindow().getDecorView().getRootView());
```

## 6位密码输入键盘

就是6位密码输入键盘.

```java
PasswordKeyboard.show(MainActivity.this, new OnKeyActionListener() {
    @Override
    public void onFinish(String input) {
        // 输入密码完成后回调
    }

    @Override
    public void onProcess(String input) {
        // 输入过程中回调，返回已输入的密码
    }
});
```

## Gradle 配置

```gradle

responsities{
    jcenter()
    ...
}


dependencies{
    compile 'com.github.yoojia:keyboard:1.6@aar'
    ...
}

```