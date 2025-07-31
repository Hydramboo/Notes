# 氢竹笔记 - 简洁高效的笔记应用

一个简洁高效的笔记应用，专为移动设备优化，具有现代化的Material Design界面。

## 功能特性

- ✅ 添加新的笔记项目
- ✅ 查看所有笔记列表
- ✅ 删除已完成的笔记
- ✅ 现代化的Material Design界面
- ✅ 响应式布局，适配不同屏幕尺寸
- ✅ 本地数据库存储，数据持久化
- ✅ 简洁直观的用户界面

## 技术栈

- **语言**: Kotlin
- **架构**: MVVM (Model-View-ViewModel)
- **数据库**: Room Database
- **UI框架**: Material Design Components
- **异步处理**: Kotlin Coroutines
- **依赖注入**: Android ViewModel

## 项目结构

```
app/src/main/java/rj/notes/
├── MainActivity.kt              # 主界面
├── AddTodoActivity.kt           # 添加笔记界面
├── TodoAdapter.kt               # RecyclerView适配器
├── model/
│   └── TodoItem.kt             # 笔记数据模型
├── viewmodel/
│   └── TodoViewModel.kt        # ViewModel
└── db/
    ├── AppDatabase.kt          # 数据库配置
    └── TodoItemDao.kt          # 数据访问对象
```

## 界面设计

应用采用现代化的Material Design设计语言：

- **卡片式布局**: 笔记项目以卡片形式展示
- **浮动操作按钮**: 快速添加新的笔记
- **圆角设计**: 现代化的圆角元素
- **响应式颜色**: 适配不同主题
- **触摸反馈**: 提供良好的交互体验

## 数据库设计

使用Room数据库进行本地存储：

- **表名**: todos
- **字段**: 
  - id (主键，自增)
  - title (笔记标题)

## 使用方法

1. 启动应用，查看笔记列表
2. 点击右下角的"+"按钮添加新的笔记
3. 在输入框中输入笔记内容
4. 点击"添加笔记"按钮保存
5. 点击笔记项目可以删除该项目

## 构建和运行

1. 确保已安装Android Studio
2. 克隆项目到本地
3. 在Android Studio中打开项目
4. 连接Android设备或启动模拟器
5. 点击运行按钮

## 系统要求

- Android API 16+ (Android 4.1+)
- 推荐Android 8.0+

## 包名信息

- **应用包名**: rj.notes
- **应用名称**: 氢竹笔记
- **版本**: 1.0.0