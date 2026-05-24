# Android Multi-Task Demo

这个 Demo 旨在测试 Android 12+ 上实现多任务独立窗口（Recents Screen independent cards）的各种组合。

## 核心原理

要在最近任务列表中显示多个独立卡片，核心在于让每个 Activity 运行在**独立的 Task** 中。

### 1. 代码方式 (Intent Flags)
最常用的组合：
- `FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_MULTIPLE_TASK`: 强制创建一个新 Task。
- `FLAG_ACTIVITY_NEW_DOCUMENT`: (Android 5.0+) 等同于 `NEW_TASK | MULTIPLE_TASK`，专门用于文档型应用。

### 2. 配置方式 (AndroidManifest.xml)
- `android:documentLaunchMode="always"`: 每次启动都会开启新 Task。
- `android:taskAffinity`: 设置不同的亲和性，可以让不同的 Activity 组显示在不同的卡片中。
- `android:launchMode="singleInstancePerTask"`: (Android 12+) 允许在不同 Task 中各有一个实例。

## 易错点与注意事项

1. **忘记组合 Flag**: 只使用 `MULTIPLE_TASK` 而不带 `NEW_TASK` 是无效的。
2. **Affinity 冲突**: 如果两个 Activity 的 `taskAffinity` 相同且没有使用 `MULTIPLE_TASK`，它们通常会挤在同一个最近任务卡片里。
3. **`singleInstance` 的误解**: `singleInstance` 在整个系统中只能有一个实例。如果你再次启动它，它只会把旧的窗口拉到前面，而不会产生新窗口。
4. **`maxRecents` 限制**: 系统对每个应用显示的最近任务数量有上限（通常是 16 个）。超过后旧的任务会被挤掉。
5. **系统 UI 差异**: 某些国产 ROM 的桌面启动器（Launcher）可能会强制合并同包名的任务卡片，这在代码层面很难绕过。
6. **Task 标题**: 建议通过 `TaskDescription` 或 `Activity.setTitle()` 动态修改标题，否则在最近任务中它们可能看起来完全一样。

## 测试建议
运行此 Demo 后，尝试以下组合：
1. **Activity 1 + NEW_TASK + MULTIPLE_TASK**: 应该能产生无限个独立窗口。
2. **Activity 5 (DLM:always)**: 不需要任何 Flag，直接点就能产生独立窗口。
3. **Activity 25 (siPerTask)**: 结合 `NEW_TASK | MULTIPLE_TASK` 观察与 `singleInstance` 的区别。
