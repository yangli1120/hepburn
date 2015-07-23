# hepburn

这个app的目的：
通过这个看图的app，学习一些新库和新的android技术，当做练手；学习Material Design及其新的控件；

api来源：
百度风（mei）景（nv）图片的url。。。

功能推进：
1. 实现图片的瀑布流，有下拉刷新和加载下一页。
2. 查看大图，界面要切换的很优雅，酷炫。
3. 图片的黑名单，本地创建数据库，记录每张图片的url，标记黑名单，每次加载到数据，自动过滤被标记黑名单的图，这里想用greendao或者其他orm的库。
4. 图片的收藏：用侧滑菜单先实现一个profile页面，这里有黑名单和搜藏选项；收藏的图片可以下载到系统图库，这里想实现一个下载服务（虽然大才小用）；
5. 图片的滤镜功能，考虑要不要做，如果做想顺便学习renderscript；


编年史
-- 20150722 16:39 --
暂时想法是图片加载用glide或者fresco，网络库用volley或者okhttp，图片流用support lib的recyclerview，遵循material design规范，所需的控件尽量只用support lib里面的。用java 8的lambda语法吧，与时俱进嘛。还想学RxAndroid。

-- 20150723 --
初步构建了网络层，实现了一个SwipeRefreshRecyclerView，并加入了load more的监听器，对RecyclerView使用还是比较生疏，没有达到理想的瀑布流效果。
